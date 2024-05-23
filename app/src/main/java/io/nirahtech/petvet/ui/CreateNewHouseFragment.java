package io.nirahtech.petvet.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.services.storage.LocalStorageService;
import io.nirahtech.petvet.services.storage.StorageService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewHouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewHouseFragment extends Fragment {



    private EditText houseNameEditText;
    private Button createButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private final void handleClick() {
        final String houseName = this.houseNameEditText.getText().toString();
        if (!houseName.isEmpty()) {

            final House house = new House(houseName);
            final Farm farm = new Farm();
            house.setFarm(farm);

            final StorageService storageService = new LocalStorageService();
            try {
                storageService.save(house, new File("petvet-house.txt"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.houseNameEditText = (EditText) getView().findViewById(R.id.editTextHouseName);
        this.createButton = (Button) getView().findViewById(R.id.buttonSubmit);

        this.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                handleClick();
            }
        });

        return inflater.inflate(R.layout.fragment_create_new_house, container, false);
    }
}