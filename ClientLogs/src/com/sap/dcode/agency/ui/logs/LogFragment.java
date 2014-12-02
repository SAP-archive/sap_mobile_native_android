package com.sap.dcode.agency.ui.logs;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sap.dcode.agency.R;
import com.sap.dcode.agency.services.UIListener;
import com.sap.dcode.agency.services.logs.AgencyLogException;
import com.sap.dcode.agency.services.logs.AgencyLogManager;
import com.sap.dcode.agency.types.AsyncResult;
import com.sap.dcode.util.TraceLog;

public class LogFragment extends Fragment implements LoaderManager.LoaderCallbacks<AsyncResult<String>>, UIListener{
    private View myView;
    private TextView childViewLogs;
    private Loader<AsyncResult<String>> loader;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (myView == null) {
			myView = inflater.inflate(R.layout.log_fragment, null);
			
			childViewLogs = (TextView) myView.findViewById(R.id.log_text);

			loader = getLoaderManager().initLoader(0, savedInstanceState, this);

		}
		return myView;
	}

	public void onUploadRequested(){
		
		try {
			AgencyLogManager.UploadFileLogs(this);
		} catch (AgencyLogException e) {
			Toast.makeText(getActivity(), getString(R.string.err_odata_unexpected, e.getMessage()),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}


	@Override
	public Loader<AsyncResult<String>> onCreateLoader(int id, Bundle args) {
		return new LogDataAsyncLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<AsyncResult<String>> listLoader, AsyncResult<String> result) {
		if (result.getException() != null || result.getData() == null) {
			Toast.makeText(getActivity(), R.string.err_odata_unexpected, Toast.LENGTH_SHORT).show();
			TraceLog.e("Error loading file logs", result.getException());

		} else {
			final String logs = result.getData();
			childViewLogs.setText(logs);
		}
	}

	@Override
	public void onLoaderReset(Loader<AsyncResult<String>> arg0) {
	}

	 /**
     * Refresh the data displayed in the view.
     */
    private void refresh() {
        loader.forceLoad();
    }

	@Override
	public void onRequestError(int operation, Exception e) {
		Toast.makeText(getActivity(), getString(R.string.err_odata_unexpected, e.getMessage()),
				Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onRequestSuccess(int operation, String key) {
		Toast.makeText(getActivity(), getString(R.string.msg_success_upload_file_logs),
				Toast.LENGTH_LONG).show();
	}
	

}
