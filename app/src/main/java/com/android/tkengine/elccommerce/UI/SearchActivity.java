package com.android.tkengine.elccommerce.UI;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.presenter.SearchPresenter;
import com.android.tkengine.elccommerce.utils.DividerItemDecoration;

import org.w3c.dom.Text;

/**
 * Created by 陈嘉shuo on 2016/8/11.
 */
public class SearchActivity extends AppCompatActivity implements SearchPresenter.callBackListener {

    private int currentPage = 1;   //当前页，用于从服务器加载搜索结果
    public int lastVisibleItem = 0;  //判断是否滑到底部，加载更多数据
    private View searchView;
    private ArrayAdapter<String> searchAdapter;
    private AutoCompleteTextView autoMatchSearch;    //自动匹配历史记录
    private TextView search;    //搜索按钮
    private TextView tip;    //搜索无结果提示
    private TextView saleSort;   //销量优先
    private LinearLayout priceSortLayout;    //价格排序
    private LinearLayout priceSectionLayout;    //价位选择
    private RecyclerView searchRecyclerView;
    private SearchPresenter searchPresenter;
    private String[] historyRecord;
    private PopupWindow priceSortPopupWindow;    //价格排序弹出框
    private PopupWindow priceSectionPopupWindow;   //价位选择弹出框
    private String lowPrice = "0",highPrice = "100000000";  //保存高低价位信息
    private String priceSortType = "";  // "price" 表示从低到高，"price desc" 表示从高到低，"sales"表示销量优先，""空字符串默认不排序



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initSearch();
    }


    private void initSearch(){
        searchView = getLayoutInflater().from(this).inflate(R.layout.activity_search,null);

        tip = (TextView)findViewById(R.id.tv_tip);

        // 获取搜索记录文件内容
        SharedPreferences sp = getSharedPreferences("search_history",MODE_PRIVATE);
        String history = sp.getString("history", "暂时没有搜索记录");
        // 用逗号分割内容返回数组
        historyRecord = history.split(",");


        //搜索框
        autoMatchSearch = (AutoCompleteTextView)findViewById(R.id.autotv_search);
        search = (TextView)findViewById(R.id.btn_search);
        searchAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,historyRecord);
        autoMatchSearch.setAdapter(searchAdapter);

        priceSortLayout = (LinearLayout)findViewById(R.id.ll_priceSort);
        priceSortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cahngeTitleColor(2);
                showPriceSortPopupWindow();
            }
        });
        priceSectionLayout = (LinearLayout)findViewById(R.id.ll_priceSection);
        priceSectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPriceSectiontPopupWindow();
                cahngeTitleColor(3);
            }
        });



        //搜索显示列表
        searchRecyclerView = (RecyclerView)findViewById(R.id.rv_search);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(searchView.getContext());
        searchRecyclerView.setLayoutManager(linearLayoutManager);
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(searchView.getContext(),LinearLayoutManager.VERTICAL));
        searchPresenter = new SearchPresenter(this,searchView.getContext());
        searchRecyclerView.setAdapter(searchPresenter.goodsRecycleViewAdapter);
        searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {     //上拉加载
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {     //判断是否滑到底部
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState && lastVisibleItem + 1 ==searchPresenter.goodsRecycleViewAdapter.getItemCount()) {
                    currentPage = currentPage + 1;
                    searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),priceSortType,lowPrice,highPrice,currentPage,8);
                }
                }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem =linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        //搜索按钮点击事件
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(! TextUtils.isEmpty(autoMatchSearch.getText().toString())){
                     tip.setVisibility(View.GONE);
                     priceSortType = "";
                    lowPrice = "0";
                    highPrice = "100000000";
                    searchPresenter.searchGoodsList.clear();
                    saveSearchRecord(autoMatchSearch.getText().toString());
                    searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"","0","100000000",1,8);
                }else{
                    Toast.makeText(view.getContext(),"请输入关键字搜索",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //销量优先
        saleSort = (TextView)findViewById(R.id.tv_saleSort);
        saleSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceSortType = "sales desc";
                searchPresenter.searchGoodsList.clear();
                currentPage = 1;
                lowPrice = "0";
                highPrice = "100000000";
                searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"sales desc","0","100000000",1,8);
                cahngeTitleColor(1);
            }
        });



    }


    //将输入内容加到历史记录中
    public void saveSearchRecord(String newRecord){
        SharedPreferences pref = getSharedPreferences("search_history",MODE_PRIVATE);
        String history = pref.getString("history","暂时没有搜索记录");

        // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
        if(!history.contains(newRecord)){
            // 保留前50条数据
            if (historyRecord.length == 50) {
               int index = history.lastIndexOf(",");
                String str = history.substring(0,index);
                str = newRecord + "," + str;
                pref.edit().putString("history",str).commit();
            }
            history = newRecord + "," + history;
            pref.edit().putString("history",history).commit();
        }
    }

    //删除所有的历史记录
    public void clearHistory(){
        SharedPreferences pref = getSharedPreferences("search_history",MODE_PRIVATE);
        pref.edit().clear().commit();
    }


    public void showPriceSortPopupWindow(){
        View contentView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_pricesort_popuplayout, null);
        priceSortPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        priceSortPopupWindow.setBackgroundDrawable(new BitmapDrawable());  //点击外部，弹出框消失
        priceSortPopupWindow.setOutsideTouchable(true);
      /*  priceSortPopupWindow.setAnimationStyle(R.style.searchPopupwindow);*/
        priceSortPopupWindow.showAsDropDown(findViewById(R.id.view_position_reference));
        TextView priceUp = (TextView)contentView.findViewById(R.id.tv_priceUp);
        TextView priceDown = (TextView)contentView.findViewById(R.id.tv_priceDown);
        priceUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceSortType = "price";
                searchPresenter.searchGoodsList.clear();
                currentPage = 1;
                lowPrice = "0";
                highPrice = "100000000";
                searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"price","0","100000000",1,8);
                priceSortPopupWindow.dismiss();
            }
        });
        priceDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceSortType = "price desc";
                searchPresenter.searchGoodsList.clear();
                currentPage = 1;
                lowPrice = "0";
                highPrice = "100000000";
                searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"price desc","0","100000000",1,8);
                priceSortPopupWindow.dismiss();
            }
        });
    }

    public void showPriceSectiontPopupWindow(){
        View contentView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_pricesection_poopuplayout, null);
        priceSectionPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        priceSectionPopupWindow.setBackgroundDrawable(new BitmapDrawable());  //点击外部，弹出框消失
        priceSectionPopupWindow.setOutsideTouchable(true);
       /* priceSectionPopupWindow.setAnimationStyle(R.style.searchPopupwindow);*/
        priceSectionPopupWindow.showAsDropDown(findViewById(R.id.view_position_reference));
        final TextView price1 = (TextView)contentView.findViewById(R.id.tv_price1);
        TextView price2 = (TextView)contentView.findViewById(R.id.tv_price2);
        TextView price3 = (TextView)contentView.findViewById(R.id.tv_price3);
        TextView price4 = (TextView)contentView.findViewById(R.id.tv_price4);
        TextView price5 = (TextView)contentView.findViewById(R.id.tv_price5);
        price1.setOnClickListener(new PriceSectionOnClickListener());
        price2.setOnClickListener(new PriceSectionOnClickListener());
        price3.setOnClickListener(new PriceSectionOnClickListener());
        price4.setOnClickListener(new PriceSectionOnClickListener());
        price5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPresenter.searchGoodsList.clear();
                currentPage = 1;
                priceSortType = "";
                lowPrice = "500";
                highPrice = "100000000";
                searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"","500","100000000",1,8);
                priceSectionPopupWindow.dismiss();
            }
        });
    }



    public class  PriceSectionOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String array[] = (((TextView)view).getText().toString()).split("--");
            lowPrice = array[0];
            highPrice = array[1];
            searchPresenter.searchGoodsList.clear();
            currentPage = 1;
            priceSortType = "";
            searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"",lowPrice,highPrice,1,8);
            priceSectionPopupWindow.dismiss();
        }
    }


    private void cahngeTitleColor(int title){
        TextView priceSortText = (TextView)findViewById(R.id.tv_priceSort);
        TextView priceSectionText = (TextView)findViewById(R.id.tv_priceSection);
        ImageView priceSortImg = (ImageView)findViewById(R.id.iv_priceSort);
        ImageView priceSectionImg = (ImageView)findViewById(R.id.iv_priceSection);
        switch (title){
            case 1:
                saleSort.setBackgroundResource(R.color.colorPrimary);
                saleSort.setTextColor(getResources().getColor(R.color.colorWhite));
                priceSortLayout.setBackgroundResource(R.color.colorWhite);
                priceSortText.setTextColor(getResources().getColor(R.color.colorBlack));
                priceSortImg.setImageResource(R.drawable.down_mark);
                priceSectionLayout.setBackgroundResource(R.color.colorWhite);
                priceSectionText.setTextColor(getResources().getColor(R.color.colorBlack));
                priceSectionImg.setImageResource(R.drawable.down_mark);
                break;
            case 2:
                saleSort.setBackgroundResource(R.color.colorWhite);
                saleSort.setTextColor(getResources().getColor(R.color.colorBlack));
                priceSortLayout.setBackgroundResource(R.color.colorPrimary);
                priceSortText.setTextColor(getResources().getColor(R.color.colorWhite));
                priceSortImg.setImageResource(R.drawable.down_mark_1);
                priceSectionLayout.setBackgroundResource(R.color.colorWhite);
                priceSectionText.setTextColor(getResources().getColor(R.color.colorBlack));
                priceSectionImg.setImageResource(R.drawable.down_mark);
                break;
            case 3:
                saleSort.setBackgroundResource(R.color.colorWhite);
                saleSort.setTextColor(getResources().getColor(R.color.colorBlack));
                priceSortLayout.setBackgroundResource(R.color.colorWhite);
                priceSortText.setTextColor(getResources().getColor(R.color.colorBlack));
                priceSortImg.setImageResource(R.drawable.down_mark);
                priceSectionLayout.setBackgroundResource(R.color.colorPrimary);
                priceSectionText.setTextColor(getResources().getColor(R.color.colorWhite));
                priceSectionImg.setImageResource(R.drawable.down_mark_1);
                break;
            default:
                break;

        }

    }


    //点击商品跳转到商品详情页面
    @Override
    public void startDisplayActivity(String productId) {
        Intent intent = new Intent(this,DisplayActivity.class);
        intent.putExtra("productID",productId);
        Log.d("productId",productId);
        startActivity(intent);
    }



}
