package com.ctut.mart4u.customer;

import android.os.Bundle;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;

public class AccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Không cần thêm logic mới, nội dung đã được hiển thị trong ScrollView
    }

    @Override
    protected int getLayoutId() {
        return R.layout.customer_account;
    }
}