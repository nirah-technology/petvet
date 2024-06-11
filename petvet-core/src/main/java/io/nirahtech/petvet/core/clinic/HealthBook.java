package io.nirahtech.petvet.core.clinic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.nirahtech.petvet.core.base.Human;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.util.Weight;

public final class HealthBook implements Serializable {
    private HealthBookIdentifier identifier;
    private final Pet pet;
    private Human owner;
    private final Set<Consultation> consultations;
    private final Set<Disease> diseases;
    private final Set<Vaccination> vaccinations;
    private final Set<Surgery> surgeries;
    private final Set<String> allergicReactions; // Added for allergic reactions

    public HealthBook(final Pet pet) {
        this.pet = Objects.requireNonNull(pet, "Pet for healthbook is required.");
        this.consultations = new LinkedHashSet<>();
        this.vaccinations = new HashSet<>();
        this.surgeries = new HashSet<>();
        this.diseases = new HashSet<>();
        this.allergicReactions = new HashSet<>(); // Initialize allergic reactions
    }

    public HealthBookIdentifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(HealthBookIdentifier identifier) {
        this.identifier = identifier;
    }

    public final Pet getPet() {
        return this.pet;
    }

    public Human getOwner() {
        return owner;
    }

    public void setOwner(Human owner) {
        this.owner = Objects.requireNonNull(owner, "Owner cannot be null.");
    }

    public final List<Consultation> getConsultations() {
        return Collections.unmodifiableList(new ArrayList<>(this.consultations));
    }

    public final void addConsultation(final Consultation consultation) {
        if (Objects.nonNull(consultation)) {
            this.consultations.add(consultation);
        } else {
            throw new IllegalArgumentException("Consultation cannot be null.");
        }
    }

    public final List<Vaccination> listAllVaccinations() {
        return Collections.unmodifiableList(new ArrayList<>(this.vaccinations));
    }

    public final List<Disease> listAllDiseases() {
        return Collections.unmodifiableList(new ArrayList<>(this.diseases));
    }

    public final List<Surgery> listAllSurgeries() {
        return Collections.unmodifiableList(new ArrayList<>(this.surgeries));
    }

    public final List<String> listAllAllergicReactions() {
        return Collections.unmodifiableList(new ArrayList<>(this.allergicReactions));
    }

    public final void addAllergicReaction(String reaction) {
        if (Objects.nonNull(reaction) && !reaction.trim().isEmpty()) {
            this.allergicReactions.add(reaction);
        } else {
            throw new IllegalArgumentException("Allergic reaction cannot be null or empty.");
        }
    }

    public final Map<LocalDate, Weight> computeWeightTimeLine() {
        final Map<LocalDate, Weight> weightTimeline = new HashMap<>();
        this.consultations.forEach(consultation -> {
            if (Objects.nonNull(consultation.getWeight())) {
                weightTimeline.put(consultation.getDateTime().toLocalDate(), consultation.getWeight());
            }
        });
        if (Objects.nonNull(pet.getAnimal().getWeight())) {
            weightTimeline.put(LocalDate.now(), pet.getAnimal().getWeight());
        }
        return weightTimeline;
    }
}
