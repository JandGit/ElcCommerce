package com.android.tkengine.elccommerce.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;

/**
 * Created by 陈嘉shuo on 2016/9/10.
 */
public class MyCommentsActivity extends AppCompatActivity{

    ImageView back;
    TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomments);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.title_back);
        name = (TextView) findViewById(R.id.title_name);

        name.setText("我的评论");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
