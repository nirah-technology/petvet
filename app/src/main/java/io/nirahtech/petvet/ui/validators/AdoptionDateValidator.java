package io.nirahtech.petvet.ui.validators;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.CalendarConstraints;

public class AdoptionDateValidator implements CalendarConstraints.DateValidator {
    public AdoptionDateValidator(Parcel in) {
        this(in.readLong());
    }

    public static final Creator<AdoptionDateValidator> CREATOR = new Parcelable.Creator<AdoptionDateValidator>() {
        @Override
        public AdoptionDateValidator createFromParcel(Parcel in) {
            return new AdoptionDateValidator(in);
        }

        @Override
        public AdoptionDateValidator[] newArray(int size) {
            return new AdoptionDateValidator[size];
        }
    };

    private final Long startTimestampInMillis;
    public AdoptionDateValidator(final Long startTimestampInMillis) {
        this.startTimestampInMillis = startTimestampInMillis;
    }
    @Override
    public boolean isValid(long date) {
        return date >= this.startTimestampInMillis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(this.startTimestampInMillis);
    }
}
