package com.bomb.jparrott.Drawing;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import java.io.File;

/**
 * Created by jparrott on 12/4/2016.
 */
public class Main extends BasicGame {

    static AppGameContainer app;
    float x = 20;
    float y = 25;
    Square square;

    Vector2f v1 = new Vector2f(8,13);
    Vector2f v2 = v1.getPerpendicular();

    public static void drawVectory(Graphics g, Vector2f v, float x, float y){
        g.drawLine(x, y, x + v.getX(), y + v.getY());
    }

    public static void main(String[] args) {

        try{
            System.setProperty("java.library.path", "./lib/");
            System.setProperty("org.lwjgl.librarypath", new File("lib/natives/natives-windows").getAbsolutePath());

            app = new AppGameContainer(new ScalableGame(new Main("Drawing Game"),100,100));
            //app.setDisplayMode(640, 480, false);
            app.setDisplayMode(500, 500, false);

            app.setShowFPS(false);
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
        Vector2f v1 =  new Vector2f(26, 7);
        Vector2f v2 = v1.getPerpendicular();
        v2.scale(2);

        square = new Square(50, 50, v1, v2);
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        //drawVectory(graphics, v1, x, y);
        //drawVectory(graphics, v2, x, y);
        //drawVectory(graphics, v2.negate(), x, y);
        square.draw(graphics);

    }


}
