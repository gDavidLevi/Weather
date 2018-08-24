package ru.davidlevi.weather.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.davidlevi.weather.R;

/**
 * Класс-контроллер для макета fragment_registration.xml
 */
public class RegistrationFragment extends BaseFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Activity activity = getActivity();
        //todo Переход на фрагмент ввода SMS-кода подтверждения

        // Перейдем к фрагменту ввода SMS-кода подтверждения
        Button button = view.findViewById(R.id.b_registration);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IBaseFragment fragmentInterface = (IBaseFragment) activity;
                if (fragmentInterface != null)
                    fragmentInterface.startFragment(RegistrationConfirmFragment.class);
            }
        });
    }
}