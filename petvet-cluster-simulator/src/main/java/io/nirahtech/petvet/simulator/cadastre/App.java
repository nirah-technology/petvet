package io.nirahtech.petvet.simulator.cadastre;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;

import io.nirahtech.petvet.simulator.cadastre.gui.PetVetLandWindow;
import io.nirahtech.petvet.simulator.cadastre.infrastructure.out.adapters.CadastresDataGouvFrWebService;

public class App {

    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel(new FlatDarkLaf()); // NimbusLookAndFeel
        PetVetLandWindow window = new PetVetLandWindow();
        window.setVisible(true);

    }

}
