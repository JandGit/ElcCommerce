package com.android.tkengine.elccommerce.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.presenter.StoreFrgPresenter;

public class ShopFragment extends Fragment {

    View mView;
    RecyclerView recyclerView;
    StoreFrgPresenter presenter;
    StoreFrgPresenter.MyRecycleViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_shop, container, false);
        recyclerView = (RecyclerView) mView.findViewById(R.id.shop_rlv);
        presenter = new StoreFrgPresenter();
        adapter = presenter.new MyRecycleViewAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        return mView;
    }
}
