package com.sap.dcode.agency.ui.offline;

import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.sap.dcode.agency.services.offline.OfflineManager;
import com.sap.dcode.agency.types.AsyncResult;
import com.sap.dcode.agency.types.OfflineError;

public class OfflineErrorListDataAsyncLoader extends AsyncTaskLoader<AsyncResult<List<OfflineError>>> {
	Context ctx;

	public OfflineErrorListDataAsyncLoader(Context context) {
		super(context);
		ctx = context;
	}

	@Override
	public AsyncResult<List<OfflineError>> loadInBackground() {
		try {
			OfflineManager.openOfflineStore(ctx);
			return new AsyncResult<List<OfflineError>>(OfflineManager.getErrorArchive());
		} catch (Exception e){
			return new AsyncResult<List<OfflineError>>(e);		
		}
		
	}

}
