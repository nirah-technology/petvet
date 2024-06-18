package io.nirahtech.petvet.esp.monitor;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import io.nirahtech.petvet.esp.monitor.brokers.MessageBroker;
import io.nirahtech.petvet.esp.monitor.brokers.UDPMessageBroker;
import io.nirahtech.petvet.esp.monitor.ui.PetvetGpsEspWindow;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws UnsupportedLookAndFeelException, UnknownHostException 
    {
        
        final MessageBroker messageBroker = UDPMessageBroker.newInstance();
        final InetAddress multicastGroup = InetAddress.getByName("224.0.10.1");
        final int multicastPort = 44666;


        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        final PetvetGpsEspWindow window = new PetvetGpsEspWindow(messageBroker, multicastGroup, multicastPort);
        window.setVisible(true);
    }
}
