
package gameinbucket.app.client.graphics;

import java.nio.ByteBuffer;

public class image {
    public int width;
    public int height;

    public ByteBuffer buffer;

    public image(int w, int h, ByteBuffer b) {
        width = w;
        height = h;
        buffer = b;
    }
}
