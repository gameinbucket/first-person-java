package land;

public class fixed
{
    public static final int FRACSHIFT = 4;
    public static final int FRACBITS = 1 << FRACSHIFT;
    public static final int FRACUNIT = 1 << FRACBITS;
    
    public static final int ONE = whole(1);
    public static final int TWO = whole(2);
    public static final int FOUR = whole(4);
    
    public static final int PI = number(3, 14159);
    public static final int TAU = mul(PI, TWO);
    public static final int HALFPI = div(PI, TWO);
    
    public static final int TO_DEGREE = div(whole(180), PI);
    
    public static int mul(int a, int b)
    {
        return (int)(((long)a * (long)b) >> FRACBITS);
    }
    
    public static int div(int a, int b)
    {
        return (int)(((long)a << FRACBITS) / b);
    }
    
    public static int whole(int val)
    {
        return val << FRACBITS;
    }
    
    private static int digits(int x)
    {
        if (x < 0) x = -x;
        
        return (x < 10 ? 10 : (x < 100 ? 100 : (x < 1000 ? 1000 : (x < 10000 ? 10000 : 100000))));
    }
    
    public static int fraction(int val)
    {
        return (val << FRACBITS) / digits(val);
    }
    
    public static int number(int whole, int fraction)
    {
        return whole < 0 ? -(whole(-whole) + fraction(fraction)) : whole(whole) + fraction(fraction);
    }
    
    public static int integer(int val)
    {
        return val >> FRACBITS;
    }
    
    public static float floating(int val)
    {
        return (float)(val >> FRACSHIFT);
    }
    
    public static int sin(int val)
    {
        final boolean precise = false;
        
        final int b = div(FOUR, PI);
        final int c = div(-FOUR, mul(PI, PI));
        final int p = 14745;
        
        if (val > PI) val -= TAU;
        else if (val < -PI) val += TAU;
        
        int y = mul(b, val) + mul(mul(c, val), (val < 0 ? -val : val));
        
        if (precise) y = mul(p, (mul(y, (y < 0 ? -y : y)) - y)) + y;
        
        return y;
    }
    
    public static int cos(int val)
    {
        return sin(val + HALFPI);
    }
}
