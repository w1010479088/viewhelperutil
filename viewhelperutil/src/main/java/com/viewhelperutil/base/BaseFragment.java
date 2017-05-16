package com.viewhelperutil.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewhelperutil.ViewHelper;

/**
 * Fragment基类
 * Created by mac on 16/10/8.
 */
public abstract class BaseFragment extends Fragment{
    protected boolean mIsVisible;
    protected boolean mIsDataLoaded;
    protected ViewHelper mHelper;
    public String TAG = this.getClass().getName();
    protected View mRootView;

    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void init(Bundle savedInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            if (savedInstanceState != null) {
                setUserVisibleHint(true);
            }
            mRootView = inflater.inflate(getLayoutId(), null);
            mHelper = new ViewHelper(mRootView, this);
            initView(savedInstanceState);
            lazyLoad(savedInstanceState);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            if (isNeedRecycled()) {
                lazyLoad(null);
            }
        } else {
            mIsVisible = false;
        }
    }

    protected void lazyLoad(@Nullable Bundle savedInstanceState) {
        if (mRootView == null || (!mIsVisible) && isNeedRecycled()) {
            return;
        }
        if (!mIsDataLoaded) {
            init(savedInstanceState);
        }
    }

    protected boolean isNeedRecycled() {
        return true;
    }

    protected void onLogout(boolean fQuit) {

    }

    protected void register_bus() {
        EventBusUtil.register_bus(this);
    }

    protected void unregister_bus() {
        EventBusUtil.unregister_bus(this);
    }

    public void post_event(Object data) {
        EventBusUtil.post_event(data);
    }

    @Override
    public void onDestroy() {
        if(mHelper != null){
            mHelper.destroy();
        }
        unregister_bus();
        super.onDestroy();
        mIsDataLoaded = false;
    }
}
