package vamk.e1700814.payapplication.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.e1700814.payapplication.R;

import vamk.e1700814.payapplication.api.UserController;
import vamk.e1700814.payapplication.api.SessionInterface;
import vamk.e1700814.payapplication.api.SessionPreferences;

public class SettingsFragment extends PreferenceFragmentCompat implements SessionInterface {

    private static final String KEY_SESSION_ID = "sessionId";
    private static final String KEY_WHOLENAME = "wholename";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";
    private static final String KEY_POSTAL = "postal";
    private static final String KEY_LOGOUT_BUTTON = "logout_button";
    private static final String KEY_LOGOUT_PROGRESSBAR = "logout_progressbar";
    private static final String KEY_CREATION_TIME = "creationTime";

    SessionPreferences sessionPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        sessionPreferences = new SessionPreferences(SettingsFragment.this.requireActivity().getApplication());
        EditTextPreference pref_session = findPreference(KEY_SESSION_ID);
        EditTextPreference pref_wholename = findPreference(KEY_WHOLENAME);
        EditTextPreference pref_phone = findPreference(KEY_PHONE);
        EditTextPreference pref_city = findPreference(KEY_CITY);
        EditTextPreference pref_postal = findPreference(KEY_POSTAL);
        EditTextPreference pref_timestamp = findPreference(KEY_CREATION_TIME);
        Preference logoutButton = findPreference(KEY_LOGOUT_BUTTON);

        if (pref_session != null) {
            pref_session.setText(sessionPreferences.getSessionId());
        }
        if (pref_wholename != null) {
            pref_wholename.setText(sessionPreferences.getWholename());
        }

        if (pref_phone != null) {
            pref_phone.setText(sessionPreferences.getPhone());
        }

        if (pref_city != null) {
            pref_city.setText(sessionPreferences.getCity());
        }

        if (pref_postal != null) {
            pref_postal.setText(sessionPreferences.getPostal());
        }

        if (pref_timestamp != null) {
            pref_timestamp.setText(sessionPreferences.getCreationTime());
        }

        if (logoutButton != null) {
            logoutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    layoutVisibility(true);
                    initializeSession();
                    return true;
                }
            });
        }
    }

    @Override
    public void initializeSession() {
        UserController userController = new UserController(SettingsFragment.this.requireActivity().getApplication());
        userController.setSessionInterface(this);
        userController.logout();
    }

    @Override
    public void onAuthenticationComplete(boolean success) {
        layoutVisibility(false);
        if (success) {
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack(R.id.homeFragment, false);
        }
    }

    @Override
    public void layoutVisibility(boolean setVisible) {
        Preference logoutProgressBar = findPreference(KEY_LOGOUT_PROGRESSBAR);
        if (logoutProgressBar != null) {
            logoutProgressBar.setVisible(setVisible);
        }
    }
}