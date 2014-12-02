package com.sap.dcode.agency.ui.online;

import java.util.List;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sap.dcode.agency.R;
import com.sap.dcode.agency.services.UIListener;
import com.sap.dcode.agency.services.online.OnlineManager;
import com.sap.dcode.agency.services.online.OnlineODataStoreException;
import com.sap.dcode.agency.types.Agency;
import com.sap.dcode.agency.types.AsyncResult;
import com.sap.dcode.agency.ui.components.ConfirmationDialog;
import com.sap.dcode.agency.ui.components.DialogListener;
import com.sap.dcode.util.TraceLog;

public class AgencyListFragment extends Fragment implements LoaderManager.LoaderCallbacks<AsyncResult<List<Agency>>>, UIListener{
	private View myView;
    private TextView childViewAgenciesTitle;
    private ListView childViewAgenciesList;
    private LayoutInflater inflater;
    private Loader<AsyncResult<List<Agency>>> loader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		if (myView == null) {
			myView = this.inflater.inflate(R.layout.agency_list_fragment, null);
			childViewAgenciesTitle = (TextView) myView.findViewById(R.id.agencies_title);
			childViewAgenciesList = (ListView) myView.findViewById(R.id.agencies_list);

			loader = getLoaderManager().initLoader(0, savedInstanceState, this);
			
		}
		return myView;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	public void onSaveRequested(){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AgencyActivity.class);
        startActivity(intent);
	}

	public void onDeleteRequested(){
        TraceLog.scoped(this).d("onDeleteRequested");

        final int selectedPos = childViewAgenciesList.getCheckedItemPosition();

        if (selectedPos != -1) {

            final AgencyListAdapter adapter = (AgencyListAdapter) childViewAgenciesList.getAdapter();
            if (adapter != null) { // if not null, then loader has completed

                final Agency agency = (Agency) childViewAgenciesList.getItemAtPosition(selectedPos);

                final ConfirmationDialog cd = new ConfirmationDialog(getActivity(), getString(R.string.title_delete_agency), getString(R.string.msg_delete_agency));
                cd.setListener(new DialogListener() {
                    @Override
                    public void onUserAction(int action, Object... args) {
                        if (action == ConfirmationDialog.YES) {
                            adapter.deleteItem(selectedPos);
                    		try {
                    			OnlineManager.deleteAgency(agency, AgencyListFragment.this);
                    		} catch (OnlineODataStoreException e) {
                    			TraceLog.e("AgencyFragment::onSaveRequest", e);
                    		}
                        }
                    }
                });
                cd.show();
            }
        }

	}

	public void onAgencySelected(Agency agency){
        Intent intent = new Intent();
        intent.setClass(getActivity(), AgencyActivity.class);
        intent.putExtra("AgencySelected", agency);
        startActivity(intent);
	}
	
	@Override
	public Loader<AsyncResult<List<Agency>>> onCreateLoader(int id, Bundle args) {
		return new AgencyListDataAsyncLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<AsyncResult<List<Agency>>> listLoader,
			AsyncResult<List<Agency>> result) {
		if (result.getException() != null || result.getData() == null) {
			Toast.makeText(getActivity(), R.string.err_odata_unexpected, Toast.LENGTH_SHORT).show();
			TraceLog.e("Error loading agencies", result.getException());

		} else {
			final List<Agency> agencies = result.getData();
			childViewAgenciesList.setAdapter(new AgencyListAdapter(this,
					childViewAgenciesList, inflater, agencies));
			childViewAgenciesTitle.setText(getString(R.string.title_agency_total, agencies.size()));
		}
	}

	@Override
	public void onLoaderReset(Loader<AsyncResult<List<Agency>>> listLoader) {
	}
	
	 /**
     * Refresh the data displayed in the view.
     */
    public void refresh() {
        loader.forceLoad();
    }
	
	@Override
	public void onRequestError(int operation, Exception e) {
		Toast.makeText(getActivity(), getString(R.string.err_odata_unexpected, e.getMessage()),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRequestSuccess(int operation, String key) {
		refresh();
		String message;
		if (TextUtils.isEmpty(key))
			message = getString(R.string.msg_success_delete_agency);
		else
			message = getString(R.string.msg_success_delete_agency_param, key);
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}

}
