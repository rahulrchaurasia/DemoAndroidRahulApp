package com.policyboss.demoandroidapp.UI.AutoCompleteDemo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.policyboss.demoandroidapp.DataModel.BankModel.BankEntity;
import com.policyboss.demoandroidapp.R;

import java.util.ArrayList;
import java.util.List;

public class BankAdapter extends ArrayAdapter<BankEntity> {

    Context context;
    int resource;
    List<BankEntity> items, tempItems, suggestions;

    public BankAdapter(Context context, int resource,  List<BankEntity> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;

        this.items = items;
        tempItems = new ArrayList<BankEntity>(items); // this makes the difference.
        suggestions = new ArrayList<BankEntity>();
    }

    @Nullable
    @Override
    public BankEntity getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drop_down_item, parent, false);
        }
        BankEntity carMasterEntity = items.get(position);
        if (carMasterEntity != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(carMasterEntity.getBankname());
        }
        return view;
    }


    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((BankEntity) resultValue).getBankname();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (BankEntity bankEntity : tempItems) {
                    if (bankEntity.getBankname().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(bankEntity);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<BankEntity> filterList = (ArrayList<BankEntity>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (BankEntity bankEntity : filterList) {
                    add(bankEntity);
                    notifyDataSetChanged();
                }
            }
        }
    };
}