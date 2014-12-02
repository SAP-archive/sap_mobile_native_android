package com.sap.dcode.agency.ui.offline;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sap.dcode.agency.R;
import com.sap.dcode.agency.types.OfflineError;

public class OfflineErrorListAdapter extends BaseAdapter {
    final private LayoutInflater inflater;
    final private OfflineErrorListFragment fragment;
    final private List<OfflineError> errors;
    final private ListView myList;

    public OfflineErrorListAdapter(OfflineErrorListFragment fragment, ListView myList, LayoutInflater inflater, List<OfflineError> errors) {
        this.inflater = inflater;
        this.fragment = fragment;
        this.myList = myList;
        this.errors = errors;
    }

	@Override
	public int getCount() {
		return (errors!=null)?errors.size():0;
	}

	@Override
	public Object getItem(int position) {
		return (errors!=null)?errors.get(position):null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.error_list_item, parent, false);
        }

        OfflineError errorArchive = errors.get(position);

        ((TextView) view.findViewById(R.id.error_method)).setText(errorArchive.getRequestMethod());
        ((TextView) view.findViewById(R.id.error_http_code)).setText(errorArchive.getHttpStatusCode());
        ((TextView) view.findViewById(R.id.request_url)).setText(errorArchive.getRequestURL());

        view.setOnClickListener(new ErrorListClickListener(errors.get(position)));
        view.setOnLongClickListener(new ErrorListLongClickListener(position));

        return view;
	}
	
    /**
     * Delete an item from the list.
     *
     * @param i the index of the item to delete
     */
    public void deleteItem(int i) {
        myList.setItemChecked(i, false);  // un-select the item that is being deleted
        errors.remove(i);
        notifyDataSetChanged();
    }

    /**
     * This listener handles clicks on a travel agency in the list.
     */
    private class ErrorListClickListener implements View.OnClickListener {

        final private OfflineError item;

        private ErrorListClickListener(OfflineError item) {
            this.item = item;
        }

        @Override
        public void onClick(final View view) {
            fragment.onAgencySelected(item);
        }
    }
    
    
    /**
     * This listener handles long clicks on a travel agency in the list. Long clicking
     * on an agency makes it available for deleting.
     */
    private class ErrorListLongClickListener implements View.OnLongClickListener {

        final private int i;

        private ErrorListLongClickListener(int i) {
            this.i = i;
        }

        @Override
        public boolean onLongClick(View view) {
            myList.setItemChecked(i, true);
            return true;
        }
    }

}
