package io.nirahtech.petvet.geopulsetracker.domain;

public final class Distance {
    private final ElectronicChipBoard from;
    private final ElectronicChipBoard to;
    private float signalStrenghtInDBM;
    
    /**
     * @param from
     * @param to
     * @param distance
     */
    public Distance(final ElectronicChipBoard from, final ElectronicChipBoard to, float signalStrenghtInDBM) {
        this.from = from;
        this.to = to;
        this.signalStrenghtInDBM = signalStrenghtInDBM;
    }
    /**
     * @return the from
     */
    public ElectronicChipBoard getFrom() {
        return from;
    }
    /**
     * @return the to
     */
    public ElectronicChipBoard getTo() {
        return to;
    }
    /**
     * @return the signalStrenghtInDBM
     */
    public float getSignalStrenghtInDBM() {
        return signalStrenghtInDBM;
    }
    /**
     * @param signalStrenghtInDBM the signalStrenghtInDBM to set
     */
    public void setSignalStrenghtInDBM(float signalStrenghtInDBM) {
        this.signalStrenghtInDBM = signalStrenghtInDBM;
    }

    public double computeDistanceInMeters() {
        return DecibelDistanceConverter.wifiSignalToMeters(signalStrenghtInDBM);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("DISTANCE[")
                .append("from=")
                .append(((ESP32)this.from).getWifi().getMacAddress())
                .append(", to=")
                .append(((ESP32)this.to).getWifi().getMacAddress())
                .append(", dbm=")
                .append(this.signalStrenghtInDBM)
                .append(", meters=")
                .append(this.computeDistanceInMeters())
                .append("]")
                .toString();
    }

}
