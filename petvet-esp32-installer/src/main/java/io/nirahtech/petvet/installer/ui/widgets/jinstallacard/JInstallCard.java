package io.nirahtech.petvet.installer.ui.widgets.jinstallacard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import io.nirahtech.petvet.installer.domain.ESP32;
import io.nirahtech.petvet.installer.infrastructure.out.adapters.ArduinoCli;

public class JInstallCard extends JPanel {

    private final ESP32 esp;
    private final Map<String, String> configuration;
    private final JProgressBar installProgressBar;
    private final String sourceCode;

    public JInstallCard(final ESP32 esp, final Map<String, String> configuration, final String sourceCode) {
        super();
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0, 0, 0));
        this.esp = esp;
        this.configuration = Map.copyOf(configuration);
        this.sourceCode = sourceCode;

        final JLabel title = new JLabel(String.format("<html><h3><strong>%s</strong></h3></html>", this.esp.getUsbPort().toString()));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        this.add(title, BorderLayout.NORTH);

        final JPanel confPanel = new JPanel(new GridLayout(configuration.entrySet().size(), 1));
        configuration.entrySet().forEach(property -> {
            final JPanel panel = new JPanel(new GridLayout(1, 2));
            panel.setBackground(this.getBackground());
            panel.add(new JLabel(property.getKey()));
            JLabel value = new JLabel(property.getValue());
            value.setHorizontalAlignment(SwingConstants.RIGHT);
            panel.add(value);
            confPanel.add(panel);
        });
        this.add(confPanel, BorderLayout.CENTER);
        this.installProgressBar = new JProgressBar();
        this.installProgressBar.setPreferredSize(new Dimension(this.installProgressBar.getPreferredSize().width, 20));
        this.add(this.installProgressBar, BorderLayout.SOUTH);
    }

    public void install() {
        this.installProgressBar.setIndeterminate(true);
        final Installer installer = new Installer();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(installer);
    }

    private final class Installer implements Runnable {
        @Override
        public void run() {            
            ArduinoCli arduinoCli;
            try {
                arduinoCli = ArduinoCli.newInstance(esp, sourceCode);
                arduinoCli.setOnCommandMessage(message -> {
                    // SwingUtilities.invokeLater(() -> statusLabel.setText(message));
                    System.out.println(message);
                });
                
                arduinoCli.install();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                installProgressBar.setIndeterminate(false);
            }
        }
    }
 }
