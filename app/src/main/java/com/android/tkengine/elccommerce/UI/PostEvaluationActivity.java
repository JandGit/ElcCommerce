package com.android.tkengine.elccommerce.UI;import android.content.Context;import android.os.Bundle;import android.os.Handler;import android.os.Message;import android.support.annotation.Nullable;import android.support.v7.app.AppCompatActivity;import android.view.View;import android.widget.EditText;import android.widget.ImageView;import android.widget.RatingBar;import android.widget.TextView;import android.widget.Toast;import com.android.tkengine.elccommerce.R;import com.android.tkengine.elccommerce.model.ElcModel;import com.squareup.picasso.Picasso;/** * Created by FangYu on 2016/9/11. */public class PostEvaluationActivity extends AppCompatActivity implements View.OnClickListener{    ImageView imageView, back;    EditText comments;    TextView name, post;    RatingBar grade;    private static final int ADD_SUCCESS = 1;    private static final int ADD_UNSUCCESS = 2;    final Handler handler = new Handler(){        @Override        public void handleMessage(Message msg) {            switch (msg.what){                case ADD_SUCCESS:                    Toast.makeText(PostEvaluationActivity.this, "提交成功", Toast.LENGTH_SHORT).show();                    finish();                    break;                case ADD_UNSUCCESS:                    Toast.makeText(PostEvaluationActivity.this, "提交失败", Toast.LENGTH_SHORT).show();                    break;            }}};    @Override    protected void onCreate(@Nullable Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_evaluation);        initView();    }    private void initView() {        imageView = (ImageView) findViewById(R.id.evaluation_imageView);        back = (ImageView) findViewById(R.id.title_back);        comments = (EditText) findViewById(R.id.evaluation_content);        name = (TextView) findViewById(R.id.title_name);        post = (TextView) findViewById(R.id.evaluation_post);        grade = (RatingBar) findViewById(R.id.evaluation_ratingBar);        back.setOnClickListener(this);        name.setText("发表评价");        Picasso.with(PostEvaluationActivity.this).load(getIntent().getStringExtra("image")).fit().into(imageView);        post.setOnClickListener(this);    }    @Override    public void onClick(View view) {        switch (view.getId()){            case R.id.title_back:                finish();                break;            case R.id.evaluation_post:                postEvaluation();                break;        }    }    private void postEvaluation() {        new Thread(new Runnable() {            @Override            public void run() {                try{                    boolean result  = new ElcModel(PostEvaluationActivity.this).postEvaluation(                            getIntent().getStringExtra("ID"), comments.getText().toString(), grade.getRating() + "");                    if(result == true){                        Message message = new Message();                        message.what = ADD_SUCCESS;                        handler.sendMessage(message);                    }else {                        Message message = new Message();                        message.what = ADD_UNSUCCESS;                        handler.sendMessage(message);                    }                }catch (Exception e){                    e.printStackTrace();                }            }        }).start();    }}