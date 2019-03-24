package gameinbucket.app.client.graphics;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class buffer {
    public ByteBuffer vertex_buffer;
    public ByteBuffer index_buffer;

    public FloatBuffer vertex_float_buffer;
    public IntBuffer index_integer_buffer;

    public int vao;
    public int vbo;
    public int ebo;

    public float[] vertices;
    public int[] indices;

    public int vertex_count;

    public int index_count;
    public int index_offset;

    public buffer(int pos, int col, int tex, int norm, int vertex_limit, int index_limit) {
        vertex_limit *= pos + col + tex + (norm * 3);

        vertices = new float[vertex_limit];
        indices = new int[index_limit];

        vertex_buffer = ByteBuffer.allocateDirect(vertex_limit * Float.BYTES);
        index_buffer = ByteBuffer.allocateDirect(index_limit * Integer.BYTES);

        opengl.vao(this, pos, col, tex, norm);
    }

    public void begin() {
        vertex_count = 0;

        index_count = 0;
        index_offset = 0;
    }

    public void end() {
        vertex_float_buffer.clear();
        vertex_float_buffer.put(vertices, 0, vertex_count);
        vertex_float_buffer.flip();

        index_integer_buffer.clear();
        index_integer_buffer.put(indices, 0, index_count);
        index_integer_buffer.flip();
    }
}
