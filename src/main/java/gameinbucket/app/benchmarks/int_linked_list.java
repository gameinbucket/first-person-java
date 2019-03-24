
package gameinbucket.app.benchmarks;

public class int_linked_list {
    public static void main(String[] arguments) {
        long before = System.currentTimeMillis();

        int total = 2000000;

        int count = 0;

        linked_list_int top = new linked_list_int(count++);
        linked_list_int bot = top;

        for (int i = 1; i < total; i++) {
            linked_list_int n = new linked_list_int(count++);

            bot.next = n;
            bot = n;
        }

        bot = top;
        while (bot != null) {
            bot.value = bot.value < 1000 ? bot.value - 1 : bot.value + 1;
            bot = bot.next;
        }

        System.out.println("time: " + (System.currentTimeMillis() - before));
    }

    // time: 150
}
