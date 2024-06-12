package io.nirahtech.petvet.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import io.nirahtech.petvet.R;
import io.nirahtech.petvet.ui.events.OTPInputTextWatcher;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinExistingHouseFragment} factory method to
 * create an instance of this fragment.
 */
public class JoinExistingHouseFragment extends Fragment {

    private EditText otpEditTextNumberCode1;
    private EditText otpEditTextNumberCode2;
    private EditText otpEditTextNumberCode3;
    private EditText otpEditTextNumberCode4;
    private EditText otpEditTextNumberCode5;
    private EditText otpEditTextNumberCode6;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private final void handleOnChangeOTPInput(final EditText currentOTP, final EditText previousOTP, EditText nextOTP) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_join_existing_house, container, false);

        // Find views by ID using the inflated view
        this.otpEditTextNumberCode1 = view.findViewById(R.id.otpEditTextNumberCode1);
        this.otpEditTextNumberCode2 = view.findViewById(R.id.otpEditTextNumberCode2);
        this.otpEditTextNumberCode3 = view.findViewById(R.id.otpEditTextNumberCode3);
        this.otpEditTextNumberCode4 = view.findViewById(R.id.otpEditTextNumberCode4);
        this.otpEditTextNumberCode5 = view.findViewById(R.id.otpEditTextNumberCode5);
        this.otpEditTextNumberCode6 = view.findViewById(R.id.otpEditTextNumberCode6);

        this.otpEditTextNumberCode1.addTextChangedListener(new OTPInputTextWatcher(null, this.otpEditTextNumberCode2));
        this.otpEditTextNumberCode2.addTextChangedListener(new OTPInputTextWatcher(this.otpEditTextNumberCode1, this.otpEditTextNumberCode3));
        this.otpEditTextNumberCode3.addTextChangedListener(new OTPInputTextWatcher(this.otpEditTextNumberCode2, this.otpEditTextNumberCode4));
        this.otpEditTextNumberCode4.addTextChangedListener(new OTPInputTextWatcher(this.otpEditTextNumberCode3, this.otpEditTextNumberCode5));
        this.otpEditTextNumberCode5.addTextChangedListener(new OTPInputTextWatcher(this.otpEditTextNumberCode4, this.otpEditTextNumberCode6));
        this.otpEditTextNumberCode6.addTextChangedListener(new OTPInputTextWatcher(this.otpEditTextNumberCode5, null));
        return view;
    }
}