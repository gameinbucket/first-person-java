package sim_landscape;


public class hexagon
{
    public static final int ocean = 0;
    public static final int river = 1;
    public static final int mountain = 2;
    public static final int land = 3;
    
    public static final int water = 0;
    public static final int tundra = 1;
    public static final int boreal = 2;
    public static final int temperate = 3;
    public static final int grassland = 4;
    public static final int tropical = 5;
    public static final int desert = 6;
    
    public hexagon[] adjacent = new hexagon[6];
    
    public int copy = -1;
    
    public int tectonic_id = -1;
    public int tectonic_force = -1;
    public int elevation = -1;
    public int type = -1;
    public int pressure = -1;
    public int default_wind = -1;
    public int wind = -1;
    public int current = -1;
    public int precipitation = -1;
    public int temperature = -1;
    public int biome = -1;
    
    public hexagon()
    {
        
    }
}
