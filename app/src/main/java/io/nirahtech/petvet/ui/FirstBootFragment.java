package io.nirahtech.petvet.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Optional;

import io.nirahtech.petvet.MainActivity;
import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.features.FeaturesRegistry;
import io.nirahtech.petvet.features.boot.DetectFirstBootFeature;
import io.nirahtech.petvet.features.boot.implementations.DefaultDetectFirstBootFeatureImpl;
import io.nirahtech.petvet.features.util.exceptions.FeatureExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstBootFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstBootFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // final AnimalClassifier animalClassifier = ClassifierFactory.animalClassifier();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_boot, container, false);
        return view;
    }
}