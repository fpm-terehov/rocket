package utils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import model.Planet;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;
import model.Rocket;

public class Main implements Runnable{

    private Thread thread;
    public boolean running = true;

    private long window;

    public static double prevmod = 0;
    double xSdvig = 0;
    double ySdvig = 0;
    double mashtab = 1/6.371e6*20;
    public static BufferedWriter br;
    public static BufferedWriter br1;
    public static Rocket rocket;
    public static Rocket rocket1;
    public static Rocket rocket2;
    public static boolean stage1 = false;
    public static boolean stage2 = false;
    public static Planet[] bodies;
    public static double realTime;

    private int width = 900, height = 900;

    private GLFWCursorPosCallback cursorPos;
    private GLFWKeyCallback keyCallback;

    public static void main(String args[]) throws IOException {
        Planet earth = new Planet();
        Vector<Double> p = new Vector<>();
        p.add(0.0);
        p.add(0.0);
        earth.position = new Vector<>(p);
        earth.m = 5.9726e24;
        earth.rad = 6.371e6;
        bodies = new Planet[3];
        bodies[1] = earth;
        Planet sun = new Planet();
        p.set(0,-1.5e11);
        sun.position = new Vector<>(p);
        sun.m = 0;//1.98892e30;
        sun.rad = 6.9551e8;
        bodies[0] = sun;
        Planet moon = new Planet();
        p.set(0,0.0);
        p.set(1,-384400000.0);
        moon.position = p;
        moon.rad = 1737000;
        moon.m = 0;
        bodies[2] = moon;
        Main game = new Main();
        rocket = new Rocket();
        fxForm.window(args, game);
    }

    public void start() throws IOException {
        running = true;
        rocket1 = new Rocket();
        rocket2 = new Rocket();
        thread = new Thread(this, rocket.name);
        thread.start();
    }

    public void init(){
        if(glfwInit() != GL_TRUE){
            System.err.println("GLFW initialization failed!");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        window = glfwCreateWindow(width, height, rocket.name, NULL, NULL);
        if(window == NULL){
            System.err.println("Could not create our Window!");
        }

        glfwSetCursorPosCallback(window, cursorPos = new CursorInput());

        glfwSetKeyCallback(window, keyCallback = new Input());

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, 500, 100);
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();
        System.out.println(glGetString(GL_VERSION));
    }


    boolean fl1 = true;
    static double speed = 0.1;

    public void update(){
        glfwPollEvents();
        fl1 = true;
        if(Input.keys[GLFW_KEY_SPACE]){
            fl1 = false;
        }
        if(Input.keys1[GLFW_KEY_W]){
            speed *= 1.1;
        }
        if(Input.keys1[GLFW_KEY_S]){
            speed /= 1.1;
        }


        if(Input.keys1[GLFW_KEY_0]){
            rocket.forceAngle = 0;
        }

        if(Input.keys1[GLFW_KEY_9]){
            rocket.force = 0;
        }

        if(Input.keys1[GLFW_KEY_A]){
            if (rocket.forceAngle >= Math.PI/3)
                return;
            rocket.forceAngle += Math.PI/5000;
            System.out.println(rocket.forceAngle);
        }
        if(Input.keys1[GLFW_KEY_D]){
            if (rocket.forceAngle <= -Math.PI/3)
                return;
            rocket.forceAngle -= Math.PI/5000;
            System.out.println(rocket.forceAngle);
        }
        if(Input.keys1[GLFW_KEY_UP]){
            ySdvig -= 0.01;
        }
        if(Input.keys1[GLFW_KEY_DOWN]){
            ySdvig += 0.01;
        }
        if(Input.keys1[GLFW_KEY_LEFT]){
            xSdvig += 0.01;
        }
        if(Input.keys1[GLFW_KEY_RIGHT]){
            xSdvig -= 0.01;
        }
        if(Input.keys1[GLFW_KEY_KP_ADD]){
            mashtab *= 1.05;
        }
        if(Input.keys1[GLFW_KEY_KP_SUBTRACT]){
            mashtab /= 1.05;
        }
    }

    static double tick = 0;
    static Vector<Vector<Double>> path = new Vector<>();
    static Vector<Vector<Double>> path1 = new Vector<>();
    static Vector<Vector<Double>> path2 = new Vector<>();

    public void render() throws InterruptedException, IOException, CloneNotSupportedException {
        if(fl1)
            tick+=speed;

        ySdvig += -6.371e6 * mashtab;
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        glClearColor(0,0,0,0f);
        rocket.start(tick,bodies);
        if(stage1) {
            rocket1.start(tick,bodies);
        }
        if(stage2)
            rocket2.start(tick,bodies);
        double rad = bodies[1].rad*mashtab;

        glColor3f(0,0.5f,0);
        glBegin(GL_TRIANGLE_STRIP);
        int tochnost = 1000;
        for (int i = 0; i <= tochnost; i++){
            glVertex2d(rad*Math.cos(2*Math.PI/tochnost*i)+ bodies[1].position.get(0)*mashtab+xSdvig,
                    rad*Math.sin(2*Math.PI/tochnost*i)+ bodies[1].position.get(1)*mashtab+ySdvig);i++;
            glVertex2d(rad*Math.cos(2*Math.PI/tochnost*i)+bodies[1].position.get(0)*mashtab+ xSdvig,
                    rad*Math.sin(2*Math.PI/tochnost*i)+ bodies[1].position.get(1)*mashtab+ySdvig);
            glVertex2d(bodies[1].position.get(0)*mashtab+ xSdvig, bodies[1].position.get(1)*mashtab+ ySdvig);
        }


        glEnd();
        glBegin(GL_TRIANGLE_STRIP);
        rad = bodies[0].rad*mashtab;
        for (int i = 0; i <= tochnost; i++){
            glVertex2d(rad*Math.cos(2*Math.PI/tochnost*i)+ bodies[0].position.get(0)*mashtab+xSdvig,
                    rad*Math.sin(2*Math.PI/tochnost*i)+ bodies[0].position.get(1)*mashtab+ySdvig);i++;
            glVertex2d(rad*Math.cos(2*Math.PI/tochnost*i)+bodies[0].position.get(0)*mashtab+ xSdvig,
                    rad*Math.sin(2*Math.PI/tochnost*i)+ bodies[0].position.get(1)*mashtab+ySdvig);
            glVertex2d(bodies[0].position.get(0)*mashtab+ xSdvig, bodies[0].position.get(1)*mashtab+ ySdvig);
        }
        glEnd();
        glBegin(GL_TRIANGLE_STRIP);
        glEnd();
        glColor3f(1,1,1);
        glBegin(GL_TRIANGLE_STRIP);
        rad = 0.02;

        glVertex2d(rad*2*Math.sin(rocket.naprAngle())+ rocket.position.get(0)*mashtab +xSdvig,
                rad*2*Math.cos(rocket.naprAngle())+ rocket.position.get(1)*mashtab+ ySdvig);

        glVertex2d(rad*1*Math.sin(rocket.naprAngle()-Math.PI*3/4)+ rocket.position.get(0)*mashtab +xSdvig,
                rad*1*Math.cos(rocket.naprAngle()-Math.PI*3/4)+ rocket.position.get(1)*mashtab+ ySdvig);
        glVertex2d(rad*1*Math.sin(rocket.naprAngle()+Math.PI*3/4)+ rocket.position.get(0)*mashtab +xSdvig,
                rad*1*Math.cos(rocket.naprAngle()+Math.PI*3/4)+ rocket.position.get(1)*mashtab+ ySdvig);

        path.add(rocket.position);
        glEnd();


        if(stage1){
            path1.add(rocket1.position);
        }


        if(stage2) {
            path2.add(rocket2.position);
        }
        glPointSize(4);
        glColor3f(1,0,0);
        glBegin(GL_POINTS);
        for (int i = 0; i < path.size(); i++){
            glVertex2d(path.get(i).get(0)*mashtab +xSdvig,path.get(i).get(1)*mashtab+ ySdvig);
        }
        glEnd();
        glColor3f(0,1,0);
        glBegin(GL_POINTS);
        for (int i = 0; i < path1.size(); i++){
            glVertex2d(path1.get(i).get(0)*mashtab +xSdvig,path1.get(i).get(1)*mashtab+ ySdvig);
        }
        glEnd();
        glColor3f(0,0,1);
        glBegin(GL_POINTS);
        for (int i = 0; i < path2.size(); i++){
            glVertex2d(path2.get(i).get(0)*mashtab +xSdvig,path2.get(i).get(1)*mashtab+ ySdvig);
        }
        glEnd();
        glColor3f(1,1,1);

        {
            glPointSize(2f);
            glColor3f(1,1,0);
            int sdvig = 0;
            if(rocket.flag) {
                if (rocket.phase == 0)
                    MyFont.drawString("first stage works", 0, 0);
                if (rocket.phase == 1)
                    MyFont.drawString("second stage works", 0, 0);
                if (rocket.phase == 2)
                    MyFont.drawString("third stage works", 0, 0);
            }else if(rocket.flag228){
                MyFont.drawString("success",0,0);
            } else {
                MyFont.drawString("failure: not enough fuel for required orbit",0,0);
            }
            sdvig -= 15;

            Double truncatedDouble = BigDecimal.valueOf(rocket.getModule(rocket.velocity))
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
            Double truncatedDouble1 = BigDecimal.valueOf(rocket.h)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
            Double truncatedDouble2 = BigDecimal.valueOf(rocket.curT)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
            MyFont.drawString("Velocity: "+ truncatedDouble +" m/s"+
                    "\nThrustangle:"+rocket.forceAngle*180/Math.PI+
                    "\nheight:"+truncatedDouble1+
                    "\nthrust:"+rocket.force/1000+" kn"+
                    "\ntime:"+truncatedDouble2,0,sdvig);
            sdvig -= 75;
            if(stage1){
                truncatedDouble = BigDecimal.valueOf(rocket1.max)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage1 max height:"+truncatedDouble,0,sdvig);
                sdvig -= 15;
                truncatedDouble = BigDecimal.valueOf(rocket1.getModule(rocket1.velocity))
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage1 velocity:"+truncatedDouble,0,sdvig);
                sdvig -= 15;
                truncatedDouble = BigDecimal.valueOf(rocket.st1sepvel)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage1 separating velocity:"+truncatedDouble,0,sdvig);
                sdvig -= 15;
                truncatedDouble = BigDecimal.valueOf(rocket.st1seph)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage1 separating height:"+truncatedDouble,0,sdvig);
                sdvig -= 15;
                truncatedDouble = BigDecimal.valueOf(rocket.st1sept)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage1 separating time:"+truncatedDouble,0,sdvig);
            }
            sdvig -= 15;
            if(!rocket1.flag2){
                truncatedDouble = BigDecimal.valueOf(rocket1.position.get(0))
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage1 fall distance:"+truncatedDouble,0,sdvig);
            }
            sdvig -= 15;
            sdvig -= 15;
            if(stage2){
                truncatedDouble = BigDecimal.valueOf(rocket2.max)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage2 max height:"+truncatedDouble,0,sdvig);
                sdvig -= 15;
                truncatedDouble = BigDecimal.valueOf(rocket2.getModule(rocket2.velocity))
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage2 velocity:"+truncatedDouble,0,sdvig);
                sdvig -= 15;
                truncatedDouble = BigDecimal.valueOf(rocket.st2sepvel)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage2 separating velocity:"+truncatedDouble,0,sdvig);
                sdvig -= 15;
                truncatedDouble = BigDecimal.valueOf(rocket.st2seph)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage2 separating height:"+truncatedDouble,0,sdvig);
                sdvig -= 15;
                truncatedDouble = BigDecimal.valueOf(rocket.st2sept)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage2 separating time:"+truncatedDouble,0,sdvig);
            }
            sdvig -= 15;
            if(!rocket2.flag2){
                truncatedDouble = BigDecimal.valueOf(rocket2.position.get(0))
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("Stage2 fall distance:"+truncatedDouble,0,sdvig);
            }
            if(!rocket.flag){
                truncatedDouble = BigDecimal.valueOf(rocket.min)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                truncatedDouble1 = BigDecimal.valueOf(rocket.max)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                truncatedDouble2 = BigDecimal.valueOf(rocket.stages[0].fuelmass+
                        rocket.stages[1].fuelmass+rocket.stages[2].fuelmass)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                double truncatedDouble3 = BigDecimal.valueOf(rocket.m)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                MyFont.drawString("reached perigee:"+truncatedDouble+
                        "\nreached apogee:"+truncatedDouble1+
                        "\ntotal delivered mass:"+truncatedDouble3+
                        "\nremaining fuel:"+truncatedDouble2+
                        "\ndelivired cargo:"+rocket.polmassa,350,0);
            }
            glColor3f(1,1,1);
        }
        /*glBegin(GL_POINTS);
        for (int i = 0; i < path.size(); i++){
            glVertex2d(path.get(i).get(0)*mashtab +xSdvig,path.get(i).get(1)*mashtab+ ySdvig);
        }
        glEnd();*/

        ySdvig -= -6.371e6 * mashtab;
        // Swaps out our buffers
        glfwSwapBuffers(window);
    }

    public void cleanup(){
        keyCallback.release();
    }


    @Override
    public void run() {
        // All our initialization code
        init();

        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                update();
                updates++;
                delta--;
            }
            try {
                render();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                //System.out.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
            if(glfwWindowShouldClose(window) == GL_TRUE){
                running = false;
            }
        }

        cleanup();
        /*
        // Our main game loop
        while(running){
            update();
            render();
            // Checks to see if either the escape button or the
            // red cross at the top were pressed.
            // if so sets our boolean to false and closes the
            // thread.
            if(glfwWindowShouldClose(window) == GL_TRUE){
                running = false;
            }
        }*/
    }
}