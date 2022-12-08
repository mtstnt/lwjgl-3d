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
uniform float u_farPlane;

in vec3 normal;
in vec3 fragPos;
in vec3 cameraPos;
in vec2 texCoords;

out vec4 o_color;

vec3 samplingOffsets[20] = vec3[] (
    vec3(1, 1,  1), vec3( 1, -1,  1), vec3(-1, -1,  1), vec3(-1, 1,  1),
    vec3(1, 1, -1), vec3( 1, -1, -1), vec3(-1, -1, -1), vec3(-1, 1, -1),
    vec3(1, 1,  0), vec3( 1, -1,  0), vec3(-1, -1,  0), vec3(-1, 1,  0),
    vec3(1, 0,  1), vec3(-1,  0,  1), vec3( 1,  0, -1), vec3(-1, 0, -1),
    vec3(0, 1,  1), vec3( 0, -1,  1), vec3( 0, -1, -1), vec3( 0, 1, -1)
);

// Rumus ga pake sampling untuk dptkan value shadow.
float shadowCalcWithoutSampling(vec3 _fragPos)
{
    // Cari distance dari fragPos ke light dan distance vector itu di shadow depth map.
    // Kalau value dari shadow depth map < distance fragPos ke light berarti shadow.
    vec3 fragToLight = _fragPos - u_lightSources[0].position;
    float closestDepth = texture(u_depthMapSampler, fragToLight).r;
    closestDepth *= u_farPlane;
    float currentDepth = length(fragToLight);

    // Bias untuk kasih toleransi depth fragPos ke light waktu dibandingkan.
    // Ini ngilangin shadow acne/belang-belang
    float bias = 0.05;
    return (currentDepth -  bias > closestDepth) ? 1.0 : 0.0;
}

float shadowCalc(vec3 _fragPos) {
    // Menghitung apakah titik dari light ke fragment position > depthmap.
    vec3 fragToLight = _fragPos - u_lightSources[0].position;

    // Depth dari fragPos ke light.
    float currentDepth = length(fragToLight);
    float shadow = 0.0;

    float bias = 0.15;

    // Vector" di offsets ini digunakan utk mendapatkan value shadow dari
    //   sekelilingnya untuk dibandingkan jadi value shadow satu titik.
    // Value shadow semakin di tengah object semakin gelap, semakin pinggir agak memudar.
    int samples = 20;
    float viewDistance = length(cameraPos - _fragPos);
    float diskRadius = 0.05;
    for(int i = 0; i < samples; ++i)
    {
        float closestDepth = texture(u_depthMapSampler, fragToLight + samplingOffsets[i] * diskRadius).r;

        // Ubah dari depthMap value [0,1] ke [0,farPlane]
        closestDepth *= u_farPlane;

        // Value shadow ditambah kalau sekitarnya itu juga shadow.
        if(currentDepth - bias > closestDepth)
        shadow += 1.0;
    }

    // Diambil rata-rata dari total semua shadow sekeliling.
    shadow /= float(samples);

    return shadow;
}

void main() {
    vec3 lightColor = u_lightSources[0].color.xyz;
    vec3 lightPosition = u_lightSources[0].position;

    vec4 objectColor = texture(u_textureSampler, texCoords);

    float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor;

    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(lightPosition - fragPos);

    float diffuseStrength = max(dot(normal, lightDir), 0.0f);
    vec3 diffuse = diffuseStrength * lightColor;

    float specularStrength = 0.8f;
    vec3 cameraDir = normalize(cameraPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(cameraDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * spec * lightColor;

    float shadow = shadowCalc(fragPos);

    // Yg terdampak shadow dari diffuse dan specular/seberapa diffuse dan specular kena persentase shadownya.
    vec3 result = (ambient + (1.0 - shadow) * (diffuse + specular)) * objectColor.xyz;

    o_color = vec4(result.xyz, 1.0);
}
