package com.xiaobin.security.engine;

import android.app.ProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhongshu on 15/7/15.
 */
public class DownloadTask {

    public static File getFile(String path,String filePath,ProgressDialog progressDialog) throws Exception{
        URL url = new URL(path);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setConnectTimeout(500);
        httpURLConnection.setRequestMethod("GET");
        if (httpURLConnection.getResponseCode() == 200){
            int total = httpURLConnection.getContentLength();
            progressDialog.setMax(total);

            InputStream is = httpURLConnection.getInputStream();
            File file = new File(filePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            int progress = 0;

            while ((len = is.read(buffer)) != -1){
                fileOutputStream.write(buffer,0,len);
                progress += len;
                progressDialog.setProgress(progress);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            is.close();
            return file;
        }
        return null;
    }
}
