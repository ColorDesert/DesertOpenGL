package com.desert.desertopengl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.desert.desertopengl.view.GLView;


public class MainActivity extends AppCompatActivity {
    //private EGLView mEGLView;
    private GLView mEGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEGLView=new GLView(this);
        setContentView(mEGLView);
        //mEGLView = findViewById(R.id.view);
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
