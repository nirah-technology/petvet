package io.nirahtech.petvet.esp.monitor;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import io.nirahtech.petvet.esp.monitor.ui.PetvetGpsEspWindow;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws UnsupportedLookAndFeelException 
    {
        UIManager.setLookAndFeel(new NimbusLookAndFeel() );
        PetvetGpsEspWindow frame = new PetvetGpsEspWindow();
        frame.setVisible(true);
    }
}
