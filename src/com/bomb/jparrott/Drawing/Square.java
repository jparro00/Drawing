package com.bomb.jparrott.Drawing;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;


/**
 * Created by jparrott on 12/4/2016.
 */
public class Square {

    Square.Point p1, p2, p3, p4;

    public Square(float x, float y, Vector2f v1, Vector2f v2) {
        p1 = new Point(x, y);
        p2 = new Point(p1.x + v1.getX(), p1.y + v1.getY());
        p3 = new Point(p2.x + v2.negate().getX(), p2.y + v2.negate().getY());
        p4 = new Point(p3.x + v1.negate().getX(), p3.y + v1.negate().getY());

    }

    public static void main(String[] args) {
        Vector2f v1 =  new Vector2f(26, 7);
        Vector2f v2 = v1.getPerpendicular();
        v2.scale(2);

        Square square = new Square(20, 20, v1, v2);
        System.out.println(square);

    }

    @Override
    public String toString() {
        String s = String.format("p1: (%f,%f); p2: (%f,%f); p3: (%f,%f); p4: (%f,%f);", p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
        return s;
    }

    public void draw(Graphics g){
        g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
        g.drawLine((int)p2.x, (int)p2.y, (int)p3.x, (int)p3.y);
        g.drawLine((int)p3.x, (int)p3.y, (int)p4.x, (int)p4.y);
        g.drawLine((int)p4.x, (int)p4.y, (int)p1.x, (int)p1.y);
    }

    public static class Point{
        float x, y;

        public Point(float x, float y){
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}


