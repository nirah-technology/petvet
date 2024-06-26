package io.nirahtech.petvet.geopulsetracker.domain;

public final class DecibelDistanceConverter {

    private static final int WIFI_FREQUENCY_IN_MHZ = 2_400;
    private static final int BLUETOOTH_FREQUENCY_IN_MHZ = 2_402;

    private DecibelDistanceConverter() { }
    
    public static final double fromFrequencyAndSignalToMeters(int frequency, float signalLevelInDb) {
        final double exp = (27.55 - (20 * Math.log10(frequency)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);

    }

    public static final double wifiSignalToMeters(float signalLevelInDb) {
        return fromFrequencyAndSignalToMeters(WIFI_FREQUENCY_IN_MHZ, signalLevelInDb);
    }

    public static final double bluetoothSignalToMeters(float signalLevelInDb) {
        return fromFrequencyAndSignalToMeters(BLUETOOTH_FREQUENCY_IN_MHZ, signalLevelInDb);
    }
}
