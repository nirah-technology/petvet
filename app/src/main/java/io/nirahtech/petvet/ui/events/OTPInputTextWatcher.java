package io.nirahtech.petvet.ui.events;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Objects;

public final class OTPInputTextWatcher implements TextWatcher {

    private final EditText previous;
    private final EditText next;

    public OTPInputTextWatcher(final EditText previous, final EditText next) {
        this.previous = previous;
        this.next = next;
    }


    private final void tryToFocusPrevious() {
        if (Objects.nonNull(this.previous)) {
            this.previous.requestFocus();
        }
    }
    private final void tryToFocusNext() {
        if (Objects.nonNull(this.next)) {
            this.next.requestFocus();
        }
    }

    private final void tryToHandleBackspaceEvent(final int before, final int count) {
        if ((count == 0) && (before > 0) && Objects.nonNull(this.previous)) {
            this.previous.requestFocus();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.tryToHandleBackspaceEvent(before, count);
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()) {
            this.tryToFocusPrevious();
        } else if (s.length() == 1) {
            this.tryToFocusNext();
        }
    }
}
