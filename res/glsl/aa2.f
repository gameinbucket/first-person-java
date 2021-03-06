#version 330 core

uniform sampler2D u_texture0;

uniform vec2 texel;

in vec2 v_texture;

layout (location = 0) out vec4 final;

const vec3 luminosity = vec3(0.2126, 0.7152, 0.0722);

const float reduce_min = 1.0 / 128.0;
const float reduce_mul = 1.0 / 8.0;
const float span_max = 8.0;

void main()
{
    vec3 middile = texture2D(u_texture0, v_texture).rgb;
    vec3 top_left = texture2D(u_texture0, v_texture + vec2(-1.0, -1.0) * texel).rgb;
    vec3 top_right = texture2D(u_texture0, v_texture + vec2(1.0, -1.0) * texel).rgb;
    vec3 bot_left = texture2D(u_texture0, v_texture + vec2(-1.0, 1.0) * texel).rgb;
    vec3 bot_right = texture2D(u_texture0, v_texture + vec2(1.0, 1.0) * texel).rgb;
    
    float luminosity_middile = dot(middile, luminosity);
    float luminosity_top_left = dot(top_left, luminosity);
    float luminosity_top_right = dot(top_right, luminosity);
    float luminosity_bot_left = dot(bot_left, luminosity);
    float luminosity_bot_right = dot(bot_right, luminosity);
    
    float luminosity_min = min(luminosity_middile, min(min(luminosity_top_left, luminosity_top_right), min(luminosity_bot_left, luminosity_bot_right)));
    float luminosity_max = max(luminosity_middile, max(max(luminosity_top_left, luminosity_top_right), max(luminosity_bot_left, luminosity_bot_right)));
   
    vec2 direction;
    
    direction.x = -((luminosity_top_left + luminosity_top_right) - (luminosity_bot_left  + luminosity_bot_right));
    direction.y =  ((luminosity_top_left + luminosity_bot_left ) - (luminosity_top_right + luminosity_bot_right));
    
    float direction_reduce = max((luminosity_top_left + luminosity_top_right + luminosity_bot_left + luminosity_bot_right) * (0.25 * reduce_mul), reduce_min);
    
    float rcp_direction_min = 1.0 / (min(abs(direction.x), abs(direction.y)) + direction_reduce);
    
    direction = min(vec2(span_max, span_max), max(vec2(-span_max, -span_max), direction * rcp_direction_min)) * texel;
    
    vec3 a = 0.5 * (texture2D(u_texture0, v_texture + direction * (1.0 / 3.0 - 0.5)).rgb + texture2D(u_texture0, v_texture + direction * (2.0 / 3.0 - 0.5)).rgb);
    vec3 b = a * 0.5 + 0.25 * (texture2D(u_texture0, v_texture + direction * -0.5).rgb + texture2D(u_texture0, v_texture + direction * 0.5).rgb);
    
    float luminosity_b = dot(b, luminosity);
    
    if (luminosity_b < luminosity_min || luminosity_b > luminosity_max)
        final = vec4(a, 1.0);
    else
        final = vec4(b, 1.0);
}
