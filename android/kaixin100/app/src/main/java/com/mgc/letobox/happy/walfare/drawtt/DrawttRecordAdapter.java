package com.mgc.letobox.happy.walfare.drawtt;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mgc.letobox.happy.R;

import java.util.List;

public class DrawttRecordAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<String> recordStringList;

    public DrawttRecordAdapter(Context context, List<String> recordStringList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.recordStringList = recordStringList;
    }

    @Override
    public int getCount() {
        return recordStringList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordStringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawttRecordAdapter.ViewHolder holder = null;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_drawtt_record, null);
            holder = new DrawttRecordAdapter.ViewHolder();
            holder.recordTextView = (TextView)convertView.findViewById(R.id.record_text);
            convertView.setTag(holder);
        } else {
            holder = (DrawttRecordAdapter.ViewHolder)convertView.getTag();
        }
        if (0 == position % 4) {
            holder.recordTextView.setTextColor(Color.parseColor("#fff700"));
            holder.recordTextView.setGravity(Gravity.RIGHT);
        } else if (1 == position %4) {
            holder.recordTextView.setTextColor(Color.WHITE);
            holder.recordTextView.setGravity(Gravity.CENTER);
        } else {
            holder.recordTextView.setTextColor(Color.WHITE);
            holder.recordTextView.setGravity(Gravity.LEFT);
        }
        holder.recordTextView.setText(recordStringList.get(position));

        return convertView;
    }

    public void setRecordStringList(List<String> recordStringList) {
        this.recordStringList = recordStringList;
    }

    class ViewHolder {
        TextView recordTextView;
    }
}
