package io.nirahtech.petvet.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.animalpark.Species;

public final class SpeciesAdapter extends RecyclerView.Adapter<SpeciesAdapter.SpeciesViewHolder> {
    private final List<Species> speciesList;

    public static final class SpeciesViewHolder extends RecyclerView.ViewHolder {
        private final TextView speciesName;
        private final RecyclerView petRecyclerView;

        public SpeciesViewHolder(View itemView) {
            super(itemView);
            this.speciesName = itemView.findViewById(R.id.speciesName);
            this.petRecyclerView = itemView.findViewById(R.id.animalRecyclerView);
        }
    }

    public SpeciesAdapter(final Map<Species, Set<Pet>> speciesList) {
        this.speciesList = speciesList;
    }

    @Override
    public SpeciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.species_item_gallery, parent, false);
        return new SpeciesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpeciesViewHolder holder, int position) {
        Species species = speciesList.get(position);
        holder.speciesName.setText(species.getName());

        // Set up the horizontal RecyclerView for animals
        PetAdapter petAdapter = new PetAdapter(species.getPets());
        holder.petRecyclerView.setLayoutManager(new LinearLayoutManager(holder.petRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.petRecyclerView.setAdapter(petAdapter);
    }

    @Override
    public int getItemCount() {
        return this.speciesList.size();
    }
}
