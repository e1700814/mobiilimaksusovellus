package vamk.e1700814.payapplication.model;

import com.e1700814.payapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class Activities {

    private int id_request;
    private String listType;
    private int imageView;
    private String timestamp;
    private boolean buttons;
    private boolean color;
    private String status;
    private String user;

    private String phone;
    private String comment;
    private double amount;

    private static final int activitiesPaymentSent = R.drawable.ic_baseline_assignment_turned_in_64;
    private static final int activitiesPaymentReceived = R.drawable.ic_baseline_assignment_turned_in_success_24;
    private static final int activitiesPaymentPending = R.drawable.ic_baseline_pending_64;

    private static final String JSON_LIST_TYPE = "listType";
    private static final String JSON_ID_REQUEST = "id_request";
    private static final String JSON_USER = "user";
    private static final String JSON_TIMESTAMP = "timestamp";
    private static final String JSON_AMOUNT = "amount";
    private static final String JSON_STATUS = "status";
    private static final String JSON_PHONE = "phone";
    private static final String JSON_COMMENT = "comment";
    private static final String JSON_SENT_REQUESTS = "sentRequests";
    private static final String JSON_SENT_PAYMENTS = "sentPayments";
    private static final String JSON_RECEIVED_PAYMENTS = "receivedPayments";
    private static final String JSON_RECEIVED_REQUESTS = "receivedRequests";
    private static final String JSON_PENDING = "pending";

    public Activities() {}

    public Activities(JSONObject jsonObject) {
        try {
            this.listType = (jsonObject.getString(JSON_LIST_TYPE));
            this.id_request = (Integer.parseInt(jsonObject.getString(JSON_ID_REQUEST)));
            this.user = (jsonObject.getString(JSON_USER));
            this.timestamp = (getLocalizedTimestamp(jsonObject.getString(JSON_TIMESTAMP)));
            this.amount = (jsonObject.getDouble(JSON_AMOUNT));
            this.status = (jsonObject.getString(JSON_STATUS));
            this.phone = (jsonObject.getString(JSON_PHONE));
            this.comment = (jsonObject.getString(JSON_COMMENT));
            setItemDetails();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setId_request(int id_request) {
        this.id_request = id_request;
    }

    public String getPhone() {
        return phone;
    }

    public String getComment() {
        return comment;
    }

    public int getId_request() {
        return id_request;
    }

    public String getUser() {
        return user;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public void setButtonVisibility(boolean buttons) {
        this.buttons = buttons;
    }

    public boolean isPendingRequest() {
        return buttons;
    }

    public void setColorGreen(boolean b) {
        this.color = b;
    }

    public boolean isColorGreen() {
        return color;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }


    public ArrayList<Activities> getParsedArrayList(String jsonInString) {
        ArrayList<Activities> activitiesList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonInString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Activities activities = new Activities(jsonObject);
                activitiesList.add(activities);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return activitiesList;
    }

    public void setItemDetails() {
        if (isSentPayment()) {
            setImageView(activitiesPaymentSent);
            setButtonVisibility(false);
            setColorGreen(false);
        }
        if (isReceivedPayment()) {
            setImageView(activitiesPaymentReceived);
            setButtonVisibility(false);
            setColorGreen(true);
        }
        if (isSentRequest()) {
            setImageView(activitiesPaymentReceived);
            setButtonVisibility(false);
            setColorGreen(true);
        }
        if (isReceivedRequest()){
            if (isPendingStatus()) {
                setImageView(activitiesPaymentPending);
                setButtonVisibility(true);
                setColorGreen(false);
            } else {
                setImageView(activitiesPaymentSent);
                setButtonVisibility(false);
                setColorGreen(false);
            }
        }
    }

    private String getLocalizedTimestamp(String timestamp) {
        OffsetDateTime dateTime = OffsetDateTime.parse(timestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        DateTimeFormatter LOCAL_FORMAT =  DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        String localizedTimeStamp = dateTime.format(LOCAL_FORMAT);

        return localizedTimeStamp;
    }

    public boolean isSentRequest() {
        if (Objects.equals(getListType(), JSON_SENT_REQUESTS)) {
            return true;
        }
        return false;
    }

    public boolean isSentPayment() {
        if (Objects.equals(getListType(), JSON_SENT_PAYMENTS)) {
            return true;
        }
        return false;
    }

    public boolean isReceivedPayment() {
        if (Objects.equals(getListType(), JSON_RECEIVED_PAYMENTS)) {
            return true;
        }
        return false;
    }

    public boolean isReceivedRequest() {
        if (Objects.equals(getListType(), JSON_RECEIVED_REQUESTS)) {
            return true;
        }
        return false;
    }

    public boolean isPendingStatus() {
        if (Objects.equals(getStatus(), JSON_PENDING)) {
            return true;
        }
        return false;
    }

}

