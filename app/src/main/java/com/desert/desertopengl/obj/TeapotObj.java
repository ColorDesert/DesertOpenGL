package com.desert.desertopengl.obj;

import android.content.Context;
import android.opengl.GLES20;

import com.desert.desertopengl.R;
import com.desert.desertopengl.utils.MatrixState;
import com.desert.desertopengl.utils.ObjUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

/**
 * Created by desert on 2017/12/30
 */

public class TeapotObj extends ObjUtil {
    private int mProgram;//自定义渲染管线着色器程序id
    private int mMVPMatrixHandle;//总变换矩阵引用
    private int mMMatrixHandle;//位置、旋转变换矩阵
    private int mPositionHandle; //顶点位置属性引用
    private int mNormalHandle; //顶点法向量属性引用
    private int mLightLocationHandle;//光源位置属性引用
    private int mCameraHandle; //摄像机位置属性引用
    private int mTexCoorHandle; //顶点纹理坐标属性引用
    private Context mContext;
    private FloatBuffer lightLocBuffer,cameraLocBuffer;
    private int textureId;

    public TeapotObj(Context context) {
        mContext = context;
    }

    public void initData() {
        try {
            InputStream stream = mContext.getAssets().open("obj/ch_t.obj");
            readObjAndTexFile(stream);
            textureId=initTexture(mContext, R.drawable.ghxp);
            float[] lightLocation=new float[3];
            lightLocation[0]=40;
            lightLocation[1]=10;
            lightLocation[2]=20;
            lightLocBuffer=getFloatBuffer(lightLocation);
            float[] cameraLocation=new float[3];
            cameraLocation[0]=0;
            cameraLocation[1]=0;
            cameraLocation[2]=20;
            cameraLocBuffer=getFloatBuffer(cameraLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initShared() {
        String verSource = loadFromAssetsFile("obj/ver.glsl", mContext.getResources());
        String fragSource = loadFromAssetsFile("obj/frag.glsl", mContext.getResources());
        mProgram = createProgram(verSource, fragSource);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "vMVPMatrix");
        mMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "vMMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "vNormal");
        mTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "vTexCoor");
        mLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "vLightLocation");
        mCameraHandle = GLES20.glGetUniformLocation(mProgram, "vCamera");
    }

    public void drawSelf() {
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glUniformMatrix4fv(mMMatrixHandle, 1, false,MatrixState.getMMatrix() , 0);
        GLES20.glUniform3fv(mLightLocationHandle,1,lightLocBuffer);
        GLES20.glUniform3fv(mCameraHandle,1,cameraLocBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glEnableVertexAttribArray(mTexCoorHandle);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, verBuffer);
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, norBuffer);
        GLES20.glVertexAttribPointer(mTexCoorHandle,2,GLES20.GL_FLOAT,false,2*4,texBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalHandle);
        GLES20.glDisableVertexAttribArray(mTexCoorHandle);
    }
}
