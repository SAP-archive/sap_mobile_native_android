package com.sap.dcode.agency.ui.offline;

import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.sap.dcode.agency.services.offline.OfflineManager;
import com.sap.dcode.agency.types.Agency;
import com.sap.dcode.agency.types.AsyncResult;

public class OfflineAgencyListDataAsyncLoader extends AsyncTaskLoader<AsyncResult<List<Agency>>> {
	Context ctx;

	public OfflineAgencyListDataAsyncLoader(Context context) {
		super(context);
		ctx = context;
	}

	@Override
	public AsyncResult<List<Agency>> loadInBackground() {
		try {
			OfflineManager.openOfflineStore(ctx);
			return new AsyncResult<List<Agency>>(OfflineManager.getAgencies());
		} catch (Exception e){
			return new AsyncResult<List<Agency>>(e);		
		}
		
	}

}
