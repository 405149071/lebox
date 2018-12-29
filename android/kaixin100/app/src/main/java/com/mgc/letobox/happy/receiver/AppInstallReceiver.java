package com.mgc.letobox.happy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.liang530.log.L;
import com.mgc.letobox.happy.LetoApplication;

import org.greenrobot.eventbus.EventBus;

/**
 * author janecer
 * 2014年4月18日上午11:58:45
 */
public class AppInstallReceiver extends BroadcastReceiver {
	private static final String TAG = "AppInstallReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString().substring(8);//package:com.xiaocaohy.huosuapp
			L.d(TAG,"安装成功："+packageName);

		}else if(intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")){//应用删除
		}
	}
	public IntentFilter getIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		intentFilter.addDataScheme("package");
		return intentFilter;
	}

	public void register(Context context) {
		context.registerReceiver(this, getIntentFilter());
	}

	public void unregister(Context context) {
		context.unregisterReceiver(this);
	}
}
