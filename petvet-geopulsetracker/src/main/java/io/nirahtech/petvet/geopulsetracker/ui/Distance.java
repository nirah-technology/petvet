package io.nirahtech.petvet.geopulsetracker.ui;

import io.nirahtech.petvet.geopulsetracker.domain.DecibelDistanceConverter;
import io.nirahtech.petvet.geopulsetracker.domain.ElectronicChipBoard;

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

}
