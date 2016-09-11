package com.android.tkengine.elccommerce.UI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.presenter.StoreFrgPresenter;
import com.android.tkengine.elccommerce.utils.StoreRefresh;

public class ShopFragment extends Fragment implements StoreRefresh{

    View mView;
    RecyclerView recyclerView;
    TextView all, grade, sales, tv_net;
    StoreFrgPresenter presenter;
    StoreFrgPresenter.MyRecycleViewAdapter adapter;
    String type = "";
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView iv_net;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_shop, container, false);
        recyclerView = (RecyclerView) mView.findViewById(R.id.shop_rlv);
        all = (TextView) mView.findViewById(R.id.store_list_add_all);
        grade = (TextView) mView.findViewById(R.id.store_list_by_grade);
        sales = (TextView) mView.findViewById(R.id.store_list_by_sales);
        tv_net = (TextView) mView.findViewById(R.id.shop_tv_networkUnconnected);
        iv_net = (ImageView) mView.findViewById(R.id.shop_networkUnconnected);


//        if(isNetWorkConnected(getContext())){
//            noNet.setVisibility(View.INVISIBLE);
//        }else {
//            noNet.setVisibility(View.VISIBLE);
//        }

        presenter = new StoreFrgPresenter();
        presenter.setmStoreRefresh(this);
        adapter = presenter.new MyRecycleViewAdapter(getContext(), type);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.store_fresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter = presenter.new MyRecycleViewAdapter(getContext(), type);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "";
                all.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                all.setTextColor(getResources().getColor(R.color.colorWhite));
                grade.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                grade.setTextColor(getResources().getColor(R.color.colorBlack));
                sales.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                sales.setTextColor(getResources().getColor(R.color.colorBlack));
                adapter = presenter.new MyRecycleViewAdapter(getContext(), type);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
            }
        });
        grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "grade";
                grade.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                grade.setTextColor(getResources().getColor(R.color.colorWhite));
                all.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                all.setTextColor(getResources().getColor(R.color.colorBlack));
                sales.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                sales.setTextColor(getResources().getColor(R.color.colorBlack));
                adapter = presenter.new MyRecycleViewAdapter(getContext(), type);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "sales";
                sales.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                sales.setTextColor(getResources().getColor(R.color.colorWhite));
                grade.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                grade.setTextColor(getResources().getColor(R.color.colorBlack));
                all.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                all.setTextColor(getResources().getColor(R.color.colorBlack));
                adapter = presenter.new MyRecycleViewAdapter(getContext(), type);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        return mView;
    }

    public boolean isNetWorkConnected(Context context){
        if(context != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    public void showViewStub() {
        adapter.notifyDataSetChanged();
        tv_net.setVisibility(View.VISIBLE);
        iv_net.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showData() {
        tv_net.setVisibility(View.GONE);
        iv_net.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }


}
