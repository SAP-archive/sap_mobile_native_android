package com.sap.dcode.online.ui.online;

import java.util.List;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sap.dcode.online.R;
import com.sap.dcode.online.types.Agency;

public class AgencyListAdapter extends BaseAdapter {
    final private LayoutInflater inflater;
    final private AgencyListFragment fragment;
    final private List<Agency> agencies;
    final private ListView myList;

    public AgencyListAdapter(AgencyListFragment fragment, ListView myList, LayoutInflater inflater, List<Agency> agencies) {
        this.inflater = inflater;
        this.fragment = fragment;
        this.myList = myList;
        this.agencies = agencies;
    }

	@Override
	public int getCount() {
		return (agencies!=null)?agencies.size():0;
	}

	@Override
	public Object getItem(int position) {
		return (agencies!=null)?agencies.get(position):null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.agency_list_item, parent, false);
        }

        Agency agency = agencies.get(position);

        ((TextView) view.findViewById(R.id.agency_name)).setText(agency.getAgencyName());
        ((TextView) view.findViewById(R.id.agency_id)).setText(agency.getAgencyId());
        
        String location = TextUtils.isEmpty(agency.getWebsite())?agency.getCity():agency.getWebsite();
        ((TextView) view.findViewById(R.id.agency_location)).setText(location);

        view.setOnClickListener(new AgencyListClickListener(agencies.get(position)));
        view.setOnLongClickListener(new AgencyListLongClickListener(position));

        return view;
	}
	
    /**
     * Delete an item from the list.
     *
     * @param i the index of the item to delete
     */
    public void deleteItem(int i) {
        myList.setItemChecked(i, false);  // un-select the item that is being deleted
        agencies.remove(i);
        notifyDataSetChanged();
    }

    /**
     * This listener handles clicks on a travel agency in the list.
     */
    private class AgencyListClickListener implements View.OnClickListener {

        final private Agency item;

        private AgencyListClickListener(Agency item) {
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
    private class AgencyListLongClickListener implements View.OnLongClickListener {

        final private int i;

        private AgencyListLongClickListener(int i) {
            this.i = i;
        }

        @Override
        public boolean onLongClick(View view) {
            myList.setItemChecked(i, true);
            return true;
        }
    }

}
