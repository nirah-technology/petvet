package io.nirahtech.petvet.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.base.Pet;
import io.nirahtech.petvet.ui.adapters.listeners.OnPetClickEventListener;

public class PetAdapter extends RecyclerView.Adapter<PetViewHolder> {
    private final List<Pet> pets;
    private final OnPetClickEventListener onPetClickEventListener;


    public PetAdapter(final List<Pet> Pets, final OnPetClickEventListener onPetClickEventListener) {
        this.onPetClickEventListener = onPetClickEventListener;
        this.pets = Pets;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        final Pet pet = this.pets.get(position);
        // Load image using a library like Glide or Picasso
        // Glide.with(holder.PetImage.getContext()).load(Pet.getImageUrl()).into(holder.PetImage);
        holder.petName.setText(pet.getName());
        holder.bind(pet, this.onPetClickEventListener);
    }

    @Override
    public int getItemCount() {
        return this.pets.size();
    }
}
