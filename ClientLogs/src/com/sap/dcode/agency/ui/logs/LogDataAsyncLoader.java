package com.sap.dcode.agency.ui.logs;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.sap.dcode.agency.services.logs.AgencyLogManager;
import com.sap.dcode.agency.types.AsyncResult;

public class LogDataAsyncLoader extends AsyncTaskLoader<AsyncResult<String>> {

	public LogDataAsyncLoader(Context context) {
		super(context);
	}

	@Override
	public AsyncResult<String> loadInBackground() {
		try {
			return new AsyncResult<String>(AgencyLogManager.getFileLogs());
		} catch (Exception e){
			return new AsyncResult<String>(e);		
		}	
	}

}
