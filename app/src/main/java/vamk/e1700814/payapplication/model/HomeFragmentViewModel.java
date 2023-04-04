package vamk.e1700814.payapplication.model;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeFragmentViewModel extends ViewModel {

    /*
            ViewModel-luokka, jossa on metodeja liittyen numeronäppäimistössä
            käytettävään logiikkaan.

            Luokka käyttää LiveData-objektia, koska käyttäjän syöttämä numeroarvo
            haluttiin olevan usean eri välilehden käytettävissä. Lisäksi, tämä
            mahdollisti numeron säilymisen siirryttäessä välilehdeltä toiselle.
     */


    private final MutableLiveData<Double> input = new MutableLiveData<>();
    private final MutableLiveData<Boolean> paymentIsSend = new MutableLiveData<>();
    private final MutableLiveData<String> comment = new MutableLiveData<>();

    public LiveData<Double> getInput() {
        return input;
    }
    public LiveData<Boolean> getPaymentIsSend() {
        return paymentIsSend;
    }

    public LiveData<String> getComment() {
        return comment;
    }

    private String key = "";
    private String wholeNumber = "";
    private String decimal = "";
    private Double currentValue = 0.0;
    private boolean radioButtonIsSend = true;
    private boolean decimalPointIsActive = false;
    private static final int MAX_DECIMAL = 2;
    private static final int MAX_WHOLE_NUMBER = 4;
    private static final String DECIMAL_POINT = ".";
    private static final String SINGLE_ZERO = "0";

    public void setRadioButtonToSend(boolean b) {
        radioButtonIsSend = b;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getWholeNumber() {
        return wholeNumber;
    }

    public String getDecimal() {
        return decimal;
    }

    public String incrementDecimal() {
        return (decimal += key);
    }

    public String incrementWholeNumber() {
        return (wholeNumber += key);
    }

    public String decrementDecimal() {
        return (decimal = decimal.substring(0, decimal.length() - 1));
    }

    public String decrementWholeNumber() {
        return (wholeNumber = wholeNumber.substring(0, wholeNumber.length() - 1));
    }

    public boolean maxWholeNumberReached() {
        return wholeNumber.length() >= MAX_WHOLE_NUMBER;
    }

    public boolean maxDecimalReached() {
        return decimal.length() >= MAX_DECIMAL;
    }

    public String setDecimalPoint(Boolean decimalPoint) {
        if (decimalPoint) {
            return DECIMAL_POINT;
        }
        return "";
    }

    public void toggleDecimalPoint(Boolean b) {
        decimalPointIsActive = b;
    }

    public boolean hasDecimalPoint() {
        return decimalPointIsActive;
    }

    public boolean hasWholeNumber() {
        return wholeNumber.length() > 0;
    }

    public boolean hasDecimal() {
        return decimal.length() > 0;
    }

    public boolean isDecimalAndMaxNotReached() {
        return hasDecimalPoint() && !(maxDecimalReached());
    }

    public boolean isWholeNumberAndMaxNotReached() {
        return !(hasDecimalPoint()) && !(maxWholeNumberReached()) && !(isSingularZero());
    }

    public boolean endsInDecimalPoint() {
        return wholeNumber.length() > 0 && hasDecimalPoint() && decimal.length() == 0;
    }

    public boolean valueIsWholeNumber() {
        return hasWholeNumber() && !(hasDecimal());
    }

    public boolean valueIsDecimal() {
        return hasWholeNumber() && hasDecimal();
    }

    public boolean valueIsEmpty() {
        return !(hasWholeNumber()) && !(hasDecimal());
    }

    /*
     * Tarkistetaan, alkaako luku nollalla, jottei lisätä ylimääräisiä nollia (TRUE, FALSE).
     * Esim.
     * SALLITTU = 0.99
     * EI SALLITTU = 0000.99
     */
    public boolean isSingularZero() {
        return wholeNumber.equals(SINGLE_ZERO);
    }

    public void setMutableValue() {
        if (valueIsWholeNumber()) {
            currentValue = Double.parseDouble(wholeNumber);
        }
        if (valueIsDecimal()) {
            currentValue = Double.parseDouble(String.join(DECIMAL_POINT, wholeNumber, decimal));
        }
        if (valueIsEmpty()) {
            currentValue = 0.0;
        }
        if (radioButtonIsSend) {
            paymentIsSend.setValue(true);
        }
        if (!radioButtonIsSend) {
            paymentIsSend.setValue(false);
        }
        input.setValue(currentValue);
    }

    public void setCommentMutableValue(String comment) {
        this.comment.setValue(comment);
    }

    public void resetMutableValue() {
        key = "";
        wholeNumber = "";
        decimal = "";
        currentValue = 0.0;
        radioButtonIsSend = true;
        decimalPointIsActive = false;
        input.setValue(currentValue);
    }

    public boolean isValidInput() {
        return !(endsInDecimalPoint()) && currentValue > 0;
    }
}
