package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.nirahtech.petvet.installer.ui.components.codeeditor.CodeEditor;
import io.nirahtech.petvet.installer.ui.stepper.Stepper;

public class SketchSelectorPanel extends JPanel {

    private final JLabel pathLabel;
    private final JFileChooser sketchFileChooser;
    private final JButton sketchSelectorButton;
    private final CodeEditor codeArea;

    private final JButton nextStepButton;
    private final JButton previousStepButton;
    private Runnable onNext = null;
    private Runnable onPrevious = null;

    private Consumer<String> onSourceCodeChangeEventHandler = null;
    
    public SketchSelectorPanel(final Stepper stepper) {
        super(new BorderLayout());
        final JLabel selectedSketchTitle = new JLabel("Sketch to install");
        this.pathLabel = new JLabel("No sketch selected.");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arduino File (*.ino)", "ino");
        this.sketchFileChooser = new JFileChooser();
        this.sketchFileChooser.setFileFilter(filter);
        this.sketchSelectorButton = new JButton("Choose a Sketch");

        final JPanel leftPanel = new JPanel(new GridLayout(3, 1));
        leftPanel.add(selectedSketchTitle);
        leftPanel.add(pathLabel);
        leftPanel.add(sketchSelectorButton);

        this.codeArea = new CodeEditor();
        this.codeArea.setEditable(false);
        

        this.sketchSelectorButton.addActionListener((event) -> {
            final int choice = sketchFileChooser.showDialog(this, "Select");
            if (choice == JFileChooser.APPROVE_OPTION) {
                File selectedFile = sketchFileChooser.getSelectedFile();
                if (Objects.nonNull(selectedFile)) {
                    if (selectedFile.isFile()) {
                        pathLabel.setText(selectedFile.getName());
                        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                            final String sourceCode = reader.lines().collect(Collectors.joining("\n"));
                            this.codeArea.setSourceCode(sourceCode);
                            if (Objects.nonNull(onSourceCodeChangeEventHandler)) {
                                onSourceCodeChangeEventHandler.accept(sourceCode);
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        this.add(leftPanel, BorderLayout.WEST);

        

        this.add(new JScrollPane(this.codeArea), BorderLayout.CENTER);

        this.previousStepButton = new JButton("Previous");
        this.previousStepButton.addActionListener(event -> {
            stepper.selectPreviousStep();
            if (Objects.nonNull(this.onPrevious)) {
                this.onPrevious.run();
            }
        });

        this.nextStepButton = new JButton("Next");
        this.nextStepButton.addActionListener(event -> {
            stepper.selectNextStep();
            if (Objects.nonNull(this.onNext)) {
                this.onNext.run();
            }
        });


        final JPanel navigatorPanel = new JPanel(new GridLayout(1, 2));
        navigatorPanel.add(this.previousStepButton);
        navigatorPanel.add(this.nextStepButton);
        this.add(navigatorPanel, BorderLayout.SOUTH);

    }


    /**
     * @param onNext the onNext to set
     */
    public void setOnNextEventHandler(Runnable onNext) {
        this.onNext = onNext;
    }

    /**
     * @param onNext the onNext to set
     */
    public void setOnPreviousEventHandler(Runnable onPrevious) {
        this.onPrevious = onPrevious;
    }


    public void addOnSourceCodeChangedEventListener(Consumer<String> callback) {
        onSourceCodeChangeEventHandler = callback;
    }
}
