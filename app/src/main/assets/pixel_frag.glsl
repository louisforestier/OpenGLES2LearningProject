#version 100
precision mediump float;
// Light source definition
uniform vec4 uAmbiantLight; 
uniform bool uLighting; 
uniform vec3 uLightPos; 
uniform vec4 uLightColor; 
// Material definition
uniform vec4 uMaterialColor;

varying vec4 posf; 
varying vec3 normalf; 

void main(void) { 
  if (uLighting) 
  { 
    vec3 normal = normalize(normalf);
    vec3 lightdir=normalize(uLightPos-posf.xyz); 
    float weight = max(dot(normal, lightdir),0.0); 
    gl_FragColor = uMaterialColor*(uAmbiantLight+weight*uLightColor); 
  } 
  else gl_FragColor = uMaterialColor; 
}
