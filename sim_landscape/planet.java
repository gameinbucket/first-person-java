package sim_landscape;


import java.util.ArrayList;
import java.util.HashSet;

public class planet
{
    public universe universe;
    
    public final int width;
    public final int height;
    
    public boolean tectonics_created = false;
    public boolean ocean_land_created = false;
    public boolean pressure_created = false;
    public boolean winds_created = false;
    public boolean currents_created = false;
    public boolean temperature_created = false;
    public boolean precipitation_created = false;
    public boolean biome_created = false;
    
    public final int oceans = 20;
    public final int mountains = 80;
    
    public tectonic[] tectonics;
    public hexagon[][] map;
    
    public planet(universe u, int w, int h)
    {
        universe = u;
        
        width = w;
        height = h;
        
        map = new hexagon[width][height];
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                map[x][y] = new hexagon();
            }
        }
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                hexagon hex = map[x][y];
                
                if ((y & 1) == 0)
                {
                    hex.adjacent[0] = hex(x + 1, y);
                    hex.adjacent[1] = hex(x + 1, y + 1);
                    hex.adjacent[2] = hex(x, y + 1);
                    hex.adjacent[3] = hex(x - 1, y);
                    hex.adjacent[4] = hex(x, y - 1);
                    hex.adjacent[5] = hex(x + 1, y - 1);
                }
                else
                {
                    hex.adjacent[0] = hex(x + 1, y);
                    hex.adjacent[1] = hex(x, y + 1);
                    hex.adjacent[2] = hex(x - 1, y + 1);
                    hex.adjacent[3] = hex(x - 1, y);
                    hex.adjacent[4] = hex(x - 1, y - 1);
                    hex.adjacent[5] = hex(x, y - 1);
                }
            }
        }
    }
    
    public hexagon hex(int x, int y)
    {
        if (x < 0) x += width;
        else if (x >= width) x -= width;
        
        if (y < 0) y += height;
        else if (y >= height) y -= height;
        
        return map[x][y];
    }
    
    public void create()
    {
        tectonics(20, 75);
        ocean_land();
        river_lake(10);
        pressure();
        winds();
        currents();
        temperature();
        precipitation();
        biome();
    }
    
    public void rest()
    {
        try
        {
            universe.paint.repaint();
            
            Thread.sleep(universe.sleep);
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(1);
        }
    }
    
    private void tectonics(int plates, int ocean_percent)
    {
        tectonics = new tectonic[plates];
        
        HashSet<hexagon> frontier = new HashSet<>();
        HashSet<hexagon> dead = new HashSet<>();
        HashSet<hexagon> found = new HashSet<>();
        
        for (int i = 0; i < plates; i++)
        {
            int x;
            int y;
            
            do
            {
                x = universe.seed.nextInt(width);
                y = universe.seed.nextInt(height);
            }
            while (map[x][y].tectonic_id > -1);
            
            map[x][y].tectonic_id = i;
            
            frontier.add(map[x][y]);
        }
        
        while (frontier.isEmpty() == false)
        {
            for (hexagon hex : frontier)
            {
                for (int j = 0; j < hex.adjacent.length; j++)
                {
                    hexagon near = hex.adjacent[j];
                    
                    if (near.tectonic_id == -1)
                    {
                        near.tectonic_id = hex.tectonic_id;
                        
                        found.add(near);
                    }
                }
                
                dead.add(hex);
            }
            
            for (hexagon hex : dead) frontier.remove(hex);
            for (hexagon hex : found) frontier.add(hex);
            
            dead.clear();
            found.clear();
        }
        
        int ocean_count = 0;
        
        for (int i = 0; i < plates; i++)
        {
            boolean oceanic = (ocean_count * 100 / plates) < ocean_percent;
            
            if (oceanic) ocean_count++;
            
            tectonics[i] = new tectonic(oceanic ? oceans / 2 : (oceans + mountains) / 2, universe.seed.nextInt(360));
        }
        
        final int intensity = 100;
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                hexagon hex = map[x][y];
                
                int drift = tectonics[hex.tectonic_id].drift;
                
                for (int j = 0; j < hex.adjacent.length; j++)
                {
                    hexagon near = hex.adjacent[j];
                    
                    int other = tectonics[near.tectonic_id].drift;
                    
                    int force = drift - other;
                    
                    if (force > 180) force -= 360;
                    else if (force < -180) force += 360;
                    
                    boolean convergent = false;
                    
                    if (force < 0)
                    {
                        if (j == 0 || j == 1 || j == 5) convergent = true;
                    }
                    else if (force > 0)
                    {
                        if (j == 2 || j == 3 || j == 4) convergent = true;
                    }
                    
                    if (convergent)
                    {
                        if (tectonics[hex.tectonic_id].elevation < tectonics[near.tectonic_id].elevation) force = -Math.abs(force);
                    }
                    else
                    {
                        force = -Math.abs(force);
                    }

                    force = force * intensity / 180;
                        
                    if (Math.abs(force) > Math.abs(hex.tectonic_force))
                    {
                        hex.tectonic_force = force;
                    }
                }
                
                if (hex.tectonic_force > intensity) hex.tectonic_force = intensity;
                else if (hex.tectonic_force < -intensity) hex.tectonic_force = -intensity;
            }
        }
        
        tectonics_created = true;
        rest();
    }
    
    private void ocean_land()
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                hexagon hex = map[x][y];
                
                hex.elevation = tectonics[hex.tectonic_id].elevation + hex.tectonic_force + universe.seed.nextInt(11) - 5;
            }
        }
        
        for (int blur = 0; blur < 3; blur++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    hexagon hex = map[x][y];
                    
                    int sum = hex.elevation;
                    
                    for (int j = 0; j < hex.adjacent.length; j++) sum += hex.adjacent[j].elevation;
                    
                    hex.copy = sum / 7;
                    
                    if (hex.copy > 100) hex.copy = 100;
                    else if (hex.copy < 0) hex.copy = 0;
                }
            }
            
            for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) map[x][y].elevation = map[x][y].copy;
        }
    }
    
    private void river_lake(int percent)
    {
        HashSet<hexagon> nearby = new HashSet<>();
        
        int mountain_count = 0;
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                hexagon hex = map[x][y];
                
                if (hex.elevation >= mountains)
                {
                    mountain_count++;
                    
                    for (int j = 0; j < hex.adjacent.length; j++)
                    {
                        hexagon near = hex.adjacent[j];
                        
                        if (near.elevation > oceans && near.elevation < mountains) nearby.add(near);
                    }
                }
            }
        }
        
        if (mountain_count > 0)
        {
            ArrayList<hexagon> available = new ArrayList<>();
            
            for (hexagon h : nearby) available.add(h);
            
            int count = 0;
            
            while (count * 100 / mountain_count < percent)
            {
                hexagon river = available.get(universe.seed.nextInt(available.size()));
                
                int last_id = -1;
                
                while (true)
                {
                    if (river.elevation <= oceans || river.type == hexagon.river) break;
                    
                    river.type = hexagon.river;
                    
                    int id = -1;
                    int lowest = river.elevation;
                    
                    for (int j = 0; j < river.adjacent.length; j++)
                    {
                        hexagon near = river.adjacent[j];
                        
                        if (near.elevation < lowest)
                        {
                            id = j;
                            lowest = near.elevation;
                        }
                    }
                    
                    if (id == -1)
                    {
                        if (last_id == -1) break;
                        else id = last_id;
                    }
                    
                    river = river.adjacent[id];
                    last_id = id;
                }
                
                count++;
            }
        }
        
        ocean_land_created = true;
        rest();
    }
    
    private void pressure()
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                hexagon hex = map[x][y];
                
                if (hex.elevation <= oceans) hex.pressure = oceans;
                else hex.pressure = hex.elevation;
            }
        }
        
        for (int blur = 0; blur < 2; blur++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    hexagon hex = map[x][y];
                    
                    int sum = hex.pressure;
                    
                    for (int j = 0; j < hex.adjacent.length; j++) sum += hex.adjacent[j].pressure;
                    
                    hex.copy = sum / 7;
                }
            }
            
            for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) map[x][y].pressure = map[x][y].copy;
        }
        
        pressure_created = true;
        rest();
    }
    
    private void winds()
    {
        final int[] zones = {135, 315, 135, 225, 45, 225};
        
        for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) map[x][y].default_wind = zones[y * zones.length / height];
        
        for (int blur = 0; blur < 0; blur++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    hexagon hex = map[x][y];
                    
                    double radian = hex.default_wind * Math.PI / 180.0;
                    
                    double nx = Math.cos(radian);
                    double ny = Math.sin(radian);
                    
                    for (int j = 0; j < hex.adjacent.length; j++)
                    {
                        radian = hex.adjacent[j].default_wind * Math.PI / 180.0;
                        
                        nx += Math.cos(radian);
                        ny += Math.sin(radian);
                    }
                    
                    hex.copy = (int)Math.round(Math.atan2(ny, nx) * 180.0 / Math.PI);
                    
                    if (hex.copy < 0) hex.copy = 0;
                    else if (hex.copy > 360) hex.copy = 360;
                }
            }
            
            for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) map[x][y].default_wind = map[x][y].copy;
        }
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                hexagon hex = map[x][y];
                
                int id = -1;
                int difference = 0;
                
                for (int j = 0; j < hex.adjacent.length; j++)
                {
                    hexagon near = hex.adjacent[j];
                    
                    int d = near.pressure - hex.pressure;
                    
                    if (d < difference)
                    {
                        id = j;
                        difference = d;
                    }
                }
                
                if (id == -1)
                {
                    hex.wind = hex.default_wind;
                }
                else
                {
                    int angle;
                    
                    switch (id)
                    {
                        case 1: angle = 45; break;
                        case 2: angle = 135; break;
                        case 3: angle = 180; break;
                        case 4: angle = 225; break;
                        case 5: angle = 315; break;
                        default: angle = 0; break;
                    }
                    
                    double def = hex.default_wind * Math.PI / 180.0;
                    double win = angle * Math.PI / 180.0;
                    
                    double nx = Math.cos(def) + Math.cos(win);
                    double ny = Math.sin(def) + Math.sin(win);
                    
                    hex.wind = (int)Math.round(Math.atan2(ny, nx) * 180.0 / Math.PI);
                    
                    if (hex.wind >= 360) hex.wind -= 360;
                }
            }
        }
        
        for (int blur = 0; blur < 1; blur++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    hexagon hex = map[x][y];
                    
                    double radian = hex.wind * Math.PI / 180.0;
                    
                    double nx = Math.cos(radian);
                    double ny = Math.sin(radian);
                    
                    for (int j = 0; j < hex.adjacent.length; j++)
                    {
                        radian = hex.adjacent[j].wind * Math.PI / 180.0;
                        
                        nx += Math.cos(radian);
                        ny += Math.sin(radian);
                    }
                    
                    hex.copy = (int)Math.round(Math.atan2(ny, nx) * 180.0 / Math.PI);
                    
                    if (hex.copy < 0) hex.copy = 0;
                    else if (hex.copy > 360) hex.copy = 360;
                }
            }
            
            for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) map[x][y].wind = map[x][y].copy;
        }
        
        winds_created = true;
        rest();
    }
    
    private void currents()
    {
        currents_created = true;
        rest();
    }
    
    private void temperature()
    {
        int half = height / 2;
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                hexagon hex = map[x][y];
                
                hex.temperature = 100 - Math.abs(y - half) * 100 / half - hex.elevation * 30 / 100;
                
                if (hex.temperature < 0) hex.temperature = 0;
            }
        }
        
        temperature_created = true;
        rest();
    }
    
    private void precipitation()
    {
        ArrayList<cloud> clouds = new ArrayList<>();
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                hexagon hex = map[x][y];
                
                if (hex.elevation <= oceans)
                {
                    hex.precipitation = 100;
                    
                    clouds.add(new cloud(x, y));
                }
                else
                {
                    hex.precipitation = 0;
                }
            }
        }
        
        while (clouds.isEmpty() == false)
        {
            for (int i = 0; i < clouds.size(); i++)
            {
                cloud c = clouds.get(i);
                
                if (c.update(this)) clouds.remove(i--);
            }
        }
        
        precipitation_created = true;
        rest();
    }
    
    private void biome()
    {
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                hexagon hex = map[x][y];
                
                if (hex.elevation <= oceans || hex.type == hexagon.river)
                {
                    hex.biome = hexagon.water;
                }
                else if (hex.precipitation > 66)
                {
                    if (hex.temperature < 10) hex.biome = hexagon.tundra;
                    else if (hex.temperature < 40) hex.biome = hexagon.boreal;
                    else if (hex.temperature < 60) hex.biome = hexagon.temperate;
                    else hex.biome = hexagon.tropical;
                }
                else if (hex.precipitation > 33)
                {
                    if (hex.temperature < 10) hex.biome = hexagon.tundra;
                    else if (hex.temperature < 40) hex.biome = hexagon.boreal;
                    else hex.biome = hexagon.grassland;
                }
                else
                {
                    if (hex.temperature < 10) hex.biome = hexagon.tundra;
                    else if (hex.temperature < 40) hex.biome = hexagon.boreal;
                    else hex.biome = hexagon.desert;
                }
            }
        }
        
        biome_created = true;
        rest();
    }
}
