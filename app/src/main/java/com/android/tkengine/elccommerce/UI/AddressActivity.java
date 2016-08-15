package com.android.tkengine.elccommerce.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.GoodsAddressBean;
import com.android.tkengine.elccommerce.presenter.AddressPresenter;
import com.android.tkengine.elccommerce.utils.CallbackActivityListener;

/**
 * Created by 陈嘉shuo on 2016/8/12.
 */
public class AddressActivity extends AppCompatActivity {


    private TextView newAddress;   //添加地址
    private AddressPresenter addressPresenter;
    private RecyclerView addressRecycleView;
    private int handleType = 0;    //判断来自哪个Activity，以便对返回值做处理


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        handleType = getIntent().getIntExtra("from",0);


        initAddressView();

    }

    private void initAddressView(){
        newAddress = (TextView)findViewById(R.id.tv_add_newAddress);
        newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this,NewAddressActivity.class);
                intent.putExtra("from",1);
                startActivityForResult(intent,1);
            }
        });
        addressRecycleView = (RecyclerView)findViewById(R.id.rv_address);
        addressPresenter = new AddressPresenter(this);
        addressRecycleView.setAdapter(addressPresenter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        addressRecycleView.setLayoutManager(linearLayoutManager);
        addressPresenter.setCallbackListener( new CallbackActivityListener(){
            public void CallbackActivity(GoodsAddressBean.ResultBean goodsAddressBean){

                    Intent intent = new Intent(AddressActivity.this,NewAddressActivity.class);
                    intent.putExtra("from",2);
                    intent.putExtra("address_info",goodsAddressBean);
                    startActivityForResult(intent,2);

            }

            public void CallbackActivityResult(RecyclerView.ViewHolder holder){
                if(handleType == 1){
                    Intent intent = new Intent();
                    intent.putExtra("receiver_name",((AddressPresenter.AddressViewHolder) holder).receiverName.getText().toString());
                    intent.putExtra("receiver_tel",((AddressPresenter.AddressViewHolder) holder).receiverTel.getText().toString());
                    intent.putExtra("receiver_address",((AddressPresenter.AddressViewHolder) holder).receiverAddress.getText().toString());
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }

        });


    }



   @Override
   protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    addressPresenter.initGoodsAddressList();
                }
                break;
            case 2:
                if(requestCode == RESULT_OK){
                    Log.d("return","ok");
                    addressPresenter.initGoodsAddressList();
                    addressPresenter.notifyDataSetChanged();
                }
            default:
                break;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        addressPresenter.initGoodsAddressList();
    }




}

