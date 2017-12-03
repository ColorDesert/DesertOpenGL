package com.desert.desertopengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.desert.desertopengl.view.EGLView;


public class MainActivity extends AppCompatActivity {
    private EGLView mEGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mEGLView=new EGLView(this);
        setContentView(R.layout.activity_main);
        mEGLView = findViewById(R.id.view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEGLView.onPause();
    }
}
