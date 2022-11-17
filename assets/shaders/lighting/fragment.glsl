#version 430 core

#define MAX_LIGHT_COUNT 64

struct LightSource {
    vec3 position;
    vec4 color;
};

uniform LightSource u_lightSources[MAX_LIGHT_COUNT];
uniform vec4 u_color;

in vec3 normal;
in vec3 fragPos;
in vec3 cameraPos;

out vec4 o_color;

void main() {
    vec3 lightColor = u_lightSources[0].color.xyz;
    vec3 lightPosition = u_lightSources[0].position;
    vec3 objectColor = u_color.xyz;

    float ambientStrength = 0.1;
    vec3 ambient = ambientStrength * lightColor;

    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(lightPosition - fragPos);

    float diffuseStrength = max(dot(normal, lightDir), 0.0f);
    vec3 diffuse = diffuseStrength * lightColor;

    float specularStrength = 0.5;
    vec3 cameraDir = normalize(cameraPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(cameraDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 result = (ambient + diffuse + specular) * objectColor;

    o_color = vec4(result, 1.0);
}