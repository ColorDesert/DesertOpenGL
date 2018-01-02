uniform mat4 vMatrix;
attribute vec3 vPosition;
attribute vec2 vCoord;
attribute vec3 vNormal;         //法向量
varying vec2 textureCoordinate;
varying vec4 vDiffuse;          //用于传递给片元着色器的散射光最终强度
//散射光=材质反射系数*散射光强度*max(cos(入射角)，0)
//              法向量      ，光照位置      ，光照强度
vec4 pointLight(vec3 normal,vec3 lightLocation,vec4 lightDiffuse){
   //得到变换之后的发向量
   vec3 newNormal=normalize((vMatrix*vec4(normal,1)).xyz);
   //表面点与光源方向的向量
   vec3 vp=  normalize( lightLocation- (vMatrix*vec4(vPosition,1)).xyz);
   return  lightDiffuse*max(0.0,dot(newNormal,vp));
}

void main(){
    gl_Position= vMatrix*vec4(vPosition,1);
    vec4 at=vec4(1.0,1.0,1.0,1.0);//光照强度
    vec3 pos=vec3(50.0,200.0,50.0);//光照位置
   vDiffuse= pointLight(vNormal,pos,at);
}