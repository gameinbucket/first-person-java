package gameinbucket.app.client.graphics;

public class occluder {
    private float[][] frustum;

    public occluder() {
        frustum = new float[2][4];
    }

    private void normalize(float[] f) {
        float n = (float) Math.sqrt(f[0] * f[0] + f[1] * f[1] + f[2] * f[2]);

        f[0] /= n;
        f[1] /= n;
        f[2] /= n;
        f[3] /= n;
    }

    public void update(float[] mvp) {
        // right

        frustum[0][0] = mvp[3] - mvp[0];
        frustum[0][1] = mvp[7] - mvp[4];
        frustum[0][2] = mvp[11] - mvp[8];
        frustum[0][3] = mvp[15] - mvp[12];

        normalize(frustum[0]);

        // left

        frustum[1][0] = mvp[3] + mvp[0];
        frustum[1][1] = mvp[7] + mvp[4];
        frustum[1][2] = mvp[11] + mvp[8];
        frustum[1][3] = mvp[15] + mvp[12];

        normalize(frustum[1]);
    }

    public boolean point(float x, float z) {
        if (frustum[0][0] * x + frustum[0][2] * z + frustum[0][3] <= 0)
            return true;
        if (frustum[1][0] * x + frustum[1][2] * z + frustum[1][3] <= 0)
            return true;

        return false;
    }

    public boolean circle(float x, float z, float radius) {
        if (frustum[0][0] * x + frustum[0][2] * z + frustum[0][3] <= -radius)
            return true;
        if (frustum[1][0] * x + frustum[1][2] * z + frustum[1][3] <= -radius)
            return true;

        return false;
    }
}
