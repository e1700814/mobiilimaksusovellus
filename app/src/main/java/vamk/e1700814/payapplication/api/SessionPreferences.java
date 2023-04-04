package vamk.e1700814.payapplication.api;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class SessionPreferences {

    /*
     * Luokka, jossa määritellään sisäänkirjautuneen käyttäjän tiedot. Tiedot on lähetetty JSON-muodossa
     * palvelimen toimesta sisäänkirjautumisen yhteydessä, ja ne tallennetaan tänne käyttäjän nähtäväksi.
    */

    private static final String PREFERENCES_NAME = "root_preferences";
    private static final String KEY_SESSION_ID = "sessionId";
    private static final String KEY_WHOLENAME = "wholename";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";
    private static final String KEY_POSTAL = "postal";

    private static final String KEY_CREATION_TIME = "creationTime";
    private static final String KEY_ACTIVITIES_LIST = "activitiesList";
    private static final String KEY_USERS_LIST = "usersList";
    private static final String KEY_RECIPIENT_ID = "recipientId";
    private static final String KEY_RECIPIENT_NAME = "recipientName";
    private static final String KEY_RECIPIENT_PHONE = "phone";

    private final SharedPreferences sharedPreferences;

    public SessionPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setSessionId(String sessionId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.apply();
    }

    public void setUserDetails(String wholename, String phone, String city, String postal) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_WHOLENAME, wholename);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_POSTAL, postal);
        editor.apply();
    }

    public void setRecipientDetails(String id_user, String wholename, String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RECIPIENT_ID, id_user);
        editor.putString(KEY_RECIPIENT_NAME, wholename);
        editor.putString(KEY_RECIPIENT_PHONE, phone);
        editor.apply();
    }

    public void setCreationTime(Long timestamp) {
        Date date = new Date(timestamp);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, Locale.getDefault());
        String time = dateFormat.format(date);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CREATION_TIME, time);
        editor.apply();
    }

    public String getSessionId() {
        return sharedPreferences.getString(KEY_SESSION_ID, null);
    }

    public String getWholename() {
        return sharedPreferences.getString(KEY_WHOLENAME, null);
    }

    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, null);
    }

    public String getCity() {
        return sharedPreferences.getString(KEY_CITY, null);
    }

    public String getPostal() {
        return sharedPreferences.getString(KEY_POSTAL, null);
    }

    public String getCreationTime() {
        return sharedPreferences.getString(KEY_CREATION_TIME, null);
    }

    public String getActivitiesList() {
        return sharedPreferences.getString(KEY_ACTIVITIES_LIST, null);
    }

    public String getUsersList() {
        return sharedPreferences.getString(KEY_USERS_LIST, null);
    }

    public String getRecipientId() {
        return sharedPreferences.getString(KEY_RECIPIENT_ID, null);
    }

    public String getRecipientName() {
        return sharedPreferences.getString(KEY_RECIPIENT_NAME, null);
    }

    public String getRecipientPhone() {
        return sharedPreferences.getString(KEY_RECIPIENT_PHONE, null);
    }

    public void setActivitiesList(JSONArray jsonArray) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACTIVITIES_LIST, jsonArray.toString());
        editor.apply();
    }

    public void setUsersList(JSONArray jsonArray) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERS_LIST, jsonArray.toString());
        editor.apply();
    }

    public void setRecipientId(String id_user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RECIPIENT_ID, id_user);
        editor.apply();
    }

    public void setRecipientName(String wholename) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RECIPIENT_NAME, wholename);
        editor.apply();
    }

    public void setRecipientPhone(String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RECIPIENT_PHONE, phone);
        editor.apply();
    }
}
