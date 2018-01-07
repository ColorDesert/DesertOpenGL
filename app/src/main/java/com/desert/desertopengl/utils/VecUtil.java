package com.desert.desertopengl.utils;

import com.desert.desertopengl.obj.Normal;

import java.util.Set;

/**
 * 向量计算工具类
 * Created by desert on 2018/1/7
 */

public class VecUtil {
    //求两个向量的叉积
    public static float[] getCrossProduct(float x1, float y1, float z1, float x2, float y2, float z2) {
        //求出两个矢量叉积矢量在XYZ轴的分量ABC
        float A = y1 * z2 - y2 * z1;
        float B = z1 * x2 - z2 * x1;
        float C = x1 * y2 - x2 * y1;
        return new float[]{A, B, C};
    }

    //求两个向量的叉积
    public static float[] getCrossProduct(float[] ab, float[] ac) {
        //求出两个矢量叉积矢量在XYZ轴的分量ABC
        float A = ab[1] * ac[2] - ab[2] * ac[1];
        float B = ab[2] * ac[0] - ab[0] * ac[2];
        float C = ab[0] * ac[1] - ab[1] * ac[0];
        return new float[]{A, B, C};
    }

    //向量规格化
    public static float[] vectorNormal(float[] vector) {
        //求向量的模
        float module = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        return new float[]{vector[0] / module, vector[1] / module, vector[2] / module};

    }

    //求法向量平均值的工具方法
    public static float[] getAverage(Set<Normal> sn) {
        //存放法向量和的数组
        float[] result = new float[3];
        //把集合中所有的法向量求和
        for (Normal n : sn) {
            result[0] += n.nx;
            result[1] += n.ny;
            result[2] += n.nz;
        }
        //将求和后的法向量规格化
        return VecUtil.vectorNormal(result);
    }
}
