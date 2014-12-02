package com.sap.dcode.agency.services.offline;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.sap.dcode.agency.services.Operation;
import com.sap.dcode.agency.services.UIListener;
import com.sap.dcode.agency.services.logs.AgencyLogManager;
import com.sap.dcode.util.TraceLog;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.offline.ODataOfflineStoreFlushListener;

public class AgencyFlushListener implements ODataOfflineStoreFlushListener {
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

	public AgencyFlushListener(UIListener uiListener) {
		super();
		this.operation = Operation.OfflineFlush.getValue();
		this.uiListener = uiListener;
	}

	/*****************
	 * Methods that implements ODataOfflineStoreFlushListener interface
	 *****************/


	@Override
	public void offlineStoreFlushFailed(ODataOfflineStore store, ODataException e) {
		TraceLog.scoped(this).d("offlineStoreFlushFailed");
		notifyErrorToListener(e);

	}

	@Override
	public void offlineStoreFlushFinished(ODataOfflineStore store) {
		TraceLog.scoped(this).d("offlineStoreFlushFinished");

	}

	@Override
	public void offlineStoreFlushStarted(ODataOfflineStore store) {
		TraceLog.scoped(this).d("offlineStoreFlushStarted");

	}

	@Override
	public void offlineStoreFlushSucceeded(ODataOfflineStore store) {
		TraceLog.scoped(this).d("offlineStoreFlushSucceeded");
		notifySuccessToListener(null);

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
        AgencyLogManager.writeLogDebug("AgencyFlushListener::notifySuccess");
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
        AgencyLogManager.writeLogError("AgencyFlushListener::notifyError", exception);
    }

}
