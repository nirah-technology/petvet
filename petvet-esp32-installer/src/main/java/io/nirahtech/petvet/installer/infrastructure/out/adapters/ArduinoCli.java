package io.nirahtech.petvet.installer.infrastructure.out.adapters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.nirahtech.petvet.installer.domain.ESP32;
import io.nirahtech.petvet.installer.infrastructure.out.ports.SketchDeployerProgram;

// https://arduino.github.io/arduino-cli/1.0/installation/
// https://arduino.github.io/arduino-cli/1.0/getting-started/
public final class ArduinoCli implements SketchDeployerProgram {

    private static final String ARDUINO_CLI_BIN = "arduino-cli";
    private static final String ARDUINO_CLI_BIN_PATH = "/bin/" + ARDUINO_CLI_BIN;
    private static final String PROJECT_NAME = "PetVetES32Sketch";
    private static final String PROGRAMMER = "esptool";
    private static final String FQBN = "esp32:esp32";
    private static final String BOARD = "node32s";

    private static ArduinoCli instance = null;

    public static ArduinoCli newInstance(final ESP32 esp32, final String sourceCode) throws IOException {
        final InputStream arduinoCliInputStream = ArduinoCli.class.getResourceAsStream(ARDUINO_CLI_BIN_PATH);
        if (Objects.isNull(arduinoCliInputStream)) {
            throw new IOException("Bin file not found in resources: " + ARDUINO_CLI_BIN_PATH);
        }

        final String userHome = System.getProperty("user.home");
        final String nirahtechFolder = ".nirahtech";
        final String petvetFolder = "petvet/installer";
        final String instance = UUID.randomUUID().toString();
        final File workspace = new File(String.format("%s/%s/%s/%s/", userHome, nirahtechFolder, petvetFolder, instance));
        workspace.deleteOnExit();
        if (!workspace.exists()) {
            workspace.mkdirs();
        }
        final File bin = new File(workspace, ARDUINO_CLI_BIN);
        // final boolean isCreated = bin.createNewFile();
        // if (!isCreated) {
        //     throw new IOException("Unable to create bin file: " + bin);
        // }
        bin.deleteOnExit();
        if (!bin.exists()) {
            Files.copy(arduinoCliInputStream, bin.toPath());
        }
        boolean canBeExecuted = bin.setExecutable(true);
        if (!canBeExecuted) {
            throw new IOException("Unable to set bin file executable: " + bin);
        }
        return new ArduinoCli(bin, workspace, esp32, sourceCode);
    }

    private final File executableBinary;
    private final File workspace;
    private File sketchFile;
    private final ESP32 esp32;
    private final String sourceCode;

    private Consumer<String> onNextInstallStepChanged = null;
    private Consumer<String> onCommandMessage = null;
    
    private ArduinoCli(final File executableBinary, final File workspace, final ESP32 esp32, final String sourceCode) {
        this.executableBinary = executableBinary;
        this.workspace = workspace;
        this.esp32 = esp32;
        this.sourceCode = sourceCode;
    }

    private final Process runCommand(final String... commandWithParameters) throws IOException {
        final ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(this.workspace);
        processBuilder.command(commandWithParameters);
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();

    }

    private final void readStream(InputStream stream, final Consumer<String> callback) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                callback.accept(line);
            }
        }
    }

    private void manageProcessExecution(final Process process) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
    
        // Read error stream
        StringBuilder errorMessageBuilder = new StringBuilder();
        executor.submit(() -> {
            try {
                readStream(process.getErrorStream(), errorMessageBuilder::append);
            } catch (IOException e) {
                e.printStackTrace(); // Handle appropriately
            }
        });
    
        // Read input stream
        executor.submit(() -> {
            try {
                readStream(process.getInputStream(), message -> {
                    if (Objects.nonNull(this.onCommandMessage)) {
                        this.onCommandMessage.accept(message);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace(); // Handle appropriately
            }
        });
    
        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(2, TimeUnit.MINUTES);
            if (!finished) {
                throw new IOException("Reading process output timed out");
            }
        } catch (InterruptedException e) {
            throw new IOException("Reading process output was interrupted", e);
        }
    
        final int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Process was interrupted", e);
        }
    
        if (exitCode != 0) {
            throw new IOException("Failed to execute the command: " + errorMessageBuilder.toString());
        }

    }

    public void createNewSketch() throws IOException {
        final Process process = this.runCommand(this.executableBinary.getAbsolutePath(), "sketch", "new", PROJECT_NAME);
        this.manageProcessExecution(process);
        final File skectchFolder = new File(this.workspace, PROJECT_NAME);
        this.sketchFile = new File(skectchFolder, String.format("%s.ino", PROJECT_NAME));
    }

    private void updateAvailablePlatformsCache() throws IOException {
        final Process process = this.runCommand(this.executableBinary.getAbsolutePath(), "core", "update-index");
        this.manageProcessExecution(process);

    }

    public void updateSketch() throws IOException {
        try (
            final FileWriter fileWriter = new FileWriter(this.sketchFile);
        ) {
            fileWriter.write(this.sourceCode);   
        }
    }

    public void installCoreForESP32() throws IOException {
        final Process process = this.runCommand(this.executableBinary.getAbsolutePath(), "core", "install", FQBN);
        this.manageProcessExecution(process);
    }

    public void add3rdPartsCore() throws IOException {

    }

    public void compile() throws IOException {
        final Process process = this.runCommand(this.executableBinary.getAbsolutePath(), "compile", "-b", FQBN+":"+BOARD, PROJECT_NAME, "--programmer", PROGRAMMER);
        this.manageProcessExecution(process);
    }

    public void upload() throws IOException {
        final Process process = this.runCommand(this.executableBinary.getAbsolutePath(), "upload", "-p", this.esp32.getUsbPort().toString(), "-b", FQBN+":"+BOARD, PROJECT_NAME);
        this.manageProcessExecution(process);
    }

    @Override
    public void install() throws IOException {
        System.out.println("Start create new sketch");
        this.createNewSketch();
        System.out.println("Start update cache");
        this.updateAvailablePlatformsCache();
        System.out.println("Start install core");
        this.installCoreForESP32();
        System.out.println("Start 3rd parts");
        this.add3rdPartsCore();
        System.out.println("Start update sketch");
        this.updateSketch();
        System.out.println("Start compile");
        this.compile();
        System.out.println("Start upload");
        this.upload();
    }

    public void setOnCommandMessage(Consumer<String> onCommandMessage) {
        this.onCommandMessage = onCommandMessage;
    }
}
