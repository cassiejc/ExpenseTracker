package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class SearchListAdapter extends ArrayAdapter<String> {

    public SearchListAdapter(@NonNull Context context, @NonNull ArrayList<String> searchDataList) {
        super(context, 0, searchDataList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        String searchData = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(searchData);
        return convertView;
    }
}
