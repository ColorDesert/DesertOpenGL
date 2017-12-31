uniform mat4 vMatrix;
attribute vec3 vPosition;
varying vec4 vDiffuse;//要传递的散射光的变量
vec4 pointLight(vec3 normal,vec3 lightLocation, vec4 lightDiffuse){
           //表面点与光源方向的向量
           //(normalize(x)标准化向量，返回一个方向和X相同但长度为1的向量 )
   vec3 vp=normalize(lightLocation-(vMatrix*vec4(vPosition,1)).xyz);
           //变换后的法向量
   vec3 newTarget=normalize((vMatrix*vec4(normal+vPosition,1)).xyz
                           - (vMatrix*vec4(vPosition,1)).xyz);
           //散射光光照效果=材质反射系数（1）*环境光强度* max（cos（入射角），0）
  return lightDiffuse* max(0.0,dot(newTarget,vp));
}
void main() {
gl_Position=vMatrix*vec4(vPosition,1);
vec3 locstion=vec3(500,500,100);
vec4 at=vec4(1.0,1.0,1.0,1.0);//光照强度
vDiffuse=pointLight(normalize(vPosition),locstion,at);

}
