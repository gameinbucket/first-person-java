
package gameinbucket.app.sim_medieval;

public class human {
    public static final int male = 0;
    public static final int female = 1;
    public static final int genders = 2;

    public static final int king = 0;
    public static final int lord = 1;
    public static final int knight = 2;
    public static final int peasant = 3;

    public static final int merchant = 0;
    public static final int farmer = 1;
    public static final int craftsman = 2;
    public static final int blacksmith = 3;
    public static final int carpenter = 4;

    public planet planet;
    public province location;
    public human allegiance;

    public human mother;
    public human father;

    public String name;
    public int age = 0;
    public int gender;
    public int rank;

    public human(planet planet, human mother, human father, String name) {
        this.planet = planet;

        this.location = mother.location;

        this.mother = mother;
        this.father = father;

        this.name = name;

        gender = planet.seed.nextInt(genders);
    }

    public void month() {
        age++;
    }
}
