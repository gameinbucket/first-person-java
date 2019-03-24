package gameinbucket.app.client.graphics;

public abstract class matrix {
    private static float[] temp = new float[16];
    private static float[] copied = new float[16];
    private static float[] tmp = new float[12];
    private static float[] src = new float[16];
    private static float[] dst = new float[16];

    private matrix() {

    }

    public static void identity(float[] m) {
        m[0] = 1.0f;
        m[1] = 0.0f;
        m[2] = 0.0f;
        m[3] = 0.0f;

        m[4] = 0.0f;
        m[5] = 1.0f;
        m[6] = 0.0f;
        m[7] = 0.0f;

        m[8] = 0.0f;
        m[9] = 0.0f;
        m[10] = 1.0f;
        m[11] = 0.0f;

        m[12] = 0.0f;
        m[13] = 0.0f;
        m[14] = 0.0f;
        m[15] = 1.0f;
    }

    public static void orthographic(float[] m, float left, float top, float right, float bottom, float near,
            float far) {
        m[0] = 2.0f / (right - left);
        m[1] = 0.0f;
        m[2] = 0.0f;
        m[3] = 0.0f;

        m[4] = 0.0f;
        m[5] = 2.0f / (top - bottom);
        m[6] = 0.0f;
        m[7] = 0.0f;

        m[8] = 0.0f;
        m[9] = 0.0f;
        m[10] = -2.0f / (far - near);
        m[11] = 0.0f;

        m[12] = -((right + left) / (right - left));
        m[13] = -((top + bottom) / (top - bottom));
        m[14] = -((far + near) / (far - near));
        m[15] = 1.0f;
    }

    public static void perspective(float[] m, float fov, float near, float far, float aspect) {
        float top = near * (float) Math.tan(fov * Math.PI / 360.0f);
        float bottom = -top;
        float left = bottom * aspect;
        float right = top * aspect;

        frustum(m, left, right, bottom, top, near, far);
    }

    public static void frustum(float[] m, float left, float right, float bottom, float top, float near, float far) {
        m[0] = (2.0f * near) / (right - left);
        m[1] = 0.0f;
        m[2] = 0.0f;
        m[3] = 0.0f;

        m[4] = 0.0f;
        m[5] = (2.0f * near) / (top - bottom);
        m[6] = 0.0f;
        m[7] = 0.0f;

        m[8] = (right + left) / (right - left);
        m[9] = (top + bottom) / (top - bottom);
        m[10] = -(far + near) / (far - near);
        m[11] = -1.0f;

        m[12] = 0.0f;
        m[13] = 0.0f;
        m[14] = -(2.0f * far * near) / (far - near);
        m[15] = 0.0f;
    }

    public static void translate(float[] m, float x, float y, float z) {
        m[12] = x * m[0] + y * m[4] + z * m[8] + m[12];
        m[13] = x * m[1] + y * m[5] + z * m[9] + m[13];
        m[14] = x * m[2] + y * m[6] + z * m[10] + m[14];
        m[15] = x * m[3] + y * m[7] + z * m[11] + m[15];
    }

    public static void rotate_x(float[] m, float sin, float cos) {
        temp[0] = 1.0f;
        temp[1] = 0.0f;
        temp[2] = 0.0f;
        temp[3] = 0.0f;

        temp[4] = 0.0f;
        temp[5] = cos;
        temp[6] = sin;
        temp[7] = 0.0f;

        temp[8] = 0.0f;
        temp[9] = -sin;
        temp[10] = cos;
        temp[11] = 0.0f;

        temp[12] = 0.0f;
        temp[13] = 0.0f;
        temp[14] = 0.0f;
        temp[15] = 1.0f;

        for (int i = 0; i < 16; i++)
            copied[i] = m[i];

        multiply(m, copied, temp);
    }

    public static void rotate_y(float[] m, float sin, float cos) {
        temp[0] = cos;
        temp[1] = 0.0f;
        temp[2] = -sin;
        temp[3] = 0.0f;

        temp[4] = 0.0f;
        temp[5] = 1.0f;
        temp[6] = 0.0f;
        temp[7] = 0.0f;

        temp[8] = sin;
        temp[9] = 0.0f;
        temp[10] = cos;
        temp[11] = 0.0f;

        temp[12] = 0.0f;
        temp[13] = 0.0f;
        temp[14] = 0.0f;
        temp[15] = 1.0f;

        for (int i = 0; i < 16; i++)
            copied[i] = m[i];

        multiply(m, copied, temp);
    }

    void rotate_z(float[] m, float sin, float cos) {
        temp[0] = cos;
        temp[1] = sin;
        temp[2] = 0.0f;
        temp[3] = 0.0f;

        temp[4] = -sin;
        temp[5] = cos;
        temp[6] = 0.0f;
        temp[7] = 0.0f;

        temp[8] = 0.0f;
        temp[9] = 0.0f;
        temp[10] = 1.0f;
        temp[11] = 0.0f;

        temp[12] = 0.0f;
        temp[13] = 0.0f;
        temp[14] = 0.0f;
        temp[15] = 1.0f;

        for (int i = 0; i < 16; i++)
            copied[i] = m[i];

        multiply(m, copied, temp);
    }

    public static void multiply(float[] m, float[] m1, float[] m2) {
        m[0] = m1[0] * m2[0] + m1[4] * m2[1] + m1[8] * m2[2] + m1[12] * m2[3];
        m[1] = m1[1] * m2[0] + m1[5] * m2[1] + m1[9] * m2[2] + m1[13] * m2[3];
        m[2] = m1[2] * m2[0] + m1[6] * m2[1] + m1[10] * m2[2] + m1[14] * m2[3];
        m[3] = m1[3] * m2[0] + m1[7] * m2[1] + m1[11] * m2[2] + m1[15] * m2[3];

        m[4] = m1[0] * m2[4] + m1[4] * m2[5] + m1[8] * m2[6] + m1[12] * m2[7];
        m[5] = m1[1] * m2[4] + m1[5] * m2[5] + m1[9] * m2[6] + m1[13] * m2[7];
        m[6] = m1[2] * m2[4] + m1[6] * m2[5] + m1[10] * m2[6] + m1[14] * m2[7];
        m[7] = m1[3] * m2[4] + m1[7] * m2[5] + m1[11] * m2[6] + m1[15] * m2[7];

        m[8] = m1[0] * m2[8] + m1[4] * m2[9] + m1[8] * m2[10] + m1[12] * m2[11];
        m[9] = m1[1] * m2[8] + m1[5] * m2[9] + m1[9] * m2[10] + m1[13] * m2[11];
        m[10] = m1[2] * m2[8] + m1[6] * m2[9] + m1[10] * m2[10] + m1[14] * m2[11];
        m[11] = m1[3] * m2[8] + m1[7] * m2[9] + m1[11] * m2[10] + m1[15] * m2[11];

        m[12] = m1[0] * m2[12] + m1[4] * m2[13] + m1[8] * m2[14] + m1[12] * m2[15];
        m[13] = m1[1] * m2[12] + m1[5] * m2[13] + m1[9] * m2[14] + m1[13] * m2[15];
        m[14] = m1[2] * m2[12] + m1[6] * m2[13] + m1[10] * m2[14] + m1[14] * m2[15];
        m[15] = m1[3] * m2[12] + m1[7] * m2[13] + m1[11] * m2[14] + m1[15] * m2[15];
    }

    public static void inverse(float[] m, float[] m1) {
        for (int i = 0; i < 4; i++) {
            src[i + 0] = m1[i * 4 + 0];
            src[i + 4] = m1[i * 4 + 1];
            src[i + 8] = m1[i * 4 + 2];
            src[i + 12] = m1[i * 4 + 3];
        }

        tmp[0] = src[10] * src[15];
        tmp[1] = src[11] * src[14];
        tmp[2] = src[9] * src[15];
        tmp[3] = src[11] * src[13];
        tmp[4] = src[9] * src[14];
        tmp[5] = src[10] * src[13];
        tmp[6] = src[8] * src[15];
        tmp[7] = src[11] * src[12];
        tmp[8] = src[8] * src[14];
        tmp[9] = src[10] * src[12];
        tmp[10] = src[8] * src[13];
        tmp[11] = src[9] * src[12];

        dst[0] = tmp[0] * src[5] + tmp[3] * src[6] + tmp[4] * src[7];
        dst[0] -= tmp[1] * src[5] + tmp[2] * src[6] + tmp[5] * src[7];
        dst[1] = tmp[1] * src[4] + tmp[6] * src[6] + tmp[9] * src[7];
        dst[1] -= tmp[0] * src[4] + tmp[7] * src[6] + tmp[8] * src[7];
        dst[2] = tmp[2] * src[4] + tmp[7] * src[5] + tmp[10] * src[7];
        dst[2] -= tmp[3] * src[4] + tmp[6] * src[5] + tmp[11] * src[7];
        dst[3] = tmp[5] * src[4] + tmp[8] * src[5] + tmp[11] * src[6];
        dst[3] -= tmp[4] * src[4] + tmp[9] * src[5] + tmp[10] * src[6];
        dst[4] = tmp[1] * src[1] + tmp[2] * src[2] + tmp[5] * src[3];
        dst[4] -= tmp[0] * src[1] + tmp[3] * src[2] + tmp[4] * src[3];
        dst[5] = tmp[0] * src[0] + tmp[7] * src[2] + tmp[8] * src[3];
        dst[5] -= tmp[1] * src[0] + tmp[6] * src[2] + tmp[9] * src[3];
        dst[6] = tmp[3] * src[0] + tmp[6] * src[1] + tmp[11] * src[3];
        dst[6] -= tmp[2] * src[0] + tmp[7] * src[1] + tmp[10] * src[3];
        dst[7] = tmp[4] * src[0] + tmp[9] * src[1] + tmp[10] * src[2];
        dst[7] -= tmp[5] * src[0] + tmp[8] * src[1] + tmp[11] * src[2];

        tmp[0] = src[2] * src[7];
        tmp[1] = src[3] * src[6];
        tmp[2] = src[1] * src[7];
        tmp[3] = src[3] * src[5];
        tmp[4] = src[1] * src[6];
        tmp[5] = src[2] * src[5];
        tmp[6] = src[0] * src[7];
        tmp[7] = src[3] * src[4];
        tmp[8] = src[0] * src[6];
        tmp[9] = src[2] * src[4];
        tmp[10] = src[0] * src[5];
        tmp[11] = src[1] * src[4];

        dst[8] = tmp[0] * src[13] + tmp[3] * src[14] + tmp[4] * src[15];
        dst[8] -= tmp[1] * src[13] + tmp[2] * src[14] + tmp[5] * src[15];
        dst[9] = tmp[1] * src[12] + tmp[6] * src[14] + tmp[9] * src[15];
        dst[9] -= tmp[0] * src[12] + tmp[7] * src[14] + tmp[8] * src[15];
        dst[10] = tmp[2] * src[12] + tmp[7] * src[13] + tmp[10] * src[15];
        dst[10] -= tmp[3] * src[12] + tmp[6] * src[13] + tmp[11] * src[15];
        dst[11] = tmp[5] * src[12] + tmp[8] * src[13] + tmp[11] * src[14];
        dst[11] -= tmp[4] * src[12] + tmp[9] * src[13] + tmp[10] * src[14];
        dst[12] = tmp[2] * src[10] + tmp[5] * src[11] + tmp[1] * src[9];
        dst[12] -= tmp[4] * src[11] + tmp[0] * src[9] + tmp[3] * src[10];
        dst[13] = tmp[8] * src[11] + tmp[0] * src[8] + tmp[7] * src[10];
        dst[13] -= tmp[6] * src[10] + tmp[9] * src[11] + tmp[1] * src[8];
        dst[14] = tmp[6] * src[9] + tmp[11] * src[11] + tmp[3] * src[8];
        dst[14] -= tmp[10] * src[11] + tmp[2] * src[8] + tmp[7] * src[9];
        dst[15] = tmp[10] * src[10] + tmp[4] * src[8] + tmp[9] * src[9];
        dst[15] -= tmp[8] * src[9] + tmp[11] * src[10] + tmp[5] * src[8];

        float det = 1.0f / (src[0] * dst[0] + src[1] * dst[1] + src[2] * dst[2] + src[3] * dst[3]);

        for (int i = 0; i < 16; i++)
            m[i] = dst[i] * det;
    }

    public static float vector_x;
    public static float vector_y;
    public static float vector_z;

    public static void multiply_vector(float[] m, float x, float y, float z) {
        vector_x = m[0] * x + m[4] * y + m[8] * z + m[12];
        vector_y = m[1] * x + m[5] * y + m[9] * z + m[13];
        vector_z = m[2] * x + m[6] * y + m[10] * z + m[14];
    }

    public static void pitch(float[] matrix, float pitch) {
        float cos = (float) Math.cos(pitch);
        float sin = (float) Math.sin(pitch);

        matrix[0] = 1.0f;
        matrix[1] = 0.0f;
        matrix[2] = 0.0f;
        matrix[3] = 0.0f;

        matrix[4] = 0.0f;
        matrix[5] = cos;
        matrix[6] = sin;
        matrix[7] = 0.0f;

        matrix[8] = 0.0f;
        matrix[9] = -sin;
        matrix[10] = cos;
        matrix[11] = 0.0f;

        matrix[12] = 0.0f;
        matrix[13] = 0.0f;
        matrix[14] = 0.0f;
        matrix[15] = 1.0f;
    }

    public static void yaw(float[] matrix, float yaw) {
        float cos = (float) Math.cos(yaw);
        float sin = (float) Math.sin(yaw);

        matrix[0] = cos;
        matrix[1] = 0.0f;
        matrix[2] = -sin;
        matrix[3] = 0.0f;

        matrix[4] = 0.0f;
        matrix[5] = 1.0f;
        matrix[6] = 0.0f;
        matrix[7] = 0.0f;

        matrix[8] = sin;
        matrix[9] = 0.0f;
        matrix[10] = cos;
        matrix[11] = 0.0f;

        matrix[12] = 0.0f;
        matrix[13] = 0.0f;
        matrix[14] = 0.0f;
        matrix[15] = 1.0f;
    }

    public static void roll(float[] matrix, float roll) {
        float cos = (float) Math.cos(roll);
        float sin = (float) Math.sin(roll);

        matrix[0] = cos;
        matrix[1] = sin;
        matrix[2] = 0.0f;
        matrix[3] = 0.0f;

        matrix[4] = -sin;
        matrix[5] = cos;
        matrix[6] = 0.0f;
        matrix[7] = 0.0f;

        matrix[8] = 0.0f;
        matrix[9] = 0.0f;
        matrix[10] = 1.0f;
        matrix[11] = 0.0f;

        matrix[12] = 0.0f;
        matrix[13] = 0.0f;
        matrix[14] = 0.0f;
        matrix[15] = 1.0f;
    }
}
