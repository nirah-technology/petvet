package io.nirahtech.petvet.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.io.File;

import io.nirahtech.petvet.R;

public class AnimalsGPSFragment extends Fragment {

    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_animals_g_p_s, container, false);

        // Setup the mapView
        mapView = view.findViewById(R.id.mapview);

        // Initialize OSMDroid
        Configuration.getInstance().load(getContext(), getContext().getSharedPreferences("osmdroid", 0));
        Configuration.getInstance().setOsmdroidBasePath(new File(getContext().getCacheDir(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache(new File(getContext().getCacheDir(), "osmdroid/tiles"));

        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(-34.0, 151.0);  // Sydney coordinates
        mapView.getController().setCenter(startPoint);

        // Add a marker in Sydney
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Marker in Sydney");
        mapView.getOverlays().add(startMarker);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapView != null) {
            mapView.onDetach();
        }
    }
}
