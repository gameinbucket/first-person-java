
package benchmarks;

public class no_extensions
{
    public static class_ab[] array = new class_ab[3000000];
    
    public static void main(String[] arguments)
    {
        long before = System.currentTimeMillis();
        
        int count = 0;
        
        for (int i = 0; i < array.length; i++)
            array[i] = new class_ab(count++);
        
        for (int i = 2; i < array.length; i++)
            array[i].set(array[i - 1].get() + array[i - 2].get());
        
        for (int i = 2; i < array.length; i++)
            array[i].set(array[i - 1].get() + array[i - 2].get());
        
        for (int i = 2; i < array.length; i++)
            array[i].set(array[i - 1].get() + array[i - 2].get());
        
        System.out.println("time: " + (System.currentTimeMillis() - before));
    }
    
    // time: 904
}
