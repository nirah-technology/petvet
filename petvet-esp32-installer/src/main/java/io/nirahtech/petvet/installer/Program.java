package io.nirahtech.petvet.installer;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;

import io.nirahtech.petvet.installer.ui.PetvetInstallerWindow;

public final class Program {

    public static final void main(final String[] cliArguments) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatDarkLaf()); // NimbusLookAndFeel
        final PetvetInstallerWindow window = new PetvetInstallerWindow();
        window.setVisible(true);
    }
}
