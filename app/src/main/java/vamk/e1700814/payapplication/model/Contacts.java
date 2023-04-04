package vamk.e1700814.payapplication.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Contacts {

    private final ArrayList<String> listOfUsers = new ArrayList<>();
    private static final String JSON_USER = "wholename";

    public ArrayList<String> getParsedArrayList(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String username = jsonObject.getString(JSON_USER);
                listOfUsers.add(username);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfUsers;
    }
}
