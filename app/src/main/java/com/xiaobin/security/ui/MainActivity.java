package com.xiaobin.security.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaobin.security.R;
import com.xiaobin.security.adapter.MainUIAdapter;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener
{

    private GridView gridView;

    private MainUIAdapter mainUIAdapter;
    private SharedPreferences sharedPreferences;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        sharedPreferences = this.getSharedPreferences("config", Context.MODE_PRIVATE);
        gridView = (GridView)findViewById(R.id.gv_main);
        mainUIAdapter = new MainUIAdapter(this);
        gridView.setAdapter(mainUIAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                if (position == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Setting");
                    builder.setMessage("Please input");
                    final EditText editText = new EditText(MainActivity.this);
                    editText.setHint("new title");
                    builder.setView(editText);
                    builder.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = editText.getText().toString();
                            if(name.equals(""))
                            {
                                Toast.makeText(MainActivity.this, "can not be null", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Editor editor = sharedPreferences.edit();
                                editor.putString("lostName", name);
                                editor.commit();

                                TextView tv = (TextView) view.findViewById(R.id.tv_main_name);
                                tv.setText(name);
                                mainUIAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // TODO Auto-generated method stub

                        }
                    });

                    builder.create().show();
                }
                return false;
            }
        });

	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position)
        {
            case 0 : //手机防盗
                break;

            case 1 : //通讯卫士
                break;

            case 2 : //软件管理
                break;

            case 3 : //流量管理
                break;

            case 4 : //任务管理
                break;

            case 5 : //手机杀毒
                break;

            case 6 : //系统优化
                break;

            case 7 : //高级工具
                break;

            case 8 : //设置中心
                break;

            default :
                break;
        }
    }
}
