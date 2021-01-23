package com.xandi.seafish.interfaces;

public interface LoginCallback {

    void userLoggedIn(String userName, Long personalRecord);
    void userLoggedOut();
}