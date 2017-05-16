package com.viewhelperutil.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.viewhelperutil.ViewHelper;

/**
 * Dialog基础类
 * Created by wangyiming on 15/11/18.
 */
public abstract class BaseDialog extends AppCompatDialogFragment {
    protected ViewHelper mHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, animateStyle());
        setCancelable(cancelAbleOutSide());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(windowWidth(), windowHeight());
                window.setGravity(gravity());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOutSideClick();
            }
        });
        mHelper = new ViewHelper(rootView, getContext());
        initView();
        return rootView;
    }

    protected void onOutSideClick(){
        dismiss();
    }

    protected boolean cancelAbleOutSide(){
        return true;
    }

    protected @StyleRes int animateStyle(){
        return 0;
    }

    protected int windowWidth(){
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    protected int windowHeight(){
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    protected int gravity(){
        return Gravity.CENTER;
    }

    public boolean isShow(){
        return isVisible();
    }

    public void show(BaseActivity activity){
        show(activity.getSupportFragmentManager(), getDialogTag());
    }

    protected abstract String getDialogTag();

    protected abstract int getLayoutId();

    protected abstract void initView();

    @Override
    public void onDestroy() {
        unregister_bus();
        super.onDestroy();
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
}
