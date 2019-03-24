
package gameinbucket.app.land;

public class vector {
	public float x;
	public float y;

	public vector() {
		x = 0;
		y = 0;
	}

	public vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public vector(vector p) {
		x = p.x;
		y = p.y;
	}

	public void copy(vector p) {
		x = p.x;
		y = p.y;
	}

	public void clear() {
		x = 0;
		y = 0;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void invert() {
		x = -x;
		y = -y;
	}

	public void perpendicular() {
		float t = x;

		x = -y;
		y = t;
	}

	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float squared_magnitude() {
		return x * x + y * y;
	}

	public void normalize() {
		float m = magnitude();

		if (m > 0.0f)
			multiply(1.0f / m);
	}

	public void multiply(float p) {
		x *= p;
		y *= p;
	}

	public void multiply(vector p) {
		x *= p.x;
		y *= p.y;
	}

	public void multiply(vector p, float s) {
		x *= p.x * s;
		y *= p.y * s;
	}

	public void add(float ax, float ay) {
		x += ax;
		y += ay;
	}

	public void add(float x, float y, float s) {
		this.x += x * s;
		this.y += y * s;
	}

	public void add(vector p) {
		x += p.x;
		y += p.y;
	}

	public void add(vector p, float s) {
		x += p.x * s;
		y += p.y * s;
	}

	public void subtract(float x, float y) {
		this.x -= x;
		this.y -= y;
	}

	public void subtract(float x, float y, float scalar) {
		this.x -= x * scalar;
		this.y -= y * scalar;
	}

	public void subtract(vector p) {
		x -= p.x;
		y -= p.y;
	}

	public void subtract(vector p, float scalar) {
		x -= p.x * scalar;
		y -= p.y * scalar;
	}

	public float dot(vector p) {
		return x * p.x + y * p.y;
	}
}
