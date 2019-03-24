
package gameinbucket.app.sim_life;

import java.util.ArrayList;
import java.util.HashMap;

public class language_new {
    public static final int person = 0;
    public static final int place = 1;
    public static final int thing = 2;
    public static final int verb = 3;
    public static final int adjective = 4;
    public static final int conjunction = 5;

    public static final char[] vowels = { 'a', 'e', 'i', 'o', 'u' };
    public static final char[] holds = { 'f', 'l', 'm', 'n', 'r', 's', 'v', 'x', 'z' };
    public static final char[] percusives = { 'b', 'c', 'd', 'k', 'p', 't' };
    public static final char[] other = { 'g', 'h', 'j', 'q', 'w', 'y' };

    public char[] alphabet;

    public ArrayList<String> phonotactics = new ArrayList<String>();

    public HashMap<String, String> dictionary = new HashMap<>();

    public language_new() {

    }

    public void create(String english, int type) {

    }
}
