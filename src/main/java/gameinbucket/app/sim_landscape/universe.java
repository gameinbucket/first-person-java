package gameinbucket.app.sim_landscape;

import gameinbucket.app.sim_life.government;
import gameinbucket.app.sim_life.province;
import java.util.ArrayList;
import java.util.Random;

public class universe {
    public int sleep = 100;

    public painter paint;

    public Random seed;

    public int year;

    public planet planet;

    public ArrayList<government> governments;
    public ArrayList<province> provinces;

    public universe(painter p) {
        paint = p;

        seed = new Random();

        governments = new ArrayList<>();
        provinces = new ArrayList<>();

        year = 1;

        planet = new planet(this, paint.planet_width, paint.planet_height);
    }

    public void begin() {
        planet.create();

        /*
         * for (int i = 0; i < 1000; i++) { System.out.println("year: " + year);
         * 
         * for (int j = 0; j < provinces.size(); j++) provinces.get(j).simulate(); for
         * (int j = 0; j < governments.size(); j++) governments.get(j).simulate();
         * 
         * int event = seed.nextInt(10);
         * 
         * if (event == 0) { provinces.add(new province(this)); } else if (event == 1) {
         * ArrayList<province> open_provinces = new ArrayList<>();
         * 
         * for (int j = 0; j < provinces.size(); j++) { province p = provinces.get(j);
         * 
         * if (p.ownership == null) open_provinces.add(p); }
         * 
         * if (open_provinces.size() > 1) { government g = new government(this);
         * 
         * province a = open_provinces.get(0); province b = open_provinces.get(0);
         * 
         * g.provinces.add(a); g.provinces.add(b);
         * 
         * a.ownership = g; b.ownership = g;
         * 
         * governments.add(g); } }
         * 
         * year++; }
         */
    }
}
