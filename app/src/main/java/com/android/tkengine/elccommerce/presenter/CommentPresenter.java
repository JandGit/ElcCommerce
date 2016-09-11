package com.android.tkengine.elccommerce.presenter;import android.content.Context;import android.os.Handler;import android.os.Message;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.RatingBar;import android.widget.TextView;import com.android.tkengine.elccommerce.R;import com.android.tkengine.elccommerce.beans.commentsBean;import com.android.tkengine.elccommerce.model.ElcModel;import com.android.tkengine.elccommerce.utils.CircleImageView;import com.squareup.picasso.Picasso;import java.util.List;/** * Created by FangYu on 2016/8/12. */public class CommentPresenter {    public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> {        private LayoutInflater mInflater;        private Context mContext;        private List<commentsBean.ResultBean> mDatas;        private static final int GET_SUCCESS = 1;        String productID;        private class MyHandler extends Handler{            @Override            public void handleMessage(Message msg) {                switch (msg.what){                    case GET_SUCCESS:                        notifyDataSetChanged();                        break;                    default:                        break;                }}        }        private MyHandler handler = new MyHandler();        public MyRecycleViewAdapter(Context context, String productID){            this.productID = productID;            this.mContext = context;            mInflater = LayoutInflater.from(context);            initData();        }        private void initData() {            new Thread(new Runnable() {                @Override                public void run() {                    try{                        mDatas = new ElcModel(mContext).getCommentsDetails(productID);                        Message message = new Message();                        message.what = GET_SUCCESS;                        handler.sendMessage(message);                    }catch (Exception e){                        e.printStackTrace();                    }                }            }).start();        }        @Override        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {            View view = mInflater.inflate(R.layout.comment_item , parent, false);            MyViewHolder myViewHolder = new MyViewHolder(view);            return myViewHolder;        }        @Override        public void onBindViewHolder(MyViewHolder holder, int position) {            if(mDatas != null && mDatas.get(position).getId() != ""){                holder.name.setText(mDatas.get(position).getUsername());                holder.content.setText(mDatas.get(position).getComment());                holder.time.setText(mDatas.get(position).getDate());                holder.grade.setProgress(Integer.parseInt(mDatas.get(position).getGrade()));                Picasso.with(mContext).load(mDatas.get(position).getUserPicture()).fit().into(holder.icon);            }        }        @Override        public int getItemCount() {            if(mDatas != null){                return mDatas.size();            }            return 0;        }    }    class MyViewHolder extends RecyclerView.ViewHolder {        TextView name, time, content;        CircleImageView icon;        RatingBar grade;        public MyViewHolder(View itemView) {            super(itemView);            name = (TextView) itemView.findViewById(R.id.comment_name);            time = (TextView) itemView.findViewById(R.id.comment_time);            content = (TextView) itemView.findViewById(R.id.comment_content);            icon = (CircleImageView) itemView.findViewById(R.id.comment_icon);            grade = (RatingBar) itemView.findViewById(R.id.comment_grade);        }    }}