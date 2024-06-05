package io.nirahtech.petvet.ui.fragments.farm;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import io.nirahtech.petvet.core.animalpark.Gender;
import io.nirahtech.petvet.core.animalpark.Species;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.core.util.Weight;
import io.nirahtech.petvet.services.storage.LocalStorageService;
import io.nirahtech.petvet.services.storage.StorageService;
import io.nirahtech.petvet.services.storage.house.HouseService;
import io.nirahtech.petvet.services.storage.house.HouseServiceImpl;
import io.nirahtech.petvet.ui.adapters.SpeciesAdapter;
import io.nirahtech.petvet.ui.adapters.listeners.OnPetClickEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FarmExpositionFragment} factory method to
 * create an instance of this fragment.
 */
public class FarmExpositionFragment extends Fragment {

    private static final StorageService STORAGE_SERVICE = new LocalStorageService();
    private static final HouseService HOUSE_SERVICE = HouseServiceImpl.getInstance();
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
            nitroAsAnimal.setGender(Gender.MALE);
            nitroAsAnimal.setWeight(Weight.kg(7.2D));
            Pet nitroAsPet = new Pet(nitroAsAnimal, "Nitro", LocalDate.of(2017, 3, 17));
            cats.add(nitroAsPet);
            HOUSE_SERVICE.get().adopt(nitroAsAnimal, nitroAsPet.getName(), nitroAsPet.getAdoptionDate());

            // Nala
            final Animal nalaAsAnimal = new Animal();
            nalaAsAnimal.setSpecies(cat);
            nalaAsAnimal.setBreed(manx);
            nalaAsAnimal.setGender(Gender.FEMALE);
            nalaAsAnimal.setWeight(Weight.kg(7.2D));
            Pet nalaAsPet = new Pet(nalaAsAnimal, "Nala", LocalDate.of(2017, 3, 17));
            cats.add(nalaAsPet);
            HOUSE_SERVICE.get().adopt(nalaAsAnimal, nalaAsPet.getName(), nalaAsPet.getAdoptionDate());

            // Simba
            final Animal simbaAsAnimal = new Animal();
            simbaAsAnimal.setSpecies(cat);
            simbaAsAnimal.setBreed(european);
            simbaAsAnimal.setGender(Gender.MALE);
            simbaAsAnimal.setWeight(Weight.kg(7.2D));
            Pet simbaAsPet = new Pet(simbaAsAnimal, "Simba", LocalDate.of(2017, 3, 17));
            cats.add(simbaAsPet);
            HOUSE_SERVICE.get().adopt(simbaAsAnimal, simbaAsPet.getName(), simbaAsPet.getAdoptionDate());

            // Ed
            final Animal edAsAnimal = new Animal();
            edAsAnimal.setSpecies(cat);
            edAsAnimal.setBreed(chartreux);
            edAsAnimal.setGender(Gender.MALE);
            edAsAnimal.setWeight(Weight.kg(7.2D));
            Pet edAsPet = new Pet(edAsAnimal, "Simba", LocalDate.of(2017, 3, 17));
            cats.add(edAsPet);
            HOUSE_SERVICE.get().adopt(edAsAnimal, edAsPet.getName(), edAsPet.getAdoptionDate());

            final Set<Pet> dogs = new HashSet<>();
            final Species dog = Species.of("Chien");
            final Breed yorkshire = Breed.of("Yorkshire");

            final Animal yumaAsAnimal = new Animal();
            this.petsBySpecies.put(dog, dogs);
            yumaAsAnimal.setSpecies(dog);
            yumaAsAnimal.setGender(Gender.FEMALE);
            yumaAsAnimal.setBreed(yorkshire);
            yumaAsAnimal.setWeight(Weight.kg(4.7D));
            Pet yumaAsPet = new Pet(yumaAsAnimal, "Yuma", LocalDate.of(2017, 3, 17));
            dogs.add(yumaAsPet);
            HOUSE_SERVICE.get().adopt(yumaAsAnimal, yumaAsPet.getName(), yumaAsPet.getAdoptionDate());

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
            picsouAsAnimal.setGender(Gender.MALE);
            picsouAsAnimal.setWeight(Weight.kg(5.7D));
            Pet picsouAsPet = new Pet(picsouAsAnimal, "Picsou", LocalDate.of(2023, 9, 9));
            galinaceas.add(picsouAsPet);
            HOUSE_SERVICE.get().adopt(picsouAsAnimal, picsouAsPet.getName(), picsouAsPet.getAdoptionDate());

            // Riri
            final Animal ririAsAnimal = new Animal();
            ririAsAnimal.setSpecies(galinacea);
            ririAsAnimal.setBreed(couNu);
            ririAsAnimal.setGender(Gender.FEMALE);
            ririAsAnimal.setWeight(Weight.kg(4.2D));
            Pet ririAsPet = new Pet(ririAsAnimal, "Riri", LocalDate.of(2023, 9, 9));
            galinaceas.add(ririAsPet);
            HOUSE_SERVICE.get().adopt(ririAsAnimal, ririAsPet.getName(), ririAsPet.getAdoptionDate());

            // Fifi
            final Animal fifiAsAnimal = new Animal();
            fifiAsAnimal.setSpecies(galinacea);
            fifiAsAnimal.setBreed(couNu);
            fifiAsAnimal.setGender(Gender.FEMALE);
            fifiAsAnimal.setWeight(Weight.kg(4.2D));
            Pet fifiAsPet = new Pet(fifiAsAnimal, "Fifi", LocalDate.of(2023, 9, 9));
            galinaceas.add(fifiAsPet);
            HOUSE_SERVICE.get().adopt(fifiAsAnimal, fifiAsPet.getName(), fifiAsPet.getAdoptionDate());

            // Loulou
            final Animal loulouAsAnimal = new Animal();
            loulouAsAnimal.setSpecies(galinacea);
            loulouAsAnimal.setBreed(couNu);
            loulouAsAnimal.setGender(Gender.FEMALE);
            loulouAsAnimal.setWeight(Weight.kg(4.2D));
            Pet loulouAsPet = new Pet(loulouAsAnimal, "Loulou", LocalDate.of(2023, 9, 9));
            galinaceas.add(loulouAsPet);
            HOUSE_SERVICE.get().adopt(loulouAsAnimal, loulouAsPet.getName(), loulouAsPet.getAdoptionDate());



            // Solo
            final Animal soloAnimal = new Animal();
            soloAnimal.setSpecies(galinacea);
            soloAnimal.setBreed(sussex);
            soloAnimal.setGender(Gender.FEMALE);
            soloAnimal.setWeight(Weight.kg(4.2D));
            Pet soloAsPet = new Pet(soloAnimal, "Solo", LocalDate.of(2023, 9, 9));
            galinaceas.add(soloAsPet);
            HOUSE_SERVICE.get().adopt(soloAnimal, soloAsPet.getName(), soloAsPet.getAdoptionDate());

            // Séké
            final Animal sekeAsAnimal = new Animal();
            sekeAsAnimal.setSpecies(galinacea);
            sekeAsAnimal.setBreed(sussex);
            sekeAsAnimal.setGender(Gender.FEMALE);
            sekeAsAnimal.setWeight(Weight.kg(4.2D));
            Pet sekeAsPet = new Pet(sekeAsAnimal, "Séké", LocalDate.of(2023, 9, 9));
            galinaceas.add(sekeAsPet);
            HOUSE_SERVICE.get().adopt(sekeAsAnimal, sekeAsPet.getName(), sekeAsPet.getAdoptionDate());

            // Gaïa
            final Animal gaiaAsAnimal = new Animal();
            gaiaAsAnimal.setSpecies(galinacea);
            gaiaAsAnimal.setBreed(greyCendre);
            gaiaAsAnimal.setGender(Gender.FEMALE);
            gaiaAsAnimal.setWeight(Weight.kg(4.2D));
            Pet gaiaAsPet = new Pet(gaiaAsAnimal, "Gaïa", LocalDate.of(2023, 9, 9));
            galinaceas.add(gaiaAsPet);
            HOUSE_SERVICE.get().adopt(gaiaAsAnimal, gaiaAsPet.getName(), gaiaAsPet.getAdoptionDate());

            // Cargo
            final Animal cargoAsAnimal = new Animal();
            cargoAsAnimal.setSpecies(galinacea);
            cargoAsAnimal.setBreed(greyCendre);
            cargoAsAnimal.setGender(Gender.FEMALE);
            cargoAsAnimal.setWeight(Weight.kg(4.2D));
            Pet cargoAsPet = new Pet(cargoAsAnimal, "Cargo", LocalDate.of(2023, 9, 9));
            galinaceas.add(cargoAsPet);
            HOUSE_SERVICE.get().adopt(cargoAsAnimal, cargoAsPet.getName(), cargoAsPet.getAdoptionDate());

            final Set<Pet> bovids = new HashSet<>();
            final Species bovid = Species.of("Ovidés");
            final Breed dwarfGoat = Breed.of("Naine");

            final Animal djaliAsAnimal = new Animal();
            this.petsBySpecies.put(bovid, bovids);
            djaliAsAnimal.setSpecies(bovid);
            djaliAsAnimal.setGender(Gender.FEMALE);
            djaliAsAnimal.setBreed(dwarfGoat);
            djaliAsAnimal.setWeight(Weight.kg(14.24D));
            Pet djaliAsPet = new Pet(djaliAsAnimal, "Djali", LocalDate.of(2024, 9, 9));
            bovids.add(djaliAsPet);
            HOUSE_SERVICE.get().adopt(djaliAsAnimal, djaliAsPet.getName(), djaliAsPet.getAdoptionDate());

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
            HOUSE_SERVICE.save(house);
        }
        this.preventErrorOnLoadFailure();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Loaded!");
        View view = inflater.inflate(R.layout.fragment_farm_exposition, container, false);

        // Initialiser la liste des espèces et des animaux
        try {
            this.loadHouse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        OnPetClickEventListener onPetClickListener = pet -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("pet", pet);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.navigation_pet_detail, bundle);
        };


        speciesRecyclerView = view.findViewById(R.id.speciesRecyclerView);
        speciesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        speciesAdapter = new SpeciesAdapter(this.petsBySpecies, onPetClickListener);
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