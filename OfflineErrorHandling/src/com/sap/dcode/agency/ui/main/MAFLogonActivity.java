package com.sap.dcode.agency.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.sap.dcode.agency.services.AppSettings;
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
		LogonCore lgCore = LogonCore.getInstance();
		if (lgCore!=null && lgCore.isStoreAvailable()) {
			LogonCoreContext lgCtx = lgCore.getLogonContext();
			if (lgCtx != null && lgCtx.isSecureStoreOpen()){
				try {
					if (!TextUtils.isEmpty(lgCtx.getConnId())) {
				        Intent intent = new Intent();
				        intent.setClass(this, MainActivity.class);
				        startActivity(intent);
				        finish();
					}
				}catch (LogonCoreException e){
					TraceLog.e(TAG + "::onCreate", e);
				}
			}
		}		

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
		// Logon successful - setup global request manager
		Log.v(TAG, message);
		if (isSuccess) {
			try {
				// get Application Connection ID
				String appConnID = LogonCore.getInstance().getLogonContext()
						.getConnId();
				Log.d(TAG, "onLogonFinished: appcid:"+ appConnID);
				Log.d(TAG, "onLogonFinished: endpointurl:"+ lgContext.getEndPointUrl());
				
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
