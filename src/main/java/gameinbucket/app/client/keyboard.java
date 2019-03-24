package gameinbucket.app.client;

import java.util.HashMap;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWKeyCallback;

public class keyboard extends GLFWKeyCallback {
    public HashMap<Integer, Boolean> keys = new HashMap<>();

    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys.put(key, action != GLFW_RELEASE);
    }

    public boolean down(int key) {
        return keys.containsKey(key) ? keys.get(key) : false;
    }
}
