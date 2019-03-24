package gameinbucket.app.client.graphics;

public abstract class colors {
    private colors() {

    }

    public static int rgb(int red, int green, int blue) {
        return (red << 16) + (green << 8) + blue;
    }

    public static int red(int rgb) {
        return (rgb >> 16) & 0xff;
    }

    public static int green(int rgb) {
        return (rgb >> 8) & 0xff;
    }

    public static int blue(int rgb) {
        return rgb & 0xff;
    }

    public static int hsb(int hue, int saturation, int brightness) {
        return (hue << 10) + (saturation << 7) + brightness;
    }

    public static int hue(int hsb) {
        return (hsb >> 10) & 0x3f;
    }

    public static int saturation(int hsb) {
        return (hsb >> 7) & 0x07;
    }

    public static int brightness(int hsb) {
        return hsb & 0x7f;
    }

    private static double conversion(double p, double q, double t) {
        if (t < 0.0)
            t += 1.0;
        if (t > 1.0)
            t -= 1.0;
        if (t < 1.0 / 6.0)
            return p + (q - p) * 6.0 * t;
        if (t < 1.0 / 2.0)
            return q;
        if (t < 2.0 / 3.0)
            return p + (q - p) * (2.0 / 3.0 - t) * 6.0;

        return p;
    }

    public static int rgb(int hsb) {
        double hue = hue(hsb) / 63.0;
        double saturation = saturation(hsb) / 7.0;
        double brightness = brightness(hsb) / 127.0;

        int r, g, b;

        if (saturation == 0.0) {
            r = g = b = (int) (brightness * 255.0 + 0.5f);
        } else {
            double q = brightness < 0.5 ? brightness * (1.0 + saturation)
                    : brightness + saturation - brightness * saturation;
            double p = 2.0 * brightness - q;

            r = (int) (conversion(p, q, hue + 1.0 / 3.0) * 255.0 + 0.5);
            g = (int) (conversion(p, q, hue) * 255.0 + 0.5);
            b = (int) (conversion(p, q, hue - 1.0 / 3.0) * 255.0 + 0.5);
        }

        return rgb(r, g, b);
    }

    public static final int grass = rgb(hsb(16, 5, 30));
    public static final int dirt = rgb(hsb(10, 4, 21));
    public static final int light_dirt = rgb(hsb(10, 5, 23));
    public static final int swamp = rgb(hsb(26, 4, 19));
    public static final int road = rgb(hsb(0, 0, 63));
    public static final int skin = rgb(350);
    public static final int cuff = rgb(2130);
    public static final int belt = rgb(660);
    public static final int boots = rgb(400);
    public static final int pants = rgb(36133);
    public static final int shirt = rgb(21662);
    public static final int wood = rgb(185, 122, 87);
    public static final int leaf = rgb(34, 177, 36);

    public static final int find_color(String name) {
        switch (name) {
        case "skin":
            return skin;
        case "cuff":
            return cuff;
        case "belt":
            return belt;
        case "boots":
            return boots;
        case "pants":
            return pants;
        case "shirt":
            return shirt;
        case "wood":
            return wood;
        case "leaf":
            return leaf;
        default:
            return 0;
        }
    }
}
