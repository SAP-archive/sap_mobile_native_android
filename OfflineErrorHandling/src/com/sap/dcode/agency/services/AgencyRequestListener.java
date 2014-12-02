package com.sap.dcode.agency.services;

import java.util.Map;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.sap.dcode.agency.services.online.OnlineODataStoreException;
import com.sap.dcode.util.TraceLog;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataError;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.store.ODataRequestExecution;
import com.sap.smp.client.odata.store.ODataRequestListener;
import com.sap.smp.client.odata.store.ODataResponse.Headers;
import com.sap.smp.client.odata.store.ODataResponseSingle;

public class AgencyRequestListener implements ODataRequestListener {
	
	private UIListener uiListener;
	private int operation;
	
	private final int SUCCESS = 0;
	private final int ERROR = -1;

	private Handler uiHandler = new Handler(Looper.getMainLooper()){

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == SUCCESS) {
				// Notify the Activity the is complete
				String key = (String) msg.obj;
				uiListener.onRequestSuccess(operation, key);
			} else if (msg.what == ERROR) {
				Exception e = (Exception) msg.obj;
				uiListener.onRequestError(operation, e);
			}
		}
	};

	public AgencyRequestListener(int operation, UIListener uiListener) {
		super();
		this.operation = operation;
		this.uiListener = uiListener;
	}

	/*****************
	 * Methods that implements ODataRequestListener interface
	 *****************/

	@Override
	public void requestCacheResponse(ODataRequestExecution request) {
		TraceLog.scoped(this).d("requestCacheResponse");

	}

	@Override
	public void requestFailed(ODataRequestExecution request, ODataException e) {
		TraceLog.scoped(this).d("requestFailed");
		if (request!=null && request.getResponse() !=null) {
			ODataPayload payload = ((ODataResponseSingle) request.getResponse()).getPayload();
			if (payload!=null && payload instanceof ODataError) {
				ODataError oError = (ODataError) payload;
				TraceLog.d("requestFailed - status message " + oError.getMessage());
				notifyErrorToListener(new OnlineODataStoreException(oError.getMessage()));
				return;
			} 
		} 
		notifyErrorToListener(e);
	}

	@Override
	public void requestFinished(ODataRequestExecution request) {
		TraceLog.scoped(this).d("requestFinished");
	}

	@Override
	public void requestServerResponse(ODataRequestExecution request) {
		TraceLog.scoped(this).d("requestServerResponse");
		if (request!=null && request.getResponse() !=null) {
			ODataResponseSingle response = (ODataResponseSingle) request.getResponse();
			Map<Headers, String> headerMap = response.getHeaders();
			String code  = headerMap.get(Headers.Code);
			TraceLog.d("requestServerResponse - status code " + code);
			String eTag = headerMap.get(Headers.ETag);
			if (!TextUtils.isEmpty(eTag)){
				notifySuccessToListener(eTag);
				return;
			} else {
				ODataPayload payload = ((ODataResponseSingle) request.getResponse()).getPayload();
				if (payload!=null && payload instanceof ODataEntity) {
					ODataEntity oEntity = (ODataEntity) payload;
					ODataPropMap properties = oEntity.getProperties();
					ODataProperty property = properties.get(Collections.TRAVEL_AGENCY_ENTRY_ID);
					notifySuccessToListener((String)property.getValue());
					return;
				}
			}
		}
		notifySuccessToListener(null);
	}

	@Override
	public void requestStarted(ODataRequestExecution request) {
		TraceLog.scoped(this).d("requestStarted");
	}

	
	
	/*****************
	 * Utils Methods
	 *****************/
		
	
	/**
     * Notify the OnlineUIListener that the request was successful.
     */
    protected void notifySuccessToListener(String key) {
        Message msg = uiHandler.obtainMessage();
        msg.what = SUCCESS;
        msg.obj = key;
        uiHandler.sendMessage(msg);
    }

    /**
     * Notify the OnlineUIListener that the request has an error.
     *
     * @param exception an Exception that denotes the error that occurred.
     */
    protected void notifyErrorToListener(Exception exception) {
        Message msg = uiHandler.obtainMessage();
        msg.what = ERROR;
        msg.obj = exception;
        uiHandler.sendMessage(msg);
        TraceLog.e("OnlineRequestListener::notifyError", exception);
    }

}
