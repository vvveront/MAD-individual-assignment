package com.example.madassignment;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {
    private final int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        try {
            String newVal = dest.toString().substring(0, dstart) +
                    source.subSequence(start, end) +
                    dest.toString().substring(dend);

            if (newVal.isEmpty()) return null;

            int input = Integer.parseInt(newVal);
            if (input >= min && input <= max) return null;
        } catch (NumberFormatException e) {
        }
        return "";
    }
}
