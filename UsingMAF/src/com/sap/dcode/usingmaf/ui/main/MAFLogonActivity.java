package com.sap.dcode.usingmaf.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

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
		//TODO 1-1 COMPLETE onCreate in MAFLogonActivity ***************************************************
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
		//TODO 1-3 COMPLETE onLogonFinished in MAFLogonActivity ***************************************************

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
		//TODO 1-2 COMPLETE showLogonScreen in MAFLogonActivity ***************************************************
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
