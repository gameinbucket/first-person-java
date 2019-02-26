#version 330 core

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;

in vec2 v_texture;
in vec3 v_normal;
in vec3 v_tangent;
in vec3 v_bitangent;

layout (location = 0) out vec3 color;
layout (location = 1) out vec2 normal;

uniform mat4 view_matrix;

void main()
{
    vec4 pixel = texture(u_texture0, v_texture);

    if (pixel.a == 0.0) discard;

    color = pixel.rgb;
    
    vec3 bump = texture(u_texture1, v_texture).xyz * 2.0 - 1.0;
    
    bump = normalize(bump);
    
    vec3 t = normalize(view_matrix * vec4(v_tangent, 0.0)).xyz;
    vec3 b = normalize(view_matrix * vec4(v_bitangent, 0.0)).xyz;
    vec3 n = normalize(view_matrix * vec4(v_normal, 0.0)).xyz;
    
    mat3 tbn = transpose(mat3(t, b, n));
    
    normal = n.xy; // normalize(bump * tbn).xy;
}