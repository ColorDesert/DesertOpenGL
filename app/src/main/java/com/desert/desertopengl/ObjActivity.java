package com.desert.desertopengl;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.desert.desertopengl.view.ObjGLView;

/**
 * Created by desert on 2017/12/11
 */

public class ObjActivity extends AppCompatActivity {
    private ObjGLView mObj2GLView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_obj);
        initView();
    }

    private void initView() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mObj2GLView = findViewById(R.id.view);
        mObj2GLView.requestFocus();
        mObj2GLView.setFocusableInTouchMode(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mObj2GLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mObj2GLView.onPause();
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
