package sim_life;


import sim_landscape.universe;
import java.math.BigDecimal;
import java.math.BigInteger;


public class province_old
{
    public final static int village = 0;
    public final static int town = 1;
    public final static int city = 2;
    public final static int capital = 3;
    public final static int tower = 4;
    public final static int fortress = 5;
    public final static int castle = 6;
    
    public final static BigDecimal half = new BigDecimal(0.5);
    
    public final static BigInteger village_limit = new BigInteger("1000");
    public final static BigInteger town_limit = new BigInteger("8000");
    
    public universe universe;
    
    public int type;
    
    public String name;
    
    public BigInteger population;
    
    public province_old(universe u)
    {
        universe = u;
        
        type = village;
        name = "hello";
        population = new BigInteger("20");
        
        System.out.println("the village " + name + " was built");
    }
    
    public void simulate()
    {
        BigDecimal growth = new BigDecimal(0.04 * universe.seed.nextDouble() + 0.01);

        population = population.add(new BigDecimal(population).multiply(growth).add(half).toBigInteger());
        
        System.out.println(name + " grew to " + population.toString() + " population");
        
        switch (type)
        {
            case village:
                if (population.compareTo(village_limit) == 1)
                {
                    type = town;
                    System.out.println("the village " + name + " became a town");
                }
                break;
            case town:
                if (population.compareTo(town_limit) == 1)
                {
                    type = city;
                    System.out.println("the town " + name + " became a city");
                }
                break;
        }
    }
}
