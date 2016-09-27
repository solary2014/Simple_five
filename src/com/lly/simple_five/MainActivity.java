package com.lly.simple_five;

import net.youmi.android.AdManager;
import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private static String TAG ="MainActivity111";
	
	private FiveView fiveView;
	private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        AdManager.getInstance(mContext).init("366a66c021382aac", "b485524848cd498d", false, true);
        fiveView = (FiveView) findViewById(R.id.five);
        setupBannerAd();
    }
    /**
	 * 设置广告条广告
	 */
	private void setupBannerAd() {
		/**
		 * 悬浮布局
		 */
		// 实例化LayoutParams
		FrameLayout.LayoutParams layoutParams =
				new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置，这里示例为右下角
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		// 获取广告条控件
		final View bannerView =
				BannerManager.getInstance(mContext).getBannerView(new BannerViewListener
						() {

					@Override
					public void onRequestSuccess() {
						Log.i(TAG, "请求广告条成功");
					}

					@Override
					public void onSwitchBanner() {
						Log.i(TAG, "广告条切换");
					}

					@Override
					public void onRequestFailed() {
						Log.i(TAG, "请求广告条失败");
					}
				});
		((Activity) mContext).addContentView(bannerView, layoutParams);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	getMenuInflater().inflate(R.menu.main, menu);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId()==R.id.action_settings){
    		fiveView.setrestart();
    	}
    	return true;
    }
    
}
