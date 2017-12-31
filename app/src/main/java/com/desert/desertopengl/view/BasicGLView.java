package com.desert.desertopengl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.desert.desertopengl.interfaces.IDrawBasicPel;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/11/27
 */

public class BasicGLView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private static final String TAG = "EGLView";
    private IDrawBasicPel mIDrawBasicPel;

    public BasicGLView(Context context) {
        this(context, null);
    }

    public BasicGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setIDrawBasicPel(IDrawBasicPel IDrawBasicPel) {
        mIDrawBasicPel = IDrawBasicPel;
    }

    private void init() {
        //设置版本信息
        // setEGLContextClientVersion(2);
        //设置渲染器
        setRenderer(this);
        //设置渲染模式
        //RENDERMODE_CONTINUOUSLY //连续不断的刷新  消耗性能
        //RENDERMODE_WHEN_DIRTY //被动的刷新  需要用户主动去调用  requestRender();
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d(TAG, "onSurfaceCreated");
        //关闭抗抖动（对于可用颜色较少的系统，可以牺牲分辨率，通过颜色抖动来增加的颜色数量）
        gl10.glDisable(GL10.GL_DITHER);//关闭
        //设置Hint模式(设置快速模式)
        gl10.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FALSE);
        //用白色清除屏幕
        gl10.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //开启深度测试
        gl10.glEnable(GL10.GL_DEPTH_TEST);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d(TAG, "onSurfaceChanged");
        //设置视口大小
        gl.glViewport(0, 0, width, height);
        //设置投影矩阵
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        //设置视角大小
        float r = (float) width / height;
        gl.glFrustumf(-r, r, -1, 1, 1, 20);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d(TAG, "onDrawFrame");
        //清除颜色缓存和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //设置模型矩阵
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        //平移
        gl.glTranslatef(0, 0, -3.0f);
        if (mIDrawBasicPel != null) {
            mIDrawBasicPel.drawSelf(gl);
        }
    }
}
