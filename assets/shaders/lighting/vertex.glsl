#version 330 core

layout(location = 0) in vec3 l_position;
layout(location = 1) in vec3 l_normal;

uniform mat4 u_projection;
uniform mat4 u_model;
uniform mat4 u_view;

uniform vec3 u_cameraPosition;

out vec4 color;
out vec3 normal;
out vec3 fragPos;
out vec3 cameraPos;

void main() {
    vec4 position = vec4(l_position, 1.0);
    gl_Position = u_projection * u_view * u_model * position;

//    normal = mat3(transpose(inverse(u_model))) * normalize(l_normal);

    normal = mat3(transpose(inverse(u_model))) * l_normal;
    fragPos = vec3(u_model * vec4(l_position, 1.0));
    cameraPos = u_cameraPosition;
}