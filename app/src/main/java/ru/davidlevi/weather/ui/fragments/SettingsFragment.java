package ru.davidlevi.weather.ui.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;

import ru.davidlevi.weather.R;
import ru.davidlevi.weather.ui.Constants;

/**
 * Класс-контроллер для макета fragment_settings.xml
 */
public class SettingsFragment extends BaseFragment {
    private Activity activity;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);

        // View
        final TextInputEditText username = view.findViewById(R.id.inputUsername);
        final TextInputEditText email = view.findViewById(R.id.inputEmail);
        Button button = view.findViewById(R.id.b_savesettings);

        // Отобразим дефолтные настройки
        username.setText(sharedPreferences.getString(Constants.CURRENT_USER,""));
        email.setText(sharedPreferences.getString(Constants.CURRENT_USER_EMAIL,""));

        // Регулярные выражения
        final Pattern patternUsername = Pattern.compile(getString(R.string.regex_check_username));
        final Pattern patternEmail = Pattern.compile(getString(R.string.regex_check_email));

        // Обработчик клика по кнопке
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit()
                        .putString(Constants.CURRENT_USER, username.getText().toString())
                        .putString(Constants.CURRENT_USER_EMAIL, email.getText().toString())
                        .apply();
                IBaseFragment fragmentInterface = (IBaseFragment) activity;
                if (fragmentInterface != null)
                    fragmentInterface.setSettings();
            }
        });

        // Контроллев ввода Имени Фамилии
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView textView = (TextView) v;
                validate(textView, patternUsername, getString(R.string.not_username));
            }
        });

        // Контроллер ввода правильного e-mail
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                TextView textView = (TextView) v;
                validate(textView, patternEmail, getString(R.string.not_email));
            }
        });
    }

    /**
     * Метод проверяет валидность фразы в TextView по паттерну.
     * В случае ошибки устанавливает в TextView сообщение об ошибке.
     *
     * @param textView     TextView
     * @param pattern      Pattern
     * @param messageError String
     * @see android.support.design.widget.TextInputEditText
     */
    private void validate(TextView textView, Pattern pattern, String messageError) {
        if (!pattern.matcher(textView.getText().toString()).matches())
            textView.setError(messageError);
        else
            textView.setError(null);
    }
}
