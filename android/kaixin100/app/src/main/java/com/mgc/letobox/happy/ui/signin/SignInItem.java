package com.mgc.letobox.happy.ui.signin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.domain.SignResponse;


/**
 * Create by zhaozhihui on 2018/8/14
 **/
public class SignInItem extends LinearLayout implements ISignInLayout {

    private Context mContext;

    View view;
    View titleView;

    RelativeLayout rl_item;

    LinearLayout ll_recover, ll_content, ll_title, ll_reward;
    ImageView iv_reward;
    TextView tv_reward, tv_title;

    FlashView flashView;


    SignResponse.RulesBean signInDay;

    ISignInListener mListener;

    int mWidth;
    int mHeight;


    public SignInItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        view = LayoutInflater.from(getContext()).inflate(R.layout.sign_in_layout_day, this, true);
        rl_item = view.findViewById(R.id.rl_item);
        ll_title = view.findViewById(R.id.ll_title);
        ll_recover = view.findViewById(R.id.ll_recover);
        ll_content = view.findViewById(R.id.ll_content);
        iv_reward = view.findViewById(R.id.iv_reward);
        tv_reward = view.findViewById(R.id.tv_reward);
        tv_title = view.findViewById(R.id.tv_title);
        ll_reward = view.findViewById(R.id.ll_reward);
        flashView = view.findViewById(R.id.flashView);
        ll_recover.setAlpha((float)0.82);
        flashView.setAlpha((float)0.82);
        ll_reward.setAlpha((float)0.82);
    }

    public SignInItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setListener(ISignInListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setSignInDay(SignResponse.RulesBean signin) {
        signInDay = signin;

        LayoutParams params = (LayoutParams) rl_item.getLayoutParams();
        params.height = mHeight;
        params.width = mWidth;
        rl_item.setLayoutParams(params);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mListener){
                    mListener.onSignIn(signInDay.getNum());
                }
            }
        });

        int number = signin.getNum();
        String title = "";
        switch (number) {
            case 1:
                title = "第一天";
                break;
            case 2:
                title = "第二天";
                break;
            case 3:
                title = "第三天";
                break;
            case 4:
                title = "第四天";
                break;
            case 5:
                title = "第五天";
                break;
            case 6:
                title = "第六天";
                break;
            case 7:
                title = "第七天";
                break;
        }

        tv_title.setText(title);

        tv_reward.setText("" + signin.getAward() + "糖果");

        //ll_reward.setBackground(getResources().getDrawable(R.drawable.sign_in_item_bottom));

        ll_recover.setBackground(getResources().getDrawable(R.drawable.sign_in_item_bottom_bg));

        if (signin.isSign) {
            flashView.setVisibility(GONE);
            ll_recover.setVisibility(VISIBLE);
        } else {
            if(signin.isTaday()){
                flashView.startRotate();
                flashView.setVisibility(VISIBLE);
            }else {
                flashView.setVisibility(GONE);
            }
            ll_recover.setVisibility(GONE);
        }

    }


    @Override
    public SignResponse.RulesBean getSignInDay() {
        return signInDay;
    }

    @Override
    public void dayClick() {

    }

    public void setSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public void setRewardImage(int resId) {
        iv_reward.setImageResource(resId);
    }

    public interface ISignInListener {
        public void onSignIn(int day);
    }
}
