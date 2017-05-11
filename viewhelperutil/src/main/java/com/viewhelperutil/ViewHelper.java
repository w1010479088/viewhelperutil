package com.viewhelperutil;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;

/**
 * View属性设置的帮助类
 * Created by songlintao on 16/10/13.
 */

@SuppressWarnings("all")
public class ViewHelper {
    private View mContentView;
    private Context mContext;
    private SparseArray<View> mSubViews = new SparseArray<>();

    public Activity getActivity() {
        return mActivity;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    private Activity mActivity;
    private Fragment mFragment;


    public ViewHelper(View view, Context context) {
        mContentView = view;
        mContext = context;
    }

    public ViewHelper(Activity activity) {
        mContentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mContext = activity;
        mActivity = activity;
    }

    public ViewHelper(View view, Activity activity) {
        mContentView = view;
        mContext = activity;
        mActivity = activity;
    }

    public ViewHelper(View view, Fragment fragment) {
        mContentView = view;
        mContext = fragment.getContext();
        mFragment = fragment;
    }

    public Context getContext() {
        return mContext;
    }

    public <T extends View> T getView(int viewId) {
        if (viewId == 0) {
            return (T) mContentView;
        }
        View view = mSubViews.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mSubViews.put(viewId, view);
        }
        return (T) view;
    }
}
