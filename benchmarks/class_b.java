
package benchmarks;

public class class_b extends class_a
{
    public int value_b;
    
    public class_b(int v)
    {
        super(v);
        value_b = v;
    }
    
    public void set(int v)
    {
        value_b = v;
    }
    
    public int get()
    {
        return value_b;
    }
}
