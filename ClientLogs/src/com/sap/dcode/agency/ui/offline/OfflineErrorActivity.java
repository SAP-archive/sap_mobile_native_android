package com.sap.dcode.agency.ui.offline;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class OfflineErrorActivity extends Activity {
    private static final String TAG_ERROR_DETAILS = "error_details";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize AgencyFragment
		Fragment fragment = Fragment.instantiate(this, OfflineErrorFragment.class.getName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_ERROR_DETAILS);
        fragmentTransaction.commit();
	}


}
