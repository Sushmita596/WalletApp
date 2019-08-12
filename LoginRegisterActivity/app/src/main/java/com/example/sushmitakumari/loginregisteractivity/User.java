package com.example.sushmitakumari.loginregisteractivity;

import android.util.Log;

/**
 * Created by sushmita.kumari on 16-01-2018.
 */

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String phonenumber;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
        Log.d("User", "name = "+name);
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        Log.d("User", "phonenumber = "+phonenumber);
    }

    public void setEmail(String email) {
        this.email = email;
        Log.d("User", "email = "+email);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        Log.d("User", "getname() name = "+name);
        return name;
    }

    public String getPhonenumber() {
        Log.d("User", "getPhonenumber() number = "+phonenumber);
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
}
