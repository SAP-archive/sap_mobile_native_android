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
import com.sap.dcode.agency.services.logs.AgencyLogManager;
import com.sap.dcode.agency.types.Agency;
import com.sap.dcode.agency.types.OfflineError;
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

	private static ODataOfflineStore offlineStore;

	/**
	 * Initialize a new offline odata store
	 * @param context the application context
	 * @return true if it's initialized, false otherwise
	 * @throws OfflineODataStoreException
	 */
	public static boolean openOfflineStore(Context context) throws OfflineODataStoreException{
		//BEGIN
		if (offlineStore==null){
			try {
				//This instantiate the native UDB libraries which are located in the libodataofflinejni.so file
				ODataOfflineStore.globalInit();

				//Get application endpoint URL
				LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
				String endPointURL = lgCtx.getAppEndPointUrl();
				URL url = new URL(endPointURL);
			
				// Define the offline store options. 
				// Connection parameter and credentials and 
				// the application connection id we got at the registration
				ODataOfflineStoreOptions options = new ODataOfflineStoreOptions();
			
				options.host = url.getHost();
				options.port = String.valueOf(url.getPort());
				options.enableHTTPS = lgCtx.isHttps();
				options.serviceRoot= endPointURL;
			
                //The logon configurator uses the information obtained in the registration 
                // (i.e endpoint URL, login, etc ) to configure the conversation manager	
				IManagerConfigurator configurator = 
                     LogonUIFacade.getInstance().getLogonConfigurator(context);
                HttpConversationManager manager = new HttpConversationManager(context);
                configurator.configure(manager);

				options.conversationManager = manager;
	
				options.enableRepeatableRequests = false;
				options.storeName="flight";
			
				//This defines the oData collections which will be stored in the offline store
				options.definingRequests.put("reg1", Collections.TRAVEL_AGENCY_COLLECTION);
				
				//Open offline store
				offlineStore = new ODataOfflineStore(context);
			    
				//Assign an Offline
				offlineStore.setRequestErrorListener(new AgencyOfflineErrorListener());

				offlineStore.openStoreSync(options);
				TraceLog.d("openOfflineStore: library version"+ ODataOfflineStore.libraryVersion());
				return true;
				
			} catch (Exception e) {
				AgencyLogManager.writeLogError("openOfflineStore", e);
				throw new OfflineODataStoreException(e);
			}
			
		} else {
			return true;
		}
		//END
	}
	
	/**
	 * Get the list of agencies from the local database
	 * @return List<Agency> list of travel agencies
	 * @throws OfflineODataStoreException
	 */
	public static List<Agency> getAgencies() throws OfflineODataStoreException{
		//BEGIN
		ArrayList<Agency> agencyList = new ArrayList<Agency>();

		//Check if the offline oData store is initialized
		if (offlineStore!=null){
			
			Agency agency;
			ODataProperty property;
			ODataPropMap properties;
			try {

				//Define the resource path 
				String resourcePath = Collections.TRAVEL_AGENCY_COLLECTION + 
						"?$orderby="+Collections.TRAVEL_AGENCY_ENTRY_ID+"%20desc";

				ODataRequestParamSingle request = new ODataRequestParamSingleDefaultImpl();
				request.setMode(Mode.Read);
				request.setResourcePath(resourcePath);

				//Send a request to read the travel agencies from the local database
				ODataResponseSingle response = (ODataResponseSingle) offlineStore.
						executeRequest(request);
				
				//Check if the response is an error
				if (response.getPayloadType() == ODataPayload.Type.Error) {
					ODataErrorDefaultImpl error =  (ODataErrorDefaultImpl) 
							response.getPayload();
					throw new OfflineODataStoreException(error.getMessage());

				//Check if the response contains EntitySet
				} else if (response.getPayloadType() == ODataPayload.Type.EntitySet) {
					ODataEntitySet feed = (ODataEntitySet) response.getPayload();
					List<ODataEntity> entities = feed.getEntities();
					//Retrieve the data from the response
					for (ODataEntity entity: entities){
						properties = entity.getProperties();
						property = properties.get(Collections.TRAVEL_AGENCY_ENTRY_ID);
						agency = new Agency((String)property.getValue());
						property = properties.get(Collections.TRAVEL_AGENCY_ENTRY_NAME);
						agency.setAgencyName((String)property.getValue());
						property = properties.get(Collections.TRAVEL_AGENCY_ENTRY_STREET);
						agency.setStreet((String)property.getValue());
						property = properties.get(Collections.TRAVEL_AGENCY_ENTRY_CITY);
						agency.setCity((String)property.getValue());
						property = properties.get(Collections.TRAVEL_AGENCY_ENTRY_COUNTRY);
						agency.setCountry((String)property.getValue());
						property = properties.get(Collections.TRAVEL_AGENCY_ENTRY_URL);
						agency.setWebsite((String)property.getValue());
						
						agency.setEditResourceURL(entity.getEditResourcePath());
						
						agencyList.add(agency);
					}

				} else  {
					OfflineODataStoreException e = new OfflineODataStoreException("Invalid payload: EntitySet expected "
							+ "but got " + response.getPayloadType().name());
					AgencyLogManager.writeLogError("openOfflineStore", e);
					throw e;
				}
			} catch (Exception e) {
				AgencyLogManager.writeLogError("openOfflineStore", e);
				throw new OfflineODataStoreException(e);
			}
		}
		return agencyList;
		//END
	}

	/**
	 * Create a new entity in the local database
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void createAgency(Agency agency, UIListener uiListener) 
			throws OfflineODataStoreException{
		//BEGIN
		//Check if the offline oData store has been initialized
		if (offlineStore==null) return;
		try {
			//Creates the entity payload
			ODataEntity newEntity = createAgencyEntity(agency);
			//Send the request to create the new agency in the local database
			offlineStore.scheduleCreateEntity(newEntity, Collections.TRAVEL_AGENCY_COLLECTION, 
					new AgencyRequestListener(Operation.CreateAgency.getValue(), uiListener), null);
		} catch (Exception e) {
			AgencyLogManager.writeLogError("createAgency", e);
			throw new OfflineODataStoreException(e);
		}
		//END
	}

	/**
	 * update an existing agency entity in the local database
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void updateAgency(Agency agency, UIListener uiListener) 
			throws OfflineODataStoreException{
		//BEGIN
		//Check if the offline oData store has been initialized
		if (offlineStore==null) return;
		try {
			//Creates the entity payload
			ODataEntity newEntity = createAgencyEntity(agency);
			//Send the request to create the new agency in the local database
			offlineStore.scheduleUpdateEntity(newEntity, 
					new AgencyRequestListener(Operation.UpdateAgency.getValue(), uiListener), 
					null);
		} catch (Exception e) {
			AgencyLogManager.writeLogError("updateAgency", e);
			throw new OfflineODataStoreException(e);
		}
		//END
	}
	
	/**
	 * Delete the agency entity in the local database
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void deleteAgency(Agency agency, UIListener uiListener) throws OfflineODataStoreException{
		//BEGIN
		//Check if the offline oData store has been initialized
		if (offlineStore==null) return;
		
		try {
			//get resource path required to send DELETE requests
			String resourcePath = agency.getEditResourceURL();
			if (!TextUtils.isEmpty(resourcePath)) {
				//AgencyRequestListener implements ODataRequestListener, 
				// which receives the response from the server and notify the activity that shows
				//the message to the user
				AgencyRequestListener agencyListener = new AgencyRequestListener(
						Operation.DeleteAgency.getValue(), uiListener);
				//Scheduling method for deleting an Entity asynchronously
				offlineStore.scheduleDeleteEntity(resourcePath, null, agencyListener, null);
			} else {
				OfflineODataStoreException e = new OfflineODataStoreException("Resource path is null");
				AgencyLogManager.writeLogError("updateAgency", e);
				throw e;
			}
		} catch (Exception e) {
			throw new OfflineODataStoreException(e);
		}
		//END
	}

	/**
	 * Asynchronously starts sending pending modification requests to the server
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void flushQueuedRequests(UIListener uiListener) throws OfflineODataStoreException{
		//BEGIN
		//Check if the offline oData store has been initialized
		if (offlineStore==null) return;
		try {
			//AgencyFlushListener implements ODataOfflineStoreFlushListener
			//used to get progress updates of a flush operation
			AgencyFlushListener flushListener = new AgencyFlushListener(uiListener);
			//Asynchronously starts sending pending modification request to the server
			offlineStore.scheduleFlushQueuedRequests(flushListener);
		} catch (ODataException e) {
			AgencyLogManager.writeLogError("flushQueuedRequests", e);
			throw new OfflineODataStoreException(e);
		}
		//END
	}

	/**
	 * Asynchronously refreshes the store with the OData service and calls the delegate 
	 * when the refresh starts, finishes, or fails
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OfflineODataStoreException
	 */
	public static void refresh(UIListener uiListener) throws OfflineODataStoreException{
		//BEGIN
		//Check if the offline oData store has been initialized
		if (offlineStore==null) return;
		try {
			//AgencyRefreshListener implements ODataOfflineStoreRefreshListener
			//used to get progress updates of a refresh operation
			AgencyRefreshListener refreshListener = new AgencyRefreshListener(uiListener);
			//Asynchronously refreshes the store with the OData service
			offlineStore.scheduleRefresh(refreshListener);
		} catch (ODataException e) {
			AgencyLogManager.writeLogError("refresh", e);
			throw new OfflineODataStoreException(e);
		}
		//END
	}
	
	public static List<OfflineError> getErrorArchive() throws OfflineODataStoreException{
		ArrayList<OfflineError> errorList = new ArrayList<OfflineError>();

		if (offlineStore!=null){
			
			OfflineError offlineError;
			ODataProperty property;
			ODataPropMap properties;
			try {

				String resourcePath = Collections.ERROR_ARCHIVE_COLLECTION;

				ODataRequestParamSingle request = new ODataRequestParamSingleDefaultImpl();
				request.setMode(Mode.Read);
				request.setResourcePath(resourcePath);

				ODataResponseSingle response = (ODataResponseSingle) offlineStore.executeRequest(request);
				
				if (response.getPayloadType() == ODataPayload.Type.Error) {
					ODataErrorDefaultImpl error =  (ODataErrorDefaultImpl) response.getPayload();
					throw new OfflineODataStoreException(error.getMessage());

				} else if (response.getPayloadType() == ODataPayload.Type.EntitySet) {
					ODataEntitySet feed = (ODataEntitySet) response.getPayload();
					List<ODataEntity> entities = feed.getEntities();
					for (ODataEntity entity: entities){
						
						properties = entity.getProperties();
						property = properties.get(Collections.ERROR_ARCHIVE_ENTRY_MESSAGE);
						offlineError = new OfflineError((String)property.getValue());
						property = properties.get(Collections.ERROR_ARCHIVE_ENTRY_REQUEST_METHOD);
						offlineError.setRequestMethod((String)property.getValue());
						property = properties.get(Collections.ERROR_ARCHIVE_ENTRY_REQUEST_BODY);
						offlineError.setRequestBody((String)property.getValue());
						property = properties.get(Collections.ERROR_ARCHIVE_ENTRY_HTTP_CODE);
						if (property !=null) {
							Integer httpStatusCode = (Integer)property.getValue();
							offlineError.setHttpStatusCode(String.valueOf(httpStatusCode));
						}
						property = properties.get(Collections.ERROR_ARCHIVE_ENTRY_CUSTOM_TAG);
						offlineError.setCustomTag((String)property.getValue());
						property = properties.get(Collections.ERROR_ARCHIVE_ENTRY_REQUEST_URL);
						offlineError.setRequestURL((String)property.getValue());
						
						offlineError.setEditResourcePath(entity.getEditResourcePath());

						errorList.add(offlineError);
					}

				} else  {
					throw new OfflineODataStoreException("Invalid payload: EntitySet expected but got " + response.getPayloadType().name());
				}
			} catch (Exception e) {
				throw new OfflineODataStoreException(e);
			}
		}
		return errorList;
	}

	public static void deleteErrorArchive(OfflineError error, UIListener uiListener) throws OfflineODataStoreException{
		if (offlineStore==null) return;
		
		try {
			String resourcePath = error.getEditResourcePath();
			if (!TextUtils.isEmpty(resourcePath)) {
				offlineStore.scheduleDeleteEntity(resourcePath, null, 
						new AgencyRequestListener(Operation.DeleteErrorArchive.getValue(), 
								uiListener), null);
				TraceLog.d("deleteErrorArchive::"+resourcePath);
			} else {
				throw new OfflineODataStoreException("Resource path is null");
			}
		} catch (Exception e) {
			throw new OfflineODataStoreException(e);
		}
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
			
			String resourcePath = agency.getEditResourceURL();
			if (!TextUtils.isEmpty(resourcePath)){
				newEntity.setResourcePath(resourcePath, resourcePath);
			}
		}
		return newEntity;
		
	}	
}
