package com.xandi.seafish.interfaces;

import java.io.Serializable;

public interface FacebookAuth extends Serializable {

    boolean isLoggedIn();

    void login();

    void logout();

    void setLoginCallback(LoginCallback loginCallback);
}
