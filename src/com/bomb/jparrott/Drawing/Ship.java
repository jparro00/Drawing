package com.bomb.jparrott.Drawing;

import com.sun.glass.events.NpapiEvent;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.*;

/**
 * Created by Steam on 12/5/2016.
 */
public class Ship extends GameObject{

    public static final double UNIT_LENGTH = 10;

    double terminalVelocity;
    double speed;

    public Ship(){
        this.body = new Polygon(new Vector2(0,0), new Vector2(10,10), new Vector2(10,15), new Vector2(7.5, 17.5), new Vector2(-7.5, 17.5), new Vector2(-10,15), new Vector2(-10,10));
        position = new Vector2(250, 250);
        Vector2 center = body.getCenter();
        body.translate(new Vector2(center.x, center.y, position.x, position.y));

        this.speed = 10 * UNIT_LENGTH;
        this.velocity = new Vector2(0,0);
        this.acceleration = new Vector2(0,0);
        terminalVelocity = 10 * UNIT_LENGTH;
    }

    public Vector2 getDirection(){
        Vector2 direction;
        Vector2 center = body.getCenter();
        Vector2 front = body.getVertices()[0];

        direction = new Vector2(center.x, center.y, front.x, front.y);
        direction.normalize();

        return direction;
    }

    public void update(BasicGame engine, GameContainer container, double dt) throws SlickException {

        //turn/accelerate
        Transform transform = new Transform();

        if(container.getInput().isKeyDown(Input.KEY_UP)) {
            acceleration = getDirection();
            acceleration.setMagnitude(speed);
        }else{
            acceleration = new Vector2(0,0);
        }

        velocity.add(acceleration);

        if(velocity.getMagnitude() > terminalVelocity){
            velocity.setMagnitude(terminalVelocity);
        }

        velocity.setMagnitude(velocity.getMagnitude() * dt);

        transform.translate(velocity);

        body.translate(transform.getTranslation());


/*
//jump/fall/turn
        Vector2 position = new Vector2(0,0);
        if(container.getInput().isKeyPressed(Input.KEY_SPACE)){
            velocity.add(new Vector2(0,-20));
        }

        acceleration.add(AsteroidGame.GRAVITY);
        velocity.add(acceleration.multiply(dt));

        position = velocity.copy();
        Transform transform = new Transform();
        transform.translate(position);

        if(!engine.sat.detect(body, transform, engine.walls[0], new Transform()) &&
                !engine.sat.detect(body, transform, engine.walls[1], new Transform())
                ){
            body.translate(position);
        }else {
            velocity = new Vector2(0,0);
            acceleration = new Vector2(0,0);
        }

        boolean colliding = false;
        if(container.getInput().isKeyDown(Input.KEY_LEFT)){
            Vector2 center = body.getCenter();
            Transform rotation = new Transform();
            transform.rotate(-.05, center.x, center.y);
            colliding = false;
            for(Rectangle rectangle : engine.walls){
                if(engine.sat.detect(body, transform, rectangle, new Transform())){
                    colliding = true;
                    break;
                }
            }

            if(!colliding){
                body.rotate(-.05, center.x, center.y);
            }
        }else if(container.getInput().isKeyDown(Input.KEY_RIGHT)){
            Vector2 center = body.getCenter();
            Transform rotation = new Transform();
            transform.rotate(.05, center.x, center.y);

            colliding = false;
            for(Rectangle rectangle : engine.walls){
                if(engine.sat.detect(body, transform, rectangle, new Transform())){
                    colliding = true;
                    break;
                }
            }

            if(!colliding){
                body.rotate(.05, center.x, center.y);
            }
        }
*/
        /*
        velocity.add(GRAVITY);
        Vector2 v = velocity.copy();
        v.setMagnitude(v.getMagnitude() * delta/1000);
        Transform transform = new Transform();
        transform.translate(v);

        if(container.getInput().isKeyDown(Input.KEY_UP)){
            Vector2 center = body.getCenter();
            Vector2 front = body.getVertices()[1];
            Vector2 t = new Vector2(center.x, center.y, front.x, front.y);

            t.setMagnitude(speed);

            if(Math.abs(velocity.y) < Math.abs(t.y) ){
                velocity.add(new Vector2(0, t.y));
            }
            if(Math.abs(velocity.x) < Math.abs(t.x) ){
                velocity.add(new Vector2(t.x, 0));
            }

        }

        boolean colliding = false;
        for(Rectangle rectangle : engine.walls){
            if(engine.sat.detect(body, transform, rectangle, new Transform())){
                colliding = true;
                break;
            }
        }

        if(velocity.y > 0 && colliding){
            velocity = new Vector2(0,0);
        }else{
            body.translate(v);
        }

        if(container.getInput().isKeyDown(Input.KEY_LEFT)){
            Vector2 center = body.getCenter();
            Transform rotation = new Transform();
            transform.rotate(-.05, center.x, center.y);
            colliding = false;
            for(Rectangle rectangle : engine.walls){
                if(engine.sat.detect(body, transform, rectangle, new Transform())){
                    colliding = true;
                    break;
                }
            }

            if(!colliding){
                body.rotate(-.05, center.x, center.y);
            }
        }else if(container.getInput().isKeyDown(Input.KEY_RIGHT)){
            Vector2 center = body.getCenter();
            Transform rotation = new Transform();
            transform.rotate(.05, center.x, center.y);

            colliding = false;
            for(Rectangle rectangle : engine.walls){
                if(engine.sat.detect(body, transform, rectangle, new Transform())){
                    colliding = true;
                    break;
                }
            }

            if(!colliding){
                body.rotate(.05, center.x, center.y);
            }
        }
        */

        /*
        int x = 0; int y = 0;
        if(container.getInput().isKeyDown(Input.KEY_UP)){

            Vector2 center = body.getCenter();
            Vector2 front = body.getVertices()[1];
            Vector2 transform = new Vector2(center.x, center.y, front.x, front.y);

            transform.setMagnitude(speed);

            body.translate(transform);

            //update position
        }else if(container.getInput().isKeyDown(Input.KEY_DOWN)){

            Vector2 center = body.getCenter();
            Vector2 front = body.getVertices()[1];
            Vector2 transform = new Vector2(front.x, front.y, center.x, center.y);

            transform.setMagnitude(.3);

            body.translate(transform);
        }else{
            acceleration = GRAVITY;
            velocity.add(acceleration);
            body.translate(velocity);
        }


        if(container.getInput().isKeyDown(Input.KEY_LEFT)){
            Vector2 center = body.getCenter();
            body.rotate(-.05, center.x, center.y);
        }else if(container.getInput().isKeyDown(Input.KEY_RIGHT)){
            Vector2 center = body.getCenter();
            body.rotate(.05, center.x, center.y);
        }
        */
    }

    public void render(GameContainer container, Graphics g) throws SlickException{

        float originX, originY, currentX, currentY, newX, newY;

        Vector2[] points = body.getVertices();
        originX = (float)points[0].x;
        originY = (float)points[0].y;

        for(int i = 0; i < points.length - 1; i++){
            g.drawLine((float) points[i].x, (float) points[i].y, (float) points[i + 1].x, (float) points[i + 1].y);
        }
        g.drawLine((float) points[points.length - 1].x, (float) points[points.length - 1].y, originX, originY);

    }

    @Override
    public GameObject move(Transform movement) {
        return null;
    }

    @Override
    public GameObject rotate(Transform rotation) {
        return null;
    }
}
