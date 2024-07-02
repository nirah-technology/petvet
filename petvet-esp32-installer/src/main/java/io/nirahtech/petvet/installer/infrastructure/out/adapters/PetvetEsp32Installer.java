package io.nirahtech.petvet.installer.infrastructure.out.adapters;

final class PetvetEsp32Installer implements Installer {

    private final Televerser televerser;

    public PetvetEsp32Installer(final Televerser televerser) {
        this.televerser = televerser;
    }

    @Override
    public void install() {
        this.televerser.televerse(null, null);
    }
    
}
