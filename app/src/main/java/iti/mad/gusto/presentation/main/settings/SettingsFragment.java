package iti.mad.gusto.presentation.main.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.List;

import com.google.android.material.button.MaterialButton;

import iti.mad.gusto.R;
import iti.mad.gusto.core.managers.LocalizationManager;
import iti.mad.gusto.core.managers.ThemeManager;
import iti.mad.gusto.presentation.auth.activity.AuthActivity;
import iti.mad.gusto.presentation.common.constant.NavigationKey;
import iti.mad.gusto.presentation.common.util.ThemeAwareIconToast;

public class SettingsFragment extends Fragment implements SettingsContract.View {

    private Spinner languageSpinner;
    private Spinner themeSpinner;
    private MaterialButton signOutBtn;
    private SettingsContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        languageSpinner = view.findViewById(R.id.spinner_language);
        themeSpinner = view.findViewById(R.id.spinner_theme);
        signOutBtn = view.findViewById(R.id.btn_sign_out);

        presenter = new SettingsPresenter(requireContext(), this);

        setupThemeSpinner();
        setupLanguageSpinner();

    }

    private void setupThemeSpinner() {

        List<String> themes = Arrays.asList(
                getString(R.string.system_default),
                getString(R.string.light),
                getString(R.string.dark)
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                themes
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);

        int currentMode = ThemeManager.getInstance().getSelectedThemeMode();

        int selectedPosition = 0;

        if (currentMode == AppCompatDelegate.MODE_NIGHT_NO)
            selectedPosition = 1;
        else if (currentMode == AppCompatDelegate.MODE_NIGHT_YES)
            selectedPosition = 2;

        themeSpinner.setSelection(selectedPosition);
    }

    private void setupLanguageSpinner() {

        List<String> languages = Arrays.asList(
                "English",
                "العربية"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                languages
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        String currentLang = LocalizationManager.getInstance().getSelectedLanguage();

        int selectedPosition = currentLang.equals("ar") ? 1 : 0;

        languageSpinner.setSelection(selectedPosition);

        signOutBtn.setOnClickListener(v -> presenter.signOut());
    }

    @Override
    public void showError(String message) {
        if (isAdded()) {
            ThemeAwareIconToast.error(requireContext(), message);
        }
    }

    @Override
    public void navigateAuth() {
        Intent intent = new Intent(requireActivity(), AuthActivity.class);
        intent.putExtra(NavigationKey.SHOULD_SKIP_OVERLAY_ANIMATION, true);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDetach();
    }
}
