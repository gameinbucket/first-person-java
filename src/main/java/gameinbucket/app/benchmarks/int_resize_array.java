
package gameinbucket.app.benchmarks;

public class int_resize_array {
    public static void main(String[] arguments) {
        long before = System.currentTimeMillis();

        int total = 2000000;

        int[] array = new int[100];

        int count = 0;

        for (int i = 0; i < total; i++) {
            if (i == array.length) {
                int[] temp = new int[array.length + 10];
                System.arraycopy(array, 0, temp, 0, array.length);
                array = temp;
            }

            array[i] = count++;
        }

        System.out.println("time: " + (System.currentTimeMillis() - before));
    }

    // time: 125250
}
