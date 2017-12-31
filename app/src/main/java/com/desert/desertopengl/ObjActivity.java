package com.desert.desertopengl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.desert.desertopengl.view.ObjGLView;

/**
 * Created by desert on 2017/12/11
 */

public class ObjActivity extends AppCompatActivity {
    private ObjGLView mObjGLView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obj);
        initView();
    }

    private void initView() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mObjGLView=findViewById(R.id.view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mObjGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mObjGLView.onPause();
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
