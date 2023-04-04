package vamk.e1700814.payapplication.api;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.e1700814.payapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserController {

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
    private final String LOGIN_URL;
    private final String LOGOUT_URL;
    private final String REGISTER_URL;
    private final String SESSION_URL;
    private final String USERS_LIST_URL;
    private final String WHOLENAME_URL;

    SessionPreferences sessionPreferences;
    private SessionInterface sessionInterface;

    public UserController(Application application) {
        this.application = application;
        requestQueue = Volley.newRequestQueue(application);
        sessionPreferences = new SessionPreferences(application.getApplicationContext());

        String BASE_URL = application.getApplicationContext().getString(R.string.BASE_URL);
        String USER_URL = "/user/";
        LOGIN_URL = BASE_URL + USER_URL + "login";
        LOGOUT_URL = BASE_URL + USER_URL + "logout";
        REGISTER_URL = BASE_URL + USER_URL + "register";
        SESSION_URL = BASE_URL + USER_URL + "session";
        USERS_LIST_URL = BASE_URL + USER_URL + "search/{wholename}";
        WHOLENAME_URL = BASE_URL + USER_URL + "search/username/{wholename}";

        Log.i("Url:", LOGIN_URL);
    }

    public void setSessionInterface(SessionInterface sessionInterface) {
        this.sessionInterface = sessionInterface;
    }

    private static final String COOKIE_KEY = "Cookie";
    private static final String COOKIE_BODY = "JSESSIONID=";
    private static final String SESSION_ID = "sessionId";

    private static final String JSON_USER_ID = "id_user";
    private static final String JSON_USER = "wholename";
    private static final String JSON_PASSWORD = "password";
    private static final String JSON_PHONE = "phone";
    private static final String JSON_CITY = "city";
    private static final String JSON_POSTAL = "postal";
    private static final String JSON_CREATION_TIME = "creationTime";

    private static final String ERROR_GENERIC = "Virhe yhteydessä. Yritä myöhemmin uudelleen.";
    private static final String ERROR_AUTHENTICATION = "Väärä käyttäjätunnus tai salasana.";
    private static final String ERROR_USER_EXISTS = "Käyttäjätunnus on jo olemassa. Valitse uusi tunnus.";
    private static final String ERROR_SESSION = "Ole hyvä ja kirjaudu uudelleen sisään.";
    private static final String ERROR_USER_DOES_NOT_EXIST = "Käyttäjätunnusta ei löytynyt. Valitse käyttäjä listasta.";

    public void setSession() {
        String sessionId = sessionPreferences.getSessionId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SESSION_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                sessionInterface.onAuthenticationComplete(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    public void login(String phone, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSON_PHONE, phone);
            jsonObject.put(JSON_PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, LOGIN_URL,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    sessionPreferences.setSessionId(response.getString(SESSION_ID));
                    sessionPreferences.setCreationTime(response.getLong(JSON_CREATION_TIME));
                    sessionPreferences.setUserDetails(response.getString(JSON_USER),
                            response.getString(JSON_PHONE),
                            response.getString(JSON_CITY),
                            response.getString(JSON_POSTAL));
                    sessionInterface.onAuthenticationComplete(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorHandler(error);
                sessionInterface.onAuthenticationComplete(false);
            }
        });
        requestQueue.add(request);
    }

    public void logout() {
        String sessionId = sessionPreferences.getSessionId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, LOGOUT_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                sessionInterface.onAuthenticationComplete(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorHandler(error);
                sessionInterface.onAuthenticationComplete(true);
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

    public void register(String wholename, String password, String phone, String city, int postal) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JSON_USER, wholename);
            jsonObject.put(JSON_PASSWORD, password);
            jsonObject.put(JSON_PHONE, phone);
            jsonObject.put(JSON_CITY, city);
            jsonObject.put(JSON_POSTAL, postal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, REGISTER_URL,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    sessionPreferences.setSessionId(response.getString(SESSION_ID));
                    sessionPreferences.setCreationTime(response.getLong(JSON_CREATION_TIME));
                    sessionPreferences.setUserDetails(wholename, phone, city, String.valueOf(postal));
                    sessionInterface.onAuthenticationComplete(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorHandler(error);
                sessionInterface.onAuthenticationComplete(false);
            }
        });
        requestQueue.add(request);
    }

    public void getUsers(String wholename) {
        String sessionId = sessionPreferences.getSessionId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, USERS_LIST_URL.replace("{wholename}", wholename),
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                sessionPreferences.setUsersList(jsonArray);
                sessionInterface.onAuthenticationComplete(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    public void getRecipientDetails(String wholename) {
        String sessionId = sessionPreferences.getSessionId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WHOLENAME_URL.replace("{wholename}", wholename),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    sessionPreferences.setRecipientDetails(response.getString(JSON_USER_ID),
                            response.getString(JSON_USER),
                            response.getString(JSON_PHONE));
                    sessionInterface.onAuthenticationComplete(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorHandler(error);
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

    private void errorHandler(VolleyError error) {
        int errorCode;
        try {
            errorCode = error.networkResponse.statusCode;
        } catch (NullPointerException e) {
            toast(ERROR_GENERIC);
            Log.e("Error", String.valueOf(error));
            return;
        }

        Log.i("VolleyError", String.valueOf(errorCode));

        switch (errorCode) {
            case 400:
                toast(ERROR_USER_EXISTS);
                break;
            case 401:
                toast(ERROR_AUTHENTICATION);
                break;
            case 403:
                toast(ERROR_SESSION);
                break;
            case 404:
                toast(ERROR_USER_DOES_NOT_EXIST);
                break;
            default:
                toast(ERROR_GENERIC);
                break;
        }
    }

    private void toast(String message) {
        Toast.makeText(application, message, Toast.LENGTH_LONG).show();
    }
}