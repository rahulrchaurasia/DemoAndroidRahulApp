package com.policyboss.demoandroidapp.Utility;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.Locale;


public class DateMask implements TextWatcher {

    private static final int MAX_LENGTH = 8;
    private static final int MIN_LENGTH = 2;

    private String updatedText;
    private boolean editing;

    private final String separator;

    // Constructor to accept the separator parameter
    public DateMask(String separator) {
        this.separator = separator;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        if (text.toString().equals(updatedText) || editing) return;

        String digits = text.toString().replaceAll("\\D", "");
        int length = digits.length();

        if (length <= MIN_LENGTH) {
            updatedText = digits;
            return;
        }

        if (length > MAX_LENGTH) {
            digits = digits.substring(0, MAX_LENGTH);
        }

        String day, month, year;
        if (length <= 4) {
            day = digits.substring(0, 2);
            month = digits.substring(2);
            updatedText = String.format(Locale.US, "%s%s%s", day, separator, month);
        } else {
            day = digits.substring(0, 2);
            month = digits.substring(2, 4);
            year = digits.substring(4);
            updatedText = String.format(Locale.US, "%s%s%s%s%s", day, separator, month, separator, year);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editing) return;

        editing = true;
        editable.clear();
        editable.insert(0, updatedText);
        editing = false;

    }
}
