package com.xandi.seafish.interfaces;

public interface FacebookAuth {

    boolean isLoggedIn();
    void login();
    void logout();
    void setLoginCallback(LoginCallback loginCallback);
}
