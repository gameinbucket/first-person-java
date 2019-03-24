
package gameinbucket.app.land;

public class map_cell extends map_base {
    public map land;

    public linedef[] lines = new linedef[0];

    public map_cell(map land) {
        this.land = land;
    }

    public void add_line(linedef line) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] == line)
                return;
        }

        linedef[] array = new linedef[lines.length + 1];

        System.arraycopy(lines, 0, array, 0, lines.length);

        array[array.length - 1] = line;

        lines = array;
    }
}
