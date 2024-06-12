package io.nirahtech.petvet.features.emergency.implementations;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.clinic.Vet;
import io.nirahtech.petvet.core.contact.Contact;
import io.nirahtech.petvet.core.util.identifier.Identifier;
import io.nirahtech.petvet.features.emergency.ModifyVetFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultModifyVetFeatureImpl implements ModifyVetFeature {
    private static ModifyVetFeature instance;

    public static final ModifyVetFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultModifyVetFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultModifyVetFeatureImpl(final House house) {
        this.house = house;
    }

    @Override
    public Vet modifyVetContact(Identifier vetIdenfitifer, String firstName, String lastName, LocalDate birthDate,
            Contact humanContact, String companyName, Contact companyContact, Contact vetContact)
            throws FeatureExecutionException {
        final Optional<Vet> vetFound = this.house.getVetDirectory().listAll()
                .filter(vet -> vet.getIdentifier().equals(vetContact))
                .findFirst();
        if (!vetFound.isPresent()) {
            throw new FeatureExecutionException("Vet not found for: ModifyVetFeature");
        }
        final Vet vet = vetFound.get();

        if (Objects.nonNull(firstName)) {
            vet.getHuman().setFirstName(firstName);
        }
        if (Objects.nonNull(lastName)) {
            vet.getHuman().setLastName(lastName);
        }
        if (Objects.nonNull(birthDate)) {
            vet.getHuman().setBirthDate(birthDate);
        }

        if (Objects.nonNull(humanContact)) {
            if (humanContact.getPhoneNumber().isPresent()) {
                vet.getHuman().getContact().setPhoneNumber(humanContact.getPhoneNumber().get());
            }
            if (humanContact.getEmail().isPresent()) {
                vet.getHuman().getContact().setEmail(humanContact.getEmail().get());
            }
            if (humanContact.getMail().isPresent()) {
                vet.getHuman().getContact().setMail(humanContact.getMail().get());
            }
        }

        if (Objects.nonNull(companyName)) {
            // vet.getCompany().setName(companyName);
        }

        if (Objects.nonNull(companyContact)) {
            if (companyContact.getPhoneNumber().isPresent()) {
                vet.getCompany().getContact().setPhoneNumber(companyContact.getPhoneNumber().get());
            }
            if (companyContact.getEmail().isPresent()) {
                vet.getCompany().getContact().setEmail(companyContact.getEmail().get());
            }
            if (companyContact.getMail().isPresent()) {
                vet.getCompany().getContact().setMail(companyContact.getMail().get());
            }
        }


        if (Objects.nonNull(vetContact)) {
            if (vetContact.getPhoneNumber().isPresent()) {
                vet.getContact().setPhoneNumber(vetContact.getPhoneNumber().get());
            }
            if (vetContact.getEmail().isPresent()) {
                vet.getContact().setEmail(vetContact.getEmail().get());
            }
            if (vetContact.getMail().isPresent()) {
                vet.getContact().setMail(vetContact.getMail().get());
            }
        }

        return vet;
    }

}
