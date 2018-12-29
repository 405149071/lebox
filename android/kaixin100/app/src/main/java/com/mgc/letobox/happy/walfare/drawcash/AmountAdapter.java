package com.mgc.letobox.happy.walfare.drawcash;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.domain.ChargePoint;

import java.util.List;

/**
 * Create by zhaozhihui on 2018/8/23
 **/
public class AmountAdapter extends BaseQuickAdapter<ChargePoint.Point, BaseViewHolder> {

    Context mContext;

    public AmountAdapter(Context context, List data) {
        super(R.layout.draw_cash_item_amount, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChargePoint.Point item) {
        TextView tvAmount = helper.getView(R.id.tv_amount);
        LinearLayout view = helper.getView(R.id.ll_root);

        tvAmount.setText(""+item.getAmount());

        if(item.isEnable()) {
            if (item.isSelected()) {
                tvAmount.setTextColor(Color.parseColor("#4f6ff6"));
                view.setBackgroundResource(R.mipmap.drawcash_amount_selected);
            } else {
                tvAmount.setTextColor(Color.parseColor("#333333"));
                view.setBackgroundResource(R.mipmap.drawcash_amount_unselect);
            }

            helper.addOnClickListener(R.id.ll_root);
        }else {
            tvAmount.setTextColor(mContext.getResources().getColor(R.color.text_lowgray));
            view.setBackgroundResource(R.mipmap.drawcash_amount_unselect);
        }

    }

}
