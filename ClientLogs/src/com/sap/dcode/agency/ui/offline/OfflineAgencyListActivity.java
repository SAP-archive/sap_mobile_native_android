package com.sap.dcode.agency.ui.offline;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sap.dcode.agency.R;

public class OfflineAgencyListActivity extends Activity {
    private static final String TAG_LIST = "agency_list";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize AgencyListFragment
		Fragment fragment = Fragment.instantiate(this, OfflineAgencyListFragment.class.getName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_LIST);
        fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_save_delete_refresh, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_save) {
            final OfflineAgencyListFragment fragment = (OfflineAgencyListFragment) getFragmentManager().findFragmentByTag(TAG_LIST);
            fragment.onSaveRequested();
            return true;
        } else if (item.getItemId() == R.id.menu_item_delete) {
            final OfflineAgencyListFragment fragment = (OfflineAgencyListFragment) getFragmentManager().findFragmentByTag(TAG_LIST);
            fragment.onDeleteRequested();
            return true;
        } else if (item.getItemId() == R.id.menu_item_refresh) {
            final OfflineAgencyListFragment fragment = (OfflineAgencyListFragment) getFragmentManager().findFragmentByTag(TAG_LIST);
            fragment.onRefreshRequested();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	
}
