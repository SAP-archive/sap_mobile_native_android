package com.sap.dcode.agency.services.online;

import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.maf.tools.logon.core.LogonCoreException;
import com.sap.smp.client.httpc.authflows.UsernamePasswordProvider;
import com.sap.smp.client.httpc.authflows.UsernamePasswordToken;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.events.ISendEvent;

public class CredentialsProvider implements UsernamePasswordProvider{
	private static CredentialsProvider instance;
	private LogonCoreContext lgCtx;
	
	private CredentialsProvider(LogonCoreContext logonContext) {
		lgCtx = logonContext;
	}

	/**
	 * @return XCSRFTokenRequestFilter
	 */
	public static CredentialsProvider getInstance(LogonCoreContext logonContext) {
		if (instance == null) {
			instance = new CredentialsProvider(logonContext);
		}
		return instance;
	}


	@Override
	public Object onCredentialsNeededForChallenge(IReceiveEvent arg0) {

		try {
			String username = lgCtx.getBackendUser();
			String password = lgCtx.getBackendPassword();
			return new UsernamePasswordToken(username, password);
		} catch (LogonCoreException e) {
			return null;
		}

	}

	@Override
	public Object onCredentialsNeededUpfront(ISendEvent arg0) {
		try {
			String username = lgCtx.getBackendUser();
			String password = lgCtx.getBackendPassword();
			return new UsernamePasswordToken(username, password);
		} catch (LogonCoreException e) {
			return null;
		}
	}

}
