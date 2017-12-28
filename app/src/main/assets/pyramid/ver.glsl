uniform mat4 vMatrix;
attribute vec3 vPosition;
attribute vec2 vTexPos;
varying vec4 vDiffuse;
varying vec2 vTexture;
vec4 pointLight(vec3 normal,vec3 lightLocation,vec4 lightDiffuse){
//光照normalize
vec3 vp=normalize(lightLocation-(vMatrix*vec4(vPosition,1)).xyz);

//变换后的法向量
//vec3 newTarget=normalize( (vMatrix*vec4(vPosition,1)).xyz);
vec3 newTarget=normalize((vMatrix*vec4(normal+vPosition,1)).xyz
                         - (vMatrix*vec4(vPosition,1)).xyz);
//漫反射光照效果=材质系数*漫反射光强度*max（0，cos（入射角））
return lightDiffuse* max(0.0,dot(vp,newTarget));
}

void main() {
 gl_Position=vMatrix*vec4(vPosition,1);
vTexture=vTexPos;
vec3 location=vec3(50,50,20);
vec4 at=vec4(1.0,1.0,1.0,1.0);
vDiffuse=pointLight(normalize(vPosition),location,at);
}

