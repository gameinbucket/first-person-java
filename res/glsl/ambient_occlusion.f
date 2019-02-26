#version 330 core

uniform sampler2D u_texture1;
uniform sampler2D u_texture2;

in vec2 v_texture;

uniform vec2 texel;

layout (location = 0) out float final;

vec3 decode_normal(vec2 normal)
{
    return vec3(normal.xy, sqrt(1 - dot(normal.xy, normal.xy)));
}

void main()
{
    /*
    float mid_z = texture2D(u_texture2, v_texture).r;
    float top_z = texture2D(u_texture2, v_texture + vec2(0.0, -texel.y)).r;
    float bot_z = texture2D(u_texture2, v_texture + vec2(0.0, texel.y)).r;
    float left_z = texture2D(u_texture2, v_texture + vec2(-texel.x, 0.0)).r;
    float right_z = texture2D(u_texture2, v_texture + vec2(texel.x, 0.0)).r;
    const float z_test = 0.001;
    if (abs(mid_z - top_z) > 0.001) final = vec3(0.0);
    else if (abs(mid_z - bot_z) > 0.001) final = vec3(0.0);
    else if (abs(mid_z - left_z) > 0.001) final = vec3(0.0);
    else if (abs(mid_z - right_z) > 0.5) final = vec3(0.0);
    else final = vec3(1.0);
    */
    
    vec3 mid_n = decode_normal(texture2D(u_texture1, v_texture).xy);
    vec3 top_n = decode_normal(texture2D(u_texture1, v_texture + vec2(0.0, -texel.y)).xy);
    vec3 bot_n = decode_normal(texture2D(u_texture1, v_texture + vec2(0.0, texel.y)).xy);
    vec3 left_n = decode_normal(texture2D(u_texture1, v_texture + vec2(-texel.x, 0.0)).xy);
    vec3 right_n = decode_normal(texture2D(u_texture1, v_texture + vec2(texel.x, 0.0)).xy);
    const float n_test = 0.5;
    if (dot(mid_n, top_n) < n_test) final = 0.0;
    else if (dot(mid_n, bot_n) < n_test) final = 0.0;
    else if (dot(mid_n, left_n) < n_test) final = 0.0;
    else if (dot(mid_n, right_n) < n_test) final = 0.0;
    else final = 1.0;
}