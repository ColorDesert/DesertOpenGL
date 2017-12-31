package com.desert.desertopengl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.desert.desertopengl.constant.Constants;
import com.desert.desertopengl.graph.dimensional.three.Cone;
import com.desert.desertopengl.graph.dimensional.three.Cube;
import com.desert.desertopengl.graph.dimensional.three.Cylinder;
import com.desert.desertopengl.graph.dimensional.three.Earth;
import com.desert.desertopengl.interfaces.IDrawAbbr;
import com.desert.desertopengl.view.GLSLView;

/**
 * Created by desert on 2017/12/26
 */

public class ThreeDimenActivity extends AppCompatActivity implements IDrawAbbr {
    private GLSLView mGLSLView;
    private Cube mCube;
    private Cylinder mCylinder;
    private Cone mCone;
    private Earth mEarth;
    private int mType = Constants.THREE_DIMENSIONAL_CUBE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abbr);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("3D图形");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mGLSLView = findViewById(R.id.view);
        mGLSLView.setIDrawAbbr(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSLView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_abbr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.cube:
                mType = Constants.THREE_DIMENSIONAL_CUBE;
                break;
            case R.id.cylinder:
                mType = Constants.THREE_DIMENSIONAl_CYLINDER;
                break;
            case R.id.cone:
                mType = Constants.THREE_DIMENSIONAL_CONE;
                break;
            case R.id.earth:
                mType = Constants.THREE_DIMENSIONAL_EARTH;
                break;
        }
        return true;
    }

    @Override
    public void onSurfaceCreated() {
        mCube = new Cube(this);
        mCylinder = new Cylinder(this);
        mCone = new Cone(this);
        mEarth = new Earth(this);
    }

    @Override
    public void drawSelf() {
        switch (mType) {
            case Constants.THREE_DIMENSIONAL_CUBE:
                mCube.drawSelf();
                break;
            case Constants.THREE_DIMENSIONAl_CYLINDER:
                mCylinder.drawSelf();
                break;
            case Constants.THREE_DIMENSIONAL_CONE:
                mCone.drawSelf();
                break;
            case Constants.THREE_DIMENSIONAL_EARTH:
                mEarth.drawSelf();
                break;
        }
    }
}
