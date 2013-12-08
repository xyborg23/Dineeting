package com.example.testapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter1 extends ArrayAdapter<String> {

    public Adapter1(Context context, int resID, ArrayList<String> items) {
        super(context, resID, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if (position == 1) {
            ((TextView) v).setTextColor(Color.GREEN);
        }
        return v;
    }

}