package com.xiaobin.security.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaobin.security.R;
import com.xiaobin.security.domain.UpdateInfo;
import com.xiaobin.security.engine.DownloadTask;
import com.xiaobin.security.engine.UpdateInfoService;

import java.io.File;

public class SplashActivity extends Activity
{
	private TextView tv_version;
	private LinearLayout ll;
    private UpdateInfo updateInfo;
    private String version_all;
    private ProgressDialog progressDialog;

    private static final String TAG = "Security";

    @SuppressLint("HandlerLeak")
    private android.os.Handler handler = new android.os.Handler(){
        public void handleMessage(Message msg){
            if (isNeedUpdate(version_all)){
                showUpdateDialog();
            }
        }
    };
	
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
        version_all = getVersion();
		tv_version.setText("版本号  " + version_all);
		
		ll = (LinearLayout) findViewById(R.id.ll_splash_main);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(2000);
		ll.startAnimation(alphaAnimation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("downloading ...");

        new Thread(){
            public void run(){
                try {
                    sleep(3000);
                    handler.sendEmptyMessage(0);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }.start();

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
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    File dir = new File(Environment.getExternalStorageDirectory(),"/security/update");
                    if(!dir.exists()){
                        dir.mkdirs();
                    }
                    String apkPath = Environment.getExternalStorageDirectory()+"/security/update/new.apk";
                    Log.i("Security","apk path:"+apkPath);
                    UpdateTask task = new UpdateTask(updateInfo.getUrl(),apkPath);
                    progressDialog.show();
                    new Thread(task).start();
                }
                else {
                    Toast.makeText(SplashActivity.this,"sdcard useless",Toast.LENGTH_SHORT).show();
                    loadMainUI();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                loadMainUI();
            }
        });

        builder.create().show();
    }

    private boolean isNeedUpdate(String version){
        // 4.0 and after need to start a new Thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    UpdateInfoService updateInfoService = new UpdateInfoService(SplashActivity.this);
//                    Looper.prepare();
                    updateInfo = updateInfoService.getUpdateInfo(R.string.serverUrl);
//                    Looper.loop();
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
			return "Not known version";
		}
	}

    private void loadMainUI(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void install(File file){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        finish();
        startActivity(intent);
    }

    class UpdateTask implements Runnable{
        private String path;
        private String filePath;

        public UpdateTask(String path,String filePath){
            this.path = path;
            this.filePath = filePath;
        }

        @Override
        public void run(){
            try{
                File file = DownloadTask.getFile(path,filePath,progressDialog);
                progressDialog.dismiss();
                install(file);
            }catch (Exception e){
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(SplashActivity.this,"update failed",Toast.LENGTH_SHORT).show();
                loadMainUI();
            }
        }
    }

}
