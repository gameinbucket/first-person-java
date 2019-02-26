
package benchmarks;

public class int_array 
{
    public static void main(String[] arguments)
    {
        long before = System.currentTimeMillis();
        
        int[] array = new int[2000000];
        
        int count = 0;
        
        for (int i = 0; i < array.length; i++)
            array[i] = count++;
        
        for (int i = 0; i < array.length; i++)
            array[i] = array[i] < 1000 ? array[i] - 1 : array[i] + 1;
        
        System.out.println("time: " + (System.currentTimeMillis() - before));
    }
    
    // time: 10
}
