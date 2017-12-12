package com.desert.desertopengl.utils;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;

import java.nio.FloatBuffer;

/**
 * Created by desert on 2017/12/9
 */

public class GLSLTriangles extends ShaderUtil {
    //4*4的投影矩阵
    public static float[] mProMatrix = new float[16];
    //4*4的相机矩阵
    public static float[] mCameraMatrix = new float[16];
    //总变换矩阵
    public static float[] mMVPMatrix;
    //变换矩阵（旋转、平移、缩放）
    public static float[] mChangeMatrix = new float[16];
    private FloatBuffer verBuffer, colorBuffer;
    private int vCount;
    private View mView;
    private int mProgram;
    private int aPosition, aColor, uMVPMatrix;

    public GLSLTriangles(View view) {
        mView = view;
        initData();
        initShader();
    }

    private void initShader() {
        String verSource = loadFromAssetsFile("ver.glsl", mView.getResources());
        String fragSource = loadFromAssetsFile("frag.glsl", mView.getResources());
        Log.e("dxf","ver: "+verSource);
        Log.e("dxf","fragSource: "+fragSource);
        mProgram = createProgram(verSource, fragSource);
        //获取程序（着色器）中顶点位置（XYZ 引用的id）
        aPosition = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序（着色器）中顶点颜色位置(RGB A引用的id)
        aColor = GLES20.glGetAttribLocation(mProgram, "aColor");
        //获取程序（着色器） 总变换矩阵的id
        uMVPMatrix = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    private void initData() {
        //定点个数
        vCount = 3;
        float UNIT_SIZE = 0.2f;//缩放比列
        //顶点数据
        float ver[] = new float[]{
                -4 * UNIT_SIZE, 0, 0,
                0 , -4 * UNIT_SIZE, 0,
                4 * UNIT_SIZE, 0, 0,

        };
        //创建顶点缓冲数据
        verBuffer = getFloatBuffer(ver);
        //顶点颜色数据
        float[] colors = new float[]{
                1, 0, 1, 0,
                1, 1, 0, 0,
                0, 1, 0, 0,
        };
        colorBuffer = getFloatBuffer(colors);
    }

    public void drawSelf() {
        //使用某套 着色器程序
        GLES20.glUseProgram(mProgram);
        //设置变化矩阵（旋转）
        Matrix.setRotateM(mChangeMatrix, 0, 0, 1, 1, 1);
        //给着色器传变量值
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false,getFinalMatrix(mChangeMatrix),0);
        //设置顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aPosition,3,GLES20.GL_FLOAT,false,0,verBuffer);
        //设置颜色顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aColor,4,GLES20.GL_FLOAT,false,0,colorBuffer);
        //开启顶点和顶点颜色绘制
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aColor);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vCount);
        //关闭顶点和顶点颜色绘制
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aColor);

    }

    public float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(
                mMVPMatrix, //总变换矩阵
                0, //总变换矩阵的起始索引
                mCameraMatrix, //照相机位置
                0,//照相机朝向起始索引
                spec, //投影变换矩阵
                0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
}
