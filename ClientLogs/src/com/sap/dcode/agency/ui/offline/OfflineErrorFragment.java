package com.sap.dcode.agency.ui.offline;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sap.dcode.agency.R;
import com.sap.dcode.agency.types.OfflineError;

public class OfflineErrorFragment extends Fragment {
    private View myView;
    private TextView childViewReqMethod, childViewReqBody, childViewHTTPCode;
    private TextView childViewMessage, childViewReqURL;
    private LayoutInflater inflater;
    private OfflineError error;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		if (myView == null) {
			myView = this.inflater.inflate(R.layout.error_fragment, null);
			childViewReqMethod = (TextView) myView.findViewById(R.id.err_detail_request_method);
			childViewReqBody = (TextView) myView.findViewById(R.id.err_detail_request_body);
			childViewHTTPCode = (TextView) myView.findViewById(R.id.err_detail_http_code);
			childViewMessage = (TextView) myView.findViewById(R.id.err_detail_message);
			childViewReqURL = (TextView) myView.findViewById(R.id.err_detail_request_url);
		}
		
		//When user select an agency, the agency will be passed as an intent
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			error = bundle.getParcelable("ErrorSelected");
			initializeViews();
		
		}
		return myView;
	}

	
	private void initializeViews(){
		if (error!=null) {
			childViewReqMethod.setText(error.getRequestMethod());
			childViewReqBody.setText(error.getRequestBody());
			childViewHTTPCode.setText(error.getHttpStatusCode());
			childViewMessage.setText(error.getMessage());
			childViewReqURL.setText(error.getRequestURL());
		}
	}

	
	
}
