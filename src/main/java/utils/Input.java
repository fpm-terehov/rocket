package utils;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Input extends GLFWKeyCallback {
    public static boolean[] keys = new boolean[65535];
    public static boolean[] keys1 = new boolean[65535];
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if(action == GLFW_PRESS)
            keys[key] = !keys[key];
        keys1[key] = action != GLFW_RELEASE;
    }
}
