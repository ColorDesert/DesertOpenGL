package com.desert.desertopengl.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.desert.desertopengl.Points;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/11/27
 */

public class EGLView extends GLSurfaceView {
    private static final String TAG = "EGLView";

    public EGLView(Context context) {
        this(context, null);
    }

    public EGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //设置版本信息
        // setEGLContextClientVersion(2);
        //设置渲染器
        setRenderer(new EGLRenderer());
        //设置渲染模式
        //RENDERMODE_CONTINUOUSLY //连续不断的刷新
        //RENDERMODE_WHEN_DIRTY //被动的刷新  需要用户主动去调用
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        //requestRender();
    }

    class EGLRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            //关闭抗抖动（对于可用颜色较少的系统，可以牺牲分辨率，通过颜色抖动来增加的颜色数量）
            gl10.glDisable(GL10.GL_DITHER);//关闭
            //设置Hint模式(设置快速模式)
            gl10.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FALSE);
            //用黑色清除屏幕
            gl10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            //开启深度测试
            gl10.glEnable(GL10.GL_DEPTH_TEST);

        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            //设置视口大小
            GLES20.glViewport(0, 0, width, height);
            //设置投影矩阵
            gl10.glMatrixMode(GL10.GL_PROJECTION);
            gl10.glLoadIdentity();
            //设置视角大小
            float r = (float) width / height;
            gl10.glFrustumf(-r, r, -1, 1, 1, 10);

        }

        Points points = new Points();

        @Override
        public void onDrawFrame(GL10 gl10) {
            //清除颜色缓存和深度缓存
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            //设置模型矩阵
            gl10.glMatrixMode(GL10.GL_MODELVIEW);
            gl10.glLoadIdentity();
            //平移
            gl10.glTranslatef(0, 0, -3.0f);
            points.drawSelf(gl10);
        }
    }
}
