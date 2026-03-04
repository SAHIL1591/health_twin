package com.example.health_twin;

import android.animation.ArgbEvaluator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.slider.Slider;

public class LifeStyleSetupFragment extends Fragment {

    private TextView tvSmokingValue, tvAlcoholValue, tvExerciseValue, tvSleepValue;
    private Slider sliderSmoking, sliderAlcohol, sliderExercise, sliderSleep;
    private MaterialCardView cardSmoking, cardAlcohol, cardExercise, cardSleep;
    private MaterialButton btnGenerate;
    private ImageView lfBack;

    private final int GREEN = Color.parseColor("#00E676");
    private final int YELLOW = Color.parseColor("#FFD54F");
    private final int RED = Color.parseColor("#FF5252");

    public LifeStyleSetupFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lifestyle_setup, container, false);

        // Text
        tvSmokingValue = view.findViewById(R.id.tvSmokingValue);
        tvAlcoholValue = view.findViewById(R.id.tvAlcoholValue);
        tvExerciseValue = view.findViewById(R.id.tvExerciseValue);
        tvSleepValue = view.findViewById(R.id.tvSleepValue);

        // Sliders
        sliderSmoking = view.findViewById(R.id.sliderSmoking);
        sliderAlcohol = view.findViewById(R.id.sliderAlcohol);
        sliderExercise = view.findViewById(R.id.sliderExercise);
        sliderSleep = view.findViewById(R.id.sliderSleep);

        // Cards
        cardSmoking = view.findViewById(R.id.cardSmoking);
        cardAlcohol = view.findViewById(R.id.cardAlcohol);
        cardExercise = view.findViewById(R.id.cardExercise);
        cardSleep = view.findViewById(R.id.cardSleep);

        btnGenerate = view.findViewById(R.id.btnGenerate);
        lfBack = view.findViewById(R.id.lfBack);

        // Back navigation
        lfBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );

        // Setup sliders
        setupRiskSlider(sliderSmoking, tvSmokingValue, cardSmoking, false);
        setupRiskSlider(sliderAlcohol, tvAlcoholValue, cardAlcohol, false);
        setupRiskSlider(sliderExercise, tvExerciseValue, cardExercise, true);
        setupSleepSlider();

        // Navigate forward
        btnGenerate.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_lifestyleSetupFragment_to_resultFragment)
        );

        return view;
    }

    // ================= GENERAL RISK SLIDER =================
    private void setupRiskSlider(Slider slider,
                                 TextView valueText,
                                 MaterialCardView card,
                                 boolean inverse) {

        slider.addOnChangeListener((s, value, fromUser) -> {

            int intValue = (int) value;
            valueText.setText(String.valueOf(intValue));

            float percent = value / 10f;
            if (inverse) percent = 1f - percent;

            int animatedColor = (int) new ArgbEvaluator()
                    .evaluate(percent, GREEN, RED);

            slider.setTrackActiveTintList(ColorStateList.valueOf(animatedColor));
            slider.setThumbTintList(ColorStateList.valueOf(animatedColor));
            valueText.setTextColor(animatedColor);

            updateCardGlow(card, animatedColor);
        });

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                slider.animate()
                        .scaleX(1.03f)
                        .scaleY(1.03f)
                        .setDuration(120)
                        .start();
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                slider.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(120)
                        .start();
            }
        });
    }

    // ================= SLEEP SLIDER =================
    private void setupSleepSlider() {

        sliderSleep.addOnChangeListener((slider, value, fromUser) -> {

            int intValue = (int) value;
            tvSleepValue.setText(intValue + "h");

            int color;

            if (intValue >= 7 && intValue <= 9) {
                color = GREEN;
            } else if (intValue >= 5) {
                color = YELLOW;
            } else {
                color = RED;
            }

            slider.setTrackActiveTintList(ColorStateList.valueOf(color));
            slider.setThumbTintList(ColorStateList.valueOf(color));
            tvSleepValue.setTextColor(color);

            updateCardGlow(cardSleep, color);
        });

        sliderSleep.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                slider.animate()
                        .scaleX(1.03f)
                        .scaleY(1.03f)
                        .setDuration(120)
                        .start();
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                slider.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(120)
                        .start();
            }
        });
    }

    // ================= CARD GLOW =================
    private void updateCardGlow(MaterialCardView card, int color) {

        card.setStrokeColor(color);

        card.animate()
                .translationZ(20f)
                .setDuration(150)
                .withEndAction(() ->
                        card.animate()
                                .translationZ(8f)
                                .setDuration(150)
                                .start()
                )
                .start();
    }
}