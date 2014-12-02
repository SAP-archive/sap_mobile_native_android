package com.sap.dcode.online.services;


public interface UIListener {
	void onRequestError(int operation, Exception e);
	void onRequestSuccess(int operation, String key);
}
