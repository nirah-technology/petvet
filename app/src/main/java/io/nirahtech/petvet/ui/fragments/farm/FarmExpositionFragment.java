package io.nirahtech.petvet.ui.fragments.farm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.animalpark.Animal;
import io.nirahtech.petvet.core.animalpark.Breed;
import io.nirahtech.petvet.core.animalpark.Species;
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

    private Button adoptionButton;



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
            final Breed chartreux = Breed.of("Chartreux");
            final Breed manx = Breed.of("Manx");
            this.petsBySpecies.put(cat, cats);

            // Nitro
            final Animal nitroAsAnimal = new Animal();
            nitroAsAnimal.setSpecies(cat);
            nitroAsAnimal.setBreed(european);
            nitroAsAnimal.setWeight(Weight.kg(7.2D));
            Pet nitroAsPet = new Pet(nitroAsAnimal, "Nitro", LocalDate.of(2017, 3, 17));
            cats.add(nitroAsPet);

            // Nala
            final Animal nalaAsAnimal = new Animal();
            nalaAsAnimal.setSpecies(cat);
            nalaAsAnimal.setBreed(european);
            nalaAsAnimal.setWeight(Weight.kg(7.2D));
            Pet nalaAsPet = new Pet(nalaAsAnimal, "Nala", LocalDate.of(2017, 3, 17));
            cats.add(nalaAsPet);

            // Simba
            final Animal simbaAsAnimal = new Animal();
            simbaAsAnimal.setSpecies(cat);
            simbaAsAnimal.setBreed(european);
            simbaAsAnimal.setWeight(Weight.kg(7.2D));
            Pet simbaAsPet = new Pet(simbaAsAnimal, "Simba", LocalDate.of(2017, 3, 17));
            cats.add(simbaAsPet);

            // Ed
            final Animal edAsAnimal = new Animal();
            edAsAnimal.setSpecies(cat);
            edAsAnimal.setBreed(european);
            edAsAnimal.setWeight(Weight.kg(7.2D));
            Pet edAsPet = new Pet(edAsAnimal, "Simba", LocalDate.of(2017, 3, 17));
            cats.add(edAsPet);

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
            final Breed sussex = Breed.of("Sussex");
            final Breed greyCendre = Breed.of("Gris Cendré");

            // Picsou
            final Animal picsouAsAnimal = new Animal();
            this.petsBySpecies.put(galinacea, galinaceas);
            picsouAsAnimal.setSpecies(galinacea);
            picsouAsAnimal.setBreed(couNu);
            picsouAsAnimal.setWeight(Weight.kg(5.7D));
            Pet picsouAsPet = new Pet(picsouAsAnimal, "Picsou", LocalDate.of(2023, 9, 9));
            galinaceas.add(picsouAsPet);

            // Riri
            final Animal ririAsAnimal = new Animal();
            ririAsAnimal.setSpecies(galinacea);
            ririAsAnimal.setBreed(couNu);
            ririAsAnimal.setWeight(Weight.kg(4.2D));
            Pet ririAsPet = new Pet(ririAsAnimal, "Riri", LocalDate.of(2023, 9, 9));
            galinaceas.add(ririAsPet);

            // Fifi
            final Animal fifiAsAnimal = new Animal();
            fifiAsAnimal.setSpecies(galinacea);
            fifiAsAnimal.setBreed(couNu);
            fifiAsAnimal.setWeight(Weight.kg(4.2D));
            Pet fifiAsPet = new Pet(fifiAsAnimal, "Fifi", LocalDate.of(2023, 9, 9));
            galinaceas.add(fifiAsPet);

            // Loulou
            final Animal loulouAsAnimal = new Animal();
            loulouAsAnimal.setSpecies(galinacea);
            loulouAsAnimal.setBreed(couNu);
            loulouAsAnimal.setWeight(Weight.kg(4.2D));
            Pet loulouAsPet = new Pet(loulouAsAnimal, "Loulou", LocalDate.of(2023, 9, 9));
            galinaceas.add(loulouAsPet);



            // Solo
            final Animal soloAnimal = new Animal();
            soloAnimal.setSpecies(galinacea);
            soloAnimal.setBreed(sussex);
            soloAnimal.setWeight(Weight.kg(4.2D));
            Pet soloAsPet = new Pet(soloAnimal, "Solo", LocalDate.of(2023, 9, 9));
            galinaceas.add(soloAsPet);

            // Séké
            final Animal sekeAsAnimal = new Animal();
            sekeAsAnimal.setSpecies(galinacea);
            sekeAsAnimal.setBreed(sussex);
            sekeAsAnimal.setWeight(Weight.kg(4.2D));
            Pet sekeAsPet = new Pet(sekeAsAnimal, "Séké", LocalDate.of(2023, 9, 9));
            galinaceas.add(sekeAsPet);

            // Gaïa
            final Animal gaiaAsAnimal = new Animal();
            gaiaAsAnimal.setSpecies(galinacea);
            gaiaAsAnimal.setBreed(greyCendre);
            gaiaAsAnimal.setWeight(Weight.kg(4.2D));
            Pet gaiaAsPet = new Pet(gaiaAsAnimal, "Gaïa", LocalDate.of(2023, 9, 9));
            galinaceas.add(gaiaAsPet);

            // Cargo
            final Animal cargoAsAnimal = new Animal();
            cargoAsAnimal.setSpecies(galinacea);
            cargoAsAnimal.setBreed(greyCendre);
            cargoAsAnimal.setWeight(Weight.kg(4.2D));
            Pet cargoAsPet = new Pet(cargoAsAnimal, "Cargo", LocalDate.of(2023, 9, 9));
            galinaceas.add(cargoAsPet);

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

        this.adoptionButton = view.findViewById(R.id.adoptionButton);
        this.adoptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_pet_adoption);
            }
        });

        return view;
    }
}