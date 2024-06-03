package io.nirahtech.petvet.ui.fragments.adoption;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.core.base.Farm;
import io.nirahtech.petvet.core.base.House;
import io.nirahtech.petvet.persistance.repositories.HouseReaderDbHelper;

public class PetAdoptionFragment extends Fragment {

    private TextInputEditText editTextBirthDate;
    private TextInputEditText editTextAdoptionDate;

    private TextInputEditText editTextName;
    private TextInputEditText editTextSpecies;
    private TextInputEditText editTextBreed;

    private LocalDate birthDate;
    private LocalDate adoptionDate;

    private MaterialButtonToggleGroup gender;

    private MaterialDatePicker<Long> datePickerBirthDate;
    private MaterialDatePicker<Long> datePickerAdoptionDate;

    private Button confirmAdoptionButton;

    private final House createHouse() {
        String name = this.editTextName.getText().toString();
        House house = new House(name);
        Farm farm = new Farm();
        house.setFarm(farm);
        return house;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_adoption, container, false);

        // Find views by ID using the inflated view
        this.editTextBirthDate = view.findViewById(R.id.editTextBirthDate);
        this.editTextAdoptionDate = view.findViewById(R.id.editTextAdoptionDate);
        this.confirmAdoptionButton = view.findViewById(R.id.confirmAdoptionButton);
        this.gender = view.<MaterialButtonToggleGroup>findViewById(R.id.toggleButton);

        this.datePickerBirthDate = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Sélectionnez la date de Naissance")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        this.datePickerBirthDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                final Date date = new Date(selection + offsetFromUTC);
                birthDate = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                editTextBirthDate.setText(simpleFormat.format(date));

                // Set minimum date for adoption date picker
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setStart(date.getTime());
                datePickerAdoptionDate = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Sélectionnez la date d'adoption")
                        .setSelection(selection)
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();
                datePickerAdoptionDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        TimeZone timeZoneUTC = TimeZone.getDefault();
                        int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        final Date date = new Date(selection + offsetFromUTC);
                        adoptionDate = date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        editTextAdoptionDate.setText(simpleFormat.format(date));
                    }
                });
            }
        });

        this.editTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerBirthDate.show(getActivity().getSupportFragmentManager(), "datePickerBirthDateTag");
            }
        });

        this.editTextAdoptionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.nonNull(birthDate)) {
                    datePickerAdoptionDate.show(getActivity().getSupportFragmentManager(), "datePickerAdoptionDateTag");
                } else {
                    // Optionally, show a message indicating that birth date needs to be selected first
                }
            }
        });


        this.confirmAdoptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                House house = createHouse();
                HouseReaderDbHelper dbHelper = new HouseReaderDbHelper(getContext());
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();


            }
        });

        this.gender.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

            }
        });



        return view;
    }
}
