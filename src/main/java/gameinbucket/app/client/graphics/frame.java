package gameinbucket.app.client.graphics;

public class frame {
    public int fbo;

    public int[] textures;
    public int[] texture_internal_format;
    public int[] texture_format;
    public int[] texture_type;

    public int width;
    public int height;

    public boolean linear;
    public boolean depth;

    public int[] draw_buffers;

    public int depth_texture;

    public frame(int width, int height, int[] internal_format, int[] format, int[] type, boolean linear,
            boolean depth) {
        this.width = width;
        this.height = height;
        this.linear = linear;
        this.depth = depth;

        if (internal_format.length != format.length || internal_format.length != type.length) {
            System.err.println("opengl frame incorrect data");
            System.exit(1);
        }

        this.textures = new int[format.length];

        texture_internal_format = internal_format;
        texture_format = format;
        texture_type = type;

        opengl.framebuffer(this);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        opengl.update_framebuffer(this);
    }
}
