package com.xiaobin.security.engine;

import android.util.Xml;

import com.xiaobin.security.domain.UpdateInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * Created by zhongshu on 15/7/13.
 */
public class UpdateInfoParse {

    public static UpdateInfo getUpdateInfo(InputStream is) throws Exception{

        UpdateInfo updateInfo = new UpdateInfo();
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(is, "utf-8");
        int type = xmlPullParser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT){
            switch (type){
                case XmlPullParser.START_TAG :
                    if (xmlPullParser.getName().equals("version")){
                        updateInfo.setVersion(xmlPullParser.nextText());
                    }
                    else if (xmlPullParser.getName().equals("description")){
                        updateInfo.setDescription(xmlPullParser.nextText());
                    }
                    else if (xmlPullParser.getName().equals("apkurl")){
                        updateInfo.setUrl(xmlPullParser.nextText());
                    }
                    break;
                default:
                    break;
            }
            type = xmlPullParser.next();
        }
        return updateInfo;
    }
}
