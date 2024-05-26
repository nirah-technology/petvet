package io.nirahtech.petvet.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.animalpark.Breed;
import io.nirahtech.petvet.core.animalpark.Species;
import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.util.Weight;
import io.nirahtech.petvet.services.storage.LocalStorageService;
import io.nirahtech.petvet.services.storage.StorageService;
import io.nirahtech.petvet.ui.adapters.SpeciesAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FarmExpositionFragment} factory method to
 * create an instance of this fragment.
 */
public class FarmExpositionFragment extends Fragment {

    private static final StorageService STORAGE_SERVICE = new LocalStorageService();
    private static final String DATABASE_FILE_NAME = "house.db";

    private RecyclerView speciesRecyclerView;
    private SpeciesAdapter speciesAdapter;
    private Map<Species, Set<Pet>> petsBySpecies;



    private final Set<Species> getAllSpecies() {
        final Set<Species> species = new HashSet<>();
        if (Objects.nonNull(this.petsBySpecies)) {
            species.addAll(this.petsBySpecies.keySet());
        }
        return species;
    }

    private final Set<Pet> getAllPetsBySpecies(final Species species) {
        final Set<Pet> pets = new HashSet<>();
        if (Objects.nonNull(this.petsBySpecies)) {
            pets.addAll(this.petsBySpecies.get(species));
        }
        return pets;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private final void preventErrorOnLoadFailure() {
        if (Objects.isNull(this.petsBySpecies)) {
            this.petsBySpecies = new HashMap<>();

            final Set<Pet> cats = new HashSet<>();
            final Species cat = Species.of("Chat");
            final Breed european = Breed.of("European Cat");
            final Animal nitroAsAnimal = new Animal();
            this.petsBySpecies.put(cat, cats);
            nitroAsAnimal.setSpecies(cat);
            nitroAsAnimal.setBreed(european);
            nitroAsAnimal.setWeight(Weight.kg(7.2D));
            Pet nitroAsPet = new Pet(nitroAsAnimal, "Nitro", LocalDate.of(2017, 3, 17));
            cats.add(nitroAsPet);

            final Set<Pet> dogs = new HashSet<>();
            final Species dog = Species.of("Chien");
            final Breed yorkshire = Breed.of("Yorkshire");
            final Animal yumaAsAnimal = new Animal();
            this.petsBySpecies.put(dog, dogs);
            yumaAsAnimal.setSpecies(dog);
            yumaAsAnimal.setBreed(yorkshire);
            yumaAsAnimal.setWeight(Weight.kg(4.7D));
            Pet yumaAsPet = new Pet(yumaAsAnimal, "Yuma", LocalDate.of(2017, 3, 17));
            dogs.add(yumaAsPet);

            final Set<Pet> galinaceas = new HashSet<>();
            final Species galinacea = Species.of("Galinacée");
            final Breed couNu = Breed.of("Cou Nu");
            final Animal picsouAsAnimal = new Animal();
            this.petsBySpecies.put(galinacea, galinaceas);
            picsouAsAnimal.setSpecies(galinacea);
            picsouAsAnimal.setBreed(couNu);
            picsouAsAnimal.setWeight(Weight.kg(4.2D));
            Pet picsouAsPet = new Pet(picsouAsAnimal, "Picsou", LocalDate.of(2023, 9, 9));
            galinaceas.add(picsouAsPet);


            final Set<Pet> bovids = new HashSet<>();
            final Species bovid = Species.of("Bovidés");
            final Breed dwarfGoat = Breed.of("Naine");
            final Animal djaliAsAnimal = new Animal();
            this.petsBySpecies.put(bovid, bovids);
            djaliAsAnimal.setSpecies(bovid);
            djaliAsAnimal.setBreed(dwarfGoat);
            djaliAsAnimal.setWeight(Weight.kg(14.24D));
            Pet djaliAsPet = new Pet(djaliAsAnimal, "DJali", LocalDate.of(2024, 9, 9));
            bovids.add(djaliAsPet);

        }
    }

    private final void loadHouse() throws IOException, ClassNotFoundException {
        final File databaseFile = new File( STORAGE_SERVICE.getMediaStore(this.getContext()), DATABASE_FILE_NAME);
        if (STORAGE_SERVICE.exists(databaseFile)) {
            final House house = STORAGE_SERVICE.load(databaseFile);
            house.getFarm().ifPresent(farm -> {
                for (Pet pet : farm.getPets()) {
                    Species species = pet.getAnimal().getSpecies();
                    this.petsBySpecies.computeIfAbsent(species, k -> new HashSet<>()).add(pet);
                }
            });
        }
        this.preventErrorOnLoadFailure();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farm_exposition, container, false);

        // Initialiser la liste des espèces et des animaux
        try {
            this.loadHouse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        speciesRecyclerView = view.findViewById(R.id.speciesRecyclerView);
        speciesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        speciesAdapter = new SpeciesAdapter(this.petsBySpecies);
        speciesRecyclerView.setAdapter(speciesAdapter);

        return view;
    }
}