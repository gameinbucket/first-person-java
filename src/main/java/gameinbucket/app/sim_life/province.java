package gameinbucket.app.sim_life;

import gameinbucket.app.sim_landscape.universe;

public class province {
    public final static int village = 0;
    public final static int town = 1;
    public final static int city = 2;
    public final static int capital = 3;
    public final static int tower = 4;
    public final static int fortress = 5;
    public final static int castle = 6;

    public final static String[] types = { "village", "town", "city", "capital", "tower", "fortress", "castle", };

    public universe universe;

    public String name;

    public int type;

    public government ownership;

    public province(universe u) {
        universe = u;

        name = language.word(universe.seed);

        type = village;

        ownership = null;

        System.out.println("the " + types[type] + " " + name + " was built");
    }

    public void simulate() {
        if (universe.seed.nextInt(2) == 0) {
            switch (type) {
            case village:
                if (universe.seed.nextInt(100) == 0) {
                    type = town;
                    System.out.println("the village " + name + " became a town");
                }
                break;
            case town:
                if (universe.seed.nextInt(100) == 0) {
                    type = city;
                    System.out.println("the town " + name + " became a city");
                }
                break;
            }
        } else if (ownership == null && universe.governments.size() > 0 && universe.seed.nextInt(100) == 0) {
            government g = universe.governments.get(universe.seed.nextInt(universe.governments.size()));

            g.provinces.add(this);

            ownership = g;

            System.out.println(
                    "the " + types[type] + " " + name + " joined the " + government.types[g.type] + " of " + g.name);
        }
    }
}
