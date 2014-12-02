package com.sap.dcode.online.services.online;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.sap.dcode.online.services.AgencyRequestListener;
import com.sap.dcode.online.services.Collections;
import com.sap.dcode.online.services.Operation;
import com.sap.dcode.online.services.UIListener;
import com.sap.dcode.online.types.Agency;
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
		//TODO 2-1 COMPLETE openOnlineStore in OnlineManager ***************************************************
		return false;
	}

	/**
	 * Get the list of agencies
	 * @return list of Agency object
	 * @throws OnlineODataStoreException
	 */
	public static List<Agency> getAgencies() throws OnlineODataStoreException{
		//TODO 2-2 COMPLETE getAgencies in OnlineManager ***************************************************
		//BEGIN
		ArrayList<Agency> agencyList = new ArrayList<Agency>();
		return agencyList;
		//END
	}
	
	/**
	 * Send a POST request to create a travel agency asynchronously
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OnlineODataStoreException
	 */
	public static void createAgency(Agency agency, UIListener uiListener) throws OnlineODataStoreException{
		//TODO 2-4 COMPLETE createAgency in OnlineManager ***************************************************

	}

	/**
	 * Send a PUT request to update a travel agency asynchronously
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OnlineODataStoreException
	 */
	public static void updateAgency(Agency agency, UIListener uiListener) throws OnlineODataStoreException{
		//TODO 2-5 COMPLETE updateAgency in OnlineManager ***************************************************
	
	}

	/**
	 * Send a DELETE request to delete a travel agency asynchronously
	 * @param agency agency information
	 * @param uiListener the activity that will receive the response to notify the user
	 * @throws OnlineODataStoreException
	 */
	public static void deleteAgency(Agency agency, UIListener uiListener) throws OnlineODataStoreException{
		//TODO 2-6 COMPLETE deleteAgency in OnlineManager ***************************************************

	}

	/**
	 * Create Travel agency entity type with the corresponding value
	 * @param store online store
	 * @param agency agency information
	 * @return ODataEntity with the agency information
	 * @throws ODataParserException
	 */
	private static ODataEntity createAgencyEntity(OnlineODataStore store, Agency agency) throws ODataParserException{
		//TODO 2-3 COMPLETE createAgencyEntity in OnlineManager ***************************************************
		//BEGIN
		ODataEntity newEntity = null;
		return newEntity;
		//END
		
	}
	
	
}
