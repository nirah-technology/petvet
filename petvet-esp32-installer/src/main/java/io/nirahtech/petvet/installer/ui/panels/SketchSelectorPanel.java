package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import io.nirahtech.petvet.installer.ui.stepper.Stepper;

public class SketchSelectorPanel extends JPanel {

    private final JLabel pathLabel;
    private final JFileChooser sketchFileChooser;
    private final JButton sketchSelectorButton;
    private final JTextPane codeArea;

    private final JButton nextStepButton;
    private final JButton previousStepButton;
    private Runnable onNext = null;
    private Runnable onPrevious = null;
    
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

        this.codeArea = new JTextPane();
        this.codeArea.setEditable(false);
        this.codeArea.setBackground(new Color(30,30,30));
        this.codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Police monospacée pour maintenir l'alignement

        this.sketchSelectorButton.addActionListener((event) -> {
            final int choice = sketchFileChooser.showDialog(this, "Select");
            if (choice == JFileChooser.APPROVE_OPTION) {
                File selectedFile = sketchFileChooser.getSelectedFile();
                if (Objects.nonNull(selectedFile)) {
                    if (selectedFile.isFile()) {
                        pathLabel.setText(selectedFile.getName());
                        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                            final String sourceCode = reader.lines().collect(Collectors.joining("\n"));
                            this.codeArea.setText(sourceCode);
                            applySyntaxHighlighting(this.codeArea, sourceCode);
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

    private void applySyntaxHighlighting(JTextPane textPane, String code) {
        // Création d'un style pour les mots-clés
        Style keywordStyle = textPane.addStyle("KeywordStyle", null);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);
        StyleConstants.setBold(keywordStyle, true);

        // Création d'un style pour les commentaires
        Style commentStyle = textPane.addStyle("CommentStyle", null);
        StyleConstants.setForeground(commentStyle, Color.GRAY);

        // Création d'un style pour les chaînes
        Style stringStyle = textPane.addStyle("StringStyle", null);
        StyleConstants.setForeground(stringStyle, Color.RED);

        // Début de l'analyse syntaxique et application des styles
        try {
            textPane.getDocument().insertString(0, code, null); // Insérer le texte dans le JTextPane

            // Analyse du texte pour appliquer les styles
            int offset = 0;
            String[] lines = code.split("\n");
            for (String line : lines) {
                if (line.contains("//")) {
                    int commentIndex = line.indexOf("//");
                    textPane.getStyledDocument().setCharacterAttributes(
                            offset + commentIndex, line.length() - commentIndex,
                            textPane.getStyle("CommentStyle"), true);
                }

                // Appliquer des règles supplémentaires pour les mots-clés, chaînes, etc.
                // Exemple simple pour les mots-clés
                highlightKeywords(textPane, line, offset);

                offset += line.length() + 1; // +1 pour le retour à la ligne
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour mettre en évidence les mots-clés
    private void highlightKeywords(JTextPane textPane, String line, int offset) {
        String[] keywords = {"void", "setup", "loop", "pinMode", "digitalWrite", "HIGH", "LOW", "delay"};

        for (String keyword : keywords) {
            int index = line.indexOf(keyword);
            while (index >= 0) {
                try {
                    textPane.getStyledDocument().setCharacterAttributes(
                            offset + index, keyword.length(),
                            textPane.getStyle("KeywordStyle"), true);
                    index = line.indexOf(keyword, index + keyword.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
}
