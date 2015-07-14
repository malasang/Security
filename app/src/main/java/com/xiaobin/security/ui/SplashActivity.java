package com.xiaobin.security.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaobin.security.R;
import com.xiaobin.security.domain.UpdateInfo;
import com.xiaobin.security.engine.UpdateInfoService;

public class SplashActivity extends Activity
{
	private TextView tv_version;
	private LinearLayout ll;
    private UpdateInfo updateInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        /**
         * 全屏，不带title，两种写法
         */
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		tv_version = (TextView) findViewById(R.id.tv_splash_version);
        String version = getVersion();
		tv_version.setText("版本号  " + version);
		
		ll = (LinearLayout) findViewById(R.id.ll_splash_main);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(2000);
		ll.startAnimation(alphaAnimation);

        if (isNeedUpdate(version)){
            showUpdateDialog();
        }
	}

    private void showUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("update notify");
        builder.setMessage(updateInfo.getDescription());
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){

            }
        });

        builder.create().show();
    }

    private boolean isNeedUpdate(String version){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    UpdateInfoService updateInfoService = new UpdateInfoService(SplashActivity.this);
                    Looper.prepare();
                    updateInfo = updateInfoService.getUpdateInfo(R.string.serverUrl);
                    Looper.loop();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        try{
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (updateInfo == null){
            Log.i("security","update version with exception");
            return false;
        }

        String v = updateInfo.getVersion();
        if (v.equals(version)){
            Log.i("security","not need to update");
            return false;
        }else {
            Log.i("security","need to udpate");
            return true;
        }
    }

	private String getVersion()
	{
		try
		{
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			
			return packageInfo.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
			return "版本号未知";
		}
	}

}
