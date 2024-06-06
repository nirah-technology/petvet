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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import io.nirahtech.petvet.services.house.HouseService;
import io.nirahtech.petvet.services.house.HouseServiceImpl;
import io.nirahtech.petvet.ui.adapters.SpeciesAdapter;
import io.nirahtech.petvet.ui.adapters.listeners.OnPetClickEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FarmExpositionFragment} factory method to
 * create an instance of this fragment.
 */
public class FarmExpositionFragment extends Fragment {

    private HouseService houseService;

    private RecyclerView speciesRecyclerView;
    private SpeciesAdapter speciesAdapter;
    private Map<Species, Set<Pet>> petsBySpecies;

    private FloatingActionButton adoptionButton;
    private Button locationButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.houseService = HouseServiceImpl.getInstance(this.getContext());
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
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(nitroAsAnimal, nitroAsPet.getName(), nitroAsPet.getAdoptionDate());
            });

            // Nala
            final Animal nalaAsAnimal = new Animal();
            nalaAsAnimal.setSpecies(cat);
            nalaAsAnimal.setBreed(manx);
            nalaAsAnimal.setGender(Gender.FEMALE);
            nalaAsAnimal.setWeight(Weight.kg(7.2D));
            Pet nalaAsPet = new Pet(nalaAsAnimal, "Nala", LocalDate.of(2017, 3, 17));
            cats.add(nalaAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(nalaAsAnimal, nalaAsPet.getName(), nalaAsPet.getAdoptionDate());
            });


            // Simba
            final Animal simbaAsAnimal = new Animal();
            simbaAsAnimal.setSpecies(cat);
            simbaAsAnimal.setBreed(european);
            simbaAsAnimal.setGender(Gender.MALE);
            simbaAsAnimal.setWeight(Weight.kg(7.2D));
            Pet simbaAsPet = new Pet(simbaAsAnimal, "Simba", LocalDate.of(2017, 3, 17));
            cats.add(simbaAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(simbaAsAnimal, simbaAsPet.getName(), simbaAsPet.getAdoptionDate());
            });

            // Ed
            final Animal edAsAnimal = new Animal();
            edAsAnimal.setSpecies(cat);
            edAsAnimal.setBreed(chartreux);
            edAsAnimal.setGender(Gender.MALE);
            edAsAnimal.setWeight(Weight.kg(7.2D));
            Pet edAsPet = new Pet(edAsAnimal, "Ed", LocalDate.of(2017, 3, 17));
            cats.add(edAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(edAsAnimal, edAsPet.getName(), edAsPet.getAdoptionDate());
            });

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
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(yumaAsAnimal, yumaAsPet.getName(), yumaAsPet.getAdoptionDate());
            });

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
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(picsouAsAnimal, picsouAsPet.getName(), picsouAsPet.getAdoptionDate());
            });

            // Riri
            final Animal ririAsAnimal = new Animal();
            ririAsAnimal.setSpecies(galinacea);
            ririAsAnimal.setBreed(couNu);
            ririAsAnimal.setGender(Gender.FEMALE);
            ririAsAnimal.setWeight(Weight.kg(4.2D));
            Pet ririAsPet = new Pet(ririAsAnimal, "Riri", LocalDate.of(2023, 9, 9));
            galinaceas.add(ririAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(ririAsAnimal, ririAsPet.getName(), ririAsPet.getAdoptionDate());
            });

            // Fifi
            final Animal fifiAsAnimal = new Animal();
            fifiAsAnimal.setSpecies(galinacea);
            fifiAsAnimal.setBreed(couNu);
            fifiAsAnimal.setGender(Gender.FEMALE);
            fifiAsAnimal.setWeight(Weight.kg(4.2D));
            Pet fifiAsPet = new Pet(fifiAsAnimal, "Fifi", LocalDate.of(2023, 9, 9));
            galinaceas.add(fifiAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(fifiAsAnimal, fifiAsPet.getName(), fifiAsPet.getAdoptionDate());
            });

            // Loulou
            final Animal loulouAsAnimal = new Animal();
            loulouAsAnimal.setSpecies(galinacea);
            loulouAsAnimal.setBreed(couNu);
            loulouAsAnimal.setGender(Gender.FEMALE);
            loulouAsAnimal.setWeight(Weight.kg(4.2D));
            Pet loulouAsPet = new Pet(loulouAsAnimal, "Loulou", LocalDate.of(2023, 9, 9));
            galinaceas.add(loulouAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(loulouAsAnimal, loulouAsPet.getName(), loulouAsPet.getAdoptionDate());
            });

            // Solo
            final Animal soloAnimal = new Animal();
            soloAnimal.setSpecies(galinacea);
            soloAnimal.setBreed(sussex);
            soloAnimal.setGender(Gender.FEMALE);
            soloAnimal.setWeight(Weight.kg(4.2D));
            Pet soloAsPet = new Pet(soloAnimal, "Solo", LocalDate.of(2023, 9, 9));
            galinaceas.add(soloAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(soloAnimal, soloAsPet.getName(), soloAsPet.getAdoptionDate());
            });

            // Séké
            final Animal sekeAsAnimal = new Animal();
            sekeAsAnimal.setSpecies(galinacea);
            sekeAsAnimal.setBreed(sussex);
            sekeAsAnimal.setGender(Gender.FEMALE);
            sekeAsAnimal.setWeight(Weight.kg(4.2D));
            Pet sekeAsPet = new Pet(sekeAsAnimal, "Séké", LocalDate.of(2023, 9, 9));
            galinaceas.add(sekeAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(sekeAsAnimal, sekeAsPet.getName(), sekeAsPet.getAdoptionDate());
            });

            // Gaïa
            final Animal gaiaAsAnimal = new Animal();
            gaiaAsAnimal.setSpecies(galinacea);
            gaiaAsAnimal.setBreed(greyCendre);
            gaiaAsAnimal.setGender(Gender.FEMALE);
            gaiaAsAnimal.setWeight(Weight.kg(4.2D));
            Pet gaiaAsPet = new Pet(gaiaAsAnimal, "Gaïa", LocalDate.of(2023, 9, 9));
            galinaceas.add(gaiaAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(gaiaAsAnimal, gaiaAsPet.getName(), gaiaAsPet.getAdoptionDate());
            });

            // Cargo
            final Animal cargoAsAnimal = new Animal();
            cargoAsAnimal.setSpecies(galinacea);
            cargoAsAnimal.setBreed(greyCendre);
            cargoAsAnimal.setGender(Gender.FEMALE);
            cargoAsAnimal.setWeight(Weight.kg(4.2D));
            Pet cargoAsPet = new Pet(cargoAsAnimal, "Cargo", LocalDate.of(2023, 9, 9));
            galinaceas.add(cargoAsPet);
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(cargoAsAnimal, cargoAsPet.getName(), cargoAsPet.getAdoptionDate());
            });

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
            this.houseService.getHouse().ifPresent((house) -> {
                house.adopt(djaliAsAnimal, djaliAsPet.getName(), djaliAsPet.getAdoptionDate());
            });

        }
    }

    private final void loadHouse() throws IOException, ClassNotFoundException {
        this.houseService.getHouse().flatMap(House::getFarm).ifPresent(farm -> {
            System.out.println("There is/are " + farm.getPets().size() + " loaded animals from the backup file.");
            for (Pet pet : farm.getPets()) {
                System.out.println("Loaded animal from backup: " + pet.getName());
                Species species = pet.getAnimal().getSpecies();
                this.petsBySpecies.computeIfAbsent(species, k -> new HashSet<>()).add(pet);
            }
        });
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
        System.out.println(this.houseService.getHouse().get().getFarm().get().getPets().size());

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
        this.locationButton = view.findViewById(R.id.locationButton);
        this.adoptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_pet_adoption);
            }
        });

        this.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_animal_gps);
            }
        });

        return view;
    }

}