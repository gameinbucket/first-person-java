package gameinbucket.app.sim_life;

public class life {
    public static final int min = 0;
    public static final int max = 100;

    public int intelligence; // grunts, language, speaking, reading, use of items, etc
    public int altruism; // willingness to help others
    public int lawful; // willingness to obey rules set in place by the group
    public int extrovert; // desire to interact with others
    public int actionable; // desire to act rather than watch
    public int aggresive; // willingness to fight over something
    public int boredom; // how quickly something commonplace becomes unsatisfying
    public int loyalty; // willingness to break commitments
    public int curiosity; // willigness to try new things or accept things differently

    public life[] diet_life;
    public plant[] diet_plant;
    public int reproduction_type; // self, mate, queen
    public int likes;
    public int dislikes;

    public int height;
    public int weight;
    public int skin_type; // skin, fur, hair, scale, rubbery
    public boolean tail;
    public boolean wings;
    public int limbs;
    public int limb_type; // legs, half leg half arm, tentacles,
    public int horns;
    public int skin_pattern; // spotted, striped
    public int skeleton_type; // normal, exoskeleton

    public life[] parents;
    public life[] children;

    public int clothing;

    // sentient life
    public int plans;
    public int occupation;
    public int allegiance;

    // skills
    public int speech;
    public int stealth;
    public int reading;
    public int armor;
    public int combat;
    public int magic;
    public int construction;
    public int herblore;
    public int ranged;
    public int endurance;
    public int smithing;
    public int strength;
    public int dexterity;

    public life reproduce(life other) {
        life child = new life();

        child.intelligence = (intelligence + other.intelligence) / 2;

        return child;
    }
}
