package client;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.system.MemoryUtil.NULL;

public class client
{
    public long window;
    
    public int width = 2 * 500;
    public int height = 1 * 500;

    public mouse mouse;
    public keyboard keyboard;
    
    private IntBuffer int_width = BufferUtils.createIntBuffer(1);
    private IntBuffer int_height = BufferUtils.createIntBuffer(1);
    
    private DoubleBuffer double_mouse_x = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer double_mouse_y = BufferUtils.createDoubleBuffer(1);
    
    public state state;
    
    public static void main(String[] args)
    {
        System.setProperty("org.lwjgl.librarypath", "C:\\Users\\Nathan\\Google Drive\\Projects\\Java\\Desktop\\lwjgl\\native");
        
        client client = new client();
        client.run();
    }
    
    public client()
    {
        GLFWErrorCallback.createPrint(System.err).set();
        
        if (!glfwInit()) throw new IllegalStateException("unable to initialize glfw");
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        window = glfwCreateWindow(width, height, "world of sword & sigil", NULL, NULL);
        
        if (window == NULL) throw new RuntimeException("failed to create glfw window");

        mouse = new mouse();
        keyboard = new keyboard();
        
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwGetCursorPos(window, double_mouse_x, double_mouse_y);
        
        glfwSetKeyCallback(window, keyboard);
        glfwSetMouseButtonCallback(window, mouse);
        
        GLFWVidMode video_mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (video_mode.width() - width) / 2, (video_mode.height() - height) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        
        GL.createCapabilities();
        
        if (GL.getCapabilities().GL_ARB_map_buffer_range == false)
        {
            System.out.println("map buffer range not supported");
            System.exit(1);
        }
        
        if (GL.getCapabilities().GL_ARB_buffer_storage == false)
        {
            System.out.println("buffer storage not supported");
            System.exit(1);
        }
        
        state = new state(this);
    }
    
    public void run()
    {
        while (!glfwWindowShouldClose(window))
        {
            glfwPollEvents();
            
            if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) break;
            
            glfwGetWindowSize(window, int_width, int_height);
            
            int width = int_width.get(0);
            int height = int_height.get(0);
            
            if (this.width != width || this.height != height)
            {
                this.width = width;
                this.height = height;
                
                state.resize();
            }
            
            double mouse_x = double_mouse_x.get(0);
            double mouse_y = double_mouse_y.get(0);
            
            glfwGetCursorPos(window, double_mouse_x, double_mouse_y);
            
            mouse.dx = (int)(double_mouse_x.get(0) - mouse_x);
            mouse.dy = (int)(double_mouse_y.get(0) - mouse_y);
            
            state.events();
            state.draw();
            
            glfwSwapBuffers(window);
        }
        
        state.close();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
