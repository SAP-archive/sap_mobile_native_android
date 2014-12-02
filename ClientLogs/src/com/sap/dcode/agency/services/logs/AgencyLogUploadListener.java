package com.sap.dcode.agency.services.logs;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Trace;

import com.sap.dcode.agency.services.UIListener;
import com.sap.dcode.util.TraceLog;
import com.sap.smp.client.supportability.UploadListener;
import com.sap.smp.client.supportability.UploadResult;

public class AgencyLogUploadListener implements UploadListener{
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

	public AgencyLogUploadListener(int operation, UIListener uiListener) {
		super();
		this.operation = operation;
		this.uiListener = uiListener;
	}

	@Override
	public void onUploadFailure(UploadResult result) {
		TraceLog.scoped(this).d("onUploadFailure");
		notifyErrorToListener(new AgencyLogException("error uploading logs: status code: "+result.getResponseStatusCode() + " "+ result.getHint()));
	}

	@Override
	public void onUploadSuccess() {
		TraceLog.scoped(this).d("onUploadSuccess");
		notifySuccessToListener(null);
		
	}

	/*****************
	 * Utils Methods
	 *****************/
		
	
	/**
     * Notify the UIListener that the request was successful.
     */
    protected void notifySuccessToListener(String key) {
        Message msg = uiHandler.obtainMessage();
        msg.what = SUCCESS;
        msg.obj = key;
        uiHandler.sendMessage(msg);
        AgencyLogManager.writeLogDebug("AgencyLogUploadListener::notifySuccessToListener: "+key);

    }

    /**
     * Notify the UIListener that the request has an error.
     *
     * @param exception an Exception that denotes the error that occurred.
     */
    protected void notifyErrorToListener(Exception exception) {
        Message msg = uiHandler.obtainMessage();
        msg.what = ERROR;
        msg.obj = exception;
        uiHandler.sendMessage(msg);
        AgencyLogManager.writeLogError("AgencyLogUploadListener::notifyErrorToListener", exception);
    }


}
