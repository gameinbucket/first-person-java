
package gameinbucket.app.benchmarks;

public class int_object_array_no_locality {
    public static object_int[] array = new object_int[2000000];

    public static vector[] vec_a = new vector[2000000];
    public static vector[] vec_b = new vector[2000000];

    public static void main(String[] arguments) {
        long before = System.currentTimeMillis();

        int x = 0;
        int y = 2000000;

        for (int i = 0; i < array.length; i++) {
            vec_a[i] = new vector();
            vec_b[i] = new vector();

            vec_a[i].x = x;
            vec_a[i].y = y;

            vec_b[i].x = y;
            vec_b[i].y = x;

            array[i] = new object_int(0); // allocating as needed, after many other allocations

            array[i].value = vec_a[i].x * vec_b[i].x + vec_a[i].y * vec_b[i].y;
        }

        for (int i = 2; i < array.length; i++)
            array[i].value = array[i - 1].value + array[i].value + array[i - 2].value;

        for (int i = array.length - 1; i > 2; i--)
            array[i].value = array[i - 1].value + array[i].value + array[i - 2].value;

        for (int i = 2; i < array.length; i++)
            array[i].value = array[i - 1].value + array[i].value + array[i - 2].value;

        for (int i = array.length - 1; i > 2; i--)
            array[i].value = array[i - 1].value + array[i].value + array[i - 2].value;

        System.out.println("time: " + (System.currentTimeMillis() - before));
    }

    // time: 1200
}
