package com.ctut.mart4u.customer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import com.ctut.mart4u.BaseActivity;
import com.ctut.mart4u.R;

public class AccountActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.customer_account; // Trả về layout nội dung của AccountActivity
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

    }
}