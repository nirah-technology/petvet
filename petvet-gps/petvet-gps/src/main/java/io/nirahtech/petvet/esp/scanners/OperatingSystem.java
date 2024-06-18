package io.nirahtech.petvet.esp.scanners;

/**
 * OperatingSystem
 */
public final class OperatingSystem {
    private static final String OPERATING_SYSTEM_NAME = System.getProperty("os.name").toLowerCase();

    private OperatingSystem() {

    }

    public static final boolean isWindows() {
        return OPERATING_SYSTEM_NAME.contains("windows");
    }

    public static final boolean isLinux() {
        return OPERATING_SYSTEM_NAME.contains("linux");
    }

    public static final boolean isMac() {
        return OPERATING_SYSTEM_NAME.contains("mac");
    }
    
}