package vamk.e1700814.payapplication.model;

public class Confirm {

    private double amount;

    private String comment;
    private boolean paymentIsSend;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean getPaymentIsSend() {
        return paymentIsSend;
    }

    public void setPaymentIsSend(boolean paymentIsSend) {
        this.paymentIsSend = paymentIsSend;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
