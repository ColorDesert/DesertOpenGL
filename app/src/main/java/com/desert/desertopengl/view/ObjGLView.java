package com.desert.desertopengl.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.desert.desertopengl.interfaces.IDrawAbbr;
import com.desert.desertopengl.obj.TeapotObj;
import com.desert.desertopengl.utils.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/11/27
 */

public class ObjGLView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private IDrawAbbr mIDrawAbbr;
    private TeapotObj mTeapotObj;
    private float xAngle, yAngle;
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//角度缩放比例


    public ObjGLView(Context context) {
        this(context, null);
    }

    public ObjGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setIDrawAbbr(IDrawAbbr IDrawAbbr) {
        mIDrawAbbr = IDrawAbbr;
    }

    private void init() {
        mTeapotObj = new TeapotObj(getContext());
        //设置版本信息
        setEGLContextClientVersion(2);
        //设置渲染器
        setRenderer(this);
        //设置渲染模式
        //RENDERMODE_CONTINUOUSLY //连续不断的刷新  消耗性能
        //RENDERMODE_WHEN_DIRTY //被动的刷新  需要用户主动去调用  requestRender();
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //用白色清除屏幕
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mTeapotObj.initData();
        mTeapotObj.initShared();
        MatrixState.setInitStack();
        MatrixState.setLightLocation(400, 100, 60);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口大小
        GLES20.glViewport(0, 0, width, height);
        //设置视角大小
        float r = (float) width / height;
        //设置投影矩阵和相机位置矩阵
        //调用此方法计算产生透视投影矩阵
        MatrixState.setProjectFrustum(-r, r, -1, 1, 1, 100);
        //调用此方法产生摄像机9参数位置矩阵
        MatrixState.setCamera(0, 0, 20, 0f, 0f, -5f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        MatrixState.pushMatrix();
        MatrixState.rotate(xAngle,1,0,0);
        MatrixState.rotate(yAngle,0,1,0);
        mTeapotObj.drawSelf();
        MatrixState.popMatrix();
    }
    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算触控笔Y位移
                float dx = x - mPreviousX;//计算触控笔X位移
                yAngle += dx * TOUCH_SCALE_FACTOR;//设置沿y轴旋转角度(x的偏移量)
                xAngle += dy * TOUCH_SCALE_FACTOR;//设置沿x轴旋转角度(y的偏移量)
                requestRender();//重绘画面
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return true;
    }
}
