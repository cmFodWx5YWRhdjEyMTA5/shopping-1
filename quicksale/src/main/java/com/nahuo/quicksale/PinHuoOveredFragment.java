package com.nahuo.quicksale;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nahuo.quicksale.adapter.PinHuoForecastAdapter;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.controls.refreshlayout.IRefreshLayout.RefreshCallBack;
import com.nahuo.quicksale.controls.refreshlayout.SwipeRefreshLayoutEx;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.PinHuoModel;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 往期拼货
 * Created by pj
 */
public class PinHuoOveredFragment extends BaseFragment implements RefreshCallBack{

    private ListView mListView;
    private PinHuoForecastAdapter mAdapter;
    private static final String EXTRA_OVERLIST_DATA = "EXTRA_OVERLIST_DATA";
    private ArrayList<PinHuoModel> mListData;
    private SwipeRefreshLayoutEx mRefreshLayout;
    public PinHuoOveredFragment(){

    }
    public static PinHuoOveredFragment getInstance(ArrayList<PinHuoModel> overList){
        PinHuoOveredFragment fragment = new PinHuoOveredFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_OVERLIST_DATA, overList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.frgm_pin_huo_overed, container, false);
        initViews();
        initData();
        return mContentView;
    }

    private void initData() {
        ArrayList<PinHuoModel> mOverList = (ArrayList<PinHuoModel>) getArguments().getSerializable(EXTRA_OVERLIST_DATA);
        for (PinHuoModel pm : mOverList)
        {
            pm.isOvered = true;
        }
        mListData = mOverList;
        if(!ListUtils.isEmpty(mListData)){
            mAdapter.setData(mListData);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initViews() {
        mRefreshLayout = $(R.id.refresh_layout);
        mRefreshLayout.setCallBack(this);
        mListView = $(R.id.listview);
        mAdapter = new PinHuoForecastAdapter(mActivity);
        mListView.setAdapter(mAdapter);
    }

    public void onEventMainThread(BusEvent event){
        switch (event.id){
            case EventBusId.LOAD_PIN_HUO_FINISHED:
                mRefreshLayout.completeRefresh();
                break;
        }
    }
    @Override
    public void onRefresh() {
        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_PIN_HUO));
    }

    @Override
    public void onLoadMore() {

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
