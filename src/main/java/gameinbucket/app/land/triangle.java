
package gameinbucket.app.land;

public class triangle {
    public float height;

    public int texture;

    public vector a;
    public vector b;
    public vector c;

    public float u1;
    public float v1;

    public float u2;
    public float v2;

    public float u3;
    public float v3;

    public float normal;

    public triangle(float height, int texture, vector a, vector b, vector c, boolean floor, float scale) {
        this.height = height;
        this.texture = texture;

        this.a = a;
        this.b = b;
        this.c = c;

        u1 = a.x * scale;
        v1 = a.y * scale;

        u2 = b.x * scale;
        v2 = b.y * scale;

        u3 = c.x * scale;
        v3 = c.y * scale;

        if (floor)
            normal = 1.0f;
        else
            normal = -1.0f;
    }
}
