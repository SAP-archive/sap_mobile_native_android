package com.sap.dcode.agency.ui.offline;

import java.util.Random;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sap.dcode.agency.R;
import com.sap.dcode.agency.services.Operation;
import com.sap.dcode.agency.services.UIListener;
import com.sap.dcode.agency.services.offline.OfflineManager;
import com.sap.dcode.agency.services.offline.OfflineODataStoreException;
import com.sap.dcode.agency.types.Agency;
import com.sap.dcode.util.TraceLog;

public class OfflineAgencyFragment extends Fragment implements UIListener{
    private View myView;
    private EditText childViewAgencyID, childViewAgencyName, childViewURL;
    private EditText childViewStreet, childViewCity, childViewCountry;
    private LayoutInflater inflater;
    private Agency agency;
    
    private boolean isNew = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		if (myView == null) {
			myView = this.inflater.inflate(R.layout.agency_fragment, null);
			childViewAgencyID = (EditText) myView.findViewById(R.id.agency_id);
			childViewAgencyName = (EditText) myView.findViewById(R.id.agency_name);
			childViewURL = (EditText) myView.findViewById(R.id.agency_url);
			childViewStreet = (EditText) myView.findViewById(R.id.street);
			childViewCity = (EditText) myView.findViewById(R.id.city);
			childViewCountry = (EditText) myView.findViewById(R.id.country);
		}
		
		//When user select an agency, the agency will be passed as an intent
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			agency = bundle.getParcelable("AgencySelected");
			initializeViews();
		} else {
			initializeAgencyId();
		}
		return myView;
	}

	private void initializeAgencyId(){
		Random rand = new Random();;
		int min = 10000000, max = 99999999;
	
		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt(max - min + 1) + min;
		childViewAgencyID.setText(String.valueOf(randomNum));

	}
	
	private void initializeViews(){
		if (agency!=null) {
			childViewAgencyID.setText(agency.getAgencyId());
			childViewAgencyName.setText(agency.getAgencyName());
			childViewURL.setText(agency.getWebsite());
			childViewStreet.setText(agency.getStreet());
			childViewCity.setText(agency.getCity());
			childViewCountry.setText(agency.getCountry());
			isNew = false;
		}
	}

	
	public void onSaveRequested() {
		if (isNew)
			agency = new Agency(childViewAgencyID.getText().toString());
		
		agency.setAgencyName(childViewAgencyName.getText().toString());
		agency.setStreet(childViewStreet.getText().toString());
		agency.setCity(childViewCity.getText().toString());
		agency.setCountry(childViewCountry.getText().toString());
		agency.setWebsite(childViewURL.getText().toString());
		try {
			if (isNew) 
				OfflineManager.createAgency(agency, this);
			else
				OfflineManager.updateAgency(agency, this);
		} catch (OfflineODataStoreException e) {
			TraceLog.e("AgencyFragment::onSaveRequest", e);
		}

	}


	@Override
	public void onRequestError(int operation, Exception e) {
		Toast.makeText(getActivity(), getString(R.string.err_odata_unexpected, e.getMessage()),
				Toast.LENGTH_LONG).show();
		getActivity().finish();
	}

	@Override
	public void onRequestSuccess(int operation, String key) {
		String message= "";
		if (operation == Operation.CreateAgency.getValue()){
			message = getString(R.string.msg_success_create_agency_param, key);
		} else if (operation == Operation.UpdateAgency.getValue()){
			if (TextUtils.isEmpty(key))
				message = getString(R.string.msg_success_update_agency);
			else
				message = getString(R.string.msg_success_update_agency_param, key);
		}
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		getActivity().finish();
	}
	
}
