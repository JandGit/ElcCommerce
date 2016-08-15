package com.android.tkengine.elccommerce.UI;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.presenter.SearchPresenter;
import com.android.tkengine.elccommerce.utils.DividerItemDecoration;

/**
 * Created by 陈嘉shuo on 2016/8/11.
 */
public class SearchActivity extends AppCompatActivity {

    private int currentPage = 1;   //当前页，用于从服务器加载搜索结果
    public int lastVisibleItem = 0;  //判断是否滑到底部，加载更多数据
    private View searchView;
    private ArrayAdapter<String> searchAdapter;
    private AutoCompleteTextView autoMatchSearch;    //自动匹配历史记录
    private Button search;    //搜索按钮
    private ExpandableListView priceSortListView;    //价格排序
    private ExpandableListView priceSectionAListView;    //价格区间选择
    private TextView saleSort;   //销量优先
    private RecyclerView searchRecyclerView;
    private SearchPresenter searchPresenter;
    private String[] historyRecord  = new String[]{"abc", "abcd", "abcde","abcdef", "abcdefg", "hij", "hijk","hijkl","hijklm","hijklmn"};
    private String lowPrice = "1",highPrice = "100000000";  //保存高低价位信息
    private String priceSortType = "";  // "price" 表示从低到高，"price desc" 表示从高到低，"sales"表示销量优先，""空字符串默认不排序



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initSearch();
    }


    private void initSearch(){
        searchView = getLayoutInflater().from(this).inflate(R.layout.activity_search,null);

        //搜索框
        autoMatchSearch = (AutoCompleteTextView)findViewById(R.id.autotv_search);
        search = (Button)findViewById(R.id.btn_search);
        searchAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,historyRecord);
        autoMatchSearch.setAdapter(searchAdapter);
        autoMatchSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });

        //搜索要求
        priceSortListView = (ExpandableListView)findViewById(R.id.elv_priceSort);
        priceSortListView.setAdapter(new PriceExpandableListAdapter(new String[]{"价格排序"},new String[][]{{"从低到高","从高到低"}}));
        priceSectionAListView = (ExpandableListView)findViewById(R.id.elv_priceSection);
        priceSectionAListView.setAdapter(new PriceExpandableListAdapter(new String[]{"价位选择"},new String[][]{{"1--50","50--100","100--200","200以上","默认"}}));



        //搜索显示列表
        searchRecyclerView = (RecyclerView)findViewById(R.id.rv_search);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(searchView.getContext());
        searchRecyclerView.setLayoutManager(linearLayoutManager);
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(searchView.getContext(),LinearLayoutManager.VERTICAL));
        searchPresenter = new SearchPresenter(searchView.getContext());
        searchRecyclerView.setAdapter(searchPresenter.goodsRecycleViewAdapter);
        searchRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {     //上拉加载
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {     //判断是否滑到底部
                super.onScrollStateChanged(recyclerView, newState);
                if (lastVisibleItem + 1 ==searchPresenter.goodsRecycleViewAdapter.getItemCount()) {
                    currentPage = currentPage + 1;
                    searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"","1","100000000",currentPage,5);
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
                searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"","1","100000000",1,5);
            }
        });

        //销量优先
        saleSort = (TextView)findViewById(R.id.tv_saleSort);
        saleSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceSortType = "sales";
                searchPresenter.searchGoodsList.clear();
                currentPage = 1;
                searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"sales","1","100000000",1,5);
            }
        });



    }


    class PriceExpandableListAdapter implements ExpandableListAdapter{

        private String[] category;
        private String[][] subCategory;

        public PriceExpandableListAdapter(String[] category,String[][] subCategory){
            this.category = category;
            this.subCategory = subCategory;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getGroupCount() {
            return category.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return subCategory[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return category[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return subCategory[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            LinearLayout ll = new LinearLayout(SearchActivity.this);
            TextView textView = new TextView(SearchActivity.this);
            textView.setText(getGroup(i).toString());
            ll.addView(textView);
            return ll;
        }

        @Override
        public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(SearchActivity.this);
            textView.setText(getChild(i,i1).toString());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("search","ok");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {    //判断用户进行哪种排序
                                    if ((getChild(i, i1).toString()).equalsIgnoreCase("从低到高")){
                                        priceSortType = "price";
                                        searchPresenter.searchGoodsList.clear();
                                        currentPage = 1;
                                        searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"price",lowPrice,highPrice,1,5);
                                    }else if((getChild(i, i1).toString()).equalsIgnoreCase("从高到低")){
                                        priceSortType = "price desc";
                                        searchPresenter.searchGoodsList.clear();
                                        currentPage = 1;
                                        searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),"price desc",lowPrice,highPrice,1,5);
                                    }else {
                                        if((getChild(i, i1).toString()).equalsIgnoreCase("200以上")){
                                            searchPresenter.searchGoodsList.clear();
                                            currentPage = 1;
                                            searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),priceSortType,"200","100000000",1,10);
                                        }else if((getChild(i, i1).toString()).equalsIgnoreCase("默认")){
                                            lowPrice = "1";
                                            highPrice = "1000000000";
                                            searchPresenter.searchGoodsList.clear();
                                            currentPage = 1;
                                            searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),priceSortType,"1","100000000",1,10);
                                        }else{
                                            String array[] = (getChild(i, i1).toString()).split("--");
                                            lowPrice = array[0];
                                            highPrice = array[1];
                                            searchPresenter.searchGoodsList.clear();
                                            currentPage = 1;
                                            searchPresenter.getGoodsList(autoMatchSearch.getText().toString(),priceSortType,lowPrice,highPrice,1,10);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    Toast.makeText(SearchActivity.this,"点击成功",Toast.LENGTH_SHORT).show();
                }
            });
            return textView;

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int i) {

        }

        @Override
        public void onGroupCollapsed(int i) {

        }

        @Override
        public long getCombinedChildId(long l, long l1) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long l) {
            return 0;
        }

     /*   private TextView getTextView() {
            TextView textView = new TextView(SearchActivity.this);
           textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            textView.setPadding(5, 0, 0, 0);
            textView.setTextSize(20);
            return textView;
        }*/



    }


}
