uniform mat4 uMVPMatrix;//总变化矩阵
attribute vec3 aPosition;//顶点数据
varying vec3 vDiffuse;//要传递的散射光的变量
vec4 pointLight(vec3 normal,vec3 lightLocation,vec4 lightDiffuse){
//表面点与光源方向的向量   (normalize(x)标椎化向量，反回一个方向和x相同但长度为1的向量)
vec3 vp=normalize(lightLocation-(uMVPMatrix*vec4(aPosition,1)).xyz);
//变换后的法向量
vec3 newTarget=normalize(uMVPMatrix*vec4(normal+aPosition,1)).xyz-(uMVPMatrix*vec4(aPosition,1)).xyz;
//散射光的光照结果
return lightDiffuse*max(0,0,dot(newTarget,vp));
}
void main(){
 gl_Position=uMVPMatrix*vec4(aPosition,1);
 vec3 pos=vec3(10.0,10.0,10.0);
 vec4 at=vec4(1.0,1.0,1.0,1.0);
 //vDiffuse=pointLight(normalize(aPosition),pos,at);
}