package com.android.tkengine.elccommerce.presenter;import android.content.Context;import android.content.Intent;import android.os.Handler;import android.os.Message;import android.support.v4.view.PagerAdapter;import android.support.v4.view.ViewPager;import android.support.v7.app.AppCompatActivity;import android.support.v7.widget.RecyclerView;import android.util.Log;import android.util.SparseArray;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.Button;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.RatingBar;import android.widget.RelativeLayout;import android.widget.TextView;import com.android.tkengine.elccommerce.R;import com.android.tkengine.elccommerce.UI.CommentsActivity;import com.android.tkengine.elccommerce.UI.StoreDetailsActivity;import com.android.tkengine.elccommerce.beans.GoodsDetailsBean;import com.android.tkengine.elccommerce.model.ElcModel;import com.android.tkengine.elccommerce.utils.CircleImageView;import com.android.tkengine.elccommerce.utils.Indicator;import com.android.tkengine.elccommerce.utils.MyIndicator;import com.squareup.picasso.Picasso;import java.util.ArrayList;import java.util.List;/** * Created by FangYu on 2016/8/11. */public class DisplayPresenter {    public static GoodsDetailsBean mData ;    public void clickBack(final AppCompatActivity activity , View v){        v.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                switch (view.getId()){                    case R.id.title_back:                        activity.finish();                        break;                    case R.id.title_message:                        break;                }            }        });    }    public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.MyViewHolder>{        Context mContext;        LayoutInflater mInflater;        String id;        private static final int TYPE_ITEM1 = 1;        private static final int TYPE_ITEM2 = 2;        private static final int TYPE_ITEM3 = 3;        private static final int GET_SUCCESS = 4;        private Handler handler = new Handler(){            @Override            public void handleMessage(Message msg) {                switch (msg.what){                    case GET_SUCCESS:                        notifyDataSetChanged();                        break;                    default:                        break;                }}};        public DisplayAdapter( String id, Context context) {            this.id = id;            this.mContext = context;            mInflater = LayoutInflater.from(mContext);            initData();        }        private void initData() {            new Thread(new Runnable() {                @Override                public void run() {                    try{                        mData = new GoodsDetailsBean();                        mData = new ElcModel(mContext).getGoodsDetails(id);                        Message message = new Message();                        message.what = GET_SUCCESS;                        handler.sendMessage(message);                    }catch (Exception e){                        e.printStackTrace();                    }                }            }).start();        }        @Override        public int getItemViewType(int position) {            if(position == 0)                return TYPE_ITEM1;            else if (position == 1)                return TYPE_ITEM2;            else                return TYPE_ITEM3;        }        @Override        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {            MyViewHolder holder = null;            switch (viewType){                case TYPE_ITEM1:                    holder = new MyViewHolder(mInflater.inflate(R.layout.display_item1, parent, false));                    break;                case TYPE_ITEM2:                    holder = new MyViewHolder(mInflater.inflate(R.layout.display_item2, parent, false));                    break;                case TYPE_ITEM3:                    holder = new MyViewHolder(mInflater.inflate(R.layout.display_item3, parent, false));                    break;            }            return holder;        }        @Override        public void onBindViewHolder(MyViewHolder holder, int position) {            switch (holder.getItemViewType()){                case TYPE_ITEM1:                    ViewPager vp = (ViewPager) holder.getView(R.id.goods_pager);                    LinearLayout indicator = (LinearLayout) holder.getView(R.id.bottom_indicator);                    vp.setAdapter(new PagerAdapter() {                        @Override                        public Object instantiateItem(ViewGroup container, int position) {                            ImageView iv = new ImageView(mContext);                            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,                                    ViewGroup.LayoutParams.MATCH_PARENT));                            if(mData.getPictures_url() != null && mData.getDetail_pictures_url().size() != 0){                                for(int i = 0; i < mData.getPictures_url().size(); i++){                                    Picasso.with(mContext).load(mData.getPictures_url().get(position).getTiny_picture_url()).fit().into(iv);                                }                            }                            container.addView(iv);                            return iv;                        }                        @Override                        public void destroyItem(ViewGroup container, int position, Object object) {                            container.removeView((View)object);                        }                        @Override                        public int getCount() {                            return 5;                        }                        @Override                        public boolean isViewFromObject(View view, Object object) {                            return view == object;                        }                    });                    if(mData.getPictures_url() != null){                        new MyIndicator().setUpViewPager(vp, indicator, mContext, mData.getPictures_url().size(), 2);                    }                    break;                case TYPE_ITEM2:                    Button allComments = (Button) holder.getView(R.id.all_evaluation);                    Button gotoStore = (Button) holder.getView(R.id.all_goods);                    allComments.setOnClickListener(new View.OnClickListener() {                        @Override                        public void onClick(View view) {                            Intent intent = new Intent(mContext, CommentsActivity.class);                            intent.putExtra("productId", mData.getProduct_id());                            mContext.startActivity(intent);                        }                    });                    gotoStore.setOnClickListener(new View.OnClickListener() {                        @Override                        public void onClick(View view) {                            if(mData.getProduct_seller() != null){                                Intent intent = new Intent(mContext, StoreDetailsActivity.class);                                intent.putExtra("storeID", mData.getProduct_seller().getId());                                mContext.startActivity(intent);                            }                        }                    });                    TextView goodsName= (TextView) holder.getView(R.id.goods_name);                    RelativeLayout commentYes = (RelativeLayout) holder.getView(R.id.comment_yes);                    RelativeLayout commentNo = (RelativeLayout) holder.getView(R.id.comment_no);                    TextView goodsPrice= (TextView) holder.getView(R.id.goods_price);                    TextView eName= (TextView) holder.getView(R.id.goods_evaluation_name);                    TextView goodsSales= (TextView) holder.getView(R.id.goods_sales);                    TextView goodsArea= (TextView) holder.getView(R.id.goods_area);                    TextView eContent= (TextView) holder.getView(R.id.goods_evaluation_content);                    TextView storeName= (TextView) holder.getView(R.id.goods_store_name);                    TextView goodsMessage= (TextView) holder.getView(R.id.goods_message);                    TextView eTime= (TextView) holder.getView(R.id.goods_evaluation_time);                    CircleImageView eIcon = (CircleImageView) holder.getView(R.id.goods_evaluation_icon);                    ImageView storeIcon = (ImageView) holder.getView(R.id.goods_store_icon);                    RatingBar goodsGrade = (RatingBar) holder.getView(R.id.goods_grade);                    RatingBar storeGrade = (RatingBar) holder.getView(R.id.goods_store_grade);                    goodsName.setText(mData.getProduct_name());                    goodsPrice.setText(mData.getProduct_price()+"");                    if(mData.getProduct_comment() != null && mData.getProduct_comment().getId() != ""){                        eName.setText(mData.getProduct_comment().getUsername());                        eContent.setText(mData.getProduct_comment().getComment());                        eTime.setText(mData.getProduct_comment().getDate());                        goodsGrade.setProgress(Integer.parseInt(mData.getProduct_comment().getGrade()));                        Picasso.with(mContext).load(mData.getProduct_comment().getUserPicture()).fit().into(eIcon);                    }else if(mData.getProduct_comment() == null || mData.getProduct_comment().getId() == ""){                        commentYes.setVisibility(View.GONE);                        commentNo.setVisibility(View.VISIBLE);                    }                    if(mData.getProduct_seller() != null){                        storeName.setText(mData.getProduct_seller().getShopName());                        storeGrade.setProgress(mData.getProduct_seller().getGrade());                        Picasso.with(mContext).load(mData.getProduct_seller().getShopPicture()).fit().into(storeIcon);                    }                    goodsSales.setText("月销"+mData.getProduct_sales()+"笔");                    goodsArea.setText(mData.getProduct_city());                    goodsMessage.setText(mData.getProduct_description());                    break;                case TYPE_ITEM3:                    if(mData.getDetail_pictures_url() != null){                        ImageView imageView1 = (ImageView) holder.getView(R.id.goods_content_image1);                        Picasso.with(mContext).load( mData.getDetail_pictures_url().get(position-2).getDetail_picture_url()).fit().into(imageView1);                    }                    break;            }        }        @Override        public int getItemCount() {            if(mData.getDetail_pictures_url() != null){                return mData.getDetail_pictures_url().size() + 2;            }            return 0;        }        public class MyViewHolder extends RecyclerView.ViewHolder{            SparseArray<View> allViews = new SparseArray<>();            View convertView;            public MyViewHolder(View itemView) {                super(itemView);                convertView = itemView;            }            //获得id对应的view            public View getView(int id){                View view = allViews.get(id);                if(null == view){                    view = convertView.findViewById(id);                    allViews.put(id, view);                }                return view;            }        }    }}