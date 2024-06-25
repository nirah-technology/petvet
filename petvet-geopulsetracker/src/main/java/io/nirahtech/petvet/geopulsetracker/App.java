package io.nirahtech.petvet.geopulsetracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;

import io.nirahtech.petvet.geopulsetracker.ui.PetvetClusterConnectionWindow;
import io.nirahtech.petvet.geopulsetracker.ui.ScanReportMessageSnifferTask;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.brokers.UDPMessageBroker;

public class App {
    private static final String NETWORK_MULTICAST_GROUP_ADDRESS = "network.multicast.group.address";
    private static final String NETWORK_MULTICAST_GROUP_PORT = "network.multicast.group.port";

    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException {

        // App configuration
        final Map<String, Object> configuration = new HashMap<>();

        // Embedded default configuration
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
        loadDefaultConfiguration(configuration, resourceBundle);

        // Parse command line arguments
        final CommandLineParser parser = new CommandLineParser(args);

        if (parser.hasOption("help")) {
            CommandLineParser.printHelp();
            return;
        }

        if (parser.hasOption("config")) {
            String configFile = parser.getOptionValue("config");
            loadConfigurationFromFile(configuration, configFile);
        }

        // Override configuration with command line arguments
        if (parser.hasOption("group")) {
            String group = parser.getOptionValue("group");
            configuration.put(NETWORK_MULTICAST_GROUP_ADDRESS, InetAddress.getByName(group));
            System.out.println("Multicast group: " + group);
        }

        if (parser.hasOption("port")) {
            String port = parser.getOptionValue("port");
            configuration.put(NETWORK_MULTICAST_GROUP_PORT, Integer.parseInt(port));
            System.out.println("Port number: " + port);
        }

        // Continue with application logic if configuration is valid
        if (isConfigurationValid(configuration)) {
            try (
                    final InputStream bannerStream = App.class.getResourceAsStream("/banner.txt");
                    final InputStreamReader bannerStreamReader = new InputStreamReader(bannerStream);
                    final BufferedReader bannerReader = new BufferedReader(bannerStreamReader);) {
                bannerReader.lines().forEach(System.out::println);
            } catch (Exception e) {

            }

            UIManager.setLookAndFeel(new FlatDarkLaf()); // NimbusLookAndFeel

            final InetAddress multicastGroup = (InetAddress) configuration.get(NETWORK_MULTICAST_GROUP_ADDRESS);
            final int multicastPort = (int) configuration.get(NETWORK_MULTICAST_GROUP_PORT);

            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            final MessageBroker messageBroker = UDPMessageBroker.newInstance();
            final PetvetClusterConnectionWindow window = new PetvetClusterConnectionWindow(messageBroker,
                    multicastGroup, multicastPort);
            
            executorService.submit(new ScanReportMessageSnifferTask(messageBroker));
            window.setVisible(true);
        }
    }

    private static void loadDefaultConfiguration(Map<String, Object> configuration, ResourceBundle resourceBundle)
            throws IOException {
        configuration.put(NETWORK_MULTICAST_GROUP_ADDRESS,
                InetAddress.getByName(resourceBundle.getString(NETWORK_MULTICAST_GROUP_ADDRESS)));
        configuration.put(NETWORK_MULTICAST_GROUP_PORT,
                Integer.parseInt(resourceBundle.getString(NETWORK_MULTICAST_GROUP_PORT)));
    }

    private static void loadConfigurationFromFile(Map<String, Object> configuration, String configFile)
            throws IOException {
        Properties properties = new Properties();
        File file = new File(configFile);

        if (!file.exists() || !file.isFile()) {
            throw new IOException("Configuration file is invalid: " + configFile);
        }

        try (FileReader fileReader = new FileReader(file)) {
            properties.load(fileReader);
        }

        if (properties.containsKey(NETWORK_MULTICAST_GROUP_ADDRESS)) {
            configuration.put(NETWORK_MULTICAST_GROUP_ADDRESS,
                    InetAddress.getByName(properties.getProperty(NETWORK_MULTICAST_GROUP_ADDRESS)));
        }
        if (properties.containsKey(NETWORK_MULTICAST_GROUP_PORT)) {
            configuration.put(NETWORK_MULTICAST_GROUP_PORT,
                    Integer.parseInt(properties.getProperty(NETWORK_MULTICAST_GROUP_PORT)));
        }
    }

    private static boolean isConfigurationValid(Map<String, Object> configuration) {
        return configuration.containsKey(NETWORK_MULTICAST_GROUP_ADDRESS)
                && configuration.containsKey(NETWORK_MULTICAST_GROUP_PORT);
    }
}