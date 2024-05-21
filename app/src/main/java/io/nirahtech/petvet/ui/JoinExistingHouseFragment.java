package io.nirahtech.petvet.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.nirahtech.petvet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinExistingHouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinExistingHouseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_existing_house, container, false);
    }
}