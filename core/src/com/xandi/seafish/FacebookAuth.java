package com.xandi.seafish;

public interface FacebookAuth {

    boolean isLoggedIn();
    void login();
    void logout();
    void setLoginCallback(LoginCallback loginCallback);
}
