package gameinbucket.app.sim_lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class document {
    public static ArrayList<String> get(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        ArrayList<String> lines = new ArrayList<>();

        String line = br.readLine();

        while (line != null) {
            lines.add(line);

            line = br.readLine();
        }

        return lines;
    }
}
