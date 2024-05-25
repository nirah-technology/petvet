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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.animalpark.Species;
import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Pet;
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

    private final void loadHouse() throws IOException, ClassNotFoundException {
        final File databaseFile = null;
        final House house = STORAGE_SERVICE.load(databaseFile);
        house.getFarm().ifPresent(farm -> {
            for (Pet pet : farm.getPets()) {
                Species species = pet.getAnimal().getSpecies();
                this.petsBySpecies.computeIfAbsent(species, k -> new HashSet<>()).add(pet);
            }
        });
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