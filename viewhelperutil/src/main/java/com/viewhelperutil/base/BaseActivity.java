package com.viewhelperutil.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.viewhelperutil.ViewHelper;

public abstract class BaseActivity extends AppCompatActivity {

    protected ViewHelper mHelper;

    protected abstract int getLayoutId();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView(savedInstanceState);
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
        }
        mHelper = new ViewHelper(this);
        init(savedInstanceState);
    }

    protected void beforeSetContentView(Bundle savedInstanceState) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void replace(int containerId, BaseFragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (addToBackStack) {
            fragmentManager.beginTransaction().addToBackStack(fragment.TAG).replace(containerId, fragment).commit();
        } else {
            int count = fragmentManager.getBackStackEntryCount();
            for (int i = count; --i >= 0; ) {
                fragmentManager.popBackStack();
            }
            fragmentManager.beginTransaction().replace(containerId, fragment, fragment.TAG).commit();
        }
    }

    public void add(int containerId, BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(fragment.TAG).add(containerId, fragment).commit();
    }

    public void pop() {
        if (0 < getSupportFragmentManager().getBackStackEntryCount()) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    protected void onLogout() {

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
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(mHelper != null){
            mHelper.destroy();
        }
        unregister_bus();
        super.onDestroy();
    }
}
