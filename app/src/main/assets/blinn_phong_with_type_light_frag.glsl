#version 100
#define POINT_LIGHT 0
#define DIRECTIONAL_LIGHT 1
#define SPOT_LIGHT 2
precision mediump float;
// Light source definition
uniform float uConstantAttenuation;
uniform float uLinearAttenuation;
uniform float uQuadraticAttenuation;
uniform vec4 uAmbiantLight;
uniform int uLightType;
uniform bool uLighting;
uniform vec3 uLightPos;
uniform vec3 uLightDir;
uniform float uCutOff;
uniform float uOuterCutOff;
uniform vec4 uLightColor;
uniform vec4 uLightSpecular;
// Material definition
uniform vec4 uMaterialColor;
uniform float uMaterialShininess;
uniform vec4 uMaterialSpecular;

varying vec4 posf;
varying vec3 normalf;


vec4 calcPointLight(vec3 lightPos, vec4 diffuse, vec4 ambient, vec4 specular, float constant, float linear, float quadratic, vec3 normal, vec3 posf, vec3 viewdir)
{
  float distance = length(lightPos-posf);
  float attenuation = 1.0 / (constant + linear * distance + quadratic * (distance * distance));
  vec3 lightdir = normalize(lightPos-posf);
  vec3 halfdir = normalize(lightdir + viewdir);
  float weight = max(dot(normal,lightdir),0.0);
  vec4 dColor = uMaterialColor * (ambient + weight*diffuse);
  float spec = pow(max(dot(halfdir,normal),0.0),uMaterialShininess*4.0);
  vec4 specColor = uMaterialSpecular * specular * spec;
  dColor *= attenuation;
  specColor *= attenuation;
  return dColor+specColor;
}

vec4 calcDirLight(vec3 lightPos, vec4 diffuse, vec4 ambient, vec4 specular, vec3 normal, vec3 viewdir)
{
  vec3 lightdir = normalize(-uLightDir);
  vec3 halfdir = normalize(lightdir + viewdir);
  float weight = max(dot(normal,lightdir),0.0);
  vec4 dColor = uMaterialColor * (ambient + weight*diffuse);
  float spec = pow(max(dot(halfdir,normal),0.0),uMaterialShininess*4.0);
  vec4 specColor = uMaterialSpecular * specular * spec;
  return dColor+specColor;
}


vec4 calcSpotLight(vec3 lightPos, vec4 diffuse, vec4 ambient, vec4 specular, float constant, float linear, float quadratic, vec3 normal, vec3 posf, vec3 viewdir, float cutoff, float outerCutOff)
{
  float distance = length(lightPos-posf);
  float attenuation = 1.0 / (constant + linear * distance + quadratic * (distance * distance));
  vec3 lightdir = normalize(lightPos-posf);
  float theta = dot(lightdir,normalize(-uLightDir));
  float epsilon = cutoff - outerCutOff;
  float intensity = clamp((theta - outerCutOff) / epsilon,0.0,1.0);
  vec3 halfdir = normalize(lightdir + viewdir);
  float weight = max(dot(normal,lightdir),0.0);
  vec4 dColor = uMaterialColor * (ambient + weight*diffuse*intensity);
  float spec = pow(max(dot(halfdir,normal),0.0),uMaterialShininess*4.0);
  vec4 specColor = uMaterialSpecular * specular * spec;
  dColor *= attenuation;
  specColor *= attenuation * intensity;
  return dColor+specColor;
}


void main(void) {
  if (uLighting)
  {
    vec3 normal = normalize(normalf);
    vec3 viewdir=normalize(-posf.xyz);
    if (uLightType == POINT_LIGHT)
    {
      gl_FragColor = calcPointLight(uLightPos,uLightColor,uAmbiantLight,uLightSpecular,uConstantAttenuation,uLinearAttenuation,uQuadraticAttenuation,normal,posf.xyz,viewdir);
    }
  else if (uLightType == DIRECTIONAL_LIGHT)
    {
      gl_FragColor = calcDirLight(uLightPos,uLightColor,uAmbiantLight,uLightSpecular,normal,viewdir);
    }
    else if (uLightType == SPOT_LIGHT)
    {
      gl_FragColor = calcSpotLight(uLightPos,uLightColor,uAmbiantLight,uLightSpecular,uConstantAttenuation,uLinearAttenuation,uQuadraticAttenuation,normal,posf.xyz,viewdir,uCutOff,uOuterCutOff);
    }
  }
  else gl_FragColor = uMaterialColor;
}
