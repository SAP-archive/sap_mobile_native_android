package com.sap.dcode.agency.ui.offline;

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
import com.sap.dcode.agency.services.Operation;
import com.sap.dcode.agency.services.UIListener;
import com.sap.dcode.agency.services.offline.OfflineManager;
import com.sap.dcode.agency.services.offline.OfflineODataStoreException;
import com.sap.dcode.agency.types.AsyncResult;
import com.sap.dcode.agency.types.OfflineError;
import com.sap.dcode.agency.ui.components.ConfirmationDialog;
import com.sap.dcode.agency.ui.components.DialogListener;
import com.sap.dcode.util.TraceLog;

public class OfflineErrorListFragment extends Fragment implements LoaderManager.LoaderCallbacks<AsyncResult<List<OfflineError>>>, UIListener{
	private View myView;
    private TextView childViewErrorTitle;
    private ListView childViewErrorList;
    private LayoutInflater inflater;
    private Loader<AsyncResult<List<OfflineError>>> loader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		if (myView == null) {
			myView = this.inflater.inflate(R.layout.error_list_fragment, null);
			childViewErrorTitle = (TextView) myView.findViewById(R.id.errors_title);
			childViewErrorList = (ListView) myView.findViewById(R.id.errors_list);

			loader = getLoaderManager().initLoader(0, savedInstanceState, this);
			
		}
		return myView;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	public void onDeleteRequested(){
        TraceLog.scoped(this).d("onDeleteRequested");

        final int selectedPos = childViewErrorList.getCheckedItemPosition();

        if (selectedPos != -1) {

            final OfflineErrorListAdapter adapter = (OfflineErrorListAdapter) childViewErrorList.getAdapter();
            if (adapter != null) { // if not null, then loader has completed

                final OfflineError error = (OfflineError) childViewErrorList.getItemAtPosition(selectedPos);

                final ConfirmationDialog cd = new ConfirmationDialog(getActivity(), getString(R.string.title_delete_error), getString(R.string.msg_delete_error));
                cd.setListener(new DialogListener() {
                    @Override
                    public void onUserAction(int action, Object... args) {
                        if (action == ConfirmationDialog.YES) {
                            adapter.deleteItem(selectedPos);
                    		try {
                    			OfflineManager.deleteErrorArchive(error, OfflineErrorListFragment.this);
                    		} catch (OfflineODataStoreException e) {
                    			TraceLog.e("AgencyFragment::onSaveRequest", e);
                    		}
                        }
                    }
                });
                cd.show();
            }
        }

	}
	

	public void onAgencySelected(OfflineError error){
        
 		Intent intent = new Intent();
        intent.setClass(getActivity(), OfflineErrorActivity.class);
        intent.putExtra("ErrorSelected", error);
        startActivity(intent);
        
	}
	
	@Override
	public Loader<AsyncResult<List<OfflineError>>> onCreateLoader(int id, Bundle args) {
		return new OfflineErrorListDataAsyncLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<AsyncResult<List<OfflineError>>> listLoader,
			AsyncResult<List<OfflineError>> result) {
		if (result.getException() != null || result.getData() == null) {
			Toast.makeText(getActivity(), R.string.err_odata_unexpected, Toast.LENGTH_SHORT).show();
			TraceLog.e("Error loading agencies", result.getException());

		} else {
			final List<OfflineError> errors = result.getData();
			childViewErrorList.setAdapter(new OfflineErrorListAdapter(this,
					childViewErrorList, inflater, errors));
			childViewErrorTitle.setText(getString(R.string.title_error_total, errors.size()));
		}
	}

	@Override
	public void onLoaderReset(Loader<AsyncResult<List<OfflineError>>> listLoader) {
	}
	
	 /**
     * Refresh the data displayed in the view.
     */
    public void refresh() {
        loader.forceLoad();
    }
	
	@Override
	public void onRequestError(int operation, Exception e) {
		Toast.makeText(getActivity(), getString(R.string.err_odata_unexpected_param, e.getMessage()),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRequestSuccess(int operation, String key) {
		String message = getString(R.string.err_odata_unexpected);
		if (operation == Operation.DeleteErrorArchive.getValue()){
			refresh();
			message = getString(R.string.msg_success_delete_error);
		} 
		Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
	}

}
