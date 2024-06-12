package io.nirahtech.petvet.features.emergency.implementations;

import java.util.Objects;

import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Human;
import io.nirahtech.petvet.core.clinic.Company;
import io.nirahtech.petvet.core.clinic.Vet;
import io.nirahtech.petvet.core.contact.Email;
import io.nirahtech.petvet.core.contact.PhoneNumber;
import io.nirahtech.petvet.features.emergency.RegisterNewVetContactFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

public class DefaultRegisterNewVetFeatureImpl implements RegisterNewVetContactFeature {
    private static RegisterNewVetContactFeature instance;

    public static final RegisterNewVetContactFeature getInstance() {
        if (Objects.isNull(instance)) {
            final House house = House.getInstance();
            instance = new DefaultRegisterNewVetFeatureImpl(house);
        }
        return instance;
    }

    private final House house;

    private DefaultRegisterNewVetFeatureImpl(final House house) {
        this.house = house;
    }

    @Override
    public Vet registerNewVet(String vetFirstName, String vetLastName, PhoneNumber vetPhoneNumber, Email vetEmail,
            String companyName, PhoneNumber companyPhoneNumber, Email companyEmail) throws FeatureExecutionException {

        final Human human = new Human(vetFirstName, vetLastName);
        final Company company = Company.of(companyName);
        company.getContact().setEmail(companyEmail);
        company.getContact().setPhoneNumber(companyPhoneNumber);
        final Vet vet = new Vet(human, company);
        vet.getContact().setEmail(vetEmail);
        vet.getContact().setPhoneNumber(vetPhoneNumber);
        this.house.getVetDirectory().add(vet);
        return vet;


    }

}
