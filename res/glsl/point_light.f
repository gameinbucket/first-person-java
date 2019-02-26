#version 330 core

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;
uniform sampler2D u_texture2;

in vec2 v_texture;

layout (location = 0) out vec3 final;

uniform vec2 texel;

uniform mat4 inverse_projection;

uniform vec3 light_view_space_position;
uniform vec3 light_color;
uniform float light_inverse_radius_squared;

// #define DEBUG

vec3 decode_normal(vec2 normal)
{
    return vec3(normal.xy, sqrt(1 - dot(normal.xy, normal.xy)));
}

void main()
{
    vec2 coord = gl_FragCoord.xy * texel;

    vec3 color = texture2D(u_texture0, coord).rgb;
    vec3 normal = decode_normal(texture2D(u_texture1, coord).xy);
    float z = texture2D(u_texture2, coord).r * 2.0 - 1.0;
    
    vec4 position = inverse_projection * vec4(coord * 2.0 - 1.0, z, 1.0);
    vec3 view_space_position = position.xyz / position.w;
    
    vec3 light_vector = light_view_space_position - view_space_position;
        
    float dist = length(light_vector);
        
    vec3 lighting = max(dot(normal, normalize(light_vector)), 0.0) * light_color * clamp(1.0 - light_inverse_radius_squared * dist * dist, 0.0, 1.0);
    
    #ifdef DEBUG
        final = light_color;
    #else
        final = color * lighting;
    #endif
}