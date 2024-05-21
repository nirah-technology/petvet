package io.nirahtech.petvet.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import io.nirahtech.petvet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewHouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewHouseFragment extends Fragment {

    private EditText houseName;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.houseName = (EditText) getView().findViewById(R.id.editTextHouseName);
        this.button = (Button) getView().findViewById(R.id.buttonSubmit);

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return inflater.inflate(R.layout.fragment_create_new_house, container, false);
    }
}