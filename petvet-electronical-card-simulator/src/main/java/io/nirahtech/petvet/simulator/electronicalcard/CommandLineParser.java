package io.nirahtech.petvet.simulator.electronicalcard;

import java.util.HashMap;
import java.util.Map;

public final class CommandLineParser {
    private final Map<String, String> options = new HashMap<>();
    private final String[] args;

    public CommandLineParser(String[] args) {
        this.args = args;
        parse();
    }

    private void parse() {
        for (int index = 0; index < args.length; index++) {
            switch (args[index]) {
                case "--help":
                case "-h":
                    options.put("help", "true");
                    break;
                case "--config":
                case "-c":
                    if (index + 1 < args.length) {
                        options.put("config", args[++index]);
                    } else {
                        throw new IllegalArgumentException("Missing value for option: --config or -c");
                    }
                    break;
                case "--group":
                case "-g":
                    if (index + 1 < args.length) {
                        options.put("group", args[++index]);
                    } else {
                        throw new IllegalArgumentException("Missing value for option: --group or -g");
                    }
                    break;
                case "--port":
                case "-p":
                    if (index + 1 < args.length) {
                        options.put("port", args[++index]);
                    } else {
                        throw new IllegalArgumentException("Missing value for option: --port or -p");
                    }
                    break;
                case "--network":
                case "-n":
                    if (index + 1 < args.length) {
                        options.put("network", args[++index]);
                    } else {
                        throw new IllegalArgumentException("Missing value for option: --network or -n");
                    }
                    break;
                case "--size":
                case "-s":
                    if (index + 1 < args.length) {
                        options.put("size", args[++index]);
                    } else {
                        throw new IllegalArgumentException("Missing value for option: --size or -s");
                    }
                    break;
                case "--mode":
                case "-m":
                    if (index + 1 < args.length) {
                        options.put("mode", args[++index]);
                    } else {
                        throw new IllegalArgumentException("Missing value for option: --mode or -m");
                    }
                    break;
                case "--scan-interval":
                case "-a":
                    if (index + 1 < args.length) {
                        options.put("scan-interval", args[++index]);
                    } else {
                        throw new IllegalArgumentException("Missing value for option: --scan-interval or -a");
                    }
                    break;
                case "--orchestrator-interval":
                case "-o":
                    if (index + 1 < args.length) {
                        options.put("orchestrator-interval", args[++index]);
                    } else {
                        throw new IllegalArgumentException("Missing value for option: --orchestrator-interval or -o");
                    }
                    break;
                case "--heartbeat-interval":
                case "-l":
                    if (index + 1 < args.length) {
                        options.put("heartbeat-interval", args[++index]);
                    } else {
                        throw new IllegalArgumentException("Missing value for option: --heartbeat-interval or -l");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown option: " + args[index]);
            }
        }
    }

    public boolean hasOption(String key) {
        return options.containsKey(key);
    }

    public String getOptionValue(String key) {
        return options.get(key);
    }

    public static void printHelp() {
        System.out.println("Usage:");
        System.out.println("  --help, -h                    Show help");
        System.out.println("  --size, -s                    Nodes count in the cluster");
        System.out.println("  --config, -c                  Configuration file");
        System.out.println("  --group, -g                   Multicast group");
        System.out.println("  --port, -p                    Port number");
        System.out.println("  --network, -n                 Network filter");
        System.out.println("  --mode, -m                    Dfault mode of nodes");
        System.out.println("  --scan-interval, -a           Scan interval in ms");
        System.out.println("  --orchestrator-interval, -o   Orchestrator discovery interval in ms");
        System.out.println("  --heartbeat-interval, -l      Node heartbeat interval in ms");
    }
}
