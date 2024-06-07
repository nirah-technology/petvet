package io.nirahtech.petvet.ai.classifiers.detectors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.MultipleEpochsIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import io.nirahtech.petvet.ai.classifiers.classification.AnimalClassification;
import io.nirahtech.petvet.ai.classifiers.exceptions.ClassificationException;

public final class CatAnimalDetector implements AnimalDetector {

    private File trainingData;
    private File testData;

    private int height;
    private int width;
    private int channels;
    private int outputNum;
    private int batchSize;
    private int epochs;
    private int seed;

    private MultiLayerNetwork model;

    public CatAnimalDetector() {
        try {
            this.loadConfiguration();
            this.trainModel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConfiguration() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("petvet-ai.properties");
        if (Objects.nonNull(inputStream)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            this.trainingData = new File(properties.getProperty("image-recognition.training-data.folder"));
            this.testData = new File(properties.getProperty("image-recognition.test-data.folder"));

            this.width = Integer.parseInt(properties.getProperty("image-recognition.picture.width"));
            this.height = Integer.parseInt(properties.getProperty("image-recognition.picture.height"));
            this.channels = Integer.parseInt(properties.getProperty("image-recognition.picture.color.channels"));
            this.outputNum = Integer.parseInt(properties.getProperty("image-recognition.classifications.outputNum"));
            this.batchSize = Integer.parseInt(properties.getProperty("image-recognition.batchSize"));
            this.epochs = Integer.parseInt(properties.getProperty("image-recognition.epochs"));
            this.seed = Integer.parseInt(properties.getProperty("image-recognition.seed"));
        }
    }

    private void trainModel() {
        try {
            FileSplit trainSplit = new FileSplit(this.trainingData, NativeImageLoader.ALLOWED_FORMATS, new Random(seed));
            FileSplit testSplit = new FileSplit(this.testData, NativeImageLoader.ALLOWED_FORMATS, new Random(seed));

            ImageRecordReader trainRecordReader = new ImageRecordReader(height, width, channels);
            ImageRecordReader testRecordReader = new ImageRecordReader(height, width, channels);

            trainRecordReader.initialize(trainSplit);
            testRecordReader.initialize(testSplit);

            DataSetIterator trainIterator = new MultipleEpochsIterator(epochs, new RecordReaderDataSetIterator(trainRecordReader, batchSize, 1, outputNum));
            DataSetIterator testIterator = new RecordReaderDataSetIterator(testRecordReader, batchSize, 1, outputNum);

            DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
            scaler.fit(trainIterator);
            trainIterator.setPreProcessor(scaler);
            testIterator.setPreProcessor(scaler);

            MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .updater(new Adam(1e-3))
                .list()
                .layer(new ConvolutionLayer.Builder(5, 5)
                    .nIn(channels)
                    .stride(1, 1)
                    .nOut(20)
                    .activation(Activation.RELU)
                    .build())
                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                    .kernelSize(2, 2)
                    .stride(2, 2)
                    .build())
                .layer(new ConvolutionLayer.Builder(5, 5)
                    .stride(1, 1)
                    .nOut(50)
                    .activation(Activation.RELU)
                    .build())
                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                    .kernelSize(2, 2)
                    .stride(2, 2)
                    .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .nOut(outputNum)
                    .activation(Activation.SOFTMAX)
                    .build())
                .build();

            this.model = new MultiLayerNetwork(configuration);
            this.model.init();
            this.model.setListeners(new ScoreIterationListener(10));

            this.model.fit(trainIterator);

            Evaluation evaluation = this.model.evaluate(testIterator);
            System.out.println(evaluation.stats());

        } catch (IOException e) {
            throw new RuntimeException("Error during model training", e);
        }
    }

    @Override
    public Optional<AnimalClassification> detect(byte[] imageBytes) throws ClassificationException {
        try {
            NativeImageLoader loader = new NativeImageLoader(height, width, channels);
            INDArray image = loader.asMatrix(imageBytes);

            DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
            scaler.transform(image);

            INDArray output = model.output(image);
            int predictedClass = output.argMax(1).getInt(0);
            
            String predictedLabel = "Chat;Manx"; // Déterminer le label basé sur `predictedClass`
            // Vous pourriez avoir une méthode pour mapper `predictedClass` à un label

            // AnimalClassification classification = new AnimalClassification(predictedLabel, output.toDoubleVector());
            AnimalClassification classification = new AnimalClassification(predictedLabel);
            return Optional.of(classification);
        } catch (IOException e) {
            throw new ClassificationException(e);
        }
    }
}
