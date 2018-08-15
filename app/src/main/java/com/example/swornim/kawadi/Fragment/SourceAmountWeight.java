package com.example.swornim.kawadi.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.swornim.kawadi.Addwaste;
import com.example.swornim.kawadi.R;

import java.text.NumberFormat;
import java.util.Locale;

public class SourceAmountWeight extends Fragment  {

    private NumberPicker amountPicker;
    private NumberPicker weightPicker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.source_amount_weight_fragment, container, false);

        amountPicker = mView.findViewById(R.id.numberPickerAmount);
        amountPicker.setMinValue(50);
        amountPicker.setMaxValue(5000);
        amountPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String str = "a12.334tyz.78x";
                str = str.replaceAll("[^\\d.]", "");
                Addwaste.amountValue=String.valueOf(newVal).replaceAll("[^\\d.]", "");
            }
        });
        amountPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return "Rupees."+value;
            }
        });



        weightPicker = mView.findViewById(R.id.numberPickerWeight);
        weightPicker.setMinValue(1);
        weightPicker.setMaxValue(50);
        weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Addwaste.weightValue=String.valueOf(newVal).replaceAll("[^\\d]", "");;
            }
        });
        weightPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value+"kg";
            }
        });

        return mView;

    }


}




