package com.android.tkengine.elccommerce.presenter;import android.support.v4.view.PagerAdapter;import android.support.v7.app.AppCompatActivity;import android.content.Context;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ImageView;import com.android.tkengine.elccommerce.R;import com.squareup.picasso.Picasso;import java.util.List;/** * Created by FangYu on 2016/8/10. */public class DisplayPresenter {    public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> {        private LayoutInflater mInflater;        private Context mContext;        private List<Integer> mDatas;        public MyRecycleViewAdapter(Context context, List<Integer> data){            this.mContext = context;            this.mDatas = data;            mInflater = LayoutInflater.from(context);        }        @Override        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {            View view = mInflater.inflate(R.layout.display_item , parent, false);            MyViewHolder myViewHolder = new MyViewHolder(view);            return myViewHolder;        }        @Override        public void onBindViewHolder(MyViewHolder holder, int position) {            Picasso.with(mContext).load(mDatas.get(position)).fit().into(holder.content);        }        @Override        public int getItemCount() {            return mDatas.size();        }    }    class MyViewHolder extends RecyclerView.ViewHolder {       ImageView content;        public MyViewHolder(View itemView) {            super(itemView);            content = (ImageView) itemView.findViewById(R.id.goods_content_image);        }    }    public void clickBack(final AppCompatActivity activity , View v){        v.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                activity.finish();            }        });    }    public class MyGoodsDisAdapter extends PagerAdapter {        private List<ImageView> mList;        public MyGoodsDisAdapter(List<ImageView> mList){            this.mList = mList;        }        @Override        public int getCount() {            return mList.size();        }        @Override        public boolean isViewFromObject(View view, Object object) {            return view == object;        }        @Override        public Object instantiateItem(ViewGroup container, int position) {            container.addView(mList.get(position % mList.size()));            return mList.get(position% mList.size());        }        @Override        public void destroyItem(ViewGroup container, int position, Object object) {            container.removeView(mList.get(position% mList.size()));        }    }}