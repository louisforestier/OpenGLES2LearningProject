#version 100
precision mediump float;
// Light source definition
uniform bool uLighting;
// Material definition
uniform vec4 uMaterialColor;
uniform float uMaterialShininess;
uniform vec4 uMaterialSpecular;

uniform sampler2D uTextureUnit;
uniform sampler2D shadowMap;
uniform bool uTexturing;

varying vec4 posf;
varying vec3 normalf;
varying vec2 texturef;
varying vec4 lightspaceposf;


struct DirLight {
    vec3 direction;

    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
};

struct PointLight {
    vec3 position;

    float constant;
    float linear;
    float quadratic;

    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
};

struct SpotLight {
    vec3 position;
    vec3 direction;
    float cutOff;
    float outerCutOff;

    float constant;
    float linear;
    float quadratic;

    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
};

#define NB_DIR_LIGHTS 1
#define NB_SPOT_LIGHTS 1
#define NB_POINT_LIGHTS 1

uniform DirLight dirLights[NB_DIR_LIGHTS];
uniform PointLight pointLights[NB_POINT_LIGHTS];
uniform SpotLight spotLights[NB_SPOT_LIGHTS];


float shadowCalculation(vec4 lightspaceposf, vec3 normal, vec3 lightdir)
{
    vec3 projCoords = lightspaceposf.xyz / lightspaceposf.w;
    projCoords = projCoords * 0.5 + 0.5;
    float closestDepth = texture2D(shadowMap, projCoords.xy).r;
    float currentDepth = projCoords.z;
    float cosTheta = clamp(dot(normal, lightdir), 0.0, 1.0);
    float bias = 0.0012*tan(acos(cosTheta));
    bias = clamp(bias, 0.0, 0.01);
    float shadow = 0.0;
    vec2 textureSize = vec2(2048.0,2048.0);
    vec2 texelSize = 1.0 / textureSize;
    for(int x = -1; x <= 1; ++x)
    {
        for(int y = -1; y <= 1; ++y)
        {
            float pcfDepth = texture2D(shadowMap, projCoords.xy + vec2(x, y) * texelSize).r;
            shadow += currentDepth - bias > pcfDepth ? 1.0 : 0.0;
        }
    }
    shadow /= 9.0;
    return shadow;
}

vec4 calcPointLight(PointLight light, vec3 normal, vec3 posf, vec3 viewdir)
{
    vec4 color;
    if (uTexturing)
    color = texture2D(uTextureUnit, texturef);
    else
    color = vec4(1, 1, 1, 1);
    float distance = length(light.position-posf);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    vec3 lightdir = normalize(light.position-posf);
    vec3 halfdir = normalize(lightdir + viewdir);
    float weight = max(dot(normal, lightdir), 0.0);
    vec4 dColor = color * uMaterialColor * (light.ambient + weight*light.diffuse);
    float spec = pow(max(dot(halfdir, normal), 0.0), uMaterialShininess*4.0);
    vec4 specColor = uMaterialSpecular * light.specular * spec;
    dColor *= attenuation;
    specColor *= attenuation;
    return dColor+specColor;
}

vec4 calcDirLight(DirLight light, vec3 normal, vec3 viewdir)
{
    vec4 color;
    if (uTexturing)
    color = texture2D(uTextureUnit, texturef);
    else
    color = vec4(1, 1, 1, 1);
    vec3 lightdir = normalize(-light.direction);
    vec3 halfdir = normalize(lightdir + viewdir);
    float weight = max(dot(normal, lightdir), 0.0);
    float shadow = shadowCalculation(lightspaceposf, normal, lightdir);
    vec4 dColor = color*uMaterialColor * (light.ambient + (1.0 - shadow) * weight*light.diffuse);
    float spec = pow(max(dot(halfdir, normal), 0.0), uMaterialShininess*4.0);
    vec4 specColor = (1.0 - shadow) * uMaterialSpecular * light.specular * spec;
    return dColor+specColor;
}

vec4 calcSpotLight(SpotLight light, vec3 normal, vec3 posf, vec3 viewdir)
{
    vec4 color;
    if (uTexturing)
    color = texture2D(uTextureUnit, texturef);
    else
    color = vec4(1, 1, 1, 1);
    float distance = length(light.position-posf);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    vec3 lightdir = normalize(light.position-posf);
    float theta = dot(lightdir, normalize(-light.direction));
    float epsilon = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);
    vec3 halfdir = normalize(lightdir + viewdir);
    float weight = max(dot(normal, lightdir), 0.0);
    vec4 dColor = color*uMaterialColor * (light.ambient + weight*light.diffuse*intensity);
    float spec = pow(max(dot(halfdir, normal), 0.0), uMaterialShininess*4.0);
    vec4 specColor = uMaterialSpecular * light.specular * spec;
    dColor *= attenuation;
    specColor *= attenuation * intensity;
    return dColor+specColor;
}


void main(void) {
    if (uLighting)
    {
        vec3 normal = normalize(normalf);
        vec3 viewdir=normalize(-posf.xyz);
        vec4 result;

        for (int i = 0; i < NB_DIR_LIGHTS; i++)
        {
            result += calcDirLight(dirLights[i], normal, viewdir);
        }
        for (int i = 0; i < NB_POINT_LIGHTS; i++)
        {
            result += calcPointLight(pointLights[i],normal,posf.xyz,viewdir);
        }
        for (int i = 0; i < NB_SPOT_LIGHTS; i++)
        {
            result += calcSpotLight(spotLights[i],normal,posf.xyz,viewdir);
        }
        gl_FragColor = result;
    }
    else gl_FragColor = uMaterialColor;
}
