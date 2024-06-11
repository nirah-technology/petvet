package io.nirahtech.petvet.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.features.boot.DetectFirstBootFeature;
import io.nirahtech.petvet.features.boot.implementations.DefaultDetectFirstBootFeatureImpl;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstBootFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstBootFragment extends Fragment {

    private DetectFirstBootFeature feature;
    private Button button;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.feature = new DefaultDetectFirstBootFeatureImpl(this.getContext().getCacheDir());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // final AnimalClassifier animalClassifier = ClassifierFactory.animalClassifier();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_boot, container, false);
        this.button = view.findViewById(R.id.testbtn);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feature.get();
            }
        });
        return view;
    }
}