package gameinbucket.app.client;

import gameinbucket.app.client.graphics.image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public abstract class io {
    private static final String path = "res/";

    private io() {

    }

    public static String text(String file) {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Files.newInputStream(Paths.get(path + file))));

            String line;

            while ((line = reader.readLine()) != null)
                text.append(line).append("\n");

            reader.close();
        } catch (Exception e) {
            System.err.println("unable to read file " + (path + file));
            e.printStackTrace();
            System.exit(0);
        }

        return text.toString();
    }

    public static image image(String file) {
        try {
            BufferedImage image = ImageIO.read(Files.newInputStream(Paths.get(path + file)));

            int width = image.getWidth();
            int height = image.getHeight();

            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];

                    buffer.put((byte) ((pixel >> 16) & 0xff));
                    buffer.put((byte) ((pixel >> 8) & 0xff));
                    buffer.put((byte) (pixel & 0xff));
                    buffer.put((byte) ((pixel >> 24) & 0xff));
                }
            }

            buffer.flip();

            return new image(width, height, buffer);
        } catch (Exception e) {
            System.err.println("unable to read image " + file + " " + e);
            System.exit(0);
        }

        return null;
    }
}
