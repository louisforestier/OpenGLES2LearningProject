#version 100
precision mediump float;
// Light source definition
uniform float uConstantAttenuation;
uniform float uLinearAttenuation;
uniform float uQuadraticAttenuation;
uniform vec4 uAmbiantLight;
uniform bool uLighting;
uniform vec3 uLightPos;
uniform vec4 uLightColor;
// Material definition
uniform vec4 uMaterialColor;
uniform float uMaterialShininess;
uniform vec4 uLightSpecular;
uniform vec4 uMaterialSpecular;

varying vec4 posf;
varying vec3 normalf;

void main(void) {
  if (uLighting)
  {
    float distance = length(uLightPos-posf.xyz);
    float attenuation = 1.0 /(uConstantAttenuation + uLinearAttenuation* distance + uQuadraticAttenuation * (distance * distance)) ;
    vec3 viewdir=normalize(-posf.xyz);
    vec3 normal = normalize(normalf);
    vec3 lightdir=normalize(uLightPos-posf.xyz);
    vec3 reflectdir=normalize(reflect(-lightdir, normal));

    float weight = max(dot(normal, lightdir),0.0);
    vec4 dColor = uMaterialColor*(uAmbiantLight+weight*uLightColor);

    float spec = pow(max(dot(viewdir, reflectdir), 0.0), uMaterialShininess);
    vec4 specColor = uMaterialSpecular*uLightSpecular*spec;

    dColor *= attenuation;
    specColor *= attenuation;
    gl_FragColor = dColor + specColor;
  }
  else gl_FragColor = uMaterialColor;
}
