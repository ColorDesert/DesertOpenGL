package com.desert.desertopengl.utils;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.FloatBuffer;
import java.util.Random;

/**
 * Created by desert on 2017/12/9
 */

public class Cone extends ShaderUtil {

    private FloatBuffer verBuffer, colorBuffer, bottomVerBuffer, topVerBuffer;
    private View mView;
    private int mProgram, aPosition, aColor, uMVPMatrix;
    private int angle = 0;
    private int vCount;

    public Cone(View view) {
        mView = view;
        initData();
        initShader();
    }

    private void initData() {
        int angle = 1;
        int count = 0;
        vCount = 360 / angle + 2;//(一个是中心点（0，0），最后一个点是重复点)
        float botVer[] = new float[3 * vCount];
        //下面圆心
        botVer[count++] = 0;
        botVer[count++] = 0;
        botVer[count++] = -3;
        //圆形
        for (float i = 0; i < 360 + angle; i += angle) {
            botVer[count++] = (float) (Math.cos(Math.toRadians(i)) - Math.sin(Math.toRadians(i)));
            botVer[count++] = (float) (Math.cos(Math.toRadians(i)) + Math.sin(Math.toRadians(i)));
            botVer[count++] = -3;

        }
        count = 0;
        float[] ver = new float[vCount * 3];
        ver[count++] = 0;
        ver[count++] = 0;
        ver[count++] = 3;
        for (int i = 3; i < botVer.length; i++) {
            ver[count++] = botVer[i];
        }

        verBuffer = getFloatBuffer(ver);
        bottomVerBuffer = getFloatBuffer(botVer);
        float colors[] = new float[vCount * 4];
        Random random = new Random(1);
        count = 0;
        for (int i = 0; i < vCount; i++) {
            colors[count++] = random.nextFloat();
            colors[count++] = random.nextFloat();
            colors[count++] = random.nextFloat();
            colors[count++] = 1.0f;
        }
        colorBuffer = getFloatBuffer(colors);
    }

    private void initShader() {
        String verSource = loadFromAssetsFile("ver.glsl", mView.getResources());
        String fragSource = loadFromAssetsFile("frag.glsl", mView.getResources());
        mProgram = createProgram(verSource, fragSource);
        //获取程序（着色器）中顶点位置（XYZ 引用的id）
        aPosition = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序（着色器）中顶点颜色位置(RGB A引用的id)
        aColor = GLES20.glGetAttribLocation(mProgram, "aColor");
        //获取程序（着色器） 总变换矩阵的id
        uMVPMatrix = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }


    public void drawSelf() {
        //使用某套 着色器程序
        GLES20.glUseProgram(mProgram);
        //设置变化矩阵（旋转）
        Matrix.translateM(mChangeMatrix, 0, 0, 0, -5);
        Matrix.setRotateM(mChangeMatrix, 0, angle, 1, 1, 0);

        //给着色器传变量值
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, getMatrix(mChangeMatrix), 0);
        //设置顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, bottomVerBuffer);
        //设置颜色顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aColor, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
        //开启顶点和顶点颜色绘制
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aColor);
        //先一个圆
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vCount);
        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, verBuffer);
        //圆柱体中间的部分
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vCount);
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aColor);
        angle += 3;

    }
}
