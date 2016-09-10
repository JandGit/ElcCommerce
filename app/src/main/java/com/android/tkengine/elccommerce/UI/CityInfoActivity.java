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
public class CityInfoActivity  extends AppCompatActivity {
    private static final int OK = 1;
    private TextView showCity;
    private ListView city;
    private ArrayAdapter<String> cityAdapter;
    private List<String> codeList = new ArrayList<String>();
    private List<String>  cityList = new ArrayList<String>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OK:
                    cityAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);

        String cityUrl = getIntent().getStringExtra("cityUrl");
        final String position = getIntent().getStringExtra("position");

        showCity = (TextView)findViewById(R.id.tv_position);
        showCity.setText("城市");
        city = (ListView)findViewById(R.id.lv_position);
        cityAdapter = new ArrayAdapter<String>(CityInfoActivity.this,android.R.layout.simple_list_item_1,cityList);
        city.setAdapter(cityAdapter);

        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String cityUrl = Constants.HTTP_GET_POSITIONINFO + codeList.get(i) + ".xml";
                switch (getIntent().getStringExtra("type")){
                    case "1":
                        Intent intent = new Intent(CityInfoActivity.this,CountyInfoActivity.class);
                        intent.putExtra("countyUrl",cityUrl);
                        intent.putExtra("position", position + cityList.get(i) + "|");
                        startActivity(intent);
                        break;
                    case "2":
                        SharedPreferences.Editor editor = getSharedPreferences("position",MODE_PRIVATE).edit();
                        editor.putString("position",cityList.get(i));
                        editor.commit();
                        break;
                }

                finish();

            }
        });


      HttpUtil.sentHttpGet(cityUrl ,new HttpCallbackListener() {
            @Override
            public void onFinish(final String result) {
                if (!TextUtils.isEmpty(result)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[] cities = result.split(",");
                            if (cities != null && cities.length > 0) {
                                for (String c : cities) {
                                    String[] array = c.split("\\|");
                                    codeList.add(array[0]);
                                    cityList.add(array[1]);
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
                Toast.makeText(CityInfoActivity.this, "网络异常，稍后再试", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
