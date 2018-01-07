package com.desert.desertopengl.utils;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Stack;

/**
 * 存储系统矩阵状态的类
 */
public class MatrixState {
    //4x4矩阵 投影用
    private static float[] mProjMatrix = new float[16];
    //摄像机位置朝向9参数矩阵
    private static float[] mVMatrix = new float[16];
    //当前变换矩阵
    private static float[] mCurrMatrix;
    //定位光光源位置
    public static float[] lightLocation = new float[]{0, 0, 0};//定位光光源位置
    //相机位置数据缓存
    public static FloatBuffer cameraFB;
    //管光照位置数据缓存
    public static FloatBuffer lightPositionFB;

    public static Stack<float[]> mStack = new Stack<>();//保护变换矩阵的栈

    /**
     * 获取不变换初始矩阵
     */
    public static void setInitStack() {
        //创建并设置为单位矩阵
        mCurrMatrix = new float[16];
        Matrix.setRotateM(mCurrMatrix, 0, 0, 1, 0, 0);
    }

    /**
     * 保护当前矩阵（现场）
     */
    public static void pushMatrix() {//保护变换矩阵
        mStack.push(mCurrMatrix.clone());
    }

    /**
     * 回复当前矩阵（现场）
     */
    public static void popMatrix() {//恢复变换矩阵
        mCurrMatrix = mStack.pop();
    }

    /**
     * 平移
     * @param x x轴偏移量
     * @param y y轴偏移量
     * @param z z轴偏移量
     */
    public static void translate(float x, float y, float z) {//设置沿xyz轴移动
        Matrix.translateM(mCurrMatrix, 0, x, y, z);
    }

    /**
     * 设置绕xyz轴移动
     * @param angle 旋转角度
     * @param x 绕X轴旋转 （1表示绕X轴0表示不绕X轴）
     * @param y 绕y轴旋转 （1表示绕X轴0表示不绕X轴）
     * @param z 绕z轴旋转 （1表示绕X轴0表示不绕X轴）
     */
    public static void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(mCurrMatrix, 0, angle, x, y, z);
    }


    /**
     * 设置摄像机
     * @param cx 摄像机X轴位置
     * @param cy 摄像机y轴位置
     * @param cz 摄像机z轴位置
     * @param tx 观察物体X轴位置
     * @param ty 观察物体y轴位置
     * @param tz 观察物体z轴位置
     * @param upx up向量
     * @param upy 为1正常观察物体
     * @param upz up向量
     */
    public static void setCamera(
            float cx, float cy, float cz,   //摄像机位置xyz
            float tx, float ty, float tz,   //摄像机目标点xyz
            float upx, float upy, float upz   //摄像机UP向量xyZ分量
    ) {
        Matrix.setLookAtM(mVMatrix, 0,
                cx, cy, cz,
                tx, ty, tz,
                upx, upy, upz
        );

        float[] cameraLocation = new float[3];//摄像机位置
        cameraLocation[0] = cx;
        cameraLocation[1] = cy;
        cameraLocation[2] = cz;
        cameraFB = getFloatBuffer(cameraLocation);
    }

    //设置透视投影参数  远小近小
    public static void setProjectFrustum(
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距离
            float far       //far面距离
    ) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //设置正交投影参数
    public static void setProjectOrtho(
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距离
            float far       //far面距离
    ) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //获取具体物体的总变换矩阵
    public static float[] getFinalMatrix() {
        float[] mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mCurrMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    //获取具体物体的变换矩阵
    public static float[] getMMatrix() {
        return mCurrMatrix;
    }

    //设置灯光位置的方法
    public static void setLightLocation(float x, float y, float z) {
        lightLocation[0] = x;
        lightLocation[1] = y;
        lightLocation[2] = z;
        lightPositionFB = getFloatBuffer(lightLocation);
    }

    public static FloatBuffer getFloatBuffer(float[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(buffer.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());//设置字节顺序
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(buffer);
        floatBuffer.position(0);
        return floatBuffer;
    }
}
