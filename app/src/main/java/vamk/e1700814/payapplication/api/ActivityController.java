package vamk.e1700814.payapplication.api;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.e1700814.payapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityController {

    /*
        * Luokka, jossa Volley:n avulla ollaan yhteydessä palvelimeen.
        * Tämän luokan Volley-metodit palauttavat interface-tyypille totuusarvomuuttujan
        * true tai false, riippuen palvelimen yhteydenoton onnistuneisuudesta.
        *
        * Interface-tyyppi on alustettu setSessionInterface-metodissa fragmentin kontekstilla,
        * ts. sovelluksen välilehden kontekstilla. Tällöin Volley-metodit tietävät, mille
        * välilehdelle totuusarvomuuttuja palautetaan.
        *
        * Interface-tyyppiä päätettiin käyttää, sillä moni sovelluksen välilehti
        * on tavalla tai toisella yhteydessä palvelimeen, mutta toteutus on aina hieman eri.
        *
        * Tällöin jokaisella välilehdellä on oma toteutus palvelimen yhteydenoton aloittamiselle
        * (initializeSession), ja sille, miten toimitaan kun yhteydenotto oli joko onnistunut
        * tai epäonnistunut (onAuthenticationComplete(boolean success)).
     */

    private final Application application;
    private final RequestQueue requestQueue;
    private final String ACTIVITY_URL;
    private final String GET_ACTIVITY_URL;
    private final String STATUS_URL;
    private final String REQUEST_URL;
    private final String SEND_URL;

    private final SessionPreferences sessionPreferences;

    private SessionInterface sessionInterface;

    public ActivityController(Application application) {
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
        sessionPreferences = new SessionPreferences(application.getApplicationContext());

        String BASE_URL = application.getApplicationContext().getString(R.string.BASE_URL);
        ACTIVITY_URL = "/activity/";
        GET_ACTIVITY_URL = BASE_URL + ACTIVITY_URL + "list/requests";
        STATUS_URL = BASE_URL + ACTIVITY_URL + "status";
        REQUEST_URL = BASE_URL + ACTIVITY_URL + "request";
        SEND_URL = BASE_URL + ACTIVITY_URL + "send";

    }

    private static final String COOKIE_KEY = "Cookie";
    private static final String COOKIE_BODY = "JSESSIONID=";

    private static final String JSON_REQUEST = "id_request";
    private static final String JSON_USER_ID = "id_user";
    private static final String JSON_RECIPIENT = "recipient";
    private static final String JSON_AMOUNT = "amount";
    private static final String JSON_COMMENT = "comment";
    private static final String JSON_STATUS = "status";


    public void setSessionInterface(SessionInterface sessionInterface) {
        this.sessionInterface = sessionInterface;
    }

    private void setActivitiesList(JSONArray jsonArray) {
        sessionPreferences.setActivitiesList(jsonArray);
    }

    public void getActivities() {
        String sessionId = sessionPreferences.getSessionId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GET_ACTIVITY_URL,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                setActivitiesList(jsonArray);
                sessionInterface.onAuthenticationComplete(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ActivityController", String.valueOf(error));
                sessionInterface.onAuthenticationComplete(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(COOKIE_KEY, COOKIE_BODY + sessionId);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public void updateStatus(int id_request, String status) {
        String sessionId = sessionPreferences.getSessionId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSON_REQUEST, id_request);
            jsonObject.put(JSON_STATUS, status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, STATUS_URL,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                sessionInterface.onAuthenticationComplete(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ActivityController", String.valueOf(error));
                sessionInterface.onAuthenticationComplete(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(COOKIE_KEY, COOKIE_BODY + sessionId);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public void newRequest(String id_user, double amount, String comment) {
        String sessionId = sessionPreferences.getSessionId();

        JSONObject recipient = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            recipient.put(JSON_USER_ID, id_user);
            jsonObject.put(JSON_RECIPIENT, recipient);
            jsonObject.put(JSON_AMOUNT, amount);
            jsonObject.put(JSON_COMMENT, comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, REQUEST_URL,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                sessionInterface.onAuthenticationComplete(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ActivityController", String.valueOf(error));
                sessionInterface.onAuthenticationComplete(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(COOKIE_KEY, COOKIE_BODY + sessionId);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public void newSend(String id_user, double amount, String comment) {
        String sessionId = sessionPreferences.getSessionId();

        JSONObject recipient = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            recipient.put(JSON_USER_ID, id_user);
            jsonObject.put(JSON_RECIPIENT, recipient);
            jsonObject.put(JSON_AMOUNT, amount);
            jsonObject.put(JSON_COMMENT, comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SEND_URL,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                sessionInterface.onAuthenticationComplete(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ActivityController", String.valueOf(error));
                sessionInterface.onAuthenticationComplete(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(COOKIE_KEY, COOKIE_BODY + sessionId);
                return headers;
            }
        };
        requestQueue.add(request);
    }
}