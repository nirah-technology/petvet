package io.nirahtech.petvet.ui.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.nirahtech.petvet.R;

public final class SpeciesViewHolder extends RecyclerView.ViewHolder {
    final TextView speciesName;
    final RecyclerView petRecyclerView;

    public SpeciesViewHolder(View itemView) {
        super(itemView);
        this.speciesName = itemView.findViewById(R.id.speciesName);
        this.petRecyclerView = itemView.findViewById(R.id.animalRecyclerView);
    }
}