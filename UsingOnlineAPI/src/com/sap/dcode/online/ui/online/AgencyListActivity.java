package com.sap.dcode.online.ui.online;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sap.dcode.online.R;

public class AgencyListActivity extends Activity {
    private static final String TAG_LIST = "agency_list";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize AgencyListFragment
		Fragment fragment = Fragment.instantiate(this, AgencyListFragment.class.getName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_LIST);
        fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_save_delete, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_save) {
            final AgencyListFragment fragment = (AgencyListFragment) getFragmentManager().findFragmentByTag(TAG_LIST);
            fragment.onSaveRequested();
            return true;
        } else if (item.getItemId() == R.id.menu_item_delete) {
            final AgencyListFragment fragment = (AgencyListFragment) getFragmentManager().findFragmentByTag(TAG_LIST);
            fragment.onDeleteRequested();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	
}
