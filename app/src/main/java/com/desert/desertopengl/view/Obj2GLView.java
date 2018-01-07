package com.desert.desertopengl.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.desert.desertopengl.interfaces.IDrawAbbr;
import com.desert.desertopengl.obj.HatObj;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/11/27
 */

public class Obj2GLView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private IDrawAbbr mIDrawAbbr;
    private HatObj mHatObj;
    private float xAngle, yAngle;
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//角度缩放比例


    public Obj2GLView(Context context) {
        this(context, null);
    }

    public Obj2GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setIDrawAbbr(IDrawAbbr IDrawAbbr) {
        mIDrawAbbr = IDrawAbbr;
    }

    private void init() {
        mHatObj = new HatObj(getContext());
        mHatObj.initData();
        //设置版本信息
        setEGLContextClientVersion(2);
        //设置渲染器
        setRenderer(this);
        //设置渲染模式
        //RENDERMODE_CONTINUOUSLY //连续不断的刷新  消耗性能
        //RENDERMODE_WHEN_DIRTY //被动的刷新  需要用户主动去调用  requestRender();
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //用白色清除屏幕
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//        if (mIDrawAbbr != null) {
//            mIDrawAbbr.onSurfaceCreated();
//        }
        mHatObj.initShared();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口大小
        GLES20.glViewport(0, 0, width, height);
        //设置视角大小
        float r = (float) width / height;
        //设置投影矩阵
        float[] matrix = mHatObj.getUnitMatrix();
        //按坐标比例缩放
        Matrix.scaleM(matrix, 0,
                //缩放因子
                0.2f, 0.2f * r, 0.2f);
        //Matrix.frustumM();
        //   Matrix.setLookAtM();
        HatObj.mMVPMatrix = matrix;

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.rotateM(HatObj.mMVPMatrix,0,xAngle,1,0,0);
        Matrix.rotateM(HatObj.mMVPMatrix,0,yAngle,0,1,0);
       // Matrix.rotateM(HatObj.mMVPMatrix,0,0.3f,0,1,0);
//        if (mIDrawAbbr != null) {
//            mIDrawAbbr.drawSelf();
//        }
        mHatObj.drawSelf();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;
            xAngle += dx * TOUCH_SCALE_FACTOR;
            yAngle += dy * TOUCH_SCALE_FACTOR;
            requestRender();
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
