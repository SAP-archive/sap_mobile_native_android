package com.sap.dcode.agency.services.offline;

import com.sap.dcode.util.TraceLog;
import com.sap.smp.client.odata.ODataError;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.offline.ODataOfflineStoreRequestErrorListener;
import com.sap.smp.client.odata.store.ODataRequestExecution;
import com.sap.smp.client.odata.store.ODataResponseSingle;

public class AgencyOfflineErrorListener implements ODataOfflineStoreRequestErrorListener {

	public AgencyOfflineErrorListener() {
		super();
	}

	/*****************
	 * Methods that implements ODataOfflineStoreRequestErrorListener interface
	 *****************/
	@Override
	public void offlineStoreRequestFailed(ODataOfflineStore store,
			ODataRequestExecution request, ODataException exception) {
		//TODO 4-1 COMPLETE offlineStoreRequestFailed in AgencyOfflineErrorListener ***********
		//BEGIN
		TraceLog.scoped(this).d("offlineStoreRequestFailed");
		//Verify if the request is null
		if (request!=null && request.getResponse() !=null) {
			ODataPayload payload = ((ODataResponseSingle) 
					request.getResponse()).getPayload();
			//Verify if the request contains payload
			if (payload!=null && payload instanceof ODataError) {
				ODataError oError = (ODataError) payload;
				//Get the error message
				OfflineODataStoreException exc = 
						new OfflineODataStoreException(oError.getMessage());
				//Log the error message
		        TraceLog.e("offlineStoreRequestFailed", exc);
			} else {
				//Log the exception
		        TraceLog.e("offlineStoreRequestFailed", exception);
			}
		} else {
			//Log the exception
	        TraceLog.e("offlineStoreRequestFailed", exception);
		}
		//END
	}


}
