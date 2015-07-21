package com.xiaobin.security.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaobin.security.R;

/**
 * Created by zhongshu on 15/7/15.
 */
public class MainUIAdapter extends BaseAdapter {

    private static final String[] NAMES = new String[]{"手机防盗", "通讯卫士", "软件管理", "流量管理", "任务管理", "手机杀毒", "系统优化", "高级工具", "设置中心"};
    private static final int[] ICONS = new int[]{R.drawable.widget01, R.drawable.widget02, R.drawable.widget03, R.drawable.widget04, R.drawable.widget05, R.drawable.widget06, R.drawable.widget07, R.drawable.widget08, R.drawable.widget09};

    private static ImageView imageView;
    private static TextView textView;

    private Context context;
    private LayoutInflater layoutInflater;
    private SharedPreferences sharedPreferences;

    public MainUIAdapter(Context context){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.sharedPreferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
    }
    @Override
    public int getCount() {
        return NAMES.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = layoutInflater.inflate(R.layout.main_item,null);
        imageView = (ImageView)view1.findViewById(R.id.iv_main_icon);
        textView = (TextView)view1.findViewById(R.id.tv_main_name);
        imageView.setImageResource(ICONS[i]);
        textView.setText(NAMES[i]);

        if(i == 0){
            String name = sharedPreferences.getString("lostname","");
            if(!name.equals("")){
                textView.setText(name);
            }
        }

        return view1;
    }
}
