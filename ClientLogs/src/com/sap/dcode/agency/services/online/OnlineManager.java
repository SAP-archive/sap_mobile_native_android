package com.sap.dcode.agency.services.online;

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
import com.sap.maf.tools.logon.core.LogonCore;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.maf.tools.logon.logonui.api.LogonUIFacade;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.IManagerConfigurator;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataEntitySet;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.impl.ODataEntityDefaultImpl;
import com.sap.smp.client.odata.impl.ODataPropertyDefaultImpl;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.store.ODataResponseSingle;
import com.sap.smp.client.odata.store.ODataStore.PropMode;

public class OnlineManager {
	public static final String TAG = OnlineManager.class.getSimpleName();

	/**
	 * Initialize an online OData store for online access
	 * @param context used only to access the application context
	 * @return true if the online is open and false otherwise
	 * @throws OnlineODataStoreException
	 */
	public static boolean openOnlineStore(Context context) throws OnlineODataStoreException{
		//AgencyOpenListener implements OpenListener interface
		//Listener to be invoked when the opening process of an OnlineODataStore object finishes
		AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		if (openListener.getStore()==null){
			LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
	
			//The logon configurator uses the information obtained in the registration 
            // (i.e endpoint URL, login, etc ) to configure the conversation manager
			IManagerConfigurator configurator = LogonUIFacade.getInstance().getLogonConfigurator(context);
			HttpConversationManager manager = new HttpConversationManager(context);
			configurator.configure(manager);
	
			//XCSRFTokenRequestFilter implements IRequestFilter
			//Request filter that is allowed to preprocess the request before sending
			XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
			XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(context, 
					requestFilter);
			manager.addFilter(requestFilter);
			manager.addFilter(responseFilter);
	
			try {
				String endPointURL = lgCtx.getAppEndPointUrl();
				URL url = new URL(endPointURL);
				//Method to open a new online store asynchronously
				OnlineODataStore.open(context, url, manager, openListener, null);
				openListener.waitForCompletion();
				if (openListener.getError() != null) {
					AgencyLogManager.writeLogError("openOnlineStore", openListener.getError());
					throw openListener.getError();
				}
			} catch(Exception e){
				throw new OnlineODataStoreException(e);
			}
			
			//Check if OnlineODataStore opened successfully
			OnlineODataStore store = openListener.getStore();
			if (store != null) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * Get the list of agencies
	 * @return list of Agency object
	 * @throws OnlineODataStoreException
	 */
	public static List<Agency> getAgencies() throws OnlineODataStoreException{
		//BEGIN
		ArrayList<Agency> agencyList = new ArrayList<Agency>();
		//Get the open online store
		AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();

		if (store!=null){
			
			Agency agency;
			ODataProperty property;
			ODataPropMap properties;
			try {
				//Executor method for reading an Entity set synchronously
				ODataResponseSingle resp = store.executeReadEntitySet(
						Collections.TRAVEL_AGENCY_COLLECTION + 
						"?$orderby="+Collections.TRAVEL_AGENCY_ENTRY_ID+"%20desc", null);
				//Get the response payload
				ODataEntitySet feed = (ODataEntitySet) resp.getPayload();
				//Get the list of ODataEntity
				List<ODataEntity> entities = feed.getEntities();
				//Loop to retrieve the information from the response
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
					
					//Obtain the edit resource path from the ODataEntity
					agency.setEditResourceURL(entity.getEditResourcePath());
					
					agencyList.add(agency);
				}
				
			} catch (Exception e) {
				AgencyLogManager.writeLogError("getAgencies", e);
				throw new OnlineODataStoreException(e);
			}
		}
		return agencyList;
		//END
	}
	
	/**
	 * Send a POST request to create an agency
	 * @param agency agency information
	 * @return the agency ID
	 * @throws OnlineODataStoreException
	 */
	public static String createAgency(Agency agency) throws OnlineODataStoreException{
		AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();

		if (store==null) return null;
			
		try {

			ODataEntity newEntity = createAgencyEntity(store, agency);

			ODataResponseSingle resp = store.executeCreateEntity(newEntity,
					Collections.TRAVEL_AGENCY_COLLECTION, null);
			ODataEntity createdEntity = (ODataEntity) resp.getPayload();
			ODataPropMap properties = createdEntity.getProperties();
			ODataProperty property = properties
					.get(Collections.TRAVEL_AGENCY_ENTRY_ID);
			return (String) property.getValue();
		} catch (Exception e) {
			AgencyLogManager.writeLogError("createAgency::"+agency.getAgencyId(), e);
			throw new OnlineODataStoreException(e);
		}
	
	}

	/**
	 * Send a POST request to create a travel agency asynchronously
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OnlineODataStoreException
	 */
	public static void createAgency(Agency agency, UIListener uiListener) throws OnlineODataStoreException{
		//BEGIN
		//Get the open online store
		AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();

		if (store==null) return;
		try {
			//The travel agency entity to be created
			ODataEntity newEntity = createAgencyEntity(store, agency);
			//AgencyRequestListener implements ODataRequestListener, 
			// which receives the response from the server and notify the activity that shows
			//the message to the user
			AgencyRequestListener agencyListener = new AgencyRequestListener(
					Operation.CreateAgency.getValue(), uiListener);
			//Scheduling method for creating an Entity asynchronously
			store.scheduleCreateEntity(newEntity, Collections.TRAVEL_AGENCY_COLLECTION, 
					agencyListener, null);
		} catch (Exception e) {
			AgencyLogManager.writeLogError("createAgency::"+agency.getAgencyId(), e);
			throw new OnlineODataStoreException(e);
		}
		//END
	}

	/**
	 * Send a PUT request to update a travel agency asynchronously
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OnlineODataStoreException
	 */
	public static void updateAgency(Agency agency, UIListener uiListener) throws OnlineODataStoreException{
		//BEGIN
		//Get the open online store
		AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();

		if (store==null) return;
		try {
			//The travel agency entity to be updated
			ODataEntity newEntity = createAgencyEntity(store, agency);
			//AgencyRequestListener implements ODataRequestListener, 
			// which receives the response from the server and notify the activity that shows
			//the message to the user
			AgencyRequestListener agencyListener = new AgencyRequestListener(
					Operation.UpdateAgency.getValue(), uiListener);
			//Scheduling method for updating an Entity asynchronously
			store.scheduleUpdateEntity(newEntity, agencyListener, null);
		} catch (Exception e) {
			AgencyLogManager.writeLogError("updateAgency::"+agency.getAgencyId(), e);
			throw new OnlineODataStoreException(e);
		}
		//END
	
	}

	public static void deleteAgency(Agency agency) throws OnlineODataStoreException{
		AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();

		if (store==null) return;
		
		try {
			String resourcePath = agency.getEditResourceURL();
			store.executeDeleteEntity(resourcePath, null, null);
		} catch (Exception e) {
			throw new OnlineODataStoreException(e);
		}
	}

	/**
	 * Send a DELETE request to delete a travel agency asynchronously
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OnlineODataStoreException
	 */
	public static void deleteAgency(Agency agency, UIListener uiListener) throws OnlineODataStoreException{
		//BEGIN
		//Get the open online store
		AgencyOpenListener openListener = AgencyOpenListener.getInstance();
		OnlineODataStore store = openListener.getStore();

		if (store==null) return;
		
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
				store.scheduleDeleteEntity(resourcePath, null, agencyListener, null);
			} else {
				throw new OnlineODataStoreException("Resource path is null");
			}

		} catch (Exception e) {
			AgencyLogManager.writeLogError("deleteAgency::"+agency.getAgencyId(), e);
			throw new OnlineODataStoreException(e);
		}
		//END
	}

	/**
	 * Create Travel agency entity type with the corresponding value
	 * @param store online store
	 * @param agency agency information
	 * @return ODataEntity with the agency information
	 * @throws ODataParserException
	 */
	private static ODataEntity createAgencyEntity(OnlineODataStore store, Agency agency) throws ODataParserException{
		//BEGIN
		ODataEntity newEntity = null;
		if (store!=null && agency!=null && agency.isInitialized()) {	
			//Use default implementation to create a new travel agency entity type
			newEntity = new ODataEntityDefaultImpl(Collections.TRAVEL_AGENCY_ENTITY_TYPE);
			//If available, it will populates those properties of an OData Entity 
			//which are defined by the allocation mode 
			store.allocateProperties(newEntity, PropMode.All);
			//If available, it populates the navigation properties of an OData Entity
			store.allocateNavigationProperties(newEntity);
			
			String agencyId = agency.getAgencyId();
			//Set the corresponding properties
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_ID,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_ID, 
							agencyId));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_NAME,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_NAME, 
							agency.getAgencyName()));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_STREET,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_STREET, 
							agency.getStreet()));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_CITY,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_CITY, 
							agency.getCity()));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_COUNTRY,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_COUNTRY, 
							agency.getCountry()));
			newEntity.getProperties().put(Collections.TRAVEL_AGENCY_ENTRY_URL,
					new ODataPropertyDefaultImpl(Collections.TRAVEL_AGENCY_ENTRY_URL, 
							agency.getWebsite()));
			
			//assigned resource path required for DELETE and PUT requests
			String resourcePath = agency.getEditResourceURL();
			if (!TextUtils.isEmpty(resourcePath)){
				newEntity.setResourcePath(resourcePath, resourcePath);
			}
		}
		return newEntity;
		//END
		
	}
	
	
}
