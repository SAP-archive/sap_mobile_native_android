package com.sap.dcode.agency.ui.offline;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sap.dcode.agency.R;

public class OfflineErrorListActivity extends Activity {
    private static final String TAG_ERROR_LIST = "error_list";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize AgencyListFragment
		Fragment fragment = Fragment.instantiate(this, OfflineErrorListFragment.class.getName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_ERROR_LIST);
        fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_delete, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==  R.id.menu_item_delete) {
            final OfflineErrorListFragment fragment = (OfflineErrorListFragment) getFragmentManager().findFragmentByTag(TAG_ERROR_LIST);
            fragment.onDeleteRequested();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	
}
