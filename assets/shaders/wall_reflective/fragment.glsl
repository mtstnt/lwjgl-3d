#version 430 core

#define MAX_LIGHT_COUNT 64

struct LightSource {
    vec3 position;
    vec4 color;
};

uniform LightSource u_lightSources[MAX_LIGHT_COUNT];
uniform vec4 u_color;

uniform sampler2D u_textureSampler;
uniform samplerCube u_depthMapSampler;
uniform samplerCube u_skyboxSampler;
uniform float u_farPlane;

in vec3 normal;
in vec3 fragPos;
in vec3 cameraPos;
in vec2 texCoords;

out vec4 o_color;

vec3 gridSamplingDisk[20] = vec3[] (
    vec3(1, 1,  1), vec3( 1, -1,  1), vec3(-1, -1,  1), vec3(-1, 1,  1),
    vec3(1, 1, -1), vec3( 1, -1, -1), vec3(-1, -1, -1), vec3(-1, 1, -1),
    vec3(1, 1,  0), vec3( 1, -1,  0), vec3(-1, -1,  0), vec3(-1, 1,  0),
    vec3(1, 0,  1), vec3(-1,  0,  1), vec3( 1,  0, -1), vec3(-1, 0, -1),
    vec3(0, 1,  1), vec3( 0, -1,  1), vec3( 0, -1, -1), vec3( 0, 1, -1)
);

float shadowCalc(vec3 _fragPos) {
    vec3 fragToLight = _fragPos - u_lightSources[0].position;
    float currentDepth = length(fragToLight);
    float shadow = 0.0;

    float bias = 0.15;
    int samples = 20;
    float viewDistance = length(cameraPos - _fragPos);
    float diskRadius = (1.0 + (viewDistance / u_farPlane)) / 25.0;
    for(int i = 0; i < samples; ++i)
    {
        float closestDepth = texture(u_depthMapSampler, fragToLight + gridSamplingDisk[i] * diskRadius).r;
        closestDepth *= u_farPlane;   // undo mapping [0;1]
        if(currentDepth - bias > closestDepth)
        shadow += 1.0;
    }
    shadow /= float(samples);

    return shadow;
}

void main() {
    vec3 lightColor = u_lightSources[0].color.xyz;
    vec3 lightPosition = u_lightSources[0].position;

    float ambientStrength = 0.3f;
    vec3 ambient = ambientStrength * lightColor;

    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(lightPosition - fragPos);

    float diffuseStrength = max(dot(normal, lightDir), 0.0f);
    vec3 diffuse = diffuseStrength * lightColor;

    float specularStrength = 0.5f;
    vec3 cameraDir = normalize(cameraPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(cameraDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * spec * lightColor;

    float shadow = shadowCalc(fragPos);
    vec3 result = (ambient + (1.0 - shadow) * (diffuse + specular)) * objectColor.xyz;

    o_color = vec4(result.xyz, 1.0);
}