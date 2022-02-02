package org.csc133.a1;

import static com.codename1.ui.CN.*;

import com.codename1.charts.util.ColorUtil;
import com.codename1.maps.BoundingBox;
import com.codename1.maps.Coord;
import com.codename1.system.Lifecycle;
import com.codename1.ui.*;
import com.codename1.ui.geom.Point;
import com.codename1.ui.layouts.*;
import com.codename1.io.*;
import com.codename1.ui.plaf.*;
import com.codename1.ui.util.Resources;
import com.codename1.ui.util.UITimer;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose
 * of building native mobile applications using Java.
 */
public class AppMain extends Lifecycle {
    @Override
    public void runApp() {
        new Game();
    }
}
class Game extends Form implements Runnable{
    GameWorld gw;

    final static int DISP_H = Display.getInstance().getDisplayHeight();
    final static int DISP_W = Display.getInstance().getDisplayWidth();

    public static int getSmallDim() {return Math.min(DISP_H,DISP_W);}
    public static int getLargeDim() {return Math.max(DISP_H,DISP_W);}

    public Game(){
        gw = new GameWorld();


        /*addKeyListener(-93,(evt) -> );
        addKeyListener(-94,(evt) -> );
        addKeyListener(-91,(evt) -> );
        addKeyListener(-92,(evt) -> );
        addKeyListener('f',(evt) -> );
        addKeyListener('d',(evt) -> );*/
        addKeyListener('Q',(evt) -> gw.quit());

        UITimer timer = new UITimer(this);
        timer.schedule(100,true,this);

        this.getAllStyles().setBgColor(ColorUtil.BLACK);
        this.show();
    }

    @Override
    public void run() {
        gw.tick();
        repaint();
    }
    public void paint (Graphics g){
        super.paint(g);
        gw.draw(g);
    }
}
class GameWorld{
    final int NUMBER_OF_FIRES = 3;
    private River river;
    private Helipad helipad;
    private ArrayList<Fire> fires;
    private Helicopter helicopter;

    public GameWorld(){
        init();
    }
    private void init(){
        river = new River();
        helipad = new Helipad();
        fires = new ArrayList<>();
        for (int i=0; i<NUMBER_OF_FIRES; i++){
            fires.add(new Fire());
            fires.get(i).place(i);
        }
        helicopter = new Helicopter();
        helicopter.changeFuel(3000);
    }
    public void quit(){
        Display.getInstance().exitApplication();
    }

    void draw(Graphics g){
        river.draw(g);
        helipad.draw(g);
        for(Fire fire : fires){
            fire.draw(g);
        }
        helicopter.draw(g);
    }

    public void tick() {
        for(Fire fire : fires){
            fire.grow();
        }
        helicopter.changeFuel(-5);
    }
}
class River{
    private Point location;
    private int height, width;
    private BoundingBox box;

    public River(){
        location = new Point(1,Display.getInstance().getDisplayHeight()/4);
        height = Display.getInstance().getDisplayHeight()/16;
        width = Display.getInstance().getDisplayWidth()-8;
        box = new BoundingBox(new Coord(location.getX(),location.getY()+height),
                new Coord(location.getX()+width,location.getY()));
    }
    void draw(Graphics g){
        g.setColor(ColorUtil.BLUE);
        g.drawRect(location.getX(),location.getY(),width,height);
    }
}
class Helipad{
    //separated variables for the square and circle to make it easier
    private Point locationS, locationC;
    private int square, circle;

    public Helipad(){
        square = Display.getInstance().getDisplayWidth()/5;
        locationS = new Point(Display.getInstance().getDisplayWidth()/2-square/2,
             Display.getInstance().getDisplayHeight()-Display.getInstance().getDisplayHeight()/5);
        circle = square-square/5;
        locationC = new Point(locationS.getX()+square/10,
                              locationS.getY()+square/10);

    }

    void draw(Graphics g){
        g.setColor(ColorUtil.GRAY);
        g.drawRect(locationS.getX(),locationS.getY(),square,square,5);
        g.drawArc(locationC.getX(),locationC.getY(),circle,circle,0,360);
    }
}
class Fire{
    private Point location;
    private int size;
    private BoundingBox box;

    public Fire(){
        location = new Point(100,100);
        size = new Random().nextInt(100)+350;
        box = new BoundingBox(new Coord(location.getX(),location.getY()+size),
                new Coord(location.getX()+size,location.getY()));
    }
    public void grow(){
        int growth = 6;
        if (new Random().nextInt(30) == 0){
            this.size += growth;
            this.location.setX(location.getX()-growth/2); //keeps it centered
            this.location.setY(location.getY()-growth/2);
        }
    }
    public void place(int n){
        switch (n){ //case numbers represent the specific fire, puts them where they belong
            case 0:
                this.location = new Point(new Random().nextInt(150)+15,
                             new Random().nextInt(150)+100);
            break;
            case 1:
                this.location = new Point(new Random().nextInt(150)+
                                Display.getInstance().getDisplayWidth()/2,
                             new Random().nextInt(150)+100);
            break;
            case 2:
                this.location = new Point(new Random().nextInt(150)+
                        Display.getInstance().getDisplayWidth()/2-150,
                        new Random().nextInt(150)+
                        Display.getInstance().getDisplayHeight()/3);
        }
    }
    void draw(Graphics g){
        g.setColor(ColorUtil.MAGENTA);
        g.fillArc(location.getX(),location.getY(),size,size,0,360);
        //TODO: font too small
        g.drawString(""+size,location.getX()+size,location.getY()+size);

    }
}
class Helicopter{
    private Point location, lineBase, lineEnd;
    private int size, length, fuel, water, speed;

    public Helicopter(){
        speed = 0;
        water = 0;
        fuel = 0;
        size = Display.getInstance().getDisplayWidth()/28;
        length = size*2+size/2;
        location = new Point(Display.getInstance().getDisplayWidth()/2-size/2,
          Display.getInstance().getDisplayHeight()-Display.getInstance().getDisplayHeight()/7-size/2);
        lineBase = new Point(location.getX()+size/2,location.getY()+size/2);
        lineEnd = new Point(lineBase.getX(),lineBase.getY()-length);
    }

    public void changeFuel(int fuel){
        this.fuel += fuel;
    }
    public void changeWater(int water){
        this.water += water;
    }

    void draw(Graphics g){
        g.setColor(ColorUtil.YELLOW);
        g.fillArc(location.getX(),location.getY(),size,size,0,360);
        g.drawLine(lineBase.getX(),lineBase.getY(),lineEnd.getX(),lineEnd.getY());
        g.drawString("F  : "+fuel,location.getX()+size/2,location.getY()+size*3);
        g.drawString("W  : "+water,location.getX()+size/2,location.getY()+size*4);
    }
}