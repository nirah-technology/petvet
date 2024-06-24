package io.nirahtech.petvet.phytotanica;

import java.io.File;

public record Plant(
    String scientificName,
    String commonName,
    String description,
    CultivationPeriod cultivationPeriod,
    Height height,
    Habitat habitat,
    String harvest,
    String usage,
    File image
)  {
    
}
