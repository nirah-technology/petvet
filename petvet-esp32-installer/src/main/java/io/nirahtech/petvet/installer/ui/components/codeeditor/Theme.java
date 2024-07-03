package io.nirahtech.petvet.installer.ui.components.codeeditor;

import java.awt.Color;

public interface Theme {
    
    /**
     * Returns the color used for keywords in the code.
     * 
     * @return the color used for keywords
     */
    Color getKeywordColor();

    /**
     * Returns the color used for comments in the code.
     * 
     * @return the color used for comments
     */
    Color getCommentColor();

    /**
     * Returns the color used for strings in the code.
     * 
     * @return the color used for strings
     */
    Color getStringColor();

    /**
     * Returns the color used for numbers in the code.
     * 
     * @return the color used for numbers
     */
    Color getNumberColor();

    /**
     * Returns the color used for types (e.g., int, char) in the code.
     * 
     * @return the color used for types
     */
    Color getTypeColor();

    /**
     * Returns the color used for preprocessor directives in the code.
     * 
     * @return the color used for preprocessor directives
     */
    Color getPreprocessorColor();

    /**
     * Returns the color used for function names in the code.
     * 
     * @return the color used for function names
     */
    Color getFunctionColor();

    /**
     * Returns the color used for variables in the code.
     * 
     * @return the color used for variables
     */
    Color getVariableColor();

    /**
     * Returns the color used for operators in the code.
     * 
     * @return the color used for operators
     */
    Color getOperatorColor();

    /**
     * Returns the color used for brackets (e.g., {}, []) in the code.
     * 
     * @return the color used for brackets
     */
    Color getBracketColor();
    
    /**
     * Returns the color used for default text in the code.
     * 
     * @return the color used for default text
     */
    Color getDefaultColor();
    
    /**
     * Returns the background color for the code editor.
     * 
     * @return the background color
     */
    Color getBackgroundColor();
}
