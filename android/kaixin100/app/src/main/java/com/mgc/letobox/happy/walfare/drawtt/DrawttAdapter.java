package com.mgc.letobox.happy.walfare.drawtt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.domain.LuckyListReponse;

import java.util.Arrays;
import java.util.List;

public class DrawttAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private boolean isChoose[];
    private Context context;
    private List<LuckyListReponse> luckyListReponses;

    public DrawttAdapter(Context context, List<LuckyListReponse> LuckyListReponses) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.luckyListReponses = LuckyListReponses;
        this.isChoose = new boolean[this.luckyListReponses.size() + 1];
        Arrays.fill(this.isChoose, false);
    }

    @Override
    public int getCount() {
        return isChoose.length;
    }

    @Override
    public Object getItem(int position) {
        return isChoose[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawttAdapter.ViewHolder holder = null;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_drawtt, null);
            holder = new DrawttAdapter.ViewHolder();
            holder.thanksLayout = (LinearLayout)convertView.findViewById(R.id.thanks_layout);
            holder.startLayout = (LinearLayout)convertView.findViewById(R.id.start_layout);
            holder.candyLayout = (LinearLayout)convertView.findViewById(R.id.candy_layout);
            holder.iphoneLayout = (LinearLayout)convertView.findViewById(R.id.iphone_layout);
            holder.selectedLayout = (LinearLayout)convertView.findViewById(R.id.selected_layout);
            holder.ttTextView = (TextView)convertView.findViewById(R.id.tt_text);
            convertView.setTag(holder);
        } else {
            holder = (DrawttAdapter.ViewHolder)convertView.getTag();
        }
        holder.thanksLayout.setVisibility(View.INVISIBLE);
        holder.startLayout.setVisibility(View.INVISIBLE);
        holder.candyLayout.setVisibility(View.INVISIBLE);
        holder.iphoneLayout.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                holder.thanksLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.candyLayout.setVisibility(View.VISIBLE);
                LuckyListReponse response = luckyListReponses.get(1);
                holder.ttTextView.setText(response.getName());
                break;
            case 2:
                holder.candyLayout.setVisibility(View.VISIBLE);
                response = luckyListReponses.get(2);
                holder.ttTextView.setText(response.getName());
                break;
            case 3:
                holder.iphoneLayout.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.startLayout.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.candyLayout.setVisibility(View.VISIBLE);
                response = luckyListReponses.get(3);
                holder.ttTextView.setText(response.getName());
                break;
            case 6:
                holder.candyLayout.setVisibility(View.VISIBLE);
                response = luckyListReponses.get(6);
                holder.ttTextView.setText(response.getName());
                break;
            case 7:
                holder.candyLayout.setVisibility(View.VISIBLE);
                response = luckyListReponses.get(5);
                holder.ttTextView.setText(response.getName());
                break;
            case 8:
                holder.candyLayout.setVisibility(View.VISIBLE);
                response = luckyListReponses.get(4);
                holder.ttTextView.setText(response.getName());
                break;
        }
        if (isChoose[position]) {
            holder.selectedLayout.setVisibility(View.VISIBLE);
        } else {
            holder.selectedLayout.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public void setSeclection(int position) {
        for (int i = 0; i < isChoose.length; ++i) {
            if (i == position) {
                isChoose[i] = true;
            } else {
                isChoose[i] = false;
            }
        }
    }

    public boolean[] getIsChoose() {
        return isChoose;
    }

    class ViewHolder {
        LinearLayout thanksLayout;
        LinearLayout startLayout;
        LinearLayout candyLayout;
        LinearLayout iphoneLayout;
        LinearLayout selectedLayout;
        TextView ttTextView;
    }
}
