#version 330 core

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;
uniform sampler2D u_texture2;

uniform mat4 inverse_mvp;
uniform mat4 previous_model_view_projection_matrix;

in vec2 v_texture;

layout (location = 0) out vec4 final;

uniform vec3 light_position;
uniform vec3 light_color;
uniform float light_inverse_radius_squared;

void main()
{
    vec3 position = texture2D(u_texture0, v_texture).xyz;
    vec3 color = texture2D(u_texture1, v_texture).rgb;
    vec3 normal = texture2D(u_texture2, v_texture).xyz;
    
    vec3 light_vector = light_position - position;
        
    float dist = length(light_vector);
        
    vec3 lighting = max(dot(normal, normalize(light_vector)), 0.0) * light_color * clamp(1.0 - light_inverse_radius_squared * dist * dist, 0.0, 1.0);
    
    final = vec4(color * lighting, 1.0);
    
    /*float near = 0.3f;
    float far = 100.0f;
    
    float x = v_texture.x * 2.0 - 1.0;
    float y = v_texture.y * 2.0 - 1.0;
    float z = near / (far - color.a * (far - near)) * far;
    
    vec4 proj = vec4(x, y, color.a, 1.0);
    vec4 projection = inverse_mvp * proj;
    vec3 position = projection.xyz / projection.w;*/
    
    /*vec4 previous = previous_model_view_projection_matrix * vec4(position, 1.0);

    previous.xyz /= previous.w;
    previous.xy = previous.xy * 0.5 + 0.5;
    
    vec2 blur_vec = previous.xy - v_texture;
    
    const int num_samples = 8;
    
    vec3 sum = texture(u_texture1, v_texture).rgb;
    for (int i = 1; i < num_samples; ++i)
    {
        vec2 offset = blur_vec * (float(i) / float(num_samples - 1) - 0.5);
        sum += texture(u_texture1, v_texture + offset).rgb;
    }
 
    sum /= float(num_samples);
    
    final = vec4(sum * accumulation, 1.0);*/
}
