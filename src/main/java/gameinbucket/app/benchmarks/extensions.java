
package gameinbucket.app.benchmarks;

public class extensions {
    public static class_b[] array = new class_b[3000000];

    public static void main(String[] arguments) {
        long before = System.currentTimeMillis();

        int count = 0;

        for (int i = 0; i < array.length; i++)
            array[i] = new class_b(count++);

        for (int i = 2; i < array.length; i++)
            array[i].set(array[i - 1].get() + array[i - 2].get());

        for (int i = 2; i < array.length; i++)
            array[i].set(array[i - 1].get() + array[i - 2].get());

        for (int i = 2; i < array.length; i++)
            array[i].set(array[i - 1].get() + array[i - 2].get());

        System.out.println("time: " + (System.currentTimeMillis() - before));
    }

    // time: 902
}
