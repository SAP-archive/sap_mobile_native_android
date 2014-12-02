package com.sap.dcode.usingmaf;

import android.app.Application;

import com.sap.dcode.util.TraceLog;

public class AgencyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		//Initialize logging for debugging
        TraceLog.initialize(this);
        TraceLog.scoped(this).d("onCreate");
        
	}

}
