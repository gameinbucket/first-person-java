package client.graphics;

public class sprite
{
    public static float scale = 1.0f;
    
    public float width;
    public float height;
    
    public int texture;
    
    public sprite(texture texture)
    {
        this.width = texture.width / 2 * scale;
        this.height = texture.height * scale;
        
        this.texture = texture.id;
    }
    
    public static void scale(float s)
    {
        scale = s;
    }
}
