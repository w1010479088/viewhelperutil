package com.viewhelper;

import android.os.Bundle;
import android.view.View;

import com.bruceewu.Requestor;
import com.viewhelperutil.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mHelper.setClick(R.id.btn_request, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Requestor.Home.home_tabs();
            }
        });
    }
}
