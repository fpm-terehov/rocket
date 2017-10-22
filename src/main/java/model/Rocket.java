package model;

import utils.Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

/**
 * Created by Dima on 05.05.2017.
 */
public class Rocket extends Body implements Cloneable, Serializable {
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public String name;
    public StageRocket[] stages;
    public int phase;
    public double forceAngle;
    public Vector<Double> naprVect;
    public double length;
    public double h, prevH;
    public double hspeed = 0;
    public double force;
    public double vrashatSkorost = 0;
    public double eps1(){
        if(flag)return  0.9*Math.pow(10, -6);
        else return 1*Math.pow(10, -4);}
    public static double eps2 = 0.5*Math.pow(10,-6);
    public static final double G = 6.67*Math.pow(10,-11);
    public int stagesCount;
    public double apogee;
    public double perigee;
    int n = 0;
    public boolean flag = true, flag55 = true;

    double xx = 0;
    public double min = 1000000000000000000000000000.0;
    public double max = 0;
    transient BufferedWriter br;
    transient BufferedWriter br1;
    boolean flag791 = true;
    public boolean ifFall(double a, Planet[] bodies){
        return orbit.getSpeed(bodies[1].rad,a)<7910;
    }
    double minR(){
        if(perigee<500000)
            return perigee;
        else
            return 500000;
    }
    boolean ellFl = true;
    public void start(double t, Planet[] bodies) throws CloneNotSupportedException, IOException {
        double hacc = 0;
        double sintangsp = 0;
        if(!flag2)
            return;

        while (curT < t) {
            n++;
            double a = orbit.getA(getModule(velocity),h+bodies[1].rad);
            forceAngle = 0;
            double limit = 3000;
            double ang = 2.0;
            if(name.equals("falcon9")) {
                ang = 2.1;
                limit = 4000;
            }
            if(name.equals("saturn5")) {
                ang = 2.4;
                limit = 8000;
            }
            if((!name.equals("stage1")&&!name.equals("stage2"))&&h>limit) {

                Vector<Double> huz= new Vector<>(2);
                huz.add(0.0);
                huz.add(1.0);
                Vector<Double> hu= new Vector<>(2);
                hu.add(0.0);
                hu.add(0.0);
                Orbit orb = new Orbit();
                orb.apo = bodies[1].rad + apogee;
                orb.perig = bodies[1].rad + perigee;
                orb.primeBMass = bodies[1].m;
                orb.maxAxis = (orb.perig + orb.apo)/2;
                orb.c = orb.maxAxis - orb.perig;
                orb.minAxis = Math.sqrt(orb.maxAxis*orb.maxAxis - orb.c*orb.c);
                double hh = orb.skorost(bodies[1].rad + perigee);
                double hhh = orb.skorost(bodies[1].rad + h);



                double ttt = (hhh-getModule(velocity))/getModule(getAcceleration(position,bodies));
                if(fi) {
                    if(vrashatSkorost>0)
                        forceAngle = -Math.PI / 2000;
                    if(vrashatSkorost<0)
                        forceAngle = Math.PI / 2000;
                    if(flag791&&Math.abs(angle2vect(naprVect,position))>Math.PI/ang) {
                        flag791 = false;
                        System.out.println("stoppov");
                        vrashatSkorost = 0;
                    }
                    if(!flag791&&(hspeed*ttt + h < perigee)){
                        forceAngle = Math.PI / 20000;
                    }
                    if(!flag791&&(hspeed>0||hspeed*ttt + h > apogee)){
                        forceAngle = -Math.PI / 20000;
                    }
                    if (flag791) {
                        forceAngle = -Math.PI / 4000;
                    }

                    if (angle2vect(naprVect, position) > Math.PI / 2 || hspeed < 0) {
                        forceAngle = Math.PI / 4000;
                    }
                }
                double ugol;
                if(!orbit.isEllips&&orbit.getSpeed(h+bodies[1].rad,0)<getModule((velocity))&&ellFl){
                    vrashatSkorost = 0;
                    force = 0;
                    deltaM = 0;
                    System.out.println("perehod"+h+" "+(stages[2].fuelmass+stages[1].fuelmass+stages[0].fuelmass)+ " "+hhh+" "+m);
                    ellFl = false;
                }
                double p;
                double c;
                {
                    double ang1 = angle2vect(velocity, position);
                    ang1 = Math.abs(ang1);
                    if(ang1>Math.PI)
                        ang1 -= Math.PI;
                    if(ang1 > Math.PI - ang1)
                        ang1 = Math.PI - ang1;
                    double sq = Math.PI - 2*ang1;
                    double r1 = a*2 - h - bodies[1].rad;
                    double r2 = h + bodies[1].rad;

                    c = r1*r1 + r2*r2 - (2*r1*r2*Math.cos(sq));
                    c = Math.sqrt(c);

                    p = a - c/2;
                    Vector<Double> vict = orb.getKasAngle(h+bodies[1].rad,1);
                    Vector<Double> ugol1 = orb.getTempPoint(h+bodies[1].rad,1);
                    double hxyz = angle2vect(vict, ugol1);
                    if(hxyz<0)
                        hxyz = Math.PI + hxyz;
                    sintangsp = Math.sin(hxyz);
                    if(Double.isNaN(sintangsp)){
                        sintangsp = 1;
                    }
                }
                if(2*a - p >perigee+bodies[1].rad&& fi&&phase==stagesCount-1){
                        force = 0;
                        deltaM = 0;
                    System.out.println(orbit.getSpeed(perigee+bodies[1].rad,a));
                        System.out.println("yes1 "+(stages[2].fuelmass+stages[1].fuelmass+stages[0].fuelmass));
                        System.out.println(hh+" "+gui*10e-9+" "+(System.currentTimeMillis()-Main.realTime));
                    vrashatSkorost = 0;

                        fi = false;
                }
                if((h>perigee-5000&&h<apogee+500)&& !fi && flag0){
                    force = stages[stagesCount-1].force;
                    deltaM = stages[stagesCount-1].deltamass;
                    System.out.println("yes"+(stages[2].fuelmass+stages[1].fuelmass+stages[0].fuelmass)
                            +" "+h+" "+orbit.getSpeed(apogee+bodies[1].rad,a));
                    System.out.println(getModule(velocity));

                    naprVect = new Vector<>(velocity);
                    double vv = getModule(naprVect);
                    naprVect.set(0, naprVect.get(0)/vv);
                    naprVect.set(1, naprVect.get(1)/vv);
                    flag0 = false;vrashatSkorost = 0;
                    flag3 = false;
                }
                if(hspeed*deltaT + h > perigee-5000&&flag0&&h<perigee){
                    deltaT = 0.01;
                }

                if(!flag0&&flag){
                    {

                    }
                    double mod = Math.abs(tangSpeed()/getModule(velocity)-sintangsp);
                    double xx1 = mod + (mod-Main.prevmod)/deltaT*ttt;
                    double znak = (mod + (mod-Main.prevmod)/deltaT*ttt)/Math.abs(mod + (mod-Main.prevmod)/deltaT*ttt);
                    //System.out.println((mod + (mod-Main.prevmod)/deltaT*ttt)+" "+ttt);
                    double koef = 5000;
                    if(mod<10e-3)
                        koef*=10;
                    if (xx1 <= sintangsp) {
                        forceAngle =  Math.PI/koef;
                    } else {
                        forceAngle = -Math.PI/koef;
                    }
                    if(orbit.getSpeed(perigee+bodies[1].rad,a)>hh&&flag666){
                        deltaM = 0;
                        force = 0;
                        vrashatSkorost = 0;
                        flag666 = false;
                    }
                }
                if(hspeed*deltaT + h > perigee-500&&flag55&&h<perigee){
                    deltaT = 0.01;
                    flag55 = false;
                }
                if(hspeed*deltaT + h < apogee+500&&flag66&&h>apogee){
                    deltaT = 0.01;
                    flag66 = false;
                }
                if(h > perigee-500 && h < apogee+500
                        && Math.abs(getModule(velocity) - hhh)<1 && flag && !flag3){
                    System.out.println(hhh+" "+getModule(velocity));
                    flag = false;
                    vrashatSkorost = 0;
                    if(Double.isNaN(angle2vect(huz,orb.getKasAngle(h+bodies[1].rad, hspeed)))){
                        velocity.set(0,hhh*Math.cos(orb.getAngle(position)));
                        velocity.set(1,-hhh*Math.sin(orb.getAngle(position)));
                        naprVect.set(0,Math.cos(orb.getAngle(position)));
                        naprVect.set(1,-Math.sin(orb.getAngle(position)));
                    }
                    else {
                        System.out.println(angle2vect(huz, orb.getKasAngle(h + bodies[1].rad, hspeed)));
                        Vector<Double> dd = orb.getTempPoint(h + bodies[1].rad, hspeed);
                        Vector<Double> ddd = new Vector<>(position);
                        System.out.println(hspeed);
                        Vector<Double> kasat = orb.getKasAngle(h + bodies[1].rad, hspeed);
                        Vector<Double> dddd = new Vector<>(velocity);
                        velocity.set(0, hhh * kasat.get(0) / getModule(kasat));
                        velocity.set(1, hhh * kasat.get(1) / getModule(kasat));
                        velocity = povernut(angle2vect(dd, ddd), hu, velocity);
                        System.out.println(angle2vect(dddd,velocity));
                        if(Math.abs(angle2vect(dddd,velocity))>Math.PI/2) {
                            velocity.set(0, -velocity.get(0));
                            velocity.set(1, -velocity.get(1));
                        }
                        System.out.println(angle2vect(dd, ddd));
                        ddd = povernut(-angle2vect(dd, ddd), hu, ddd);
                        ddd.set(1, ddd.get(1) + (orb.maxAxis - orb.perig));
                        System.out.println(dd);
                        System.out.println((ddd.get(0) * ddd.get(0)) / (orb.minAxis * orb.minAxis) + (ddd.get(1) * ddd.get(1)) / (orb.maxAxis * orb.maxAxis));
                    }
                    force = 0;
                    deltaM = 0;
                    System.out.println("perehod"+h+" "+(stages[2].fuelmass+stages[1].fuelmass)+ " "+hhh+" "+m);
                    System.out.println(stages[2].m);
                }
                if(xx< h)
                    xx = h;

                double cufra = 0.65;
                if(!flag0)
                    cufra = 0.5;
                if (Math.abs(naprAngle() - velAngle()) > cufra) {
                    if (naprAngle() < velAngle())
                        forceAngle = -Math.PI / 4000;
                    else
                        forceAngle = Math.PI / 4000;
                }
                double cifra = 0.011;
                if(!flag0)
                    cifra = 0.011;
                if (Math.abs(vrashatSkorost) > cifra) {
                    if (vrashatSkorost > 0)
                        forceAngle = -Math.PI / 1000;
                    else
                        forceAngle = Math.PI / 1000;
                }
                Main.prevmod = Math.abs(tangSpeed()/getModule(velocity)-sintangsp);
            }

            if(!flag&&min> h){
                min = h;
                //System.out.println("min"+min);
            }
            if(!flag&&max< h){
                max = h;
                //System.out.println("max"+max);
            }
            if(curT<1000&&name.equals("saturn5")&&n%100==0) {
                Main.br.write(Double.toString(BigDecimal.valueOf(curT)
                        .setScale(3, RoundingMode.HALF_UP)
                        .doubleValue()));
                Main.br.newLine();
                Main.br.flush();
                Main.br1.write(Double.toString(BigDecimal.valueOf(deltaT*100)
                        .setScale(3, RoundingMode.HALF_UP)
                        .doubleValue()));
                Main.br1.newLine();
                Main.br1.flush();
            }
            hacc = hspeed;

            iterationRunge(bodies);
            //iterationEuler(bodies);

            hacc = hspeed - hacc;
        }
    }
    boolean flag666 = true;
    boolean flag66 = true;
    boolean fi = true;
    boolean flag3 = true;
    boolean flag0 = true;
    public double timeRemain(){
        double ans = 0;
        for(int i = phase; i < stagesCount; i++){
            ans += stages[i].fuelmass/stages[i].deltamass;
        }
        return ans;
    }
    /*public double timeForSpeed(double speed){
        if()
    }*/
    public int polmassa;
    double c = 0.25;
    double S = Math.PI * 2*2;
    double ro(){
        if(h>1000000)
            return 0;
        double x1 = 270;
        if(h>50000)
            x1 = 240;
        return 1.225*Math.exp(-0.029*10*h/(8.31*x1));
        /*if(h<1000)
            return 1.2;
        if(h<10000)
            return 0.8;
        if(h<15000)
            return 0.3;
        if(h<20000)
            return 0.1;
        if(h<50000)
            return 1.027e-3;
        if(h<90000)
            return 1e-7;
        if(h<120000)
            return 2e-8;
        return 0;*/
    }
    double sopr(Vector<Double> vel){
        return c*ro()*getModule(vel)*getModule(vel)*S/2;
    }
    public void init() throws IOException {
    }

    public void init1st() throws IOException {
        phase = 0;
        curT = 0;
        deltaT = 0.01;
        stages[0].m = stages[0].drymass + stages[0].fuelmass;
        stages[1].m = stages[1].drymass + stages[1].fuelmass;
        stages[2].m = stages[2].drymass + stages[2].fuelmass;
        force = stages[0].force;
        m = stages[0].m + stages[1].m + stages[2].m;
        length = (stages[0].dlina + stages[1].dlina +stages[2].dlina)/2;
        forceAngle = 0;
        naprVect = new Vector<>(dimCount);
        naprVect.add(0.0);
        naprVect.add(1.0);
        velocity = new Vector<>(dimCount);
        position = new Vector<>(dimCount);
        position.add(0.0);
        position.add(6.371e6 + 1);
        h = 1;
        prevH = h;
        orbit = new Orbit();
        orbit.angle = 0;
        orbit.primeBMass = 5.9726e24;
        orbit.maxAxis = 6.371e6+100000;
        orbit.minAxis = 6.371e6+100000;
        orbit.secBMass = m;
        orbit.povorot = 0;
        deltaM = stages[0].deltamass;
        velocity.add(465.0);
        velocity.add(0.0);
    }
    public void init2st(){
    }
    public double naprAngle(){
        if(Math.asin(naprVect.get(0))>0)
            return Math.acos(naprVect.get(1));
        else
            return -Math.acos(naprVect.get(1));
    }

    public double velAngle(){
        double koef = 1/getModule(velocity);
        Vector<Double> vikt = new Vector<>(2);
        vikt.add(velocity.get(0)*koef);
        vikt.add(velocity.get(1)*koef);
        if(Math.asin(vikt.get(0))>0)
            return Math.acos(vikt.get(1));
        else
            return -Math.acos(vikt.get(1));
    }

    public Vector<Double> getForceVector(){
        Vector<Double> ans = new Vector<>(dimCount);
        for(double i:naprVect){
            ans.add(i);
        }

        double mod = getModule(ans);
        double koef = force*Math.cos(forceAngle)/mod;
        for(int i = 0; i < dimCount; i++){
            ans.set(i,ans.get(i) * koef);
        }

        Vector<Double> nil = new Vector<>(dimCount);
        nil.add(0.0);
        nil.add(0.0);
        ans = povernut(forceAngle, nil, ans);
        double koef1 = -sopr(velocity)/getModule(velocity);
        ans.set(0, ans.get(0)+velocity.get(0)*koef1);
        ans.set(1, ans.get(1)+velocity.get(1)*koef1);
        return ans;
    }

    public double tangSpeed(){
        //return getModule(velocity)*Math.sin(velAngle()-orbit.getAngle(position));
        return Math.sqrt(Math.pow(getModule(velocity), 2) - hspeed*hspeed);
    }

    public double momentVrasheniya(){
        return m * Math.pow(2* length,2)/12;
    }

    public Vector<Double> povernut(double angle, Vector<Double> center, Vector<Double> tochka){
        Vector<Double> newTochka = new Vector<>(tochka);
        for(int i = 0; i < dimCount; i++){
            newTochka.set(i, tochka.get(i) - center.get(i));
        }
        double x, y;
        x = newTochka.get(0)*Math.cos(angle) - newTochka.get(1)*Math.sin(angle);
        y = newTochka.get(0)*Math.sin(angle) + newTochka.get(1)*Math.cos(angle);
        newTochka.set(0,x);newTochka.set(1,y);

        for(int i = 0; i < dimCount; i++){
            newTochka.set(i, newTochka.get(i) + center.get(i));
        }
        return newTochka;
    }

    public double razn(Vector<Double> a, Vector<Double> b){
        double err = 0;
        for(int i = 0; i < dimCount; i++){
            err += Math.pow(a.get(i) - b.get(i),2);
        }
        err = Math.pow(err,0.5);


        return err;
    }

    public Vector<Double> getAcceleration(Vector<Double> pos, Planet[] bodies){
        Vector<Double> acc = getForceVector();
        for (Body b:bodies) {
            Vector<Double> tmp = b.getAcc(pos, m);
            for(int i = 0; i < dimCount; i++){
                acc.set(i,acc.get(i) + tmp.get(i));
            }
        }

        for (int i = 0; i < dimCount; i++){
            acc.set(i, acc.get(i)/m);
        }

        return acc;
    }

    public static double getModule(Vector<Double> vect){
        double ans = 0;
        for(double i:vect){
            ans += i*i;
        }
        ans = Math.sqrt(ans);
        return ans;
    }
    public double gui = 0;

    static int ccc = 0;
    public boolean flag2 = true;
    boolean flag1 = true;
    public void iterationRunge(Planet[] bodies) throws CloneNotSupportedException {
        deltaT *= 1.1;
        if(stages[phase].fuelmass - deltaT*deltaM < 0 && deltaM != 0){
            deltaT = stages[phase].fuelmass/deltaM + 0.001;
        }
        if(stages[phase].fuelmass <= 0&&flag1) {
            deltaT = 0.01;
            if(phase<stagesCount-1)
                length -= stages[phase].dlina/2;
            phase++;
            if(phase == 1&&stagesCount>1){
                Main.stage1 = true;
                Main.rocket1 = (Rocket)Main.rocket.clone();
                Main.rocket1.stagesCount = 1;
                Main.rocket1.phase = 0;
                Main.rocket1.m = Main.rocket1.stages[0].drymass;
                Main.rocket1.force = 0;
                Main.rocket1.deltaM = 0;
                Main.rocket1.flag1 = false;
                Main.rocket1.flag = false;
                Main.rocket1.name = "stage1";
            }
            if(phase == 2&&stagesCount>2){
                Main.stage2 = true;
                Main.rocket2 = (Rocket)Main.rocket.clone();
                Main.rocket2.stagesCount = 2;
                Main.rocket2.phase = 1;
                Main.rocket2.m = Main.rocket2.stages[1].drymass;
                Main.rocket2.force = 0;
                Main.rocket2.deltaM = 0;
                Main.rocket2.flag1 = false;
                Main.rocket2.flag = false;
                Main.rocket2.name = "stage2";
            }
            if(phase >= stagesCount){
                deltaM = 0;
                force = 0;
                phase--;
                if(flag1){
                    flag = false;
                    flag228 = false;
                    System.out.println("not enough fuel for this orbit");
                }
                flag1 = false;
            }
            else {
                m = 0;
                for (int i = phase; i < stagesCount; i++) {
                    m += stages[i].m;
                }
                deltaM = stages[phase].deltamass;
                force = stages[phase].force;
                if(phase == 1){
                    st1sepvel = getModule(velocity);
                    st1seph = h;
                    st1sept = curT;
                }
                if(phase == 2){
                    st2sepvel = getModule(velocity);
                    st2seph = h;
                    st2sept = curT;
                }
                System.out.println(getModule(velocity) + " " + h + " " + curT + " ");
            }
        }
        Vector<Double> vk = new Vector<>(dimCount);
        vk.add(0.0);
        vk.add(0.0);


        while(true) {
            double ko = position.get(1);
            runge(position, velocity, deltaT, bodies);
            Vector<Double> pos1 = newPos;
            Vector<Double> vel1 = newV;

            runge(position, velocity, deltaT / 2, bodies);
            Vector<Double> pos2 = newPos;
            Vector<Double> vel2 = newV;

            Vector<Double> temp = naprVect;
            naprVect = povernut(vrashatSkorost * deltaT / 2, vk, naprVect);
            m -= deltaM * deltaT / 2;

            runge(pos2, vel2, deltaT/2, bodies);
            pos2 = newPos;
            vel2 = newV;
            m += deltaM * deltaT / 2;



            if (razn(pos1, pos2) < eps1() && razn(vel1, vel2) < eps2) {
                ccc++;
                position = pos2;
                velocity = vel2;
                curT += deltaT;
                m -= deltaM * deltaT;
                stages[phase].fuelmass -= deltaM * deltaT;


                naprVect = povernut(vrashatSkorost * deltaT, vk, naprVect);
                vrashatSkorost += getForcePovorot() * deltaT / momentVrasheniya();
                prevH = h;
                h = (bodies[1].getDist(position) - bodies[1].rad);
                hspeed = (h - prevH) / deltaT;
                break;
            } else {
                deltaT /= 2;
                naprVect = temp;
            }
        }

        for(Planet b:bodies){
            if(b.getDist(position) < b.rad){
                flag2 = false;
                if((!name.equals("stage1")&&!name.equals("stage2")))
                    System.out.println(curT);
            }
        }
    }
    public boolean flag228 = true;
    public double st1sepvel, st1seph, st1sept;
    public double st2sepvel, st2seph, st2sept;

    public Vector<Double> runge(Vector<Double> v1, Vector<Double> v2, double delta, Planet[] bodies){
        Vector<Double> k1, k2, k3, k4;
        Vector<Double> l1, l2 = new Vector<>(dimCount), l3 = new Vector<>(dimCount), l4 = new Vector<>(dimCount);
        k1 = new Vector<>(getAcceleration(v1, bodies));
        l1 = new Vector<>(v2);

        Vector<Double> viktor = new Vector<>(v1);
        for(int i = 0; i < dimCount; i++){
            l2.add(l1.get(i) + k1.get(i)*delta/2);
            viktor.set(i, v1.get(i) + l2.get(i)*delta/2);
        }
        Vector<Double> vk = new Vector<>(dimCount);
        vk.add(0.0);
        vk.add(0.0);
        Vector<Double> temp = naprVect;
        naprVect = povernut(vrashatSkorost * deltaT / 2, vk, naprVect);

        m-= deltaM/2;
        k2 = getAcceleration(viktor, bodies);
        for(int i = 0; i < dimCount; i++){
            l3.add(l1.get(i) + k2.get(i)*delta/2);
            viktor.set(i, v1.get(i) + l3.get(i)*delta/2);
        }
        k3 = getAcceleration(viktor, bodies);
        for(int i = 0; i < dimCount; i++){
            l4.add(l1.get(i) + k3.get(i)*delta);
            viktor.set(i, v1.get(i) + l3.get(i)*delta);
        }
        m-=deltaM/2;
        naprVect = povernut(vrashatSkorost * deltaT / 2, vk, naprVect);
        k4 = getAcceleration(viktor, bodies);
        m+=deltaM;
        naprVect = temp;

        newPos = new Vector<>(dimCount);
        newPos.add(0.0);
        newPos.add(0.0);
        newV = new Vector<>(dimCount);
        newV.add(0.0);
        newV.add(0.0);
        for(int i = 0; i < dimCount; i++){
            newV.set(i, v2.get(i) + (k1.get(i) + k2.get(i)*2 + k3.get(i)*2 + k4.get(i))*delta/6);
            newPos.set(i, v1.get(i) + (l1.get(i) + l2.get(i)*2 + l3.get(i)*2 + l4.get(i))*delta/6);
        }

        return newPos;
    }

    public void iterationEuler(Planet[] bodies) throws CloneNotSupportedException {
        Vector<Double> acceleration = getAcceleration(position, bodies);
        deltaT *= 1.1;
        if(stages[phase].fuelmass - deltaT*deltaM < 0 && deltaM != 0){
            deltaT = stages[phase].fuelmass/deltaM;
        }
        if(stages[phase].fuelmass <= 0&&flag1) {
            deltaT = 0.01;
            phase++;
            if(phase == 1){
                Main.stage1 = true;
                Main.rocket1 = (Rocket)Main.rocket.clone();
                Main.rocket1.stagesCount = 1;
                Main.rocket1.phase = 0;
                Main.rocket1.m = Main.rocket1.stages[0].drymass;
                Main.rocket1.force = 0;
                Main.rocket1.deltaM = 0;
                Main.rocket1.flag1 = false;
                Main.rocket1.name = "stage1";
            }
            if(phase == 2){
                Main.stage2 = true;
                Main.rocket2 = (Rocket)Main.rocket.clone();
                Main.rocket2.stagesCount = 2;
                Main.rocket2.phase = 1;
                Main.rocket2.m = Main.rocket2.stages[1].drymass;
                Main.rocket2.force = 0;
                Main.rocket2.deltaM = 0;
                Main.rocket2.flag1 = false;
                Main.rocket2.name = "stage2";
            }
            if(phase >= stagesCount){
                deltaM = 0;
                force = 0;
                phase--;
                if(flag1){
                    System.out.println("not enough fuel for this orbit");
                }
                flag1 = false;
            }
            else {
                m = 0;
                for (int i = phase; i < stagesCount; i++) {
                    m += stages[i].m;
                }
                deltaM = stages[phase].deltamass;
                force = stages[phase].force;
                System.out.println(getModule(velocity) + " " + h + " " + curT + " ");
            }
        }

        Vector<Double> vk = new Vector<>(dimCount);
        vk.add(0.0);
        vk.add(0.0);

        while(true){
            Vector<Double> pos1 = euler(position, velocity, deltaT);
            Vector<Double> vel1 = euler(velocity, acceleration, deltaT);

            Vector<Double> pos2 = euler(position, velocity, deltaT/2);
            Vector<Double> vel2 = euler(velocity, acceleration, deltaT/2);

            Vector<Double> temp = naprVect;
            naprVect = povernut(vrashatSkorost*deltaT/2,vk,naprVect);
            m -= deltaM*deltaT/2;

            Vector<Double> acc2 = getAcceleration(pos2, bodies);
            pos2 = euler(pos2, vel2, deltaT/2);
            vel2 = euler(vel2, acc2, deltaT/2);
            m += deltaM*deltaT/2;


            if(razn(pos1, pos2) < eps1() && razn(vel1, vel2) < eps2){
                position = pos2;
                velocity = vel2;
                curT += deltaT;
                m-=deltaM*deltaT;
                stages[phase].fuelmass -= deltaM * deltaT;

                naprVect = povernut(vrashatSkorost*deltaT,vk,naprVect);
                vrashatSkorost += getForcePovorot() * deltaT/momentVrasheniya();
                prevH = h;
                h = (bodies[1].getDist(position)-bodies[1].rad);
                hspeed = (h - prevH)/deltaT;
                break;
            }
            else{
                deltaT /= 2;
                naprVect = temp;
            }
        }
        for(Planet b:bodies){
            if(b.getDist(position) < b.rad){
                flag2 = false;
            }
        }
    }

    public Vector<Double> euler(Vector<Double> ar1, Vector<Double> ar2, double t){
        Vector<Double> ans = new Vector<>(dimCount);
        ans.add(0.0);
        ans.add(0.0);
        for(int i = 0; i < dimCount; i++) {
            ans.set(i, ar1.get(i)+ar2.get(i) * t);
        }
        return ans;
    }

    public double getForcePovorot(){
        return force*Math.sin(forceAngle)* length;
    }
    public double angle2vect(Vector<Double> a,Vector<Double> b){
        return Math.atan2(a.get(0),a.get(1))- Math.atan2(b.get(0),b.get(1));
    }
}
