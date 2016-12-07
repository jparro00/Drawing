package com.bomb.jparrott.Drawing;

import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * Created by Steam on 12/6/2016.
 */
public abstract class GameObject {

    protected Polygon body;
    protected Vector2 position, velocity, acceleration;

    public abstract void update(BasicGame engine, GameContainer container, double dt) throws SlickException;

    public abstract void render(GameContainer container, Graphics g) throws SlickException;

    public abstract GameObject move(Transform movement);

    public abstract GameObject rotate(Transform rotation);

}
