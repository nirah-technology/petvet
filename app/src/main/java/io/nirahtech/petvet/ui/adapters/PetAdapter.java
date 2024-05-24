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

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {
    private List<Pet> pets;

    static class PetViewHolder extends RecyclerView.ViewHolder {
        private final ImageView petImage;
        private final TextView petName;

        public PetViewHolder(final View itemView) {
            super(itemView);
            this.petImage = itemView.findViewById(R.id.petImage);
            this.petName = itemView.findViewById(R.id.petName);
        }
    }

    private PetAdapter(final List<Pet> Pets) {
        this.pets = Pets;
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        final Pet Pet = this.pets.get(position);
        // Load image using a library like Glide or Picasso
        // Glide.with(holder.PetImage.getContext()).load(Pet.getImageUrl()).into(holder.PetImage);
        holder.petName.setText(Pet.getName());
    }

    @Override
    public int getItemCount() {
        return this.pets.size();
    }
}
