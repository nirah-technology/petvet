package io.nirahtech.petvet.geopulsetracker.domain;

public abstract class ElectronicChipBoard implements MicroController {

    private final int widthInCm;
    private final int heightInCm;
    protected ElectronicChipBoard(final int widthInCm, final int heightInCm) {
        this.widthInCm = widthInCm;
        this.heightInCm = heightInCm;
    }

    public int getWidthInCm() {
        return widthInCm;
    }

    public int getHeightInCm() {
        return heightInCm;
    }

    @Override
    public void powerOn() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void powerOff() {
        // TODO Auto-generated method stub
        
    }

}
