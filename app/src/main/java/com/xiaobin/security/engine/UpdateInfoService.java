package com.xiaobin.security.engine;

import android.content.Context;

import com.xiaobin.security.domain.UpdateInfo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhongshu on 15/7/13.
 */
public class UpdateInfoService {

    private Context context;

    public UpdateInfoService(Context context){
        this.context = context;
    }

    public UpdateInfo getUpdateInfo(int urlId) throws Exception{
        String path = context.getResources().getString(urlId);
        URL url = new URL(path);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setRequestMethod("GET");
        InputStream is = null;
        try {
            is = httpURLConnection.getInputStream();
        }catch (Exception e){
            e.printStackTrace();
        }
//        InputStream is = httpURLConnection.getInputStream();
        return UpdateInfoParse.getUpdateInfo(is);
    }
}
