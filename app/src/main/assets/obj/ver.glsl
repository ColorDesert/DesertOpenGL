uniform mat4 vMVPMatrix;//总变化矩阵
uniform mat4 vMMatrix;//变化矩阵

uniform vec3 vLightLocation;//光照位置
uniform vec3 vCamera;//相机位置

attribute vec3 vPosition;//顶点位置
attribute vec3 vNormal;//顶点法向量
attribute vec2 vTexCoor;//纹理坐标

//用于传递给片元着色器的变量
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying vec2 vTextureCoord;
void pointLight(
in vec3 normal,		//法向量
inout vec4 ambient,	//环境光最终强度
inout vec4 diffuse,	//散射光最终强度
inout vec4 specular,//镜面光最终强度
in vec4 lightAmbient,//环境光强度
in vec4 lightDiffuse,//散射光强度
in vec4 lightSpecular//镜面光强度
){
//环境光=材质反射系数*环境光强度
ambient=lightAmbient;
//变化后的法向量
vec3 newNormal=normalize((vMMatrix*vec4(normal,1)).xyz);
//计算变面点到光照位置的向量
vec3 vp=normalize(vLightLocation-(vMMatrix*vec4(vPosition,1)).xyz);
vp=normalize(vp);
//计算变面点到相机位置的向量
vec3 eye=normalize(vCamera-(vMMatrix*vec4(vPosition,1)).xyz);
//计算半向量
vec3 halfVector=normalize(vp+eye);
float shininess=30.0;//粗糙度值越小越光滑
//漫反射光=材质反射系数*漫反射光的强度*max（0，cos(入射角)） 角度为入射角（光照向量和顶点法向量的夹角） 用点积计算
diffuse=lightDiffuse*max(0.0,dot(vp,newNormal));
                                       //的粗糙度 角度为:半向量（点到光照位置和点到相机位置之间向量）和顶点法向量的夹角  用点积计算
//镜面光=材质反射系数*镜面光的强度*max（0，cos(入射角)「粗糙度的次方」）
//法向量和半向量的点积
//dot(halfVector,newNormal)
//镜面反射的光强度因子
//pow(dot(halfVector,newNormal),shininess)
specular=lightSpecular*max(0.0,pow(dot(halfVector,newNormal),shininess));

}
void main() {
gl_Position=vMVPMatrix*vec4(vPosition,1);
vec4 ambientTemp, diffuseTemp, specularTemp;   //存放环境光、散射光、镜面反射光的临时变量
    pointLight(normalize(vNormal),ambientTemp,diffuseTemp,specularTemp,vec4(0.15,0.15,0.15,1.0),vec4(0.9,0.9,0.9,1.0),vec4(0.4,0.4,0.4,1.0));

    ambient=ambientTemp;
    diffuse=diffuseTemp;
    specular=specularTemp;
    vTextureCoord = vTexCoor;//将接收的纹理坐标传递给片元着色器
}
