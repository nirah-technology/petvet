package io.nirahtech.petvet.simulator.cadastre;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;

import io.nirahtech.petvet.simulator.cadastre.gui.PetVetLandWindow;

public class App {

    public static void main(String[] args) throws UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel(new FlatDarkLaf()); // new NimbusLookAndFeel()
        PetVetLandWindow window = new PetVetLandWindow();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

    }

}
