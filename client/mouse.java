package client;

import java.util.HashMap;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class mouse extends GLFWMouseButtonCallback
{
    public HashMap<Integer, Boolean> buttons = new HashMap<>();
    
    public int dx;
    public int dy;
    
    public void invoke(long window, int button, int action, int mods)
    {
        buttons.put(button, action != GLFW_RELEASE);
    }
    
    public boolean down(int button)
    {
        return buttons.containsKey(button) ? buttons.get(button) : false;
    }
}