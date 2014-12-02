package com.sap.dcode.online.ui.online;

import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.sap.dcode.online.services.online.OnlineManager;
import com.sap.dcode.online.types.Agency;
import com.sap.dcode.online.types.AsyncResult;

public class AgencyListDataAsyncLoader extends AsyncTaskLoader<AsyncResult<List<Agency>>> {
	Context ctx;

	public AgencyListDataAsyncLoader(Context context) {
		super(context);
		ctx = context;
	}

	@Override
	public AsyncResult<List<Agency>> loadInBackground() {
		try {
			OnlineManager.openOnlineStore(ctx);
			return new AsyncResult<List<Agency>>(OnlineManager.getAgencies());
		} catch (Exception e){
			return new AsyncResult<List<Agency>>(e);		
		}
		
	}

}
