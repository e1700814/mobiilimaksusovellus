package vamk.e1700814.payapplication.model;

public class User {

    private String wholename;
    private String password;
    private String phone;
    private String city;
    private int postal;

    public String getWholename() {
        return wholename;
    }

    public void setWholename(String wholename) {
        this.wholename = wholename;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostal() {
        return postal;
    }

    public void setPostal(int postal) {
        this.postal = postal;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    public boolean isValidUserInputForRegister() {
        return getWholename().length() > 0 && getPassword().length() > 0 && getPhone().length() > 0 && getCity().length() > 0 && getPostal() > 0;
    }

    public boolean isValidUserInputForLogin() {
        return getPhone().length() > 0 && getPassword().length() > 0;
    }
}
