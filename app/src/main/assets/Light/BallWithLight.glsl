precision mediump float;
varying vec4 vDiffuse;//要传递的散射光变量
void main(){
vec4 finalColor=vec4(1.0);
gl_FragColor= vDiffuse ;  // finalColor*vDiffuse+finalColor*vec4(0.15,0.15,0.15,1.0);
}
