package gameinbucket.app.sim_landscape;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class painter extends JPanel {
    private int hex_size = 7;
    private int hex_width = (int) (Math.sqrt(3) / 2 * (hex_size * 2));
    private int hex_height = (int) (hex_size * 2 * 0.75f);

    private int[] hex_x = new int[6];
    private int[] hex_y = new int[6];

    private Polygon poly = new Polygon(new int[6], new int[6], 6);

    public int planet_width = 80;
    public int planet_height = 60;

    private int width = planet_width * hex_width + 200;
    private int height = planet_height * hex_height + 200;

    private int pad_x = 20;
    private int pad_y = 100;

    private static final int view_plate_elevation = 0;
    private static final int view_plate_drift = 1;
    private static final int view_plate_forces = 2;
    private static final int view_ocean_land_elevation = 3;
    private static final int view_pressure = 4;
    private static final int view_default_winds = 5;
    private static final int view_winds = 6;
    private static final int view_ocean_currents = 7;
    private static final int view_temperature = 8;
    private static final int view_precipitation = 9;
    private static final int view_biome = 10;

    private int view = view_biome;

    public universe universe;

    public static void main(String[] arguments) {
        JFrame frame = new JFrame("world of sword & sigil");

        painter p = new painter();

        frame.getContentPane().add(p, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(p.width, p.height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        p.run();
    }

    public painter() {
        for (int i = 0; i < 6; i++) {
            double d = 60 * i + 30;
            double r = Math.PI / 180.0 * d;

            hex_x[i] = (int) (hex_size * Math.cos(r));
            hex_y[i] = (int) (hex_size * Math.sin(r));
        }

        JButton tectonic_elevation = new JButton("tectonic elevation");
        JButton tectonic_drift = new JButton("tectonic drift");
        JButton tectonic_forces = new JButton("tectonic forces");
        JButton ocean_land_elevation = new JButton("ocean, land, elevation");
        JButton pressure = new JButton("pressure");
        JButton default_winds = new JButton("default winds");
        JButton winds = new JButton("winds");
        JButton ocean_currents = new JButton("ocean currents");
        JButton temperature = new JButton("temperature");
        JButton precipitation = new JButton("precipitation");
        JButton biome = new JButton("biome");

        tectonic_elevation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_plate_elevation;
                repaint();
            }
        });
        tectonic_drift.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_plate_drift;
                repaint();
            }
        });
        tectonic_forces.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_plate_forces;
                repaint();
            }
        });
        ocean_land_elevation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_ocean_land_elevation;
                repaint();
            }
        });
        pressure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_pressure;
                repaint();
            }
        });
        default_winds.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_default_winds;
                repaint();
            }
        });
        winds.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_winds;
                repaint();
            }
        });
        ocean_currents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_ocean_currents;
                repaint();
            }
        });
        temperature.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_temperature;
                repaint();
            }
        });
        precipitation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_precipitation;
                repaint();
            }
        });
        biome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view = view_biome;
                repaint();
            }
        });

        add(tectonic_elevation);
        add(tectonic_drift);
        add(tectonic_forces);
        add(ocean_land_elevation);
        add(pressure);
        add(default_winds);
        add(winds);
        add(ocean_currents);
        add(temperature);
        add(precipitation);
        add(biome);
    }

    public void run() {
        universe = new universe(this);

        universe.begin();
    }

    private void tectonic_elevation(Graphics g, planet p, hexagon h) {
        g.setColor(Color.getHSBColor(0.0f, 0.0f, p.tectonics[h.tectonic_id].elevation / 100.0f));
        g.fillPolygon(poly);
    }

    private void tectonic_drift(Graphics g, planet p, hexagon h) {
        g.setColor(Color.getHSBColor(0.0f, 0.9f, p.tectonics[h.tectonic_id].drift / 360.0f));
        g.fillPolygon(poly);
    }

    private void tectonic_forces(Graphics g, hexagon h) {
        if (h.tectonic_force < 0)
            g.setColor(Color.getHSBColor(0.7f, 0.9f, Math.abs(h.tectonic_force) / 100.0f));
        else
            g.setColor(Color.getHSBColor(0.0f, 0.9f, Math.abs(h.tectonic_force) / 100.0f));

        g.fillPolygon(poly);
    }

    private void ocean_land_elevation(Graphics g, planet p, hexagon h) {
        if (h.type == hexagon.river)
            g.setColor(Color.getHSBColor(0.7f, 0.9f, h.elevation / 100.0f));
        else if (h.elevation <= p.oceans)
            g.setColor(Color.getHSBColor(0.7f, 0.9f, h.elevation / 100.0f));
        else if (h.elevation >= p.mountains)
            g.setColor(Color.getHSBColor(0.0f, 0.0f, (h.elevation - 50) / 100.0f));
        else
            g.setColor(Color.getHSBColor(0.3f, 0.9f, h.elevation / 100.0f));

        g.fillPolygon(poly);
    }

    private void pressure(Graphics g, hexagon h) {
        g.setColor(Color.getHSBColor(0.0f, 0.0f, h.pressure / 100.0f));
        g.fillPolygon(poly);
    }

    private void default_winds(Graphics g, hexagon h, int x, int y) {
        pressure(g, h);
        g.fillPolygon(poly);

        final int step = hex_size / 2;

        double rad = h.default_wind * Math.PI / 180.0;
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);

        int ax = x + (int) (cos * step);
        int ay = y + (int) (sin * step);

        g.setColor(Color.red);
        g.drawLine(x, y, ax, ay);

        rad = (h.default_wind - 135) * Math.PI / 180.0;
        sin = Math.sin(rad);
        cos = Math.cos(rad);
        g.drawLine(ax, ay, ax + (int) (cos * step / 2), ay + (int) (sin * step / 2));

        rad = (h.default_wind + 135) * Math.PI / 180.0;
        sin = Math.sin(rad);
        cos = Math.cos(rad);
        g.drawLine(ax, ay, ax + (int) (cos * step / 2), ay + (int) (sin * step / 2));
    }

    private void winds(Graphics g, hexagon h, int x, int y) {
        pressure(g, h);
        g.fillPolygon(poly);

        final int step = hex_size / 2;

        double rad = h.wind * Math.PI / 180.0;
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);

        int ax = x + (int) (cos * step);
        int ay = y + (int) (sin * step);

        g.setColor(Color.red);
        g.drawLine(x, y, ax, ay);

        rad = (h.wind - 135) * Math.PI / 180.0;
        sin = Math.sin(rad);
        cos = Math.cos(rad);
        g.drawLine(ax, ay, ax + (int) (cos * step / 2), ay + (int) (sin * step / 2));

        rad = (h.wind + 135) * Math.PI / 180.0;
        sin = Math.sin(rad);
        cos = Math.cos(rad);
        g.drawLine(ax, ay, ax + (int) (cos * step / 2), ay + (int) (sin * step / 2));
    }

    private void ocean_currents(Graphics g, hexagon h) {
        g.setColor(Color.getHSBColor(0.0f, 0.0f, h.current / 360.0f));
        g.fillPolygon(poly);
    }

    private void temperature(Graphics g, hexagon h) {
        g.setColor(Color.getHSBColor(0.0f, 1.0f, h.temperature / 100.0f));
        g.fillPolygon(poly);
    }

    private void precipitation(Graphics g, hexagon h) {
        g.setColor(Color.getHSBColor(0.7f, 0.9f, h.precipitation / 100.0f));
        g.fillPolygon(poly);
    }

    private void biome(Graphics g, hexagon h) {
        if (h.biome == hexagon.water)
            g.setColor(Color.getHSBColor(0.7f, 0.9f, h.elevation / 100.0f));
        else if (h.biome == hexagon.tundra)
            g.setColor(Color.getHSBColor(0.5f, 0.9f, h.elevation / 100.0f));
        else if (h.biome == hexagon.boreal)
            g.setColor(Color.getHSBColor(0.4f, 0.9f, h.elevation / 100.0f));
        else if (h.biome == hexagon.temperate)
            g.setColor(Color.getHSBColor(0.3f, 0.9f, h.elevation / 100.0f));
        else if (h.biome == hexagon.grassland)
            g.setColor(Color.getHSBColor(0.2f, 0.9f, h.elevation / 100.0f));
        else if (h.biome == hexagon.tropical)
            g.setColor(Color.getHSBColor(0.1f, 0.9f, h.elevation / 100.0f));
        else if (h.biome == hexagon.desert)
            g.setColor(Color.getHSBColor(0.0f, 0.9f, h.elevation / 100.0f));

        g.fillPolygon(poly);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        if (universe == null)
            return;

        planet p = universe.planet;

        for (int y = 0; y < p.height; y++) {
            for (int x = 0; x < p.width; x++) {
                int offset = 0;

                if ((y & 1) == 0)
                    offset = hex_width / 2;

                hexagon hex = p.map[x][y];

                int px = pad_x + x * hex_width + offset;
                int py = pad_y + y * hex_height;

                for (int n = 0; n < 6; n++) {
                    poly.xpoints[n] = hex_x[n] + px;
                    poly.ypoints[n] = hex_y[n] + py;
                }

                if (p.precipitation_created) {
                    switch (view) {
                    case view_plate_elevation:
                        tectonic_elevation(g, p, hex);
                        break;
                    case view_plate_drift:
                        tectonic_drift(g, p, hex);
                        break;
                    case view_plate_forces:
                        tectonic_forces(g, hex);
                        break;
                    case view_ocean_land_elevation:
                        ocean_land_elevation(g, p, hex);
                        break;
                    case view_pressure:
                        pressure(g, hex);
                        break;
                    case view_default_winds:
                        default_winds(g, hex, px, py);
                        break;
                    case view_winds:
                        winds(g, hex, px, py);
                        break;
                    case view_ocean_currents:
                        ocean_currents(g, hex);
                        break;
                    case view_temperature:
                        temperature(g, hex);
                        break;
                    case view_precipitation:
                        precipitation(g, hex);
                        break;
                    case view_biome:
                        biome(g, hex);
                        break;
                    }
                } else if (p.biome_created)
                    biome(g, hex);
                else if (p.temperature_created)
                    temperature(g, hex);
                else if (p.currents_created)
                    ocean_currents(g, hex);
                else if (p.winds_created)
                    winds(g, hex, px, py);
                else if (p.pressure_created)
                    pressure(g, hex);
                else if (p.ocean_land_created)
                    ocean_land_elevation(g, p, hex);
                else if (p.tectonics_created)
                    tectonic_elevation(g, p, hex);
            }
        }
    }
}
