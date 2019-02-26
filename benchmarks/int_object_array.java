
package benchmarks;

public class int_object_array
{
    public static void main(String[] arguments)
    {
        long before = System.currentTimeMillis();
        
        object_int[] array = new object_int[2000000];
        
        int count = 0;
        
        for (int i = 0; i < array.length; i++)
            array[i] = new object_int(count++);
        
        for (int i = 0; i < array.length; i++)
            array[i].value = array[i].value < 1000 ? array[i].value - 1 : array[i].value + 1;
        
        System.out.println("time: " + (System.currentTimeMillis() - before));
    }
    
    // time: 57
}
