package com.sap.dcode.agency.ui.offline;

import com.sap.dcode.agency.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class OfflineAgencyActivity extends Activity {
    private static final String TAG_DETAILS = "agency_details";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize AgencyFragment
		Fragment fragment = Fragment.instantiate(this, OfflineAgencyFragment.class.getName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_DETAILS);
        fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_save, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_save) {
            final OfflineAgencyFragment fragment = (OfflineAgencyFragment) getFragmentManager().findFragmentByTag(TAG_DETAILS);
            fragment.onSaveRequested();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}


}
