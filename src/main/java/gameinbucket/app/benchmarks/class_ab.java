
package gameinbucket.app.benchmarks;

public class class_ab {
    public int value_a;
    public int value_b;

    public class_ab(int v) {
        value_a = v;
        value_b = v;
    }

    public void set(int v) {
        value_b = v;
    }

    public int get() {
        return value_b;
    }
}
