package com.desert.desertopengl.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;

import com.desert.desertopengl.interfaces.IDrawAbbr;
import com.desert.desertopengl.utils.ShaderUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/11/27
 */

public class GLSLView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private IDrawAbbr mIDrawAbbr;

    public GLSLView(Context context) {
        this(context, null);
    }

    public GLSLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setIDrawAbbr(IDrawAbbr IDrawAbbr) {
        mIDrawAbbr = IDrawAbbr;
    }

    private void init() {
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
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        if (mIDrawAbbr != null) {
            mIDrawAbbr.onSurfaceCreated();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视口大小
        GLES20.glViewport(0, 0, width, height);
        //设置视角大小
        float r = (float) width / height;
        //设置投影矩阵
        Matrix.frustumM(ShaderUtil.mProMatrix, 0, -r, r, -1, 1, 1, 10);
        //设置照相机位置
        Matrix.setLookAtM(ShaderUtil.mVMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
        //之前是设置投影矩阵然后设置单位矩阵

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (mIDrawAbbr != null) {
            mIDrawAbbr.drawSelf();
        }
    }
}
