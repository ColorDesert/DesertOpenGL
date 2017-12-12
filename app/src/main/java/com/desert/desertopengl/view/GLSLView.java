package com.desert.desertopengl.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;

import com.desert.desertopengl.utils.Cone;
import com.desert.desertopengl.utils.Cube;
import com.desert.desertopengl.utils.Cylinder;
import com.desert.desertopengl.utils.GLSLTriangles;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by desert on 2017/11/27
 */

public class GLSLView extends GLSurfaceView {
    private static final String TAG = "SEGLView";

    public GLSLView(Context context) {
        this(context, null);
    }

    public GLSLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //设置版本信息
        setEGLContextClientVersion(2);
        //设置渲染器
        setRenderer(new EGLRenderer(this));
        //设置渲染模式
        //RENDERMODE_CONTINUOUSLY //连续不断的刷新  消耗性能
        //RENDERMODE_WHEN_DIRTY //被动的刷新  需要用户主动去调用  requestRender();
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    class EGLRenderer implements Renderer {
        private GLSLTriangles mGLSLTriangles;
        private Cylinder mCylinder;
        private Cube mCube;
        private Cone mCone;
        private GLSLView mGLSLView;

        public EGLRenderer(GLSLView GLSLView) {
            mGLSLView = GLSLView;
        }

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            //关闭抗抖动（对于可用颜色较少的系统，可以牺牲分辨率，通过颜色抖动来增加的颜色数量）
            // GLES20.glDisable(GL10.GL_DITHER);//关闭
            //设置Hint模式(设置快速模式)
            // GLES20.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FALSE);
            //用白色清除屏幕
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            //开启深度测试
            GLES20.glEnable(GL10.GL_DEPTH_TEST);
            //mGLSLTriangles=new GLSLTriangles(mGLSLView);
            //mCube = new Cube(mGLSLView);
            mCone = new Cone(mGLSLView);
//设置逆时针方法为面的"前面"
//            GLES20.glFrontFace(GL10.GL_CCW);
//            GLES20.glEnable(GL10.GL_CULL_FACE);
//            GLES20.glCullFace(GL10.GL_FRONT);
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            //设置视口大小
            GLES20.glViewport(0, 0, width, height);
            //设置视角大小
            float r = (float) width / height;
            //设置投影矩阵
            Matrix.frustumM(Cone.mProMatrix, 0, -r, r, -1, 1, 3, 20);
            //设置照相机位置
            Matrix.setLookAtM(Cone.mCameraMatrix, 0, 0, 0, 10, 0, 0, 0, 0, 1, 0);
            //之前是设置投影矩阵然后设置单位矩阵

        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            //清除颜色缓存和深度缓存
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            mCone.drawSelf();
        }
    }
}
