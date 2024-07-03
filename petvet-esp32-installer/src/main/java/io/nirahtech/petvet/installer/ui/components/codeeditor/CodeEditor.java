package io.nirahtech.petvet.installer.ui.components.codeeditor;

import java.awt.Font;
import java.util.Objects;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class CodeEditor extends JTextPane {

    private Theme theme = new DarkPlusTheme();
    private String sourceCode = null;

    public CodeEditor() {
        super();
        this.setBackground(theme.getBackgroundColor());
        this.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Police monospacée pour maintenir l'alignement
    }

    public void setTheme(Theme theme) {
        Objects.requireNonNull(theme);
        this.theme = theme;
        this.applySyntaxHighlighting();
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
        this.applySyntaxHighlighting();
    }

    private void applySyntaxHighlighting() {
        // Création des styles
        Style keywordStyle = this.addStyle("KeywordStyle", null);
        StyleConstants.setForeground(keywordStyle, this.theme.getKeywordColor());
        StyleConstants.setBold(keywordStyle, true);

        Style typeStyle = this.addStyle("TypeStyle", null);
        StyleConstants.setForeground(typeStyle, this.theme.getTypeColor());
        StyleConstants.setBold(typeStyle, true);

        Style commentStyle = this.addStyle("CommentStyle", null);
        StyleConstants.setForeground(commentStyle, this.theme.getCommentColor());

        Style stringStyle = this.addStyle("StringStyle", null);
        StyleConstants.setForeground(stringStyle, this.theme.getStringColor());

        Style numberStyle = this.addStyle("NumberStyle", null);
        StyleConstants.setForeground(numberStyle, this.theme.getNumberColor());

        Style functionStyle = this.addStyle("FunctionStyle", null);
        StyleConstants.setForeground(functionStyle, this.theme.getFunctionColor());

        Style variableStyle = this.addStyle("VariableStyle", null);
        StyleConstants.setForeground(variableStyle, this.theme.getVariableColor());

        Style operatorStyle = this.addStyle("OperatorStyle", null);
        StyleConstants.setForeground(operatorStyle, this.theme.getOperatorColor());

        Style bracketStyle = this.addStyle("BracketStyle", null);
        StyleConstants.setForeground(bracketStyle, this.theme.getBracketColor());

        try {
            this.getDocument().remove(0, this.getDocument().getLength());
            this.getDocument().insertString(0, this.sourceCode, null); // Insérer le texte dans le JTextPane

            int offset = 0;
            String[] lines = this.sourceCode.split("\n");
            for (String line : lines) {
                highlightKeywords(line, offset);
                highlightStrings(line, offset);
                highlightNumbers(line, offset);
                highlightFunctions(line, offset);
                highlightVariables(line, offset);
                highlightOperators(line, offset);
                highlightBrackets(line, offset);
                highlightTypes(line, offset);
                highlightComments(line, offset);
                
                offset += line.length() + 1; // +1 pour le retour à la ligne
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void highlightComments(String line, int offset) {
        // Single-line comments
        int index = line.indexOf("//");
        while (index >= 0) {
            try {
                this.getStyledDocument().setCharacterAttributes(
                        offset + index, line.length() - index,
                        this.getStyle("CommentStyle"), true);
                index = line.indexOf("//", index + 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        // Multi-line comments (assuming /* ... */ are on the same line)
        index = line.indexOf("/*");
        while (index >= 0) {
            int endIndex = line.indexOf("*/", index + 2);
            if (endIndex >= 0) {
                try {
                    this.getStyledDocument().setCharacterAttributes(
                            offset + index, endIndex - index + 2,
                            this.getStyle("CommentStyle"), true);
                    index = line.indexOf("/*", endIndex + 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Handles the case where the multi-line comment starts but doesn't end on this line
                try {
                    this.getStyledDocument().setCharacterAttributes(
                            offset + index, line.length() - index,
                            this.getStyle("CommentStyle"), true);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

    private void highlightKeywords(String line, int offset) {
        String[] keywords = {"setup", "loop", "if", "while", "do", "then", "else", "try", "catch", "except", "for", "const", "static"};
        for (String keyword : keywords) {
            int index = line.indexOf(keyword);
            while (index >= 0) {
                this.getStyledDocument().setCharacterAttributes(
                        offset + index, keyword.length(),
                        this.getStyle("KeywordStyle"), true);
                index = line.indexOf(keyword, index + keyword.length());
            }
        }
    }

    private void highlightTypes(String line, int offset) {
        String[] types = {"void", "int", "long", "short", "bool", "double", "float", "signed", "unsigned", "char", "char16_t", "char32_t", "int16_t", "int32_t"};
        for (String type : types) {
            int index = line.indexOf(type);
            while (index >= 0) {
                this.getStyledDocument().setCharacterAttributes(
                        offset + index, type.length(),
                        this.getStyle("TypeStyle"), true);
                index = line.indexOf(type, index + type.length());
            }
        }
    }

    private void highlightStrings(String line, int offset) {
        int index = line.indexOf('"');
        while (index >= 0) {
            int endIndex = line.indexOf('"', index + 1);
            if (endIndex >= 0) {
                this.getStyledDocument().setCharacterAttributes(
                        offset + index, endIndex - index + 1,
                        this.getStyle("StringStyle"), true);
                index = line.indexOf('"', endIndex + 1);
            } else {
                break;
            }
        }
    }

    private void highlightNumbers(String line, int offset) {
        String[] numbers = line.split("\\D+");
        for (String number : numbers) {
            if (!number.isEmpty()) {
                int index = line.indexOf(number);
                while (index >= 0) {
                    this.getStyledDocument().setCharacterAttributes(
                            offset + index, number.length(),
                            this.getStyle("NumberStyle"), true);
                    index = line.indexOf(number, index + number.length());
                }
            }
        }
    }

    private void highlightFunctions(String line, int offset) {
        // Ajoutez votre logique pour détecter les noms de fonctions
    }

    private void highlightVariables(String line, int offset) {
        // Ajoutez votre logique pour détecter les noms de variables
    }

    private void highlightOperators(String line, int offset) {
        String[] operators = {"+", "-", "*", "/", "=", "==", "!="};
        for (String operator : operators) {
            int index = line.indexOf(operator);
            while (index >= 0) {
                this.getStyledDocument().setCharacterAttributes(
                        offset + index, operator.length(),
                        this.getStyle("OperatorStyle"), true);
                index = line.indexOf(operator, index + operator.length());
            }
        }
    }

    private void highlightBrackets(String line, int offset) {
        char[] brackets = {'{', '}', '(', ')', '[', ']'};
        for (char bracket : brackets) {
            int index = line.indexOf(bracket);
            while (index >= 0) {
                this.getStyledDocument().setCharacterAttributes(
                        offset + index, 1,
                        this.getStyle("BracketStyle"), true);
                index = line.indexOf(bracket, index + 1);
            }
        }
    }
}
