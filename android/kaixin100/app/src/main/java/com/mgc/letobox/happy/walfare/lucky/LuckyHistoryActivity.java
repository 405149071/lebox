package com.mgc.letobox.happy.walfare.lucky;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.mgc.letobox.happy.R;
import com.liang530.log.T;
import com.mgc.letobox.happy.base.BaseActivity;
import com.mgc.letobox.happy.config.SdkApi;
import com.mgc.letobox.happy.domain.ListBaseRequestBean;
import com.mgc.letobox.happy.domain.LuckyListReponse;
import com.mgc.letobox.happy.util.GsonUtil;
import com.mgc.letobox.happy.walfare.lucky.dialog.LuckyDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LuckyHistoryActivity extends BaseActivity {


    @BindView(R.id.circle_name)
    TextView circle_name;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;


    //@BindView(R.id.emptyView)
    //View emptyView;

    @BindView(R.id.tv_no_comment)
    TextView tv_no_comment;

    @BindView(R.id.lv_lucky)
    //PinnedSectionListView mListView;
            ListView mListView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    RecordGroupAdapter mRecordGroupAdapter;


    private List<RecordDateGroup> mList = new ArrayList<>();
    List<LuckyListReponse> responsesList = new ArrayList<>();

    int mPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_history);
        ButterKnife.bind(this);

        circle_name.setText("我的奖品");
        //tv_no_comment.setText("暂无抽奖记录");

        mRecordGroupAdapter = new RecordGroupAdapter(LuckyHistoryActivity.this, mList);
        mListView.setAdapter(mRecordGroupAdapter);

        initOnClick();

        mPage = 1;
        getPageData(mPage);
    }


    int totalItemCount;
    int lastVisibleItem;
    boolean isLoading;

    private void initOnClick() {
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                getPageData(mPage);
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                mPage = 1;
                getPageData(mPage);
            }
        });
    }

    /**
     * 我的中奖纪录
     */
    private void getPageData(int page) {
        ListBaseRequestBean requestBean = new ListBaseRequestBean();
        requestBean.setPage(page);
        requestBean.setOffset(10);
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(requestBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<List<LuckyListReponse>>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(List<LuckyListReponse> data) {
                if (data != null) {
                    mList.clear();
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<LuckyListReponse> responses = gson.fromJson(str, new TypeToken<List<LuckyListReponse>>() {
                    }.getType());

                    if (responses != null && responses.size() != 0) {
                        if (mPage == 1) {
                            responsesList.clear();
                            responsesList.addAll(responses);
                            mList.addAll(RecordDateGroup.getData(responsesList));
                            mRecordGroupAdapter.notifyDataSetChanged();
                            refreshLayout.finishRefresh();
                            refreshLayout.setNoMoreData(false);
                        } else {
                            responsesList.addAll(responses);
                            mList.addAll(RecordDateGroup.getData(responsesList));
                            mRecordGroupAdapter.notifyDataSetChanged();
                            refreshLayout.finishLoadMore(0);
                            if (responses.size() < 10) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    } else {
                        refreshLayout.finishLoadMore(0);
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    if (mList != null && mList.size() == 0) {
                        refreshLayout.setNoMoreData(true);
                    }
                    refreshLayout.finishLoadMore(0);
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
                if (null == mList || mList.size() == 0) {
                    tv_no_comment.setVisibility(View.VISIBLE);
                } else {
                    tv_no_comment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
//                if (refreshLayout != null) {
//                    refreshLayout.finishRefresh();
//                    refreshLayout.finishLoadMore();
//                    refreshLayout.finishLoadMoreWithNoMoreData();
//                }
            }
        };
        RxVolley.post(SdkApi.getMyLog(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    public class RecordGroupAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        private List<RecordDateGroup> list;

        private Context mContext;

        public RecordGroupAdapter(Context context,
                                  List<RecordDateGroup> listData) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
            this.list = listData;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        public void setList(List<RecordDateGroup> listData) {
            this.list = listData;
        }

        @Override
        public Object getItem(int position) {
            return list == null ? null : list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.lucky_item_history, null);
                viewHolder.rl_bill_detail = (RelativeLayout) convertView.findViewById(R.id.rl_bill_detail);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tv_event = (TextView) convertView.findViewById(R.id.tv_event);
                viewHolder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
                viewHolder.btn_draw = (TextView) convertView.findViewById(R.id.btn_draw);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            RecordDateGroup bean = (RecordDateGroup) getItem(position);

            if (bean.type == RecordDateGroup.SECTION) {
                viewHolder.itemTitle.setVisibility(View.VISIBLE);
                viewHolder.itemTitle.setText(list.get(position).getDetail().getCreate_time());
                viewHolder.rl_bill_detail.setVisibility(View.GONE);
            } else {
                viewHolder.itemTitle.setVisibility(View.GONE);
                viewHolder.rl_bill_detail.setVisibility(View.VISIBLE);

                int month = 1, year = 1977, day = 0, hour = 0, min = 0, sec = 0;
                // 用parse方法，可能会异常，所以要try-catch
                Date date = new Date(Long.parseLong(list.get(position).getDetail().getCreate_time()) * 1000);
                // 获取日期实例
                Calendar calendar = Calendar.getInstance();
                // 将日历设置为指定的时间
                calendar.setTime(date);
                // 获取年
                year = calendar.get(Calendar.YEAR);
                // 这里要注意，月份是从0开始。
                month = calendar.get(Calendar.MONTH) + 1;
                // 获取天
                day = calendar.get(Calendar.DAY_OF_MONTH);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);
                sec = calendar.get(Calendar.SECOND);

                String hourStr = hour < 10 ? "0" + hour : "" + hour;
                String minStr = min < 10 ? "0" + min : "" + min;


                viewHolder.tv_time.setText("" + hourStr + ":" + minStr/*+":"+sec*/);

                //viewHolder.tv_time.setText(""+hour+":"+min/*+":"+sec*/);

                viewHolder.tv_event.setText(list.get(position).getDetail().getLottery_name().trim());

                viewHolder.btn_draw.setOnClickListener(null);

                if (list.get(position).getDetail().getStatus() == 1) {
                    viewHolder.btn_draw.setVisibility(View.VISIBLE);
                    viewHolder.btn_draw.setBackground(getResources().getDrawable(R.drawable.lucky_history_btn_draw));
                    viewHolder.btn_draw.setText("已领取");
                } else if (list.get(position).getDetail().getStatus() == 2) {
                    viewHolder.btn_draw.setVisibility(View.VISIBLE);
                    viewHolder.btn_draw.setBackground(getResources().getDrawable(R.drawable.lucky_history_btn));
                    viewHolder.btn_draw.setText("领取");
                    viewHolder.btn_draw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new LuckyDialog().showDialog(mContext, false, "恭喜您获得", list.get(position).getDetail().getLottery_name(), list.get(position).getDetail().getPic(),
                                    true, list.get(position).getDetail().getRemark(), "复制", null, new LuckyDialog.ConfirmDialogListener() {
                                        @Override
                                        public void ok() {
                                            ClipboardManager cm = (ClipboardManager) getSystemService(Context
                                                    .CLIPBOARD_SERVICE);
                                            if (cm != null) {
                                                ClipData clip = ClipData.newPlainText("simple text", list.get(position).getDetail().getRemark());
                                                cm.setPrimaryClip(clip);
                                                T.s(mContext, "已复制");
                                            }
                                        }

                                        @Override
                                        public void cancel() {

                                        }

                                        @Override
                                        public void dismiss() {

                                        }
                                    });
                        }
                    });
                } else {
                    viewHolder.btn_draw.setVisibility(View.GONE);
                }

            }

            return convertView;
        }

        class ViewHolder {
            RelativeLayout rl_bill_detail;
            TextView btn_draw, itemTitle, tv_event, tv_time;
        }


    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LuckyHistoryActivity.class);
        context.startActivity(intent);
    }
}