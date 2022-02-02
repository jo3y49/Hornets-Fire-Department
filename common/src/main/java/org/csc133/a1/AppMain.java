package org.csc133.a1;

import static com.codename1.ui.CN.*;

import com.codename1.charts.util.ColorUtil;
import com.codename1.system.Lifecycle;
import com.codename1.ui.*;
import com.codename1.ui.geom.Point;
import com.codename1.ui.layouts.*;
import com.codename1.io.*;
import com.codename1.ui.plaf.*;
import com.codename1.ui.util.Resources;
import com.codename1.ui.util.UITimer;

import java.awt.event.ActionListener;

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
    //public void addKeyListener(int KeyCode, ActionListener listener){}
    @Override
    public void run() {
        //gw.tick();
        repaint();
    }
    public void paint (Graphics g){
        super.paint(g);
        gw.draw(g);
    }
}
class GameWorld{
    private River river;
    private Helipad helipad;

    public GameWorld(){
        init();
    }
    private void init(){
        river = new River();
        helipad = new Helipad();
    }
    public void quit(){
        Display.getInstance().exitApplication();
    }
    void draw(Graphics g){
        river.draw(g);
        helipad.draw(g);
    }
}
class River{
    private Point location;
    private int height, width;

    public River(){
        location = new Point(1,Display.getInstance().getDisplayHeight()/5);
        height = Display.getInstance().getDisplayHeight()/16;
        width = Display.getInstance().getDisplayWidth()-8;
    }
    void draw(Graphics g){
        g.setColor(ColorUtil.BLUE);
        g.drawRect(location.getX(),location.getY(),width,height);
    }
}
class Helipad{
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
    public Fire(){

    }

    void draw(Graphics g){

    }
}
class Helicopter{
    public Helicopter(){

    }

    void draw(Graphics g){

    }
}