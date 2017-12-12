package com.desert.desertopengl.utils;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by desert on 2017/12/9
 */

public class Cube extends ShaderUtil {

    private FloatBuffer verBuffer, colorBuffer;
    private ByteBuffer indexBuffer;
    private View mView;
    private int mProgram, aPosition, aColor, uMVPMatrix;
    private int angle = 0;

    public Cube(View view) {
        mView = view;
        initData();
        initShader();
    }

    private void initData() {
        float[] ver = {
                -1.0f, 1.0f, 1.0f,    //正面左上0
                -1.0f, -1.0f, 1.0f,   //正面左下1
                1.0f, -1.0f, 1.0f,    //正面右下2
                1.0f, 1.0f, 1.0f,     //正面右上3
                -1.0f, 1.0f, -1.0f,    //反面左上4
                -1.0f, -1.0f, -1.0f,   //反面左下5
                1.0f, -1.0f, -1.0f,    //反面右下6
                1.0f, 1.0f, -1.0f,     //反面右上7
        };
        verBuffer = getFloatBuffer(ver);
        float colors[] = {
//        (1111代表白色）颜色排列：RGB A
                1f, 1f, 1f, 1f,
                0f, 1f, 0f, 1f,
                1f, 1f, 0f, 1f,
                1f, 0f, 1f, 1f,
                0f, 0f, 1f, 1f,
                0f, 0f, 0.5f, 1f,
                1f, 1f, 1f, 1f,
                0.5f, 0f, 0f, 1f,
        };
        colorBuffer = getFloatBuffer(colors);
        byte index[] = {
                6, 7, 4, 6, 4, 5,    //后面
                6, 3, 7, 6, 2, 3,    //右面
                6, 5, 1, 6, 1, 2,    //下面
                0, 3, 2, 0, 2, 1,    //正面
                0, 1, 5, 0, 5, 4,    //左面
                0, 7, 3, 0, 4, 7,    //上面
        };
        indexBuffer = getByteBuffer(index);

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
        //Matrix.setRotateM(mChangeMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mChangeMatrix, 0, 0, 0, 1);
        Matrix.setRotateM(mChangeMatrix, 0, angle, 1, 1, 1);

        //给着色器传变量值
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, getMatrix(mChangeMatrix), 0);
        //设置顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, verBuffer);
        //设置颜色顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aColor, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
        //开启顶点和顶点颜色绘制
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aColor);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, indexBuffer);
        //关闭顶点和顶点颜色绘制
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aColor);
        angle += 3;

    }
}
