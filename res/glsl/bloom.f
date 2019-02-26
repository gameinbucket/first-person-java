#version 330 core

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;
uniform sampler2D u_texture2;
uniform sampler2D u_texture3;

in vec2 v_texture;

layout (location = 0) out vec4 final;

const vec3 gamma = vec3(1.0 / 2.2);
const float exposure = 1.0;

const float A = 0.15;
const float B = 0.50;
const float C = 0.10;
const float D = 0.20;
const float E = 0.02;
const float F = 0.30;
const float W = 11.2;

vec3 tonemap(vec3 x)
{
   return vec3(((x * (A * x + C * B) + D * E) / (x * (A * x + B) + D * F)) - E / F);
}

void main()
{
    vec3 original = texture2D(u_texture0, v_texture).rgb;
    //vec3 divide_2 = texture2D(u_texture1, v_texture).rgb;
    //vec3 divide_4 = texture2D(u_texture2, v_texture).rgb;
    vec3 divide_8 = texture2D(u_texture3, v_texture).rgb;
    
    float shadow = texture2D(u_texture1, v_texture).r;
    
    vec3 bloomed = original * shadow + divide_8;
    
    vec3 mapped = tonemap(exposure * bloomed);
    vec3 white_scale = 1.0 / tonemap(vec3(W));
    final = vec4(pow(mapped * white_scale, gamma), 1.0);
        
    // final = vec4(pow(vec3(1.0) - exp(-bloomed) * exposure, gamma), 1.0);
        
    // final = vec4(bloomed, 1.0);
}