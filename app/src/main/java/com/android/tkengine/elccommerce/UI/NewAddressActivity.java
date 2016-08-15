package com.android.tkengine.elccommerce.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.GoodsAddressBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.presenter.AddressPresenter;

import java.io.Serializable;

/**
 * Created by 陈嘉shuo on 2016/8/12.
 */
public class NewAddressActivity extends AppCompatActivity {

    private  static final int NEW_SUCCESS = 1;     //创建新地址
    private static final int EDIT_SUCCESS = 2;   //编辑地址
    private int handleType = 0;    //用于onCreate方法中判断来自哪个操作请求
    private boolean firstEditData = false;  //用于onResume方法中判断是否为编辑地址状态（第一次）
    private GoodsAddressBean.ResultBean  goodsAddressBean = null;   //用于保存编辑前的信息（主要是地址id）
    private View newAddressView;
    private TextView saveNewAddress;    //保存地址
    private EditText newReceiverName;   //收货人
    private EditText newReceiverTel;    //联系电话
    private TextView selectNewAddress;   //所在地区
    private EditText newConcreteAddress;   //详细地址
    private CheckBox setDefaultAddress;    //设为默认地址
    private GoodsAddressBean.ResultBean addressBean;   //新建地址信息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NEW_SUCCESS:
                   /* Intent intent = new Intent(NewAddressActivity.this,AddressActivity.class);
                    intent.putExtra("new_address", addressBean);
                    startActivity(intent);*/
                    Intent intent = new Intent();
                    intent.putExtra("data_return","OK");
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                case EDIT_SUCCESS:
                    Intent intent1 = new Intent();

                    intent1.putExtra("data_return","OK");
                    setResult(RESULT_OK,intent1);
                    Log.d("return","edit");
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaddress);

        handleType = getIntent().getIntExtra("from",0);
        switch (handleType){
            case 1:
                handleType = 1;
                firstEditData = false;
                initNewAddressView();
                break;
            case 2:
                handleType = 2;
                firstEditData = true;
                initNewAddressView();
                goodsAddressBean = (GoodsAddressBean.ResultBean)getIntent().getSerializableExtra("address_info");
                newReceiverName.setText(goodsAddressBean.getReceiver());
                newReceiverTel.setText(goodsAddressBean.getPhone());
                String position = goodsAddressBean.getProvince() + "|" + goodsAddressBean.getCity() + "|" + goodsAddressBean.getCountyTown();
                selectNewAddress.setText(position);
                newConcreteAddress.setText(goodsAddressBean.getDetailsAddress());
                setDefaultAddress.setChecked(goodsAddressBean.isDefaultAddress());
                break;
            default:
                break;

        }
        initNewAddressView();

    }

    public void initNewAddressView(){

        newAddressView = getLayoutInflater().from(this).inflate(R.layout.activity_newaddress,null);
        newReceiverName = (EditText)findViewById(R.id.et_receiver_name);
        newReceiverTel = (EditText)findViewById(R.id.et_receiver_tel);
        selectNewAddress = (TextView)findViewById(R.id.tv_receiver_position);
        newConcreteAddress = (EditText)findViewById(R.id.et_receiver_concrete_position);
        setDefaultAddress = (CheckBox)findViewById(R.id.chk_set_default_address);

        selectNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewAddressActivity.this, ProvinceInfoActivity.class);
                startActivity(intent);
            }
        });

        saveNewAddress = (TextView)findViewById(R.id.tv_save_newAddress);
        saveNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addressBean = new GoodsAddressBean.ResultBean();
                        if(handleType == 2){
                            addressBean.setId(goodsAddressBean.getId());
                        }
                        addressBean.setReceiver(newReceiverName.getText().toString());
                        addressBean.setPhone(newReceiverTel.getText().toString());
                        String position = selectNewAddress.getText().toString();
                        String[] array = position.split("\\|");
                        addressBean.setProvince(array[0]);
                        Log.d("province",array[0]);
                        addressBean.setCity(array[1]);
                        Log.d("province",array[1]);
                        addressBean.setCountyTown(array[2]);
                        Log.d("province",array[2]);
                        addressBean.setStreet("1");
                        addressBean.setDetailsAddress(newConcreteAddress.getText().toString());
                        addressBean.setDefaultAddress(setDefaultAddress.isChecked());
                        try {
                            if(handleType == 1){   //创建新地址
                                if(new ElcModel(newAddressView.getContext()).postAddressInfo("2",addressBean)){
                                    Message message = new Message();
                                    message.what = NEW_SUCCESS;
                                    handler.sendMessage(message);
                                }
                            }else{    //编辑地址
                                if(new ElcModel(newAddressView.getContext()).postEditAddressInfo("2",addressBean)){
                                    Message message = new Message();
                                    message.what = EDIT_SUCCESS;
                                    handler.sendMessage(message);
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onResume(){     //获取用户新添加的地址（省/市/区）
        super.onResume();

        if( !firstEditData){   //创建地址
            SharedPreferences sharedPreferences = getSharedPreferences("position_data",MODE_PRIVATE);
            String address = sharedPreferences.getString("position","请选择 >");
            selectNewAddress.setText(address);
            sharedPreferences.edit().putString("position","请选择 >").commit();
        }else{
            firstEditData = false;
        }


    }






}
