package model;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Dima on 05.05.2017.
 */
public class Orbit implements Serializable {
    double minAxis;
    double maxAxis;
    double c;
    double primeBMass;
    double secBMass;
    double angle;
    double povorot;
    boolean spin;
    double perig;
    double apo;
    double E;
    boolean isEllips = true;
    double p(){
        return perig*(1+E());
    }
    double E(){
        return c/maxAxis;
    }
    public Orbit(){}
    public double skorost(double rad){
        double mu = Rocket.G*primeBMass;
        double ans = Math.sqrt(mu*(2/rad - 1/a()));
        return ans;
    }
    public double getSpeed(double rad, double a){
        double mu = Rocket.G * primeBMass;
        if(!isEllips){
            double ans = Math.sqrt(2 * (mu / rad));
            return ans;
        }
        else {
            double ans = Math.sqrt(mu * (2 / rad - 1 / a));
            return ans;
        }
    }
    public double a(){
        return (apo+perig)/2;
    }
    public double r1(){
        double x = maxAxis*Math.cos(angle);
        double y = minAxis*Math.sin(angle);
        double ans = Math.sqrt(x*x + y*y);
        return ans;
    }
    public double getA(double speed, double r){
        double mu = Rocket.G*primeBMass;
        double ans = speed*speed;
        ans = ans/mu;
        ans = 2/r - ans;
        return 1/ans;
    }
    double getAngle(Vector<Double> pos){
        Vector<Double> ll = new Vector<>(pos);
        double hh = Rocket.getModule(pos);
        ll.set(0, ll.get(0)/hh);
        ll.set(1, ll.get(1)/hh);
        if(Math.asin(ll.get(0))>0)
            return Math.acos(ll.get(1));
        else
            return -Math.acos(ll.get(1));
    }
    Vector<Double> getTempPoint(double rad, double znak){
        znak = 1;
        Vector<Double> ans = new Vector<>(2);
        ans.add(rad*Math.sin(getTempang(rad,znak)));
        ans.add(rad*Math.cos(getTempang(rad,znak)));
        return ans;
    }
    double getTempang(double rad, double znak){
        double ans = p()/rad;
        ans = ans - 1;
        ans = ans/E();
        return Math.acos(ans)*znak/Math.abs(znak);
    }
    Vector<Double> getKasAngle(double r, double znak){
        znak = 1;
        Vector<Double> ans = new Vector<>(2);
        Vector<Double> t = getTempPoint(r, znak);
        t.set(1,t.get(1)+(maxAxis-perig));
        double a = (minAxis*minAxis)/t.get(0), b = (maxAxis*maxAxis)/t.get(1);
        ans.add(a);
        ans.add(-b);
        return ans;
    }
}
