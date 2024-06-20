package io.nirahtech.petvet.simulator.electronicalcard;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;


public class App {
    private static final String CLUSTER_NODES_COUNT = "cluster.nodes.size";
    private static final String NETWORK_INTERFACE_IP_FILTER = "network.interface.ip.filter";
    private static final String NETWORK_MULTICAST_GROUP_ADDRESS = "network.multicast.group.address";
    private static final String NETWORK_MULTICAST_GROUP_PORT = "network.multicast.group.port";

    public static void main(String[] args) throws IOException {
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
        if (parser.hasOption("size")) {
            String size = parser.getOptionValue("size");
            configuration.put(CLUSTER_NODES_COUNT, Integer.parseInt(size));
            System.out.println("Cluster size: " + size);
        }

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

        if (parser.hasOption("network")) {
            String network = parser.getOptionValue("network");
            configuration.put(NETWORK_INTERFACE_IP_FILTER, InetAddress.getByName(network));
            System.out.println("Network filter: " + network);
        }

        // Continue with application logic if configuration is valid
        if (isConfigurationValid(configuration)) {
            InetAddress filter = (InetAddress) configuration.get(NETWORK_INTERFACE_IP_FILTER);
            InetAddress group = (InetAddress) configuration.get(NETWORK_MULTICAST_GROUP_ADDRESS);
            int port = (int) configuration.get(NETWORK_MULTICAST_GROUP_PORT);
            int clusterSize = (int) configuration.get(CLUSTER_NODES_COUNT);

            final Cluster cluster = Cluster.create(clusterSize, filter, group, port);
            cluster.run();
        }
    }

    private static void loadDefaultConfiguration(Map<String, Object> configuration, ResourceBundle resourceBundle) throws IOException {
        configuration.put(CLUSTER_NODES_COUNT, Integer.parseInt(resourceBundle.getString(CLUSTER_NODES_COUNT)));
        configuration.put(NETWORK_INTERFACE_IP_FILTER, InetAddress.getByName(resourceBundle.getString(NETWORK_INTERFACE_IP_FILTER)));
        configuration.put(NETWORK_MULTICAST_GROUP_ADDRESS, InetAddress.getByName(resourceBundle.getString(NETWORK_MULTICAST_GROUP_ADDRESS)));
        configuration.put(NETWORK_MULTICAST_GROUP_PORT, Integer.parseInt(resourceBundle.getString(NETWORK_MULTICAST_GROUP_PORT)));
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
    }

    private static boolean isConfigurationValid(Map<String, Object> configuration) {
        return configuration.containsKey(CLUSTER_NODES_COUNT)
                && configuration.containsKey(NETWORK_INTERFACE_IP_FILTER)
                && configuration.containsKey(NETWORK_MULTICAST_GROUP_ADDRESS)
                && configuration.containsKey(NETWORK_MULTICAST_GROUP_PORT);
    }
}
