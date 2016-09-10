package com.android.tkengine.elccommerce.UI;

import android.content.Intent;
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
import com.android.tkengine.elccommerce.presenter.AddressPresenter;
import com.android.tkengine.elccommerce.utils.HttpCallbackListener;
import com.android.tkengine.elccommerce.utils.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/12.
 */
public class ProvinceInfoActivity extends AppCompatActivity {

    private static final int OK = 1;
    private TextView showPronvice;
    private ListView province;
    private ArrayAdapter<String> provinceAdapter;
    private List<Province> dataList = new ArrayList<Province>();
    private List<String>  provinceList = new ArrayList<String>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OK:
                    provinceAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);

        showPronvice = (TextView)findViewById(R.id.tv_position);
        province = (ListView)findViewById(R.id.lv_position);
        provinceAdapter = new ArrayAdapter<String>(ProvinceInfoActivity.this,android.R.layout.simple_list_item_1,provinceList);
        province.setAdapter(provinceAdapter);

        province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String cityUrl = Constants.HTTP_GET_POSITIONINFO + dataList.get(i).getProvinceCode() + ".xml";
                Intent intent = new Intent(ProvinceInfoActivity.this,CityInfoActivity.class);
                intent.putExtra("cityUrl",cityUrl);
                intent.putExtra("type",getIntent().getStringExtra("type"));
                Log.d("cityUrl",cityUrl);
                intent.putExtra("position",provinceList.get(i) + "|");
                startActivity(intent);
                finish();

            }
        });

        HttpUtil.sentHttpGet(Constants.HTTP_GET_POSITIONINFO +".xml",new HttpCallbackListener() {
            @Override
            public void onFinish(final String result) {
                if (!TextUtils.isEmpty(result)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String[] provinces = result.split(",");
                            if (provinces != null && provinces.length > 0) {
                                for (String p : provinces) {
                                    String[] array = p.split("\\|");
                                    Province province = new Province();
                                    province.setProvinceCode(array[0]);
                                    province.setProvinceName(array[1]);
                                    dataList.add(province);
                                    provinceList.add(array[1]);
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
                Toast.makeText(ProvinceInfoActivity.this, "网络异常，稍后再试", Toast.LENGTH_SHORT).show();
            }

        });

    }



    public class Province{
        private String provinceName;
        private String provinceCode;

        public void setProvinceName(String provinceName){
            this.provinceName = provinceName;
        }

        public void setProvinceCode(String provinceCode){
            this.provinceCode = provinceCode;
        }

        public String getProvinceName(){
            return provinceName;
        }

        public String getProvinceCode(){
            return provinceCode;
        }

    }




}
