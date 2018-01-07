package com.desert.desertopengl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.desert.desertopengl.constant.Constants;
import com.desert.desertopengl.interfaces.IDrawTexture;
import com.desert.desertopengl.picture.Image;
import com.desert.desertopengl.textures.Pyramid;
import com.desert.desertopengl.textures.TriangleTexture;
import com.desert.desertopengl.view.GLView;

/**
 * Created by desert on 2017/12/11
 */

public class TextureActivity extends AppCompatActivity implements IDrawTexture, View.OnClickListener {
    private GLView mGLView;
    private TriangleTexture mTriangleTexture;
    private Pyramid mPyramid;
    private Image mImage;
    private int mType=Constants.TYPE_TEXTURE_BASE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture);
        initView();
    }

    private void initView() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mGLView = findViewById(R.id.view);
        mGLView.setIDrawTexture(this);
        findViewById(R.id.txt_base).setOnClickListener(this);
        findViewById(R.id.txt_pyramid).setOnClickListener(this);
        findViewById(R.id.txt_earth).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
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

    @Override
    public void onSurfaceCreated() {
        mTriangleTexture = new TriangleTexture(this);
        mPyramid = new Pyramid(this);
        mImage = new Image(this);
    }

    @Override
    public void drawSelf() {
        switch (mType) {
            case Constants.TYPE_TEXTURE_BASE:
                mTriangleTexture.drawSelf();
                break;
            case Constants.TYPE_TEXTURE_PYRAMID:
                mPyramid.drawSelf();
                break;
            case Constants.TYPE_TEXTURE_EARTH:
                mImage.drawSelf();
                break;
            default:
                mTriangleTexture.drawSelf();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txt_base) {
            mType = Constants.TYPE_TEXTURE_BASE;
        } else if (id == R.id.txt_pyramid) {
            mType = Constants.TYPE_TEXTURE_PYRAMID;
        } else if (id == R.id.txt_earth) {
            mType = Constants.TYPE_TEXTURE_EARTH;
        }
    }
}
