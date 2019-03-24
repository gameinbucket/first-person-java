
package gameinbucket.app.sim_medieval;

public class province {
    public final static int village = 0;
    public final static int town = 1;
    public final static int city = 2;
    public final static int tower = 3;
    public final static int fortress = 4;
    public final static int castle = 5;

    public planet planet;
    public String name;
    public int type;

    public province(planet planet, String name, int type) {
        this.planet = planet;
        this.name = name;
        this.type = type;
    }
}
