package com.android.tkengine.elccommerce.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.presenter.StoreFrgPresenter;

public class ShopFragment extends Fragment {

    View mView;
    RecyclerView recyclerView;
    TextView all, grade, sales;
    StoreFrgPresenter presenter;
    StoreFrgPresenter.MyRecycleViewAdapter adapter;
    String type = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_shop, container, false);
        recyclerView = (RecyclerView) mView.findViewById(R.id.shop_rlv);
        all = (TextView) mView.findViewById(R.id.store_list_add_all);
        grade = (TextView) mView.findViewById(R.id.store_list_by_grade);
        sales = (TextView) mView.findViewById(R.id.store_list_by_sales);
        presenter = new StoreFrgPresenter();
        adapter = presenter.new MyRecycleViewAdapter(getContext(), type);

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
}
