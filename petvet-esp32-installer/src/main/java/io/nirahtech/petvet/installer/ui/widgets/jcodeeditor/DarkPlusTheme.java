package io.nirahtech.petvet.installer.ui.widgets.jcodeeditor;

import java.awt.Color;

public class DarkPlusTheme implements Theme {

    @Override
    public Color getKeywordColor() {
        return new Color(197, 134, 192); // #C586C0
    }

    @Override
    public Color getCommentColor() {
        return new Color(106, 153, 85); // #6A9955
    }

    @Override
    public Color getStringColor() {
        return new Color(206, 145, 120); // #CE9178
    }

    @Override
    public Color getNumberColor() {
        return new Color(181, 206, 168); // #B5CEA8
    }

    @Override
    public Color getTypeColor() {
        return new Color(78, 201, 176); // #4EC9B0
    }

    @Override
    public Color getPreprocessorColor() {
        return new Color(197, 134, 192); // #C586C0
    }

    @Override
    public Color getFunctionColor() {
        return new Color(220, 205, 170); // #DCDCAA
    }

    @Override
    public Color getVariableColor() {
        return new Color(156, 220, 254); // #9CDCFE
    }

    @Override
    public Color getOperatorColor() {
        return new Color(212, 212, 212); // #D4D4D4
    }

    @Override
    public Color getBracketColor() {
        return new Color(212, 212, 212); // #D4D4D4
    }

    @Override
    public Color getDefaultColor() {
        return new Color(212, 212, 212); // #D4D4D4
    }

    @Override
    public Color getBackgroundColor() {
        return new Color(30, 30, 30); // #1E1E1E
    }
}
