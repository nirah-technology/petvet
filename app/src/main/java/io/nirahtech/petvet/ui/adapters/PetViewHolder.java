package io.nirahtech.petvet.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.ui.adapters.listeners.OnPetClickEventListener;

public class PetViewHolder extends RecyclerView.ViewHolder {
    final ImageView petImage;
    final TextView petName;

    public PetViewHolder(final View itemView) {
        super(itemView);
        this.petImage = itemView.findViewById(R.id.petImage);
        this.petName = itemView.findViewById(R.id.petName);
    }
    public void bind(final Pet pet, final OnPetClickEventListener listener) {
        petName.setText(pet.getName());
        itemView.setOnClickListener(v -> listener.onPetClick(pet));
    }

}