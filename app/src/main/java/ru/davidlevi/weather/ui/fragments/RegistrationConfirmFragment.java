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
 * Класс-контроллер для макета fragment_registration_confirm.xml
 */
public class RegistrationConfirmFragment extends BaseFragment {
    private Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();

        Button button = view.findViewById(R.id.b_confirm_smscode);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IBaseFragment fragmentInterface = (IBaseFragment) activity;
                if (fragmentInterface != null)
                    fragmentInterface.setSettings();
            }
        });
    }
}