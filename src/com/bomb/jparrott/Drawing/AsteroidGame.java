package com.bomb.jparrott.Drawing;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Steam on 12/6/2016.
 */
public class AsteroidGame extends BasicGame{


    public static final int UNIT_LENGTH = 50;
    public static final Vector2 GRAVITY = new Vector2(0, 50);

    private Set<GameObject> gameObjects;

    public static void main(String[] args) {

        AppGameContainer app = null;
        try{
            System.setProperty("java.library.path", "./lib/");
            System.setProperty("org.lwjgl.librarypath", new File("lib/natives/natives-windows").getAbsolutePath());

            app = new AppGameContainer(new ScalableGame(new AsteroidGame("Asteroid"), 640, 360));
            //app.setDisplayMode(640, 480, false);
            //app.setDisplayMode(1920, 1080, false);

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

    public AsteroidGame(String name){
        super(name);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {

        gameObjects = new HashSet<>();
        gameObjects.add(new Ship());

    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        double dt = (double)delta / 1000;
        for(GameObject gameObject : gameObjects){
            gameObject.update(this, gameContainer, dt);
        }

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

        for(GameObject gameObject : gameObjects){
            gameObject.render(gameContainer, graphics);
        }

    }
}
