package io.nirahtech.petvet.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.base.PetIdentifier;
import io.nirahtech.petvet.features.FeaturesRegistry;
import io.nirahtech.petvet.features.pets.RetrieveAnimalInformationSheetFeature;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PetDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PET = "pet";

    private Pet pet;

    private final RetrieveAnimalInformationSheetFeature feature = FeaturesRegistry.getInstance().retrieveAnimalInformationSheetFeature();

    private TextView petNameTextView;
    private TextView petSpeciesTextView;
    private TextView petBreedTextView;
    private TextView petSexeTextView;
    private TextView petBirthDateTextView;
    private TextView petAdoptionDateTextView;
    private TextView petWweightDateTextView;

    public PetDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pet_detail, container, false);

        if (Objects.nonNull(getArguments())) {
            final Pet pet = (Pet) getArguments().getSerializable(ARG_PET);
            final PetIdentifier petIdentifier = (PetIdentifier) getArguments().getSerializable(ARG_PET);
            try {
                this.feature.retrieveAnimalInformationSheet(petIdentifier).ifPresent(petFound -> {
                    this.pet = petFound;
                });
            } catch (FeatureExecutionException e) {
                throw new RuntimeException(e);
            }
            // if (Objects.nonNull(pet)) {
                // this.pet = pet;
            // }
        }

        if (Objects.nonNull(this.pet)) {
            this.petNameTextView = rootView.findViewById(R.id.petName);
            this.petSpeciesTextView = rootView.findViewById(R.id.petSpecies);
            this.petBreedTextView = rootView.findViewById(R.id.petBreed);
            this.petSexeTextView = rootView.findViewById(R.id.petSexe);
            this.petBirthDateTextView = rootView.findViewById(R.id.petBirthDate);
            this.petAdoptionDateTextView = rootView.findViewById(R.id.petAdoptionDate);
            this.petWweightDateTextView = rootView.findViewById(R.id.petWeight);

            if (Objects.nonNull(this.pet.getName())) {
                this.petNameTextView.setText(this.pet.getName().toUpperCase());
            } else {
                this.petNameTextView.setText("");
            }
            if (Objects.nonNull(this.pet.getAnimal().getSpecies())) {
                this.petSpeciesTextView.setText(this.pet.getAnimal().getSpecies().getName().toUpperCase());
            } else {
                this.petSpeciesTextView.setText("");
            }
            if (Objects.nonNull(this.pet.getAnimal().getBreed())) {
                this.petBreedTextView.setText(this.pet.getAnimal().getBreed().getName().toUpperCase());
            } else {
                this.petBreedTextView.setText("");
            }
            if (Objects.nonNull(this.pet.getAnimal().getGender())) {
                this.petSexeTextView.setText(this.pet.getAnimal().getGender().name());
            } else {
                this.petSexeTextView.setText("");
            }
            if (Objects.nonNull(this.pet.getAnimal().getBirthDate())) {
                this.petBirthDateTextView.setText(this.pet.getAnimal().getBirthDate().toString());
            } else {
                this.petBirthDateTextView.setText("");
            }
            if (Objects.nonNull(this.pet.getAdoptionDate())) {
                this.petAdoptionDateTextView.setText(this.pet.getAdoptionDate().toString());
            } else {
                this.petAdoptionDateTextView.setText("");
            }
        }

        FloatingActionButton fabOpen = rootView.findViewById(R.id.fabOpen);
        FloatingActionButton fabClose = rootView.findViewById(R.id.fabClose);
        ExtendedFloatingActionButton fabAddConsultationToHealthBook = rootView.findViewById(R.id.fabAddConsultationToHealthBook);
        ExtendedFloatingActionButton fabAddEventToCalendar = rootView.findViewById(R.id.fabAddEventToCalendar);
        ExtendedFloatingActionButton fabAddCureToPharmacy = rootView.findViewById(R.id.fabAddCureToPharmacy);

        fabOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle visibility of fab1, fab2, and fab3
                if (fabOpen.getVisibility() == View.VISIBLE) {
                    fabAddConsultationToHealthBook.show();
                    fabAddEventToCalendar.show();
                    fabAddCureToPharmacy.show();
                    fabClose.show();
                    fabOpen.hide();

                }
            }
        });
        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle visibility of fab1, fab2, and fab3
                if (fabClose.getVisibility() == View.VISIBLE) {
                    fabAddConsultationToHealthBook.hide();
                    fabAddEventToCalendar.hide();
                    fabAddCureToPharmacy.hide();
                    fabOpen.show();
                    fabClose.hide();

                }
            }
        });


        return rootView;

    }
}