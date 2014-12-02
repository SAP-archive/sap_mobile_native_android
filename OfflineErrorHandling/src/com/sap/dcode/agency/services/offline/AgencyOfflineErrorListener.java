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
		
		TraceLog.scoped(this).d("offlineStoreRequestFailed");
		if (request!=null && request.getResponse() !=null) {
			ODataPayload payload = ((ODataResponseSingle) request.getResponse()).getPayload();
			if (payload!=null && payload instanceof ODataError) {
				ODataError oError = (ODataError) payload;
				OfflineODataStoreException exc = new OfflineODataStoreException(oError.getMessage());
		        TraceLog.e("offlineStoreRequestFailed", exc);
			} else {
		        TraceLog.e("offlineStoreRequestFailed", exception);
			}
		} else {
	        TraceLog.e("offlineStoreRequestFailed", exception);
		}

	}


}
