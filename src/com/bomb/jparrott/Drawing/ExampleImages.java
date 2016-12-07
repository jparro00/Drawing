package com.bomb.jparrott.Drawing;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.*;

public class ExampleImages extends JFrame {
    /** The scale 45 pixels per meter */
    public static final double SCALE = 45.0;

    /** The conversion factor from nano to base */
    public static final double NANO_TO_BASE = 1.0e9;

    private static final BufferedImage BASKETBALL = getImageSuppressExceptions("data/Basketball.png");
    private static final BufferedImage CRATE = getImageSuppressExceptions("./data/Crate.png");

    private static final BufferedImage getImageSuppressExceptions(String pathOnClasspath) {
        File file = new File(pathOnClasspath);
        System.out.println(file.getAbsoluteFile());
        System.out.println(file.exists());
        try {
            BufferedImage image = ImageIO.read(ExampleImages.class.getResource(pathOnClasspath));
            System.out.println(image);
            return image;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static class GameObject extends Body {
        /** The color of the object */
        protected Color color;

        /**
         * Default constructor.
         */
        public GameObject() {
            // randomly generate the color
            this.color = new Color(
                    (float)Math.random() * 0.5f + 0.5f,
                    (float)Math.random() * 0.5f + 0.5f,
                    (float)Math.random() * 0.5f + 0.5f);
        }

        /**
         * Draws the body.
         * <p>
         * Only coded for polygons and circles.
         * @param g the graphics object to render to
         */
        public void render(Graphics2D g) {
            // save the original transform
            AffineTransform ot = g.getTransform();

            // transform the coordinate system from world coordinates to local coordinates
            AffineTransform lt = new AffineTransform();
            lt.translate(this.transform.getTranslationX() * SCALE, this.transform.getTranslationY() * SCALE);
            lt.rotate(this.transform.getRotation());

            // apply the transform
            g.transform(lt);

            // loop over all the body fixtures for this body
            for (BodyFixture fixture : this.fixtures) {
                // get the shape on the fixture
                Convex convex = fixture.getShape();
                // check the shape type
                if (convex instanceof Polygon) {
                    // since Triangle, Rectangle, and Polygon are all of
                    // type Polygon in addition to their main type
                    Polygon p = (Polygon) convex;
                    if (this.getFixtureCount() == 1 && convex instanceof Rectangle) {
                        Rectangle r = (Rectangle)convex;
                        Vector2 c = r.getCenter();
                        double w = r.getWidth();
                        double h = r.getHeight();
                        g.drawImage(CRATE,
                                (int)Math.ceil((c.x - w / 2.0) * SCALE),
                                (int)Math.ceil((c.y - h / 2.0) * SCALE),
                                (int)Math.ceil(w * SCALE),
                                (int)Math.ceil(h * SCALE),
                                null);
                    } else {
                        int l = p.getVertices().length;
                        int[] x = new int[l];
                        int[] y = new int[l];

                        int i = 0;
                        for (Vector2 v : p.getVertices()) {
                            x[i] = (int)(v.x * SCALE);
                            y[i] = (int)(v.y * SCALE);
                            i++;
                        }

                        java.awt.Polygon poly = new java.awt.Polygon(x, y, l);

                        // set the color
                        g.setColor(this.color);
                        // fill the shape
                        g.fillPolygon(poly);
                        // set the color
                        g.setColor(this.color.darker());
                        // draw the shape
                        g.drawPolygon(poly);
                    }
                } else if (convex instanceof Circle) {
                    // cast the shape to get the radius
                    Circle c = (Circle) convex;
                    double r = c.getRadius();
                    Vector2 cc = c.getCenter();
                    int x = (int)Math.ceil((cc.x - r) * SCALE);
                    int y = (int)Math.ceil((cc.y - r) * SCALE);
                    int w = (int)Math.ceil(r * 2 * SCALE);
                    if (this.getFixtureCount() == 1) {
                        // lets us an image instead
                        g.drawImage(BASKETBALL, x, y, w, w, null);
                    } else {
                        // set the color
                        g.setColor(this.color);
                        // fill the shape
                        g.fillOval(x, y, w, w);
                        // set the color
                        g.setColor(this.color.darker());
                        // draw the shape
                        g.drawOval(x, y, w, w);
                    }
                }
            }

            // set the original transform
            g.setTransform(ot);
        }
    }

    /** The canvas to draw to */
    protected Canvas canvas;

    /** The dynamics engine */
    protected World world;

    /** Wether the example is stopped or not */
    protected boolean stopped;

    /** The time stamp for the last iteration */
    protected long last;

    /**
     * Default constructor for the window
     */
    public ExampleImages() {
        super("Graphics2D Example");
        // setup the JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add a window listener
        this.addWindowListener(new WindowAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
             */
            @Override
            public void windowClosing(WindowEvent e) {
                // before we stop the JVM stop the example
                stop();
                super.windowClosing(e);
            }
        });

        // create the size of the window
        Dimension size = new Dimension(800, 600);

        // create a canvas to paint to
        this.canvas = new Canvas();
        this.canvas.setPreferredSize(size);
        this.canvas.setMinimumSize(size);
        this.canvas.setMaximumSize(size);

        // add the canvas to the JFrame
        this.add(this.canvas);

        // make the JFrame not resizable
        // (this way I dont have to worry about resize events)
        this.setResizable(false);

        // size everything
        this.pack();

        // make sure we are not stopped
        this.stopped = false;

        // setup the world
        this.initializeWorld();
    }

    /**
     * Creates game objects and adds them to the world.
     * <p>
     * Basically the same shapes from the Shapes test in
     * the TestBed.
     */
    protected void initializeWorld() {
        // create the world
        this.world = new World();

        // create all your bodies/joints

        // create the floor
        Rectangle floorRect = new Rectangle(15.0, 1.0);
        GameObject floor = new GameObject();
        floor.addFixture(new BodyFixture(floorRect));
        floor.setMass(MassType.INFINITE);
        // move the floor down a bit
        floor.translate(0.0, -4.0);
        this.world.addBody(floor);

        // create a triangle object
        Triangle triShape = new Triangle(
                new Vector2(0.0, 0.5),
                new Vector2(-0.5, -0.5),
                new Vector2(0.5, -0.5));
        GameObject triangle = new GameObject();
        triangle.addFixture(triShape);
        triangle.setMass();
        triangle.translate(-1.0, 2.0);
        // test having a velocity
        //triangle.getVelocity().set(5.0, 0.0);
        this.world.addBody(triangle);

        // create a circle
        Circle cirShape = new Circle(0.5);
        GameObject circle = new GameObject();
        circle.addFixture(cirShape);
        circle.setMass();
        circle.translate(2.0, 2.0);
        // test adding some force
        circle.applyForce(new Vector2(-100.0, 0.0));
        // set some linear damping to simulate rolling friction
        circle.setLinearDamping(0.05);
        this.world.addBody(circle);

        // try a rectangle
        Rectangle rectShape = new Rectangle(1.0, 1.0);
        GameObject rectangle = new GameObject();
        rectangle.addFixture(rectShape);
        rectangle.setMass();
        rectangle.translate(0.0, 2.0);
        //rectangle.getVelocity().set(-5.0, 0.0);
        this.world.addBody(rectangle);

        // try a polygon with lots of vertices
        Polygon polyShape = Geometry.createUnitCirclePolygon(10, 1.0);
        GameObject polygon = new GameObject();
        polygon.addFixture(polyShape);
        polygon.setMass();
        polygon.translate(-2.5, 2.0);
        // set the angular velocity
        polygon.setAngularVelocity(Math.toRadians(-20.0));
        this.world.addBody(polygon);

        // try a compound object (Capsule)
        Circle c1 = new Circle(0.5);
        BodyFixture c1Fixture = new BodyFixture(c1);
        c1Fixture.setDensity(0.5);
        Circle c2 = new Circle(0.5);
        BodyFixture c2Fixture = new BodyFixture(c2);
        c2Fixture.setDensity(0.5);
        Rectangle rm = new Rectangle(2.0, 1.0);
        // translate the circles in local coordinates
        c1.translate(-1.0, 0.0);
        c2.translate(1.0, 0.0);
        GameObject capsule = new GameObject();
        capsule.addFixture(c1Fixture);
        capsule.addFixture(c2Fixture);
        capsule.addFixture(rm);
        capsule.setMass();
        capsule.translate(0.0, 4.0);
        this.world.addBody(capsule);

        GameObject issTri = new GameObject();
        issTri.addFixture(Geometry.createIsoscelesTriangle(1.0, 3.0));
        issTri.setMass();
        issTri.translate(2.0, 3.0);
        this.world.addBody(issTri);

        GameObject equTri = new GameObject();
        equTri.addFixture(Geometry.createEquilateralTriangle(2.0));
        equTri.setMass();
        equTri.translate(3.0, 3.0);
        this.world.addBody(equTri);

        GameObject rightTri = new GameObject();
        rightTri.addFixture(Geometry.createRightTriangle(2.0, 1.0));
        rightTri.setMass();
        rightTri.translate(4.0, 3.0);
        this.world.addBody(rightTri);
    }

    /**
     * Start active rendering the example.
     * <p>
     * This should be called after the JFrame has been shown.
     */
    public void start() {
        // initialize the last update time
        this.last = System.nanoTime();
        // don't allow AWT to paint the canvas since we are
        this.canvas.setIgnoreRepaint(true);
        // enable double buffering (the JFrame has to be
        // visible before this can be done)
        this.canvas.createBufferStrategy(2);
        // run a separate thread to do active rendering
        // because we don't want to do it on the EDT
        Thread thread = new Thread() {
            public void run() {
                // perform an infinite loop stopped
                // render as fast as possible
                while (!isStopped()) {
                    gameLoop();
                    // you could add a Thread.yield(); or
                    // Thread.sleep(long) here to give the
                    // CPU some breathing room
                }
            }
        };
        // set the game loop thread to a daemon thread so that
        // it cannot stop the JVM from exiting
        thread.setDaemon(true);
        // start the game loop
        thread.start();
    }

    /**
     * The method calling the necessary methods to update
     * the game, graphics, and poll for input.
     */
    protected void gameLoop() {
        // get the graphics object to render to
        Graphics2D g = (Graphics2D)this.canvas.getBufferStrategy().getDrawGraphics();

        // before we render everything im going to flip the y axis and move the
        // origin to the center (instead of it being in the top left corner)
        AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
        AffineTransform move = AffineTransform.getTranslateInstance(400, -300);
        g.transform(yFlip);
        g.transform(move);

        // now (0, 0) is in the center of the screen with the positive x axis
        // pointing right and the positive y axis pointing up

        // render anything about the Example (will render the World objects)
        this.render(g);

        // dispose of the graphics object
        g.dispose();

        // blit/flip the buffer
        BufferStrategy strategy = this.canvas.getBufferStrategy();
        if (!strategy.contentsLost()) {
            strategy.show();
        }

        // Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();

        // update the World

        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = (double)diff / NANO_TO_BASE;
        // update the world with the elapsed time
        this.world.update(elapsedTime);
    }

    /**
     * Renders the example.
     * @param g the graphics object to render to
     */
    protected void render(Graphics2D g) {
        // lets draw over everything with a white background
        g.setColor(Color.WHITE);
        g.fillRect(-400, -300, 800, 600);

        // lets move the view up some
        g.translate(0.0, -1.0 * SCALE);

        // draw all the objects in the world
        for (int i = 0; i < this.world.getBodyCount(); i++) {
            // get the object
            GameObject go = (GameObject) this.world.getBody(i);
            // draw the object
            go.render(g);
        }
    }

    /**
     * Stops the example.
     */
    public synchronized void stop() {
        this.stopped = true;
    }

    /**
     * Returns true if the example is stopped.
     * @return boolean true if stopped
     */
    public synchronized boolean isStopped() {
        return this.stopped;
    }

    /**
     * Entry point for the example application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // create the example JFrame
        ExampleImages window = new ExampleImages();

        // show it
        window.setVisible(true);

        // start it
        window.start();
    }
}

