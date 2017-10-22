package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Dima on 05.05.2017.
 */
public class Body implements Serializable{
    public Vector<Double> position;
    public Vector<Double> velocity;
    public Vector<Double> newPos;
    public Vector<Double> newV;
    public double deltaT;
    public double deltaM;
    public double m;
    public Orbit orbit;
    public static final double G = 6.67*Math.pow(10,-11);
    public double curT;
    public int dimCount = 2;

    public Vector<Double> getAcc(Vector<Double> p, double massB){
        Vector<Double> acc = new Vector<>(dimCount);
        acc.add(0.0);
        acc.add(0.0);
        double modT = 0;
        for (int i = 0; i < dimCount; i++){
            acc.set(i,position.get(i) - p.get(i));
            modT += acc.get(i)*acc.get(i);
        }
        modT = Math.pow(modT, 0.5);

        double dist = getDist(p);
        double modA = massB*m*G/(dist*dist);
        double koef = modA/modT;

        for (int i = 0; i < dimCount; i++){
            acc.set(i,acc.get(i)*koef);
        }
        return acc;
    }

    public double getDist(Vector<Double> p){
        double dist = 0;
        for(int i = 0; i < dimCount; i++){
            dist += Math.pow(position.get(i) - p.get(i),2);
        }

        if(dist == 0){
            dist = 2.22507e-300;
        }
        return Math.pow(dist,0.5);
    }
}
