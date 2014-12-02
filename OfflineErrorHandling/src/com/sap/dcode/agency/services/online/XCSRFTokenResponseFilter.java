package com.sap.dcode.agency.services.online;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.filters.IResponseFilter;
import com.sap.smp.client.httpc.filters.IResponseFilterChain;
import com.sap.smp.client.httpc.utils.SAPLoggerUtils;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.supportability.ClientLogLevel;
import com.sap.smp.client.supportability.ClientLogger;
import com.sap.smp.client.supportability.Supportability;

public class XCSRFTokenResponseFilter implements IResponseFilter {
	private static XCSRFTokenResponseFilter instance;

	private Context context;
	private XCSRFTokenRequestFilter requestFilter;

	
	private XCSRFTokenResponseFilter(Context context, XCSRFTokenRequestFilter requestFilter) {
		this.context = context;
		this.requestFilter = requestFilter;
	}

	/**
	 * @return XCSRFTokenResponseFilter
	 */
	public static XCSRFTokenResponseFilter getInstance(Context context, XCSRFTokenRequestFilter requestFilter) {
		if (instance == null) {
			instance = new XCSRFTokenResponseFilter(context, requestFilter);
		}
		return instance;
	}


	@Override
	public Object filter(IReceiveEvent event, IResponseFilterChain chain)
			throws IOException {
		ClientLogger logger = Supportability.getInstance().getClientLogger(this.context, OnlineODataStore.class.getCanonicalName());

		SAPLoggerUtils.logResponseDetails(event, logger, ClientLogLevel.INFO, true, true);
		List<String> xcsrfTokens = event.getResponseHeaders().get("X-CSRF-Token");
		Log.i("XCSRFTokenResponseFilter", "xcsrfTokens: " + xcsrfTokens);
		if (xcsrfTokens != null) {
			String xcsrfToken = xcsrfTokens.get(0);
			if (xcsrfToken != null)
				requestFilter.setLastXCSRFToken(xcsrfToken);
		}

		return chain.filter();
	}

	@Override
	public Object getDescriptor() {
		return "XCSRFTokenResponseFilter";
	}

}
