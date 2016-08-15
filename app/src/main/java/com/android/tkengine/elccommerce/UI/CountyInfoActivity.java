package com.android.tkengine.elccommerce.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.utils.HttpCallbackListener;
import com.android.tkengine.elccommerce.utils.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/12.
 */
public class CountyInfoActivity  extends AppCompatActivity {
    private static final int OK = 1;
    private TextView showCounty;
    private ListView county;
    private ArrayAdapter<String> countyAdapter;
    private List<String> codeList = new ArrayList<String>();     //县/区所对应链接的代号（用于网络请求）
    private List<String>  countyList = new ArrayList<String>();   //县/区名
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OK:
                    countyAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);

        String countyUrl = getIntent().getStringExtra("countyUrl");
        final String position = getIntent().getStringExtra("position");

        showCounty = (TextView)findViewById(R.id.tv_position);
        showCounty.setText("县/区");
        county = (ListView)findViewById(R.id.lv_position);
        countyAdapter = new ArrayAdapter<String>(CountyInfoActivity.this,android.R.layout.simple_list_item_1,countyList);
        county.setAdapter(countyAdapter);

        county.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = getSharedPreferences("position_data",MODE_PRIVATE).edit();
                editor.putString("position",position +countyList.get(i));
                editor.commit();
               /* Intent intent = new Intent(CountyInfoActivity.this,NewAddressActivity.class);
                startActivity(intent);*/

                finish();
            }
        });

        HttpUtil.sentHttpGet(countyUrl ,new HttpCallbackListener() {
            @Override
            public void onFinish(final String result) {
                if (!TextUtils.isEmpty(result)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[] counties = result.split(",");
                            if (counties != null && counties.length > 0) {
                                for (String c : counties) {
                                    String[] array = c.split("\\|");
                                    codeList.add(array[0]);
                                    countyList.add(array[1]);
                                }
                            }
                            Message message = new Message();
                            message.what = OK;
                            handler.sendMessage(message);
                        }
                    }).start();

                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(CountyInfoActivity.this, "网络异常，稍后再试", Toast.LENGTH_SHORT).show();
            }

        });

    }
}

