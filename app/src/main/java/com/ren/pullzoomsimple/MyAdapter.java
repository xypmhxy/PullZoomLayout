package com.ren.pullzoomsimple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * Created by Administrator on 2017/7/28
 */

public class MyAdapter extends BaseAdapter {
    private List<String> list;
    private LayoutInflater inflater;

    public MyAdapter(List<String> list, Context context) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(getItem(position));
        return convertView;
    }
}
