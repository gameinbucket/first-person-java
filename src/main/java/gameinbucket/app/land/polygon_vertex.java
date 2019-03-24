
package gameinbucket.app.land;

import java.util.ArrayList;

public class polygon_vertex {
    public int index;

    boolean merge = false;

    public ArrayList<polygon_vertex> last = new ArrayList<>();
    public vector point;
    public ArrayList<polygon_vertex> next = new ArrayList<>();

    public boolean perimeter = false;

    public polygon_vertex(vector p) {
        this.point = p;
    }

    public void print() {
        System.out.println("point " + point.x + ", " + point.y);
    }
}
