package io.nirahtech.petvet.geopulsetracker;

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
        System.out.println("  --help, -h       Show help");
        System.out.println("  --config, -c     Configuration file");
        System.out.println("  --group, -g      Multicast group");
        System.out.println("  --port, -p       Port number");
    }
}
