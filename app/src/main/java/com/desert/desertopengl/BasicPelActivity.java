package com.desert.desertopengl;

import android.opengl.GLES20;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.desert.desertopengl.constant.Constants;
import com.desert.desertopengl.graph.basic.Triangles;
import com.desert.desertopengl.graph.basic.Lines;
import com.desert.desertopengl.graph.basic.Points;
import com.desert.desertopengl.interfaces.IDrawBasicPel;
import com.desert.desertopengl.view.BasicGLView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/12/11
 */

public class BasicPelActivity extends AppCompatActivity implements IDrawBasicPel, RadioGroup.OnCheckedChangeListener {
    private BasicGLView mBasicGLView;
    private int mPelType;
    private Points mPoints;
    private Lines mLines;
    private Triangles mTriangles;
    private RadioButton mRbTypeOne;
    private RadioButton mRbTypeTwo;
    private RadioButton mRbTypeThree;
    private RadioGroup mRgType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_pel);
        mBasicGLView = findViewById(R.id.basicGLView);
        mRgType = findViewById(R.id.rg);
        mRgType.setOnCheckedChangeListener(this);
        RadioGroup rgPel = findViewById(R.id.rg_pel);
        rgPel.setOnCheckedChangeListener(this);
        mRbTypeOne = findViewById(R.id.type_one);
        mRbTypeTwo = findViewById(R.id.type_two);
        mRbTypeThree = findViewById(R.id.type_three);
        ((RadioButton) findViewById(R.id.rb_point)).setChecked(true);
        mBasicGLView.setIDrawBasicPel(this);
    }

    private void initData() {
        mPelType = getIntent().getIntExtra(Constants.EVENT_BASIC_GRAPH_TYPE, Constants.BASIC_PEL_POINT);
        switch (mPelType) {
            case Constants.BASIC_PEL_POINT:
                mPoints = new Points();
                break;
            case Constants.BASIC_PEL_LINE:
                mLines = new Lines();
                mRbTypeOne.setText("GL_LINES");
                mRbTypeTwo.setText("GL_LINE_STRIP");
                mRbTypeThree.setText("GL_LINE_LOOP");
                mLines.setMode(GLES20.GL_LINES);
                break;
            case Constants.BASIC_PEL_TRIANGLE:
                mTriangles = new Triangles();
                mRbTypeOne.setText("GL_TRIANGLES");
                mRbTypeTwo.setText("GL_TRIANGLE_STRIP");
                mRbTypeThree.setText("GL_TRIANGLE_FAN");
                mTriangles.setMode(GLES20.GL_TRIANGLES);
                break;
            default:
                mPoints = new Points();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBasicGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBasicGLView.onPause();
    }

    @Override
    public void drawSelf(GL10 gl) {
        switch (mPelType) {
            case Constants.BASIC_PEL_POINT:
                mPoints.drawSelf(gl);
                break;
            case Constants.BASIC_PEL_LINE:
                mLines.drawSelf(gl);
                break;
            case Constants.BASIC_PEL_TRIANGLE:
                mTriangles.drawSelf(gl);
                break;
            default:
                mPoints.drawSelf(gl);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.e("dxf", "checkedId=" + checkedId);

        switch (checkedId) {
            case R.id.rb_point:
                Log.e("dxf", "rb_point");
                mRgType.setVisibility(View.GONE);
                if (mPoints == null) {
                    mPoints = new Points();
                }
                mPelType = Constants.BASIC_PEL_POINT;
                mRbTypeOne.setChecked(true);
                break;
            case R.id.rb_triangle:
                mRgType.setVisibility(View.VISIBLE);
                if (mLines == null) {
                    mLines = new Lines();
                }
                mRbTypeOne.setText("GL_LINES");
                mRbTypeTwo.setText("GL_LINE_STRIP");
                mRbTypeThree.setText("GL_LINE_LOOP");
                mLines.setMode(GLES20.GL_LINES);
                mPelType = Constants.BASIC_PEL_LINE;
                mRbTypeOne.setChecked(true);
                break;
            case R.id.rb_circle:
                mRgType.setVisibility(View.VISIBLE);
                if (mTriangles == null) {
                    mTriangles = new Triangles();
                }
                mRbTypeOne.setText("GL_TRIANGLES");
                mRbTypeTwo.setText("GL_TRIANGLE_STRIP");
                mRbTypeThree.setText("GL_TRIANGLE_FAN");
                mTriangles.setMode(GLES20.GL_TRIANGLES);
                mPelType = Constants.BASIC_PEL_TRIANGLE;
                mRbTypeOne.setChecked(true);
                break;
        }
        if (checkedId == R.id.type_one) {
            switch (mPelType) {
                case Constants.BASIC_PEL_LINE:
                    mLines.setMode(GLES20.GL_LINES);
                    break;
                case Constants.BASIC_PEL_TRIANGLE:
                    mTriangles.setMode(GLES20.GL_TRIANGLES);
                    break;
            }
        } else if (checkedId == R.id.type_two) {
            switch (mPelType) {
                case Constants.BASIC_PEL_LINE:
                    mLines.setMode(GLES20.GL_LINE_STRIP);
                    break;
                case Constants.BASIC_PEL_TRIANGLE:
                    mTriangles.setMode(GLES20.GL_TRIANGLE_STRIP);
                    break;
            }

        } else if (checkedId == R.id.type_three) {
            switch (mPelType) {
                case Constants.BASIC_PEL_LINE:
                    mLines.setMode(GLES20.GL_LINE_LOOP);
                    break;
                case Constants.BASIC_PEL_TRIANGLE:
                    mTriangles.setMode(GLES20.GL_TRIANGLE_FAN);
                    break;
            }
        }
        mBasicGLView.requestRender();
    }
}
