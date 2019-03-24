package gameinbucket.app.sim_life;

import gameinbucket.app.sim_landscape.universe;
import java.util.ArrayList;

public class government {
    public final static int kingdom = 0;
    public final static int oligarchy = 1;
    public final static int republic = 2;
    public final static int total = 3;

    public final static String[] types = { "kingdom", "oligarchy", "republic", };

    public universe universe;

    public String name;

    public int type;

    public ArrayList<province> provinces;
    public ArrayList<government> wars;

    public government(universe u) {
        universe = u;

        name = language.word(universe.seed);

        type = u.seed.nextInt(total);

        provinces = new ArrayList<>();
        wars = new ArrayList<>();

        System.out.println("the " + types[type] + " of " + name + " was formed");
    }

    public void simulate() {
        if (universe.seed.nextInt(100) == 0 && universe.governments.size() > 1) {
            for (int j = 0; j < universe.governments.size(); j++) {
                government g = universe.governments.get(j);

                if (g == this)
                    continue;

                if (wars.contains(g))
                    continue;

                wars.add(g);
                g.wars.add(this);

                System.out.println("the " + types[type] + " of " + name + " declared war against the " + types[g.type]
                        + " of " + g.name);

                break;
            }
        } else if (wars.size() > 0 && universe.seed.nextInt(100) == 0) {
            government g = wars.get(universe.seed.nextInt(wars.size()));

            System.out.println("the " + types[type] + " of " + name + " declared peace with the " + types[g.type]
                    + " of " + g.name);

            wars.remove(g);
            g.wars.remove(this);
        } else if (wars.size() > 0 && universe.seed.nextInt(10) == 0) {
            government g = wars.get(universe.seed.nextInt(wars.size()));
            province p = g.provinces.get(universe.seed.nextInt(g.provinces.size()));

            if (universe.seed.nextInt(3) == 0) {
                p.ownership = this;
                provinces.add(p);
                g.provinces.remove(p);

                System.out.println("the " + types[type] + " of " + name + " defeated the " + types[g.type] + " of "
                        + g.name + " at the " + province.types[p.type] + " " + p.name);

                if (g.provinces.size() == 0) {
                    System.out.println("the " + types[g.type] + " of " + g.name + " was dissolved");

                    universe.governments.remove(g);

                    for (int j = 0; j < g.wars.size(); j++)
                        g.wars.get(j).wars.remove(g);
                }
            } else {
                System.out.println("the " + types[type] + " of " + name + " fought the " + types[g.type] + " of "
                        + g.name + " at the " + province.types[p.type] + " " + p.name);
            }
        }
    }
}
