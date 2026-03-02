package com.example.health_twin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;

import android.widget.CheckBox;

public class ConsentFragment extends Fragment {

    public ConsentFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_consent, container, false);

        CheckBox cbAgree = view.findViewById(R.id.cbAgree);
        MaterialButton btnContinue = view.findViewById(R.id.btnContinue);

        // Enable button only if checkbox is checked
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnContinue.setEnabled(isChecked);
        });

        // Navigate when button clicked
        btnContinue.setOnClickListener(v -> {

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_consentFragment_to_profileSetupFragment);

        });

        return view;
    }
}