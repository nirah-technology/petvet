package io.nirahtech.petvet.ui.fragments.adoption;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.animalpark.Breed;
import io.nirahtech.petvet.core.animalpark.Gender;
import io.nirahtech.petvet.core.animalpark.Species;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.clinic.HealthBook;
import io.nirahtech.petvet.services.house.HouseService;
import io.nirahtech.petvet.services.house.HouseServiceImpl;
import io.nirahtech.petvet.ui.validators.AdoptionDateValidator;

public class PetAdoptionFragment extends Fragment {

    private HouseService houseService;

    // Name
    private TextInputEditText editTextName;

    // Species
    private TextInputEditText editTextSpecies;

    // Breed
    private TextInputEditText editTextBreed;

    // Birth Date
    private TextInputEditText editTextBirthDate;
    private MaterialDatePicker<Long> datePickerBirthDate;
    private LocalDate birthDate;

    // Adoption Date
    private TextInputEditText editTextAdoptionDate;
    private MaterialDatePicker<Long> datePickerAdoptionDate;
    private LocalDate adoptionDate;

    // Gender
    private MaterialButtonToggleGroup buttonToggleGroupGender;
    private Gender gender;


    private Button confirmAdoptionButton;

    private final Pet createPet() {
        final Animal animal = new Animal();
        final Species species = Species.of(this.editTextSpecies.getText().toString().trim().toUpperCase());
        final Breed breed = Breed.of(this.editTextBreed.getText().toString().trim().toUpperCase());
        final String name = this.editTextName.getText().toString().trim().toUpperCase();
        animal.setBreed(breed);
        animal.setSpecies(species);
        animal.setGender(this.gender);
        animal.birth(this.birthDate);
        Pet pet = new Pet(animal, name, this.adoptionDate);
        return pet;
    }

    private Optional<String> retrieveName(View view) {
        Optional<String> value = Optional.empty();
        if (Objects.isNull(this.editTextName)) {
            this.editTextName = view.findViewById(R.id.editTextAnimalName);
        }
        if (Objects.nonNull(this.editTextName) && Objects.nonNull(this.editTextName.getText())) {
            if (!this.editTextName.getText().toString().trim().isEmpty()) {
                value = Optional.ofNullable(this.editTextName.getText().toString().toUpperCase());
            }
        }
        return value;
    }
    private Optional<String> retrieveSpecies(View view) {
        Optional<String> value = Optional.empty();
        if (Objects.isNull(this.editTextSpecies)) {
            this.editTextSpecies = view.findViewById(R.id.editTextAnimalSpecies);
        }
        if (Objects.nonNull(this.editTextSpecies) && Objects.nonNull(this.editTextSpecies.getText())) {
            if (!this.editTextSpecies.getText().toString().trim().isEmpty()) {
                value = Optional.ofNullable(this.editTextSpecies.getText().toString().toUpperCase());
            }
        }
        return value;
    }

    private Optional<String> retrieveBreed(View view) {
        Optional<String> value = Optional.empty();
        if (Objects.isNull(this.editTextBreed)) {
            this.editTextBreed = view.findViewById(R.id.editTextAnimalBreed);
        }
        if (Objects.nonNull(this.editTextBreed) && Objects.nonNull(this.editTextBreed.getText())) {
            if (!this.editTextBreed.getText().toString().trim().isEmpty()) {
                value = Optional.ofNullable(this.editTextBreed.getText().toString().toUpperCase());
            }
        }
        return value;
    }

    private void handleAdoptionButtonOnClick(View view) {
        final Optional<String> name = this.retrieveName(view);
        final Optional<String> speciesName = this.retrieveSpecies(view);
        final Optional<String> breedName = this.retrieveBreed(view);
        if (!name.isPresent()) {
            this.editTextName.setError("Le nom de l'animal est obligatoire.");
            this.editTextName.setSelected(true);
        }
        else if (!speciesName.isPresent()) {
            this.editTextSpecies.setError("L'espèce de "+name.get()+" est obligatoire.");
            this.editTextSpecies.setSelected(true);
        } else if (!breedName.isPresent()) {
            this.editTextBreed.setError("La race de "+name.get()+" est obligatoire.");
            this.editTextBreed.setSelected(true);
        } else if (Objects.isNull(this.gender)) {

        }
        else if (Objects.isNull(this.birthDate)) {
            this.editTextBirthDate.setError("La date de naissance de "+name.get()+" est obligatoire..");
            this.editTextBirthDate.setSelected(true);
        } else if (Objects.isNull(this.adoptionDate)) {
            this.editTextBirthDate.setError("La date d'adoption de " + name.get() + " est obligatoire..");
            this.editTextAdoptionDate.setSelected(true);
        } else if (this.adoptionDate.isBefore(this.birthDate)) {
            this.editTextBirthDate.setError("La date d'adoption de " + name.get() + " ne peut pas être antérieur à la date de naissance.");
            this.editTextAdoptionDate.setSelected(true);
        } else {
            final Pet pet = createPet();
            final House house = this.houseService.getHouse().get();
            final HealthBook healthBook = house.adopt(pet.getAnimal(), pet.getName(), pet.getAdoptionDate());
            final NavController navController = Navigation.findNavController(view);
            navController.navigateUp();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.houseService = HouseServiceImpl.getInstance(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_adoption, container, false);

        // Find views by ID using the inflated view
        this.editTextBirthDate = view.findViewById(R.id.editTextBirthDate);
        this.editTextAdoptionDate = view.findViewById(R.id.editTextAdoptionDate);
        this.confirmAdoptionButton = view.findViewById(R.id.confirmAdoptionButton);
        this.buttonToggleGroupGender = view.<MaterialButtonToggleGroup>findViewById(R.id.toggleButton);

        this.datePickerBirthDate = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Sélectionnez la date de Naissance")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        this.datePickerBirthDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                final Date date = new Date(selection + offsetFromUTC);
                birthDate = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                editTextBirthDate.setText(simpleFormat.format(date));

                // Set minimum date for adoption date picker
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setStart(date.getTime());
                constraintsBuilder.setValidator(new AdoptionDateValidator(birthDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()));
                datePickerAdoptionDate = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Sélectionnez la date d'adoption")
                        .setSelection(selection)
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();
                datePickerAdoptionDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        TimeZone timeZoneUTC = TimeZone.getDefault();
                        int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        final Date date = new Date(selection + offsetFromUTC);
                        adoptionDate = date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        editTextAdoptionDate.setText(simpleFormat.format(date));
                    }
                });
            }
        });

        this.editTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerBirthDate.show(getActivity().getSupportFragmentManager(), "datePickerBirthDateTag");
            }
        });

        this.editTextAdoptionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.nonNull(birthDate)) {
                    datePickerAdoptionDate.show(getActivity().getSupportFragmentManager(), "datePickerAdoptionDateTag");
                } else {
                    // Optionally, show a message indicating that birth date needs to be selected first
                }
            }
        });


        this.confirmAdoptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAdoptionButtonOnClick(view);
            }
        });

        this.buttonToggleGroupGender.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if (isChecked) {
                    if (checkedId == R.id.buttonGenderMale) {
                        gender = Gender.MALE;
                    }
                    else if (checkedId == R.id.buttonGenderFemale) {
                        gender = Gender.FEMALE;
                    }
                    else if (checkedId == R.id.buttonGenderHermaphodite) {
                        gender = Gender.HERMAPHRODITE;
                    } else {
                        gender = Gender.HERMAPHRODITE;

                    }
                }
            }
        });

        return view;
    }


}
