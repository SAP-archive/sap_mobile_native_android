package com.sap.dcode.usingmaf.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sap.dcode.usingmaf.services.AppSettings;
import com.sap.dcode.util.TraceLog;
import com.sap.maf.tools.logon.core.LogonCore;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.maf.tools.logon.core.LogonCoreException;
import com.sap.maf.tools.logon.logonui.api.LogonListener;
import com.sap.maf.tools.logon.logonui.api.LogonUIFacade;
import com.sap.maf.tools.logon.manager.LogonContext;
import com.sap.maf.tools.logon.manager.LogonManager.LogonManagerException;

public class MAFLogonActivity extends Activity implements LogonListener {
	private final String TAG = MAFLogonActivity.class.getSimpleName();
	private LogonUIFacade mLogonUIFacade;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get an instance of the LogonUIFacade
		mLogonUIFacade = LogonUIFacade.getInstance();
		// set context reference
		mContext = this;

		this.showLogonScreen();
	}

	@Override
	public void objectFromSecureStoreForKey() {

	}

	@Override
	public void onApplicationSettingsUpdated() {

	}

	@Override
	public void onBackendPasswordChanged(boolean arg0) {

	}

	@Override
	public void onLogonFinished(String message, boolean isSuccess,
			LogonContext lgContext) {
		TraceLog.d("onLogonFinished: "+message);
		//Check if it finished successfully
		if (isSuccess) {
			try {
				// For debugging purposes will log the application connection id and
				// the end point url. In a productive app, remember to remove this logs
				String appConnID = LogonCore.getInstance().getLogonContext()
						.getConnId();
				TraceLog.d("onLogonFinished: appcid:"+ appConnID);
				TraceLog.d("onLogonFinished: endpointurl:"+ lgContext.getEndPointUrl());
				
			} catch (LogonManagerException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			} catch (LogonCoreException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			}

			// Navigate to the Main menu screen
			Intent goToNextActivity = new Intent(this, MainActivity.class);
			startActivity(goToNextActivity);
			finish();
		} 

	}

	@Override
	public void onSecureStorePasswordChanged(boolean arg0, String arg1) {

	}

	@Override
	public void onUserDeleted() {

	}

	@Override
	public void registrationInfo() {

	}

	private void showLogonScreen() {
		mLogonUIFacade.init(this, mContext, AppSettings.APP_ID);

		// ask LogonUIFacede to present the logon screen
		// set the resulting view as the content view for this activity
		setContentView(mLogonUIFacade.logon());
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.showLogonScreen();
	}

	@Override
	public void onRefreshCertificate(boolean arg0, String arg1) {
	}

}
