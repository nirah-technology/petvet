package io.nirahtech.petvet.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.animalpark.Species;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.ui.adapters.listeners.OnPetClickEventListener;

public final class SpeciesAdapter extends RecyclerView.Adapter<SpeciesViewHolder> {
    private final Map<Species, Set<Pet>> sortedPetsBySpecies;
    private final OnPetClickEventListener onPetClickEventListener;


    public SpeciesAdapter(final Map<Species, Set<Pet>> speciesList, final OnPetClickEventListener onPetClickEventListener) {
        this.sortedPetsBySpecies = speciesList;
        this.onPetClickEventListener = onPetClickEventListener;
    }

    @Override
    public SpeciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.species_item_gallery, parent, false);
        return new SpeciesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpeciesViewHolder holder, int position) {
        final Species species = new ArrayList<>(this.sortedPetsBySpecies.keySet()).get(position);
        holder.speciesName.setText(species.getName().toUpperCase());

        // Set up the horizontal RecyclerView for animals
        final List<Pet> petsOfSpecies = new ArrayList<>(this.sortedPetsBySpecies.get(species));
        final PetAdapter petAdapter = new PetAdapter(petsOfSpecies, this.onPetClickEventListener);
        holder.petRecyclerView.setLayoutManager(new LinearLayoutManager(holder.petRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.petRecyclerView.setAdapter(petAdapter);
    }

    @Override
    public int getItemCount() {
        return this.sortedPetsBySpecies.size();
    }
}
