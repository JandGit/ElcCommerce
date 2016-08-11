package com.android.tkengine.elccommerce.presenter;import android.content.Context;import android.support.v4.view.PagerAdapter;import android.support.v4.view.ViewPager;import android.support.v7.app.AppCompatActivity;import android.support.v7.widget.RecyclerView;import android.util.Log;import android.util.SparseArray;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ImageView;import com.android.tkengine.elccommerce.R;import com.android.tkengine.elccommerce.beans.DisItemBean;import com.squareup.picasso.Picasso;import java.util.List;/** * Created by FangYu on 2016/8/11. */public class DisplayPresenter {    public void clickBack(final AppCompatActivity activity , View v){        v.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                activity.finish();            }        });    }    public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.MyViewHolder>{        List<DisItemBean> mData;        Context mContext;        LayoutInflater mInflater;        public HomepageAdapter(List<DisItemBean> mData, Context context) {            this.mData = mData;            this.mContext = context;            mInflater = LayoutInflater.from(mContext);        }        @Override        public int getItemViewType(int position) {            return mData.get(position).type;        }        @Override        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {            MyViewHolder holder = null;            switch (viewType){                case DisItemBean.TYPE_ITEM1:                    holder = new MyViewHolder(mInflater.inflate(R.layout.display_item1, parent, false));                    break;                case DisItemBean.TYPE_ITEM2:                    holder = new MyViewHolder(mInflater.inflate(R.layout.display_item2, parent, false));                    break;                case DisItemBean.TYPE_ITEM3:                    holder = new MyViewHolder(mInflater.inflate(R.layout.display_item3, parent, false));                    break;                default:                    Log.e("MyViewHodler", "未知的DisItemBean类型，错误！");                    holder = null;            }            return holder;        }        @Override        public void onBindViewHolder(MyViewHolder holder, int position) {            DisItemBean data = mData.get(position);            switch (holder.getItemViewType()){                case DisItemBean.TYPE_ITEM1:                    ViewPager vp = (ViewPager) holder.getView(R.id.goods_pager);                    final int[] imgsId = (int[]) data.data.get("goods");                    vp.setAdapter(new PagerAdapter() {                        @Override                        public Object instantiateItem(ViewGroup container, int position) {                            ImageView iv = new ImageView(mContext);                            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,                                    ViewGroup.LayoutParams.MATCH_PARENT));                            Picasso.with(mContext).load(imgsId[position]).fit().into(iv);                            container.addView(iv);                            return iv;                        }                        @Override                        public void destroyItem(ViewGroup container, int position, Object object) {                            container.removeView((View)object);                        }                        @Override                        public int getCount() {                            return 4;                        }                        @Override                        public boolean isViewFromObject(View view, Object object) {                            return view == object;                        }                    });                    break;                case DisItemBean.TYPE_ITEM2:                    break;                case DisItemBean.TYPE_ITEM3:                    final int[] content = (int[]) data.data.get("content");                    ImageView imageView = (ImageView) holder.getView(R.id.goods_content_image);                    Picasso.with(mContext).load(content[position]).fit().into(imageView);                    break;            }        }        @Override        public int getItemCount() {            return mData.size();        }        public class MyViewHolder extends RecyclerView.ViewHolder{            SparseArray<View> allViews = new SparseArray<>();            View convertView;            public MyViewHolder(View itemView) {                super(itemView);                convertView = itemView;            }            //获得id对应的view            public View getView(int id){                View view = allViews.get(id);                if(null == view){                    view = convertView.findViewById(id);                    allViews.put(id, view);                }                return view;            }        }    }}