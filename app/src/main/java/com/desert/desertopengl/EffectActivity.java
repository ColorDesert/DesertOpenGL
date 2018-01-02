package com.desert.desertopengl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.desert.desertopengl.view.EffectGLView;

/**
 * Created by desert on 2017/12/11
 */

public class EffectActivity extends AppCompatActivity {
    private EffectGLView mEffectGLView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);
        initView();
    }

    private void initView() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEffectGLView = findViewById(R.id.view);
        mEffectGLView.setFocusableInTouchMode(true);//设置为可触控
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEffectGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEffectGLView.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
