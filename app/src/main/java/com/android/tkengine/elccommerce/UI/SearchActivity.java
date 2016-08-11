package com.android.tkengine.elccommerce.UI;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import com.android.tkengine.elccommerce.R;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/11.
 */
public class SearchActivity extends AppCompatActivity {
    private SearchView fruitSearchView;
    private ListView fruitListView;
    private final String[] mStrings = {"aaaa", "aa", "aabbcc", "aabbdd"};
    private ExpandableListAdapter priceOrderAdapter;
    private ExpandableListAdapter priceSectioonAdapter;

    private  List<String> groupArray;
    private  List<List<String>> childArray;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

}
