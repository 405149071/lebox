package com.mgc.letobox.happy.game;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ledong.lib.minigame.IGameSwitchListener;
import com.ledong.lib.minigame.view.ProgressButton;
import com.leto.game.base.bean.MinigameResultBean;
import com.leto.game.base.util.MResource;

import java.util.List;

public class MiniMoreAdapter extends BaseAdapter {
    private List<MinigameResultBean.GameListModel> entity;
    private Context mContext;

    IGameSwitchListener switchListener;


    public MiniMoreAdapter(Context context, List<MinigameResultBean.GameListModel> mListEntity, IGameSwitchListener switchListener) {
        this.mContext = context;
        this.entity = mListEntity;
        this.switchListener = switchListener;
    }

    @Override
    public int getCount() {
        if (entity.size() > 0) {
            return entity.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return entity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MiniMoreAdapter.ViewHolder holder;
        if (null == convertView) {
            holder = new MiniMoreAdapter.ViewHolder();
            convertView = View.inflate(mContext, MResource.getIdByName(mContext, "R.layout.leto_minigame_game_more_list"), null);

            holder.tv_game_paihang = (TextView) convertView.findViewById(MResource.getIdByName(mContext, "R.id.textView_PaiHang"));
            holder.tv_hint = (TextView) convertView.findViewById(MResource.getIdByName(mContext, "R.id.tv_hint"));
            holder.tv_game_name = (TextView) convertView.findViewById(MResource.getIdByName(mContext, "R.id.tv_game_name"));
            holder.textView_game_Fraction = (TextView) convertView.findViewById(MResource.getIdByName(mContext, "R.id.textView_game_Fraction"));
            holder.tv_game_desc = (TextView) convertView.findViewById(MResource.getIdByName(mContext, "R.id.tv_game_desc"));
            holder.iv_game_icon = (ImageView) convertView.findViewById(MResource.getIdByName(mContext, "R.id.iv_game_icon"));
            holder.open_btn = (ProgressButton) convertView.findViewById(MResource.getIdByName(mContext, "R.id.open_btn"));
            convertView.setTag(holder);
        } else {
            holder = (MiniMoreAdapter.ViewHolder) convertView.getTag();
        }
        holder.tv_game_paihang.setVisibility(View.GONE);
        MinigameResultBean.GameListModel gameModel = entity.get(position);
        holder.tv_game_name.setText(gameModel.getName());
        holder.textView_game_Fraction.setText(gameModel.getPlay_num() + "万人在玩");
        holder.tv_game_desc.setText(gameModel.getPublicity());
        if (gameModel.getIcon().endsWith("gif") || gameModel.getIcon().endsWith("GIF")) {
            Glide.with(mContext).load(gameModel.getIcon()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv_game_icon);
        } else {
            Glide.with(mContext).load(gameModel.getIcon()).into(holder.iv_game_icon);
        }

        if (gameModel.getIs_keynote()==2){
            holder.tv_hint.setVisibility(View.VISIBLE);
        }else{
            holder.tv_hint.setVisibility(View.GONE);
        }

        holder.open_btn.setGameBean(gameModel);
        holder.open_btn.setGameSwitchListener(switchListener);

        return convertView;
    }

    class ViewHolder {
        TextView tv_hint;
        TextView tv_game_name;
        TextView tv_game_paihang;
        TextView textView_game_Fraction;
        TextView tv_game_desc;
        ImageView iv_game_icon;
        ProgressButton open_btn;
    }


}
