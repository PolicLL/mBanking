package com.example.mbankingapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mbankingapp.R;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    private String[] dates;
    private String[] descriptions;
    private String[] amounts;
    private String[] types;

    public MyListAdapter(Activity context  , String[] dates, String[] descriptions
            , String[] amounts , String[] types)
    {
        super(context, R.layout.mylist, dates);
        // TODO Auto-generated constructor stub

        this.context = context;

        this.dates = dates;
        this.descriptions = descriptions;
        this.amounts = amounts;
        this.types = types;

    }

    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null,true);

        TextView date = rowView.findViewById(R.id.textViewDate);
        TextView description =  rowView.findViewById(R.id.textViewDescription);
        TextView amount =  rowView.findViewById(R.id.textViewAmount);
        TextView type = rowView.findViewById(R.id.textViewType);

        date.setText(dates[position]);
        description.setText(descriptions[position]);
        amount.setText(amounts[position]);
        type.setText(types[position]);

        return rowView;

    }
}