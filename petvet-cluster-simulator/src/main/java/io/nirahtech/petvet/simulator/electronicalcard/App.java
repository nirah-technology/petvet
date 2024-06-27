package io.nirahtech.petvet.simulator.electronicalcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;

import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.simulator.electronicalcard.gui.PetvetClusterSimumatorWindow;


public class App {
    private static final String CLUSTER_NODES_COUNT = "cluster.size";
    private static final String NETWORK_INTERFACE_IP_FILTER = "network.ip.filter";
    private static final String NETWORK_MULTICAST_GROUP_ADDRESS = "network.multicast.group";
    private static final String NETWORK_MULTICAST_GROUP_PORT = "network.multicast.port";
    private static final String NODE_MODE = "node.mode";
    private static final String SCAN_INTERVAL = "node.interval.scan";
    private static final String ORCHESTRATOR_AVAILABILITY_INTERVAL = "node.interval.orchestrator.request";
    private static final String NODE_HEARTBEAT_INTERVAL = "node.interval.heartbeat";
    private static final String GUI_ENABLED = "gui.enabled";

    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException {
        // App configuration
        final Map<String, Object> configurationAsMap = new HashMap<>();

        // Embedded default configuration
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
        loadDefaultConfiguration(configurationAsMap, resourceBundle);

        // Parse command line arguments
        final CommandLineParser parser = new CommandLineParser(args);

        if (parser.hasOption("help")) {
            CommandLineParser.printHelp();
            return;
        }

        if (parser.hasOption("config")) {
            String configFile = parser.getOptionValue("config");
            loadConfigurationFromFile(configurationAsMap, configFile);
        }

        // Override configuration with command line arguments
        if (parser.hasOption("size")) {
            String size = parser.getOptionValue("size");
            configurationAsMap.put(CLUSTER_NODES_COUNT, Integer.parseInt(size));
            System.out.println("Cluster size: " + size);
        }

        if (parser.hasOption("group")) {
            String group = parser.getOptionValue("group");
            configurationAsMap.put(NETWORK_MULTICAST_GROUP_ADDRESS, InetAddress.getByName(group));
            System.out.println("Multicast group: " + group);
        }

        if (parser.hasOption("port")) {
            String port = parser.getOptionValue("port");
            configurationAsMap.put(NETWORK_MULTICAST_GROUP_PORT, Integer.parseInt(port));
            System.out.println("Port number: " + port);
        }

        if (parser.hasOption("network")) {
            String network = parser.getOptionValue("network");
            configurationAsMap.put(NETWORK_INTERFACE_IP_FILTER, InetAddress.getByName(network));
            System.out.println("Network filter: " + network);
        }

        if (parser.hasOption("windowed")) {
            String windowed = parser.getOptionValue("windowed");
            configurationAsMap.put(GUI_ENABLED, Boolean.parseBoolean(windowed));
            System.out.println("Windowed: " + windowed);
        }


        if (parser.hasOption("scan-interval")) {
            String scanIntervalInMS = parser.getOptionValue("scan-interval");
            configurationAsMap.put(SCAN_INTERVAL, Duration.ofMillis(Long.parseLong(scanIntervalInMS)));
            System.out.println("Scan Interval: " + scanIntervalInMS);
        }

        // Continue with application logic if configuration is valid
        if (isConfigurationValid(configurationAsMap)) {
            try (
                final InputStream bannerStream = App.class.getResourceAsStream("/banner.txt");    
                final InputStreamReader bannerStreamReader = new InputStreamReader(bannerStream);
                final BufferedReader bannerReader = new BufferedReader(bannerStreamReader);
            ) {
                bannerReader.lines().forEach(System.out::println);
            } catch (Exception e) {
                
            }

            System.out.println("Configuration successfully loaded: " + configurationAsMap);

            final InetAddress filter = (InetAddress) configurationAsMap.get(NETWORK_INTERFACE_IP_FILTER);
            final InetAddress group = (InetAddress) configurationAsMap.get(NETWORK_MULTICAST_GROUP_ADDRESS);
            final int port = (int) configurationAsMap.get(NETWORK_MULTICAST_GROUP_PORT);
            final int clusterSize = (int) configurationAsMap.get(CLUSTER_NODES_COUNT);
            final EmitterMode mode = (EmitterMode) configurationAsMap.get(NODE_MODE);
            final Duration scanInterval = (Duration) configurationAsMap.get(SCAN_INTERVAL);
            final Duration orchestratorInterval = (Duration) configurationAsMap.get(ORCHESTRATOR_AVAILABILITY_INTERVAL);
            final Duration heartbeatInterval = (Duration) configurationAsMap.get(NODE_HEARTBEAT_INTERVAL);
            final boolean guiEnabled = (Boolean) configurationAsMap.get(GUI_ENABLED);

            final Configuration configuration = new Configuration(clusterSize, filter, group, port, mode, scanInterval, orchestratorInterval, heartbeatInterval);
            if (!guiEnabled) {
                final Cluster cluster = Cluster.create(configuration);
                cluster.turnOn();
            } else {
                UIManager.setLookAndFeel(new FlatDarkLaf()); // NimbusLookAndFeel
                PetvetClusterSimumatorWindow window = new PetvetClusterSimumatorWindow(configuration);
                window.setVisible(true);
            }
        }
    }

    private static void loadDefaultConfiguration(Map<String, Object> configuration, ResourceBundle resourceBundle) throws IOException {
        configuration.put(CLUSTER_NODES_COUNT, Integer.parseInt(resourceBundle.getString(CLUSTER_NODES_COUNT)));
        configuration.put(NETWORK_INTERFACE_IP_FILTER, InetAddress.getByName(resourceBundle.getString(NETWORK_INTERFACE_IP_FILTER)));
        configuration.put(NETWORK_MULTICAST_GROUP_ADDRESS, InetAddress.getByName(resourceBundle.getString(NETWORK_MULTICAST_GROUP_ADDRESS)));
        configuration.put(NETWORK_MULTICAST_GROUP_PORT, Integer.parseInt(resourceBundle.getString(NETWORK_MULTICAST_GROUP_PORT)));
        configuration.put(NODE_MODE, EmitterMode.valueOf(resourceBundle.getString(NODE_MODE)));
        configuration.put(SCAN_INTERVAL, Duration.ofMillis(Long.parseLong(resourceBundle.getString(SCAN_INTERVAL))));
        configuration.put(ORCHESTRATOR_AVAILABILITY_INTERVAL, Duration.ofMillis(Long.parseLong(resourceBundle.getString(ORCHESTRATOR_AVAILABILITY_INTERVAL))));
        configuration.put(NODE_HEARTBEAT_INTERVAL, Duration.ofMillis(Long.parseLong(resourceBundle.getString(NODE_HEARTBEAT_INTERVAL))));
        configuration.put(GUI_ENABLED, Boolean.parseBoolean(resourceBundle.getString(GUI_ENABLED)));


    }

    private static void loadConfigurationFromFile(Map<String, Object> configuration, String configFile) throws IOException {
        Properties properties = new Properties();
        File file = new File(configFile);

        if (!file.exists() || !file.isFile()) {
            throw new IOException("Configuration file is invalid: " + configFile);
        }

        try (FileReader fileReader = new FileReader(file)) {
            properties.load(fileReader);
        }

        if (properties.containsKey(CLUSTER_NODES_COUNT)) {
            configuration.put(CLUSTER_NODES_COUNT, Integer.parseInt(properties.getProperty(CLUSTER_NODES_COUNT)));
        }
        if (properties.containsKey(NETWORK_INTERFACE_IP_FILTER)) {
            configuration.put(NETWORK_INTERFACE_IP_FILTER, InetAddress.getByName(properties.getProperty(NETWORK_INTERFACE_IP_FILTER)));
        }
        if (properties.containsKey(NETWORK_MULTICAST_GROUP_ADDRESS)) {
            configuration.put(NETWORK_MULTICAST_GROUP_ADDRESS, InetAddress.getByName(properties.getProperty(NETWORK_MULTICAST_GROUP_ADDRESS)));
        }
        if (properties.containsKey(NETWORK_MULTICAST_GROUP_PORT)) {
            configuration.put(NETWORK_MULTICAST_GROUP_PORT, Integer.parseInt(properties.getProperty(NETWORK_MULTICAST_GROUP_PORT)));
        }
        if (properties.containsKey(NODE_MODE)) {
            configuration.put(NODE_MODE, EmitterMode.valueOf(properties.getProperty(NODE_MODE)));
        }
        if (properties.containsKey(SCAN_INTERVAL)) {
            configuration.put(SCAN_INTERVAL, Duration.ofMillis(Long.parseLong(properties.getProperty(SCAN_INTERVAL))));
        }
        if (properties.containsKey(ORCHESTRATOR_AVAILABILITY_INTERVAL)) {
            configuration.put(ORCHESTRATOR_AVAILABILITY_INTERVAL, Duration.ofMillis(Long.parseLong(properties.getProperty(ORCHESTRATOR_AVAILABILITY_INTERVAL))));
        }
        if (properties.containsKey(NODE_HEARTBEAT_INTERVAL)) {
            configuration.put(NODE_HEARTBEAT_INTERVAL, Duration.ofMillis(Long.parseLong(properties.getProperty(NODE_HEARTBEAT_INTERVAL))));
        }
        if (properties.containsKey(GUI_ENABLED)) {
            configuration.put(GUI_ENABLED, Boolean.parseBoolean(properties.getProperty(GUI_ENABLED)));
        }
    }

    private static boolean isConfigurationValid(Map<String, Object> configuration) {
        return configuration.containsKey(CLUSTER_NODES_COUNT)
                && configuration.containsKey(NETWORK_INTERFACE_IP_FILTER)
                && configuration.containsKey(NETWORK_MULTICAST_GROUP_ADDRESS)
                && configuration.containsKey(NETWORK_MULTICAST_GROUP_PORT)
                && configuration.containsKey(NODE_MODE)
                && configuration.containsKey(SCAN_INTERVAL)
                && configuration.containsKey(ORCHESTRATOR_AVAILABILITY_INTERVAL)
                && configuration.containsKey(GUI_ENABLED)
                && configuration.containsKey(NODE_HEARTBEAT_INTERVAL)
                ;
    }
}
