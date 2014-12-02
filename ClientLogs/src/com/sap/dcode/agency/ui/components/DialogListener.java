package com.sap.dcode.agency.ui.components;

public abstract class DialogListener {

    public void onUserAction(int action, Object... args) {};
    public void onError(Exception e) {};

}
