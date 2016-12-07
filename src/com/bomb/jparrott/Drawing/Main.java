package com.bomb.jparrott.Drawing;


import org.dyn4j.collision.narrowphase.Sat;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.*;
import org.newdawn.slick.*;

import java.io.File;

/**
 * Created by jparrott on 12/4/2016.
 */
public class Main extends BasicGame {

    static AppGameContainer app;
    static int draw = 3;
    Sat sat = new Sat();
    Vector2 dot;

    Ship ship;
    Triangle triangle;
    Rectangle base, left, right, top;
    Rectangle[] walls;
    Body body;

    public static void drawPolygon(Graphics g, Polygon polygon){

        float originX, originY, currentX, currentY, newX, newY;

        Vector2[] points = polygon.getVertices();
        originX = (float)points[0].x;
        originY = (float)points[0].y;

        for(int i = 0; i < points.length - 1; i++){
            g.drawLine((float) points[i].x, (float) points[i].y, (float) points[i + 1].x, (float) points[i + 1].y);
        }
        g.drawLine((float)points[points.length - 1].x, (float)points[points.length - 1].y, originX, originY);

    }

    public static void main(String[] args) {

        try{
            System.setProperty("java.library.path", "./lib/");
            System.setProperty("org.lwjgl.librarypath", new File("lib/natives/natives-windows").getAbsolutePath());

            app = new AppGameContainer(new ScalableGame(new Main("Drawing Game"), 500, 500));
            //app.setDisplayMode(640, 480, false);
            app.setDisplayMode(1080, 1080, false);

            app.setShowFPS(true);
            app.setTargetFrameRate(60);
            app.start();

        }
        catch (SlickException e){
            e.printStackTrace();
        } finally {
            if(app != null){
                app.destroy();
            }
        }
    }

    public Main(String name){
        super(name);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        ship = new Ship();
        triangle = new Triangle(new Vector2(30,10), new Vector2(30,20), new Vector2(20,20));
        walls = new Rectangle[4];

        walls[0] = new Rectangle(450, 50);
        walls[1] = new Rectangle(450, 50);
        walls[2] = new Rectangle(50, 450);
        walls[3] = new Rectangle(50, 450);

        walls[0].translate(250, 450);
        walls[1].translate(250, 50);
        walls[2].translate(450, 250);
        walls[3].translate(50, 250);
        body = new Body();

    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        ship.update(this, container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        ship.render(container, g);
        drawPolygon(g, triangle);
        for(Rectangle rectangle : walls){
            drawPolygon(g, rectangle);
        }


    }


}
