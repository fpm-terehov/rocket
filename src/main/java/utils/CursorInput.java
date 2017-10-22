package utils;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorInput extends GLFWCursorPosCallback {
    @Override
    public void invoke(long window, double xpos, double ypos) {
        //System.out.println(xpos+" "+ypos);
    }
}
