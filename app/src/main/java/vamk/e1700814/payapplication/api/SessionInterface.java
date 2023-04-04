package vamk.e1700814.payapplication.api;

public interface SessionInterface {
    void initializeSession();
    void onAuthenticationComplete(boolean success);
    void layoutVisibility(boolean setVisible);
}