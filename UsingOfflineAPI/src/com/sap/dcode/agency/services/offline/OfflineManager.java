package com.sap.dcode.agency.services.offline;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.sap.dcode.agency.services.AgencyRequestListener;
import com.sap.dcode.agency.services.Collections;
import com.sap.dcode.agency.services.Operation;
import com.sap.dcode.agency.services.UIListener;
import com.sap.dcode.agency.types.Agency;
import com.sap.dcode.util.TraceLog;
import com.sap.maf.tools.logon.core.LogonCore;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.maf.tools.logon.logonui.api.LogonUIFacade;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.IManagerConfigurator;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataPayload;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.impl.ODataEntityDefaultImpl;
import com.sap.smp.client.odata.impl.ODataErrorDefaultImpl;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;
import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.offline.ODataOfflineStoreOptions;
import com.sap.smp.client.odata.store.ODataRequestParamSingle;
import com.sap.smp.client.odata.store.ODataRequestParamSingle.Mode;
import com.sap.smp.client.odata.store.ODataResponseSingle;
import com.sap.smp.client.odata.store.impl.ODataRequestParamSingleDefaultImpl;

public class OfflineManager {
	public static final String TAG = OfflineManager.class.getSimpleName();
	private static final String HTTP_HEADER_SMP_APPCID = "X-SMP-APPCID";

	private static ODataOfflineStore offlineStore;

	/**
	 * Initialize a new offline odata store
	 * @param context the application context
	 * @return true if it's initialized, false otherwise
	 * @throws OfflineODataStoreException
	 */
	public static boolean openOfflineStore(Context context) throws OfflineODataStoreException{
		//TODO 3-1 COMPLETE openOfflineStore in OfflineManager ***************************************************
		return false;
	}
	
	/**
	 * Get the list of agencies from the local database
	 * @return List<Agency> list of travel agencies
	 * @throws OfflineODataStoreException
	 */
	public static List<Agency> getAgencies() throws OfflineODataStoreException{
		//TODO 3-2 COMPLETE getAgencies in OfflineManager ***************************************************
		//BEGIN
		ArrayList<Agency> agencyList = new ArrayList<Agency>();
		return agencyList;
		//END
	}

	/**
	 * Create a new entity in the local database
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void createAgency(Agency agency, UIListener uiListener) throws OfflineODataStoreException{
		//TODO 3-3 COMPLETE createAgency in OfflineManager ***************************************************

	}

	/**
	 * update an existing agency entity in the local database
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void updateAgency(Agency agency, UIListener uiListener) throws OfflineODataStoreException{
		//TODO 3-4 COMPLETE updateAgency in OfflineManager ***************************************************

	}

	/**
	 * Delete the agency entity in the local database
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void deleteAgency(Agency agency, UIListener uiListener) throws OfflineODataStoreException{
		//TODO 3-5 COMPLETE deleteAgency in OfflineManager ***************************************************

	}

	/**
	 * Asynchronously starts sending pending modification requests to the server
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void flushQueuedRequests(UIListener uiListener) throws OfflineODataStoreException{
		//TODO 3-6 COMPLETE flushQueuedRequests in OfflineManager ***************************************************

	}

	/**
	 * Asynchronously refreshes the store with the OData service and calls the delegate 
	 * when the refresh starts, finishes, or fails
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void refresh(UIListener uiListener) throws OfflineODataStoreException{
		//TODO 3-7 COMPLETE refresh in OfflineManager ***************************************************

	}

	/**
	 * Create Travel agency entity type (payload) with the corresponding value
	 * @param agency agency information
	 * @return ODataEntity with the agency information
	 * @throws ODataParserException
	 */
	private static ODataEntity createAgencyEntity(Agency agency) throws ODataParserException{
		ODataEntity newEntity = null;
		if (agency!=null && agency.isInitialized()) {		
			newEntity = new ODataEntityDefaultImpl(Collections.TRAVEL_AGENCY_ENTITY_TYPE);
			
			String agencyId = agency.getAgencyId();
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_ID,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_ID, agencyId));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_NAME,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_NAME, agency.getAgencyName()));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_STREET,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_STREET, agency.getStreet()));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_CITY,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_CITY, agency.getCity()));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_COUNTRY,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_COUNTRY, agency.getCountry()));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_URL,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_URL, agency.getWebsite()));
			
			if (!TextUtils.isEmpty(agencyId)){
				String resourcePath = Collections.getEditResourcePath(Collections.TRAVEL_AGENCY_COLLECTION, agencyId);
				newEntity.setResourcePath(resourcePath, resourcePath);
				newEntity.setEtag(agencyId);
			}
		}
		return newEntity;
		
	}	
}
