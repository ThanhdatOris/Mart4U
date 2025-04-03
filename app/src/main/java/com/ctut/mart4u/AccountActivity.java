package com.ctut.mart4u;

import android.os.Bundle;

public class AccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Không cần thêm logic mới, nội dung đã được hiển thị trong ScrollView
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }
}