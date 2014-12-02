package com.sap.dcode.agency;

import android.app.Application;

import com.sap.dcode.agency.services.logs.AgencyLogManager;
import com.sap.dcode.util.TraceLog;

public class AgencyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
        AgencyLogManager.initialize(this);

		//Initialize logging for debugging
        TraceLog.initialize(this);
        TraceLog.scoped(this).d("onCreate");
        
	}

}
