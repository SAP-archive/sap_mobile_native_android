package com.sap.dcode.agency.ui.logs;

import com.sap.dcode.agency.R;
import com.sap.dcode.agency.ui.main.MainFragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LogActivity extends Activity {
    private static final String TAG_LOG = "agency_logs";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize MainFragment
		Fragment fragment = Fragment.instantiate(this, LogFragment.class.getName());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment, TAG_LOG);
        fragmentTransaction.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_upload, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_upload) {
            final LogFragment fragment = (LogFragment) getFragmentManager().findFragmentByTag(TAG_LOG);
            fragment.onUploadRequested();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}


	

}
