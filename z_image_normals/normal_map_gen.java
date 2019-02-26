
package z_image_normals;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class normal_map_gen
{
    public static void main(String[] arguments)
    {
        String file_name = "baron-front-melee-0";
        
        String input = "C:\\Users\\Nathan\\Desktop\\world of sword & sigil\\src\\res\\" + file_name + ".png";
        String output = "C:\\Users\\Nathan\\Desktop\\" + file_name + "-normal.png";
        
        BufferedImage img = load(input);
        
        int width = img.getWidth();
        int height = img.getHeight();
        
        double[] luminosity = new double[width * height];
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int rgb = img.getRGB(x, y);
                
                int r = red(rgb);
                int g = green(rgb);
                int b = blue(rgb);
                
                luminosity[x + y * width] = 0.2126 * r + 0.7152 * g + 0.0722 * b;
            }
        }
        
        double[] depth = new double[width * height];
        
        double max_depth = 0.0;
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int rgb = img.getRGB(x, y);
                
                int r = red(rgb);
                int g = green(rgb);
                int b = blue(rgb);
                
                if (r == 0 && g == 0 && b == 0)
                    depth[x + y * width] = 0.0;
                else
                {
                    double d = distance_to_edge(img, width, height, x, y);
                    
                    if (d > max_depth)
                        max_depth = d;
                    
                    depth[x + y * width] = d;
                }
            }
        }
        
        for (int i = 0; i < depth.length; i++)
            depth[i] /= max_depth;
        
        double[] sobel = new double[width * height];
        
        final int[][] sobel_x = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
        };
        
        final int[][] sobel_y = {
            {-1, -2, -1},
            { 0,  0,  0},
            { 1,  2,  1}
        };
        
        double min = 0.0;
        double max = 255.0;
        
        for (int y = 1; y < height - 1; y++)
        {
            for (int x = 1; x < width - 1; x++)
            {
                double mag_x = sobel(luminosity, width, x, y, sobel_x);
                double mag_y = sobel(luminosity, width, x, y, sobel_y);
                
                double mag = Math.sqrt(mag_x * mag_x + mag_y * mag_y);
                
                if (mag < min)
                    min = mag;
                else if (mag > max)
                    max = mag;
                
                sobel[x + y * width] = mag;
            }
        }
        
        final double diff = max - min;
        
        for (int i = 0; i < sobel.length; i++)
            sobel[i] = (sobel[i] - min) / diff;
        
        final double strength = 1.0;
        
        double[] normal_x = new double[width * height];
        double[] normal_y = new double[width * height];
        double[] normal_z = new double[width * height];
        
        for (int y = 1; y < height - 1; y++)
        {
            for (int x = 1; x < width - 1; x++)
            {
                double left = sobel[x - 1 + y * width];
                double right = sobel[x + 1 + y * width];
                double top = sobel[x + (y - 1) * width];
                double bottom = sobel[x + (y + 1) * width];
                
                /*double left = depth[x - 1 + y * width];
                double right = depth[x + 1 + y * width];
                double top = depth[x + (y - 1) * width];
                double bottom = depth[x + (y + 1) * width];*/
                
                /*double left = depth[x - 1 + y * width] + sobel[x - 1 + y * width];
                double right = depth[x + 1 + y * width] + sobel[x + 1 + y * width];
                double top = depth[x + (y - 1) * width] + sobel[x + (y - 1) * width];
                double bottom = depth[x + (y + 1) * width] + sobel[x + (y + 1) * width];*/
                
                double dx_x = 1.0;
                double dx_y = 0.0;
                double dx_z = (right - left) * strength;
                
                double dy_x = 0.0;
                double dy_y = 1.0;
                double dy_z = (bottom - top) * strength;
                
                int i = x + y * width;
                
                normal_x[i] = dx_y * dy_z - dx_z * dy_y;
                normal_y[i] = dx_z * dy_x - dx_x * dy_z;
                normal_z[i] = dx_x * dy_y - dx_y * dy_x;
            }
        }
        
        BufferedImage normal_map = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int i = x + y * width;
                
                int r = (int)Math.floor((normal_x[i] + 1.0) * 127.5);
                int g = 255 - (int)Math.floor((normal_y[i] + 1.0) * 127.5);
                int b = (int)Math.floor((normal_z[i] + 1.0) * 127.5);
                
                normal_map.setRGB(x, y, rgb(r, g, b));
                
                /*int d = (int)Math.floor(depth[i] * 255.0);
                normal_map.setRGB(x, y, rgb(d, d, d));*/
            }
        }
        
        save(normal_map, output);
    }
    
    public static BufferedImage load(String path)
    {
        try
        {
            File in = new File(path);
            return ImageIO.read(in);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        
        return null;
    }
    
    public static void save(BufferedImage img, String path)
    {
        try
        {
            File out = new File(path);
            ImageIO.write(img, "png", out);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
    
    public static double distance(int x, int y, int xn, int yn)
    {
        double dx = x - xn;
        double dy = y - yn;
        
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    public static double distance_to_edge(BufferedImage img, int width, int height, int x, int y)
    {
        double dist = Double.MAX_VALUE;
        
        int perimeter = 1;
        
        boolean searching = true;
        
        while (searching)
        {
            for (int yn = y - perimeter; yn <= y + perimeter; yn++)
            {
                for (int xn = x - perimeter; xn <= x + perimeter; xn++)
                {
                    if (xn == x && yn == y)
                        continue;
                    
                    if (yn < 0 || yn >= height || xn < 0 || xn >= width)
                    {
                        double d = distance(x, y, xn, yn);
                        
                        if (d < dist)
                            dist = d;
                        
                        searching = false;
                    }
                    else
                    {
                        int rgb = img.getRGB(xn, yn);
                        
                        int r = red(rgb);
                        int g = green(rgb);
                        int b = blue(rgb);
                        
                        if (r == 0 && g == 0 && b == 0)
                        {
                            double d = distance(x, y, xn, yn);
                            
                            if (d < dist)
                                dist = d;
                            
                            searching = false;
                        }
                    }
                }
            }
            
            perimeter++;
        }
        
        return dist;
    }
    
    public static double sobel(double[] lum, int width, int x, int y, int[][] kernel)
    {
        double mag = 0.0;
        
        for (int b = 0; b < 3; b++)
        {
            for (int a = 0; a < 3; a++)
            {
                int xn = x + a - 1;
                int yn = y + b - 1;
                
                mag += lum[xn + yn * width] * kernel[a][b];
            }
        }
        
        return mag;
    }
    
    public static int rgb(int red, int green, int blue)
    {
        return (red << 16) + (green << 8) + blue;
    }
    
    public static int red(int rgb)
    {
        return (rgb >> 16) & 0xff;
    }
    
    public static int green(int rgb)
    {
        return (rgb >> 8) & 0xff;
    }
    
    public static int blue(int rgb)
    {
        return rgb & 0xff;
    }
}
