package com.eim.winder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Mari on 03.02.2016.
 */
public class CustomArrayAdapter extends ArrayAdapter<String> {

    public CustomArrayAdapter(Context context, String[] text) {
        super(context, R.layout.row_layout, text);
    }
    @Override
    public View getView(int position, View contentView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customRowView = inflater.inflate(R.layout.row_layout, parent, false);

        String singleItem = getItem(position);
        TextView itemNameText = (TextView) customRowView.findViewById(R.id.itemname_textview);
        itemNameText.setText(singleItem);

        return customRowView;
    }
}
