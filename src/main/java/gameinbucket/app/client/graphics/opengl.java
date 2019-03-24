package gameinbucket.app.client.graphics;

import gameinbucket.app.client.io;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import java.nio.ByteOrder;
import static org.lwjgl.opengl.ARBBufferStorage.GL_MAP_COHERENT_BIT;
import static org.lwjgl.opengl.ARBBufferStorage.GL_MAP_PERSISTENT_BIT;
import static org.lwjgl.opengl.ARBBufferStorage.glBufferStorage;

public abstract class opengl {
    public static float[] model_view_matrix = new float[16];
    public static float[] model_view_projection_matrix = new float[16];

    private static FloatBuffer matrix_buffer = ByteBuffer.allocateDirect(16 * 4).asFloatBuffer();

    private static int program;

    private opengl() {

    }

    public static void vao(buffer b, int pos, int col, int tex, int norm) {
        int stride = pos + col + tex + (norm * 3);

        b.vao = glGenVertexArrays();
        glBindVertexArray(b.vao);

        b.vbo = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, b.vbo);
        glBufferStorage(GL_ARRAY_BUFFER, b.vertex_buffer.limit(),
                GL_MAP_WRITE_BIT | GL_MAP_PERSISTENT_BIT | GL_MAP_COHERENT_BIT);
        b.vertex_buffer = glMapBufferRange(GL_ARRAY_BUFFER, 0, b.vertex_buffer.limit(),
                GL_MAP_WRITE_BIT | GL_MAP_PERSISTENT_BIT | GL_MAP_COHERENT_BIT, null).order(ByteOrder.nativeOrder());

        b.ebo = glGenBuffers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, b.ebo);
        glBufferStorage(GL_ELEMENT_ARRAY_BUFFER, b.index_buffer.limit(),
                GL_MAP_WRITE_BIT | GL_MAP_PERSISTENT_BIT | GL_MAP_COHERENT_BIT);
        b.index_buffer = glMapBufferRange(GL_ELEMENT_ARRAY_BUFFER, 0, b.index_buffer.limit(),
                GL_MAP_WRITE_BIT | GL_MAP_PERSISTENT_BIT | GL_MAP_COHERENT_BIT, null).order(ByteOrder.nativeOrder());

        b.vertex_float_buffer = b.vertex_buffer.asFloatBuffer();
        b.index_integer_buffer = b.index_buffer.asIntBuffer();

        int index = 0;

        glVertexAttribPointer(index, pos, GL_FLOAT, false, stride * Float.BYTES, 0);
        glEnableVertexAttribArray(index);
        index++;

        if (col > 0) {
            glVertexAttribPointer(index, col, GL_FLOAT, false, stride * Float.BYTES, (pos) * Float.BYTES);
            glEnableVertexAttribArray(index);
            index++;
        }

        if (tex > 0) {
            glVertexAttribPointer(index, tex, GL_FLOAT, false, stride * Float.BYTES, (pos + col) * Float.BYTES);
            glEnableVertexAttribArray(index);
            index++;
        }

        if (norm > 0) {
            // normal
            glVertexAttribPointer(index, norm, GL_FLOAT, false, stride * Float.BYTES, (pos + col + tex) * Float.BYTES);
            glEnableVertexAttribArray(index);
            index++;

            // tangent
            glVertexAttribPointer(index, norm, GL_FLOAT, false, stride * Float.BYTES,
                    (pos + col + tex + norm) * Float.BYTES);
            glEnableVertexAttribArray(index);
            index++;

            // bitangent
            glVertexAttribPointer(index, norm, GL_FLOAT, false, stride * Float.BYTES,
                    (pos + col + tex + norm + norm) * Float.BYTES);
            glEnableVertexAttribArray(index);
            index++;
        }

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public static void update_framebuffer(frame f) {
        glBindFramebuffer(GL_FRAMEBUFFER, f.fbo);

        for (int i = 0; i < f.textures.length; i++) {
            glBindTexture(GL_TEXTURE_2D, f.textures[i]);
            glTexImage2D(GL_TEXTURE_2D, 0, f.texture_internal_format[i], f.width, f.height, 0, f.texture_format[i],
                    f.texture_type[i], (ByteBuffer) null);
        }

        if (f.depth) {
            glBindTexture(GL_TEXTURE_2D, f.depth_texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, f.width, f.height, 0, GL_DEPTH_STENCIL,
                    GL_UNSIGNED_INT_24_8, (ByteBuffer) null);
        }
    }

    public static void framebuffer(frame f) {
        f.fbo = glGenFramebuffers();

        glBindFramebuffer(GL_FRAMEBUFFER, f.fbo);

        f.draw_buffers = new int[f.textures.length];

        for (int i = 0; i < f.textures.length; i++) {
            f.textures[i] = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, f.textures[i]);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            if (f.linear) {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            } else {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            }

            glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, f.textures[i], 0);

            f.draw_buffers[i] = GL_COLOR_ATTACHMENT0 + i;
        }

        glDrawBuffers(f.draw_buffers);

        if (f.depth) {
            f.depth_texture = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, f.depth_texture);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, f.depth_texture, 0);
        }

        update_framebuffer(f);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("framebuffer error " + glGetError());
            System.exit(0);
        }
    }

    public static texture texture(String id, boolean clamp) {
        image image = null;

        try {
            image = io.image(id + ".png");
        } catch (Exception e) {
            System.out.println("texture error: " + e);
            System.exit(0);
        }

        int gl_wrap;

        if (clamp)
            gl_wrap = GL_CLAMP_TO_EDGE;
        else
            gl_wrap = GL_REPEAT;

        return texture(image, gl_wrap);
    }

    public static texture texture(image image, int gl_wrap) {
        int gl_texture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, gl_texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, gl_wrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, gl_wrap);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.buffer);

        return new texture(image.width, image.height, gl_texture);
    }

    private static int shader(String file, int type) {
        String source = io.text(file);

        int shader = glCreateShader(type);

        glShaderSource(shader, source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("shader error (" + file + "): " + glGetShaderInfoLog(shader, 512));
            System.exit(0);
        }

        return shader;
    }

    public static int make_program(String id, char v) {
        int vertex;

        if (v == 's')
            vertex = shader("glsl/screen_space.v", GL_VERTEX_SHADER);
        else
            vertex = shader("glsl/" + id + ".v", GL_VERTEX_SHADER);

        int fragment = shader("glsl/" + id + ".f", GL_FRAGMENT_SHADER);

        int program = glCreateProgram();

        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("program error: " + glGetShaderInfoLog(program, 512));
            System.exit(0);
        }

        glDeleteShader(vertex);
        glDeleteShader(fragment);

        glUseProgram(program);
        glUniform1i(glGetUniformLocation(program, "u_texture0"), 0);
        glUniform1i(glGetUniformLocation(program, "u_texture1"), 1);
        glUniform1i(glGetUniformLocation(program, "u_texture2"), 2);
        glUniform1i(glGetUniformLocation(program, "u_texture3"), 3);
        glUseProgram(0);

        return program;
    }

    public static void program(int id) {
        program = id;
        glUseProgram(program);
    }

    public static void texture0(int id) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public static void texture1(int id) {
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public static void texture2(int id) {
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public static void texture3(int id) {
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public static void framebuffer(int id) {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    public static void sub_image(int id, int width, int height, ByteBuffer data) {
        glBindTexture(GL_TEXTURE_2D, id);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, data);
    }

    /*
     * public static void perspective(float[] perspective, float[] view_matrix,
     * float x, float y, float z, float sin_x, float cos_x, float sin_y, float
     * cos_y) { matrix.identity(view_matrix);
     * 
     * matrix.rotate_x(view_matrix, sin_x, cos_x); matrix.rotate_y(view_matrix,
     * sin_y, cos_y);
     * 
     * for (int i = 0; i < 16; i++) model_view_matrix[i] = view_matrix[i];
     * 
     * matrix.translate(model_view_matrix, x, y, z);
     * matrix.multiply(model_view_projection_matrix, perspective,
     * model_view_matrix); }
     */

    public static void orthographic(float[] orthographic, float x, float y) {
        matrix.identity(model_view_matrix);
        matrix.translate(model_view_matrix, x, y, 0);
        matrix.multiply(model_view_projection_matrix, orthographic, model_view_matrix);
    }

    public static void mvp() {
        matrix_buffer.clear();
        matrix_buffer.put(model_view_projection_matrix);
        matrix_buffer.flip();

        int location = glGetUniformLocation(program, "u_mvp");

        glUniformMatrix4fv(location, false, matrix_buffer);
    }

    public static void mv() {
        matrix_buffer.clear();
        matrix_buffer.put(model_view_matrix);
        matrix_buffer.flip();

        int location = glGetUniformLocation(program, "u_mv");

        glUniformMatrix4fv(location, false, matrix_buffer);
    }

    public static void clear_color(float red, float green, float blue) {
        glClearColor(red, green, blue, 1.0f);
    }

    public static void clear_color(float red, float green, float blue, float alpha) {
        glClearColor(red, green, blue, alpha);
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void clear_color() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public static void clear_depth() {
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public static void depth_on() {
        glEnable(GL_DEPTH_TEST);
    }

    public static void depth_off() {
        glDisable(GL_DEPTH_TEST);
    }

    public static void depth_mask_on() {
        glDepthMask(true);
    }

    public static void depth_mask_off() {
        glDepthMask(false);
    }

    public static void polygon_offset(float factor, float units) {
        glPolygonOffset(factor, units);
    }

    public static void uniform_int(String uniform, int value) {
        glUniform1i(glGetUniformLocation(program, uniform), value);
    }

    public static void uniform(String uniform, float value) {
        glUniform1f(glGetUniformLocation(program, uniform), value);
    }

    public static void uniform(String uniform, float a, float b) {
        glUniform2f(glGetUniformLocation(program, uniform), a, b);
    }

    public static void uniform(String uniform, float a, float b, float c) {
        glUniform3f(glGetUniformLocation(program, uniform), a, b, c);
    }

    public static void uniform_float_array(String uniform, float[] array) {
        glUniform3fv(glGetUniformLocation(program, uniform), array);
    }

    public static void uniform(String uniform, float[] m) {
        matrix_buffer.clear();
        matrix_buffer.put(m);
        matrix_buffer.flip();

        int location = glGetUniformLocation(program, uniform);

        glUniformMatrix4fv(location, false, matrix_buffer);
    }

    public static void view(int x, int y, int width, int height) {
        glViewport(x, y, width, height);
        glScissor(x, y, width, height);
    }

    public static void draw_elements(buffer b) {
        glBindVertexArray(b.vao);
        glDrawElements(GL_TRIANGLES, b.index_count, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public static void draw_elements_lines(buffer b) {
        glBindVertexArray(b.vao);
        glDrawElements(GL_LINES, b.index_count, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}
