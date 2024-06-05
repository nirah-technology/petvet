package io.nirahtech.petvet.ui;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import io.nirahtech.petvet.MainActivity;
import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.services.house.HouseService;
import io.nirahtech.petvet.services.house.HouseServiceImpl;
import io.nirahtech.petvet.services.storage.LocalStorageService;
import io.nirahtech.petvet.services.storage.StorageService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewHouseFragment} factory method to
 * create an instance of this fragment.
 */
public class CreateNewHouseFragment extends Fragment {



    private TextInputEditText houseNameEditText;
    private Button createButton;

    private HouseService houseService;


    private final void redirectoToMainActivuty() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("io.nirahtech.petvet", MainActivity.class.getName()));
        getActivity().finish();
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.houseService = HouseServiceImpl.getInstance(this.getContext());
        this.houseService.getHouse().ifPresent(house -> this.redirectoToMainActivuty());
    }

    private final void handleClick() {
        final String houseName = this.houseNameEditText.getText().toString();
        if (!houseName.isEmpty()) {

            final House house = new House(houseName);
            final Farm farm = new Farm();
            house.setFarm(farm);
            this.houseService.save(house);
            new AlertDialog.Builder(this.getContext())
                    .setTitle("Parfait!")
                    .setMessage("Votre habitat a bien été sauvegardé.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            redirectoToMainActivuty();
                        }

                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_house, container, false);

        // Find views by ID using the inflated view
        this.houseNameEditText = view.findViewById(R.id.editTextHouseName);
        this.createButton = view.findViewById(R.id.buttonSubmit);

        this.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                handleClick();
            }
        });

        return view;
    }
}