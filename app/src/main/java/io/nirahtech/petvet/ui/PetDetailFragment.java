package io.nirahtech.petvet.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.base.Pet;

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
            if (Objects.nonNull(pet)) {
                this.pet = pet;
            }
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
                this.petNameTextView.setText(this.pet.getName());
            }
            if (Objects.nonNull(this.pet.getAnimal().getSpecies())) {
                this.petSpeciesTextView.setText(this.pet.getAnimal().getSpecies().getName());
            }
            if (Objects.nonNull(this.pet.getAnimal().getBreed())) {
                this.petBreedTextView.setText(this.pet.getAnimal().getBreed().getName());
            }
            if (Objects.nonNull(this.pet.getAnimal().getGender())) {
                this.petSexeTextView.setText(this.pet.getAnimal().getGender().name());
            }
            if (Objects.nonNull(this.pet.getAnimal().getBirthDate())) {
                this.petBirthDateTextView.setText(this.pet.getAnimal().getBirthDate().toString());
            }
            if (Objects.nonNull(this.pet.getAdoptionDate())) {
                this.petAdoptionDateTextView.setText(this.pet.getAdoptionDate().toString());
            }
        }

        return rootView;

    }
}