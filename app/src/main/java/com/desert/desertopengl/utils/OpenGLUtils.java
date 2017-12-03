package com.desert.desertopengl.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by desert on 2017/12/2
 */

public class OpenGLUtils {
    public IntBuffer getIntBuffer(int[] ver) {
        ByteBuffer bbf = ByteBuffer.allocateDirect(ver.length * 4);
        bbf.order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = bbf.asIntBuffer();
        intBuffer.put(ver);
        intBuffer.position(0);
        return intBuffer;
    }

    public FloatBuffer getFloatBuffer(float[] buffer) {
        //由于不同平台字节顺序不同，数据单元不是字节的
        //一定要经过ByteBuffer转换，关键是通过ByteBuffer设置nativeOrder
        ByteBuffer bbf = ByteBuffer.allocateDirect(buffer.length * 4);
        bbf.order(ByteOrder.nativeOrder());//设置这个字节缓冲的字节顺序为本地平台的字节顺序
        FloatBuffer intBuffer = bbf.asFloatBuffer();//转换为Float型缓冲
        intBuffer.put(buffer);//向缓冲区中放入数据
        intBuffer.position(0);//设置缓冲区的起始位置
        return intBuffer;
    }

    public ByteBuffer getByteBuffer(byte[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(buffer.length);
        byteBuffer.put(buffer);//向缓冲区中放入数据
        byteBuffer.position(0);//设置缓冲区的起始位置
        return byteBuffer;
    }
}
