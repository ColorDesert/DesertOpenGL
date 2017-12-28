package com.desert.desertopengl.picture;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.View;

import com.desert.desertopengl.utils.ShaderUtil;

import java.nio.FloatBuffer;

/**
 * Created by desert on 2017/12/25
 */

public class Image extends ShaderUtil {
    private FloatBuffer verBuffer, coorBuffer;
    private View mView;
    private int mProgram, aPosition, aTexture, uMVPMatrix;
    private int vCount;

    public Image(View view) {
        mView = view;
        initData();
        initShader();
    }

    private void initData() {
        float ver[] = {
                -1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
        };
        vCount = 4;
        verBuffer = getFloatBuffer(ver);
        float coor[] = new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
        };
        coorBuffer = getFloatBuffer(coor);
    }

    private void initShader() {
        String verSource = loadFromAssetsFile("texVer.glsl", mView.getResources());
        String fragSource = loadFromAssetsFile("texFrag.glsl", mView.getResources());
        mProgram = createProgram(verSource, fragSource);
        //获取程序（着色器）中顶点位置（XYZ 引用的id）
        aPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //获取程序（着色器）中顶点纹理位置的id
        aTexture = GLES20.glGetAttribLocation(mProgram, "vTexPos");
        //获取程序（着色器） 总变换矩阵的id
        uMVPMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
    }


    public void drawSelf(int textureID) {
        //使用某套 着色器程序
        GLES20.glUseProgram(mProgram);
        //设置变化矩阵（旋转）
        Matrix.translateM(mChangeMatrix, 0, 0, 0, -3);
        // Matrix.setRotateM(mChangeMatrix, 0, 90, 1, 0, 0);
        Matrix.setRotateM(mChangeMatrix, 0, 0, 0, 1, 0);

        //给着色器传变量值
        GLES20.glUniformMatrix4fv(uMVPMatrix, 1, false, getMatrix(mChangeMatrix), 0);
        //设置顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aPosition, 3, GLES20.GL_FLOAT, false, 0, verBuffer);
        //设置颜色顶点数据 normalized是否归一化
        GLES20.glVertexAttribPointer(aTexture, 2, GLES20.GL_FLOAT, false, 0, coorBuffer);
        //开启顶点和顶点纹理绘制
        GLES20.glEnableVertexAttribArray(aPosition);
        GLES20.glEnableVertexAttribArray(aTexture);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0,4);
        GLES20.glDisableVertexAttribArray(aPosition);
        GLES20.glDisableVertexAttribArray(aTexture);
    }
}
