package com.example.health_twin;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileSetupFragment extends Fragment {

    private TextInputEditText etName, etAge, etHeight, etWeight;
    private MaterialButton btnMale, btnFemale, btnContinue;
    private MaterialCardView bmiCard;
    private android.widget.TextView tvBMIValue, tvBMIStatus;

    private String selectedGender = "";

    public ProfileSetupFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_setup, container, false);

        bindViews(view);
        setupGenderToggle();
        setupTextWatchers();
        setupContinue();

        return view;
    }

    private void bindViews(View view) {
        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);

        btnMale = view.findViewById(R.id.btnMale);
        btnFemale = view.findViewById(R.id.btnFemale);
        btnContinue = view.findViewById(R.id.btnContinue);

        bmiCard = view.findViewById(R.id.bmiCard);
        tvBMIValue = view.findViewById(R.id.tvBMIValue);
        tvBMIStatus = view.findViewById(R.id.tvBMIStatus);
    }

    // ===================== GENDER TOGGLE =====================
    private void setupGenderToggle() {

        btnMale.setOnClickListener(v -> {
            selectedGender = "Male";
            selectGender(btnMale, btnFemale);
            validateForm();
        });

        btnFemale.setOnClickListener(v -> {
            selectedGender = "Female";
            selectGender(btnFemale, btnMale);
            validateForm();
        });
    }

    private void selectGender(MaterialButton selected, MaterialButton unselected) {
        selected.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), R.color.genderSelected));
        unselected.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), android.R.color.transparent));
    }

    // ===================== TEXT WATCHERS =====================
    private void setupTextWatchers() {

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateBMI();
                validateForm();
            }
        };

        etHeight.addTextChangedListener(watcher);
        etWeight.addTextChangedListener(watcher);
        etName.addTextChangedListener(watcher);
        etAge.addTextChangedListener(watcher);
    }

    // ===================== BMI CALCULATION =====================
    private void calculateBMI() {

        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            tvBMIValue.setText("--");
            tvBMIStatus.setText("Awaiting data");
            return;
        }

        try {
            double height = Double.parseDouble(heightStr) / 100.0;
            double weight = Double.parseDouble(weightStr);

            if (height <= 0) return;

            double bmi = weight / (height * height);
            tvBMIValue.setText(String.format("%.1f", bmi));

            updateBMIUI(bmi);

        } catch (Exception ignored) {}
    }

    private void updateBMIUI(double bmi) {

        int bgColor;
        int textColor;
        String status;

        if (bmi < 18.5) {
            status = "Underweight";
            bgColor = ContextCompat.getColor(requireContext(), R.color.bmiUnder_bg);
            textColor = ContextCompat.getColor(requireContext(), R.color.bmiUnder_text);
        } else if (bmi < 25) {
            status = "Healthy";
            bgColor = ContextCompat.getColor(requireContext(), R.color.bmiHealthy_bg);
            textColor = ContextCompat.getColor(requireContext(), R.color.bmiHealthy_text);
        } else if (bmi < 30) {
            status = "Overweight";
            bgColor = ContextCompat.getColor(requireContext(), R.color.bmiOver_bg);
            textColor = ContextCompat.getColor(requireContext(), R.color.bmiOver_text);
        } else {
            status = "Obese";
            bgColor = ContextCompat.getColor(requireContext(), R.color.bmiObese_bg);
            textColor = ContextCompat.getColor(requireContext(), R.color.bmiObese_text);
        }

        tvBMIStatus.setText(status);
        tvBMIValue.setTextColor(textColor);
        tvBMIStatus.setTextColor(textColor);

        animateCardBackground(bgColor);
    }

    private void animateCardBackground(int targetColor) {

        int currentColor = ((ColorDrawable) bmiCard.getBackground()).getColor();

        ValueAnimator animator = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                currentColor,
                targetColor
        );

        animator.setDuration(300);
        animator.addUpdateListener(animation ->
                bmiCard.setCardBackgroundColor((int) animation.getAnimatedValue())
        );

        animator.start();
    }

    // ===================== VALIDATION =====================
    private void validateForm() {

        boolean valid =
                !etName.getText().toString().trim().isEmpty() &&
                        !etAge.getText().toString().trim().isEmpty() &&
                        !etHeight.getText().toString().trim().isEmpty() &&
                        !etWeight.getText().toString().trim().isEmpty() &&
                        !selectedGender.isEmpty();

        btnContinue.setEnabled(valid);
        btnContinue.setAlpha(valid ? 1f : 0.5f);
    }

    // ===================== NAVIGATION =====================
    private void setupContinue() {
        btnContinue.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_profileSetupFragment_to_lifestyleSetupFragment)
        );
    }
}