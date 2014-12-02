package com.sap.dcode.online.ui.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sap.dcode.online.R;
import com.sap.dcode.online.ui.online.AgencyListActivity;
import com.sap.dcode.util.TraceLog;
import com.sap.maf.tools.logon.core.LogonCore;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.maf.tools.logon.core.LogonCoreException;

public class MainFragment extends Fragment implements OnClickListener {
	private final String TAG = MainFragment.class.getSimpleName();
    private View myView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (myView == null) {
			myView = inflater.inflate(R.layout.main_fragment, null);
			try {
				this.initializeViews();
			} catch(LogonCoreException e){
				TraceLog.e(TAG + "::onCreateView", e);
			}
		}
		return myView;
	}
	
	@Override
	public void onClick(View v) {
		int btnId = v.getId();
		Intent intent = new Intent();

		switch (btnId){
		
		case R.id.online_button:
	        intent.setClass(getActivity(), AgencyListActivity.class);
	        startActivity(intent);
			break;
		
		case R.id.offline_button:
			break;
		}
		
	}


	//******* Helper Methods ********
	
	/**
	 * Initialize the UI elements in the screen
	 * @throws LogonCoreException 
	 */
	private void initializeViews() throws LogonCoreException{
		LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
		String mUsername = (lgCtx.getBackendUser()!=null)?lgCtx.getBackendUser():"";

		String title = String.format(getString(R.string.title_welcome), mUsername); 
		TextView titleEdit = (TextView) myView.findViewById(R.id.welcome_title);
		titleEdit.setText(title);
				
		Button onlineBtn = (Button) myView.findViewById(R.id.online_button);
		onlineBtn.setOnClickListener(this);
		
		Button offlineBtn = (Button) myView.findViewById(R.id.offline_button);
		offlineBtn.setOnClickListener(this);
		
	}



}
