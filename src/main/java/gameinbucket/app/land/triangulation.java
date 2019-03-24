
package gameinbucket.app.land;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class triangulation {
    private static boolean console = false;

    private triangulation() {

    }

    private static double angle(vector a, vector b) {
        double value = Math.atan2(a.y - b.y, a.x - b.x);

        if (value < 0)
            value += Math.PI * 2.0;

        return value;
    }

    private static double interior_angle(vector a, vector b, vector c) {
        double angle1 = Math.atan2(a.y - b.y, a.x - b.x);
        double angle2 = Math.atan2(b.y - c.y, b.x - c.x);

        double result = angle2 - angle1;

        if (result < 0)
            result += Math.PI * 2;

        return result;
    }

    private static boolean intersect(vector a, vector b, vector c, vector d) {
        double a1 = b.y - a.y;
        double b1 = a.x - b.x;
        double c1 = (b.x * a.y) - (a.x * b.y);

        double r3 = ((a1 * c.x) + (b1 * c.y) + c1);
        double r4 = ((a1 * d.x) + (b1 * d.y) + c1);

        if ((r3 != 0) && (r4 != 0) && r3 * r4 >= 0)
            return false;

        double a2 = d.y - c.y;
        double b2 = c.x - d.x;
        double c2 = (d.x * c.y) - (c.x * d.y);

        double r1 = (a2 * a.x) + (b2 * a.y) + c2;
        double r2 = (a2 * b.x) + (b2 * b.y) + c2;

        if ((r1 != 0) && (r2 != 0) && r1 * r2 >= 0)
            return false;

        double denom = (a1 * b2) - (a2 * b1);

        if (denom == 0)
            return false;

        return true;
    }

    private static boolean contains(vector[] poly, vector p) {
        boolean odd = false;

        int j = poly.length - 1;

        for (int i = 0; i < poly.length; i++) {
            if ((poly[i].y > p.y != poly[j].y > p.y)
                    && (p.x < (poly[j].x - poly[i].x) * (double) (p.y - poly[i].y) / (double) (poly[j].y - poly[i].y)
                            + poly[i].x))
                odd = !odd;

            j = i;
        }

        return odd;
    }

    private static boolean valid(ArrayList<vector> polygon, vector a, vector b, vector c) {
        if (interior_angle(a, b, c) > Math.PI)
            return false;

        vector[] triangle = { a, b, c };

        for (int i = 0; i < polygon.size(); i++) {
            vector p = polygon.get(i);

            if (p == a || p == b || p == c)
                continue;

            if (contains(triangle, p))
                return false;
        }

        return true;
    }

    private static boolean valid(ArrayList<polygon_vertex> polygon, vector a, vector b) {
        for (int i = 0; i < polygon.size(); i++) {
            polygon_vertex p = polygon.get(i);

            vector c = p.point;
            vector d = p.last.get(0).point;

            if (a != c && a != d && b != c && b != d && intersect(a, b, c, d))
                return false;
        }

        return true;
    }

    private static polygon_vertex find(ArrayList<polygon_vertex> points, vector test) {
        for (int j = 0; j < points.size(); j++) {
            polygon_vertex original = points.get(j);

            if (test.x == original.point.x && test.y == original.point.y)
                return original;
        }

        return null;
    }

    private static ArrayList<polygon_vertex> populate(sector sector, boolean floor) {
        ArrayList<polygon_vertex> points = new ArrayList<>();

        for (sector inner : sector.inside) {
            if (floor) {
                if (inner.c_floor == false)
                    continue;
            } else {
                if (inner.c_ceil == false)
                    continue;
            }

            populate_with_vectors(points, inner);
        }

        for (sector inner : sector.inside) {
            if (floor) {
                if (inner.c_floor == false)
                    continue;
            } else {
                if (inner.c_ceil == false)
                    continue;
            }

            populate_links(points, inner, false);
        }

        clean_population(points);

        populate_with_vectors(points, sector);
        populate_links(points, sector, true);

        for (int i = 0; i < points.size(); i++)
            points.get(i).index = i;

        return points;
    }

    private static void populate_with_vectors(ArrayList<polygon_vertex> points, sector sector) {
        vector[] vecs = sector.points;

        for (int i = 0; i < vecs.length; i++) {
            vector v = vecs[i];

            polygon_vertex original = find(points, v);

            if (original == null) {
                int j;

                for (j = 0; j < points.size(); j++) {
                    polygon_vertex o = points.get(j);

                    if (v.y < o.point.y || (v.y == o.point.y && v.x > o.point.x))
                        continue;

                    break;
                }

                points.add(j, new polygon_vertex(v));
            }
        }
    }

    private static void populate_links(ArrayList<polygon_vertex> points, sector sector, boolean clockwise) {
        vector[] vecs = sector.points;

        for (int i = 0; i < vecs.length; i++) {
            polygon_vertex original = find(points, vecs[i]);

            polygon_vertex last;
            polygon_vertex next;

            if (clockwise) {
                if (i == 0)
                    last = find(points, vecs[vecs.length - 1]);
                else
                    last = find(points, vecs[i - 1]);

                if (i == vecs.length - 1)
                    next = find(points, vecs[0]);
                else
                    next = find(points, vecs[i + 1]);
            } else {
                if (i == 0)
                    next = find(points, vecs[vecs.length - 1]);
                else
                    next = find(points, vecs[i - 1]);

                if (i == vecs.length - 1)
                    last = find(points, vecs[0]);
                else
                    last = find(points, vecs[i + 1]);
            }

            if (original.last.isEmpty()) {
                original.last.add(last);
            } else {
                polygon_vertex using_last = original.last.get(0);

                double angle = angle(using_last.point, original.point);

                if (angle(last.point, original.point) < angle) {
                    original.last.add(0, last);
                }
            }

            if (original.next.isEmpty()) {
                original.next.add(next);
            } else {
                polygon_vertex using_next = original.next.get(0);

                double angle = angle(using_next.point, original.point);

                if (angle(next.point, original.point) < angle) {
                    original.next.add(0, next);
                }
            }
        }
    }

    private static void clean_population(ArrayList<polygon_vertex> points) {
        ArrayList<polygon_vertex> remaining = new ArrayList<>(points.size());

        for (int i = 0; i < points.size(); i++)
            remaining.add(points.get(i));

        while (remaining.isEmpty() == false) {
            polygon_vertex start = remaining.get(0);
            polygon_vertex current = start;

            HashSet<polygon_vertex> todo = new HashSet<>();
            HashSet<polygon_vertex> temp = new HashSet<>();
            HashSet<polygon_vertex> dead = new HashSet<>();

            do {
                current.perimeter = true;

                remaining.remove(current);

                while (current.next.size() != 1) {
                    todo.add(current.next.get(1));
                    current.next.remove(1);
                }

                while (current.last.size() != 1)
                    current.last.remove(1);

                current = current.next.get(0);
            } while (current != start);

            while (todo.isEmpty() == false) {
                for (polygon_vertex pv : todo) {
                    dead.add(pv);

                    for (int i = 0; i < pv.next.size(); i++) {
                        polygon_vertex n = pv.next.get(i);

                        if (!n.perimeter && !todo.contains(n) && !temp.contains(n) && !dead.contains(n))
                            temp.add(n);
                    }
                }

                todo.clear();

                for (polygon_vertex pv : temp)
                    todo.add(pv);

                temp.clear();
            }

            for (polygon_vertex pv : dead) {
                remaining.remove(pv);
                points.remove(pv);
            }
        }
    }

    private static ArrayList<polygon_vertex> classify(ArrayList<polygon_vertex> points) {
        ArrayList<polygon_vertex> start = new ArrayList<>();
        ArrayList<polygon_vertex> merge = new ArrayList<>();
        ArrayList<polygon_vertex> split = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            polygon_vertex pos = points.get(i);
            polygon_vertex pre = pos.last.get(0);
            polygon_vertex nex = pos.next.get(0);

            boolean reflex = interior_angle(pre.point, pos.point, nex.point) > Math.PI;
            boolean both_above = pre.point.y < pos.point.y && nex.point.y <= pos.point.y;
            boolean both_below = pre.point.y >= pos.point.y && nex.point.y >= pos.point.y;
            boolean collinear = nex.point.y == pos.point.y;

            if (both_above && reflex) {
                // if (start.isEmpty() || pre != start.get(start.size() - 1))
                {
                    start.add(pos);
                    if (console)
                        System.out.println("start " + pos.point.x + ", " + pos.point.y);
                }
            } else if (both_above && reflex == false) {
                if (collinear == false) {
                    split.add(pos);
                    if (console)
                        System.out.println("split " + pos.point.x + ", " + pos.point.y);
                }
            } else if (both_below && reflex) {
                // end
            } else if (both_below && reflex == false) {
                if (collinear == false) {
                    merge.add(pos);
                    if (console)
                        System.out.println("merge " + pos.point.x + ", " + pos.point.y);
                }
            } else if (pre.point.y < nex.point.y) {
                // regular up
            } else {
                // regular down
            }
        }

        for (int i = 0; i < merge.size(); i++) {
            polygon_vertex p = merge.get(i);

            int j;

            for (j = p.index + 1; j < points.size(); j++) {
                polygon_vertex diagonal = points.get(j);

                if (valid(points, p.point, diagonal.point))
                    break;
            }

            polygon_vertex d = points.get(j);

            p.merge = true;

            p.next.add(d);
            p.last.add(d);

            d.last.add(p);
            d.next.add(p);

            if (console)
                System.out.println("-- adding merge diagonal from (" + p.point.x + ", " + p.point.y + ") to ("
                        + d.point.x + ", " + d.point.y + ")");
        }

        for (int i = 0; i < split.size(); i++) {
            polygon_vertex p = split.get(i);

            int j;

            for (j = p.index - 1; j > -1; j--) {
                polygon_vertex diagonal = points.get(j);

                if (valid(points, p.point, diagonal.point))
                    break;
            }

            polygon_vertex d = points.get(j);

            if (d.merge) {
                if (console)
                    System.out.println("-- split using same diagonal from merge (" + p.point.x + ", " + p.point.y
                            + ") to (" + d.point.x + ", " + d.point.y + ")");
                continue;
            }

            start.add(d);

            p.next.add(d);
            p.last.add(d);

            d.last.add(p);
            d.next.add(p);

            if (console)
                System.out.println("-- adding split diagonal from (" + p.point.x + ", " + p.point.y + ") to ("
                        + d.point.x + ", " + d.point.y + ")");
        }

        return start;
    }

    private static void clip(sector sector, boolean floor, ArrayList<triangle> triangles, ArrayList<vector> monotone,
            float scale) {
        int i = 0;

        while (monotone.size() > 3) {
            int i_last = i - 1;
            int i_next = i + 1;

            if (i_last == -1)
                i_last += monotone.size();
            if (i_next == monotone.size())
                i_next -= monotone.size();

            vector last = monotone.get(i_last);
            vector pos = monotone.get(i);
            vector next = monotone.get(i_next);

            if (valid(monotone, last, pos, next)) {
                if (floor)
                    triangles.add(new triangle(sector.floor, sector.t_floor, last, pos, next, floor, scale));
                else
                    triangles.add(new triangle(sector.ceil, sector.t_ceil, next, pos, last, floor, scale));

                monotone.remove(i);
            } else {
                i++;
            }

            if (i == monotone.size())
                i = 0;
        }

        if (floor)
            triangles.add(new triangle(sector.floor, sector.t_floor, monotone.get(0), monotone.get(1), monotone.get(2),
                    floor, scale));
        else
            triangles.add(new triangle(sector.ceil, sector.t_ceil, monotone.get(2), monotone.get(1), monotone.get(0),
                    floor, scale));
    }

    private static void build(sector sector, boolean floor, ArrayList<triangle> triangles, float scale) {
        if (floor) {
            if (sector.t_floor == -1)
                return;
        } else {
            if (sector.t_ceil == -1)
                return;
        }

        ArrayList<polygon_vertex> points = populate(sector, floor);

        if (console) {
            System.out.println();
            System.out.println("total of " + points.size() + " points");
            System.out.println(sector.inside.size() + " inner polygons");
            for (polygon_vertex pv : points)
                pv.print();
            System.out.println();
        }

        ArrayList<polygon_vertex> monotone_begin = classify(points);

        if (console)
            System.out.println();
        if (console)
            System.out.println(monotone_begin.size() + " monotone polygons");

        try {
            for (int i = 0; i < monotone_begin.size(); i++) {
                ArrayList<vector> monotone = new ArrayList<>();

                polygon_vertex first = monotone_begin.get(i);

                polygon_vertex nex = first.next.get(0);
                polygon_vertex pos = first;

                do {
                    monotone.add(new vector(pos.point));

                    polygon_vertex pre = null;

                    double angle = Double.MAX_VALUE;

                    for (int j = 0; j < pos.last.size(); j++) {
                        polygon_vertex test = pos.last.get(j);

                        vector a = nex.point;
                        vector b = pos.point;
                        vector c = test.point;

                        double angle1 = Math.atan2(a.x - b.x, a.y - b.y);
                        double angle2 = Math.atan2(b.x - c.x, b.y - c.y);

                        double interior = angle2 - angle1;

                        if (interior < 0)
                            interior += Math.PI * 2;

                        interior += Math.PI;

                        if (interior > Math.PI * 2)
                            interior -= Math.PI * 2;

                        if (console)
                            System.out.println(nex.point.x + ", " + nex.point.y + " | " + pos.point.x + ", "
                                    + pos.point.y + " | " + test.point.x + ", " + test.point.y + " | "
                                    + (interior * 180.0 / Math.PI));

                        if (interior < angle) {
                            pre = test;
                            angle = interior;
                        }
                    }

                    pos.next.remove(nex);
                    pos.last.remove(pre);

                    nex = pos;
                    pos = pre;
                } while (pos != first);

                if (console)
                    System.out.println(
                            "polygon has " + monotone.size() + " points " + (monotone.size() - 2) + " triangles");

                clip(sector, floor, triangles, monotone, scale);
            }
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public static void make(sector sector, float scale) {
        ArrayList<triangle> triangles = new ArrayList<>();

        build(sector, true, triangles, scale);
        build(sector, false, triangles, scale);

        sector.triangles = new triangle[triangles.size()];

        for (int i = 0; i < triangles.size(); i++)
            sector.triangles[i] = triangles.get(i);
    }
}
