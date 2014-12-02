package com.sap.dcode.online.ui.components;

public abstract class DialogListener {

    public void onUserAction(int action, Object... args) {};
    public void onError(Exception e) {};

}
