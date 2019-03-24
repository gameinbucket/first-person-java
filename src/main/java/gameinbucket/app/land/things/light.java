
package gameinbucket.app.land.things;

public class light {
    public float x;
    public float y;
    public float z;

    public float r;
    public float g;
    public float b;

    public final float radius;

    public final float inverse_radius_squared;

    public light(float x, float z, float y, float r, float g, float b, float radius) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.r = r;
        this.g = g;
        this.b = b;

        this.radius = radius;

        this.inverse_radius_squared = 1.0f / (radius * radius);
    }
}
