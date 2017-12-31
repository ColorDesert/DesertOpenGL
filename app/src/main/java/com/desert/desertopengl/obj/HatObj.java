package com.desert.desertopengl.obj;

import android.content.Context;
import android.opengl.GLES20;

import com.desert.desertopengl.utils.ObjUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by desert on 2017/12/30
 */

public class HatObj extends ObjUtil {
    private int mProgram, mHPosition, mHMatrix, mHNormal;
    private float angle;
    private Context mContext;

    public HatObj(Context context) {
        mContext = context;
    }

    public void initData() {
        try {
            InputStream stream = mContext.getAssets().open("td/hat.obj");
            readObjFile(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initShared() {
        String verSource = loadFromAssetsFile("td/obj.vert", mContext.getResources());
        String fragSource = loadFromAssetsFile("td/obj.frag", mContext.getResources());
        mProgram = createProgram(verSource, fragSource);
        mHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        mHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHNormal = GLES20.glGetAttribLocation(mProgram, "vNormal");
    }

    public void drawSelf() {
        GLES20.glUseProgram(mProgram);
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, mMVPMatrix, 0);
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition, 3, GLES20.GL_FLOAT, false, 3 * 4, verBuffer);
        GLES20.glEnableVertexAttribArray(mHNormal);
        GLES20.glVertexAttribPointer(mHNormal, 3, GLES20.GL_FLOAT, false, 3 * 4, norBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHNormal);
        angle += 0.3;
    }
}
