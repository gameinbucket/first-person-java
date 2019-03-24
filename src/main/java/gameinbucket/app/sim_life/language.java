package gameinbucket.app.sim_life;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class language {
    private static LinkedHashMap<Character, ArrayList<String>> phoneme_categories = new LinkedHashMap<Character, ArrayList<String>>();

    private static ArrayList<String> phonotactics = new ArrayList<String>();

    static {
        ArrayList<String> vowels = new ArrayList<String>();

        vowels.add("a");
        vowels.add("e");
        vowels.add("i");
        vowels.add("o");
        vowels.add("u");

        phoneme_categories.put('v', vowels);

        ArrayList<String> holds = new ArrayList<String>();

        holds.add("f");
        holds.add("l");
        holds.add("m");
        holds.add("n");
        holds.add("r");
        holds.add("v");
        holds.add("z");
        holds.add("x");

        phoneme_categories.put('h', holds);

        ArrayList<String> percusives = new ArrayList<String>();

        percusives.add("c");
        percusives.add("b");
        percusives.add("d");
        percusives.add("p");
        percusives.add("t");

        phoneme_categories.put('p', percusives);

        phonotactics.add("pvpvhpv");
        phonotactics.add("vhhvv");
        phonotactics.add("hvhvhv");
        phonotactics.add("phvpvph");
        phonotactics.add("phvv");
        phonotactics.add("phvvhpv");
        phonotactics.add("pvhvphvhvh");
        phonotactics.add("pvhpvh");
        phonotactics.add("hvphvvph");
        phonotactics.add("phvhvpvh");
        phonotactics.add("pvhpv");

        phonotactics.add("hvhv");
        phonotactics.add("hvhvhv");

        phonotactics.add("pv");
        phonotactics.add("pvh");
        phonotactics.add("vh");
        phonotactics.add("hvh");
        phonotactics.add("vv");
    }

    public static String word(Random seed) {
        String word = "";

        String type = phonotactics.get(seed.nextInt(phonotactics.size()));

        for (int i = 0; i < type.length(); i++) {
            ArrayList<String> phonemes = phoneme_categories.get(type.charAt(i));

            String letter = phonemes.get(seed.nextInt(phonemes.size()));

            word += letter;
        }

        return word;
    }
}
