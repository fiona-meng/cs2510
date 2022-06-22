import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library

import java.awt.Color; // general colors (as triples of red,green,blue values)
                       // and predefined colors (Color.RED, Color.GRAY, etc.)
import java.util.function.*;

class MyPosn extends Posn {

  // standard constructor
  MyPosn(int x, int y) {
    super(x, y);
  }

  // constructor to convert from a Posn to a MyPosn
  MyPosn(Posn p) {
    this(p.x, p.y);
  }

  // add the given position's x and y to this position
  MyPosn add(MyPosn mp) {
    return new MyPosn(this.x + mp.x, this.y + mp.y);
  }

  // judge whether this position is outside of the given screen
  boolean isOffscreen(int width, int height) {
    return 0 <= this.x && this.x <= width && 0 <= this.y && this.y <= height;
  }
}

class Circle {

  MyPosn position; // in pixels
  MyPosn velocity; // in pixels/tick
  int radius;

  Circle(MyPosn position, MyPosn velocity, int radius) {
    this.position = position;
    this.velocity = velocity;
    this.radius = radius;
  }

  Circle move() {
    this.position = this.position.add(this.velocity);
    return this;
  }

  WorldImage draw() {
    return new VisiblePinholeImage(new CircleImage(20, OutlineMode.SOLID, Color.BLUE));
  }

  WorldScene place(WorldScene scene) {
    return new Utils().placeImage(scene, this.draw(), this.position);
  }
}

class Utils {
  WorldScene placeImage(WorldScene scene, WorldImage image, MyPosn p) {
    return scene.placeImageXY(image, p.x, p.y);
  }

  boolean circleIsOffscreen(Circle c, int width, int height) {
    int radius = c.radius;
    return c.position.add(new MyPosn(radius, radius)).isOffscreen(width, height)
        && c.position.add(new MyPosn(-radius, -radius)).isOffscreen(width, height);
  }

  Circle circle(MyPosn p) {
    return new Circle(p, new MyPosn(0, -2), 20);
  }
}

class PlaceCircleOnAScene implements BiFunction<Circle, WorldScene, WorldScene> {

  public WorldScene apply(Circle c, WorldScene s) {
    return c.place(s);
  }
}

abstract class BiFunctionWithScreenSize<T> implements BiFunction<Circle, T, T> {
  int screenWidth;
  int screenHeight;

  BiFunctionWithScreenSize(int screenWidth, int screenHeight) {
    super();
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }

  abstract public T apply(Circle c, T t);
}

class CircleIsOffscreenOrMap extends BiFunctionWithScreenSize<Boolean> {

  CircleIsOffscreenOrMap(int screenWidth, int screenHeight) {
    super(screenWidth, screenHeight);
  }

  public Boolean apply(Circle c, Boolean r) {
    return r || c.position.isOffscreen(this.screenWidth, this.screenHeight);
  }
}

class RemoveCircleOffScreenHelper extends BiFunctionWithScreenSize<ILoCircle> {

  RemoveCircleOffScreenHelper(int screenWidth, int screenHeight) {
    super(screenWidth, screenHeight);
  }

  public ILoCircle apply(Circle c, ILoCircle l) {
    if (c.position.isOffscreen(this.screenWidth, this.screenHeight)) {
      return new ConsLoCircle(c, l);
    }
    else {
      return l;
    }
  }
}

interface ILoCircle {
  ILoCircle moveAll();

  <T> T foldr(BiFunction<Circle, T, T> f, T base);

  Boolean isOffscreen(int width, int height);

  ILoCircle removeOffscreen(int width, int height);

  WorldScene placeAll(WorldScene scene);
}

class MtLoCircle implements ILoCircle {
  MtLoCircle() {
  }

  public ILoCircle moveAll() {
    return this;
  }

  public <T> T foldr(BiFunction<Circle, T, T> f, T base) {
    return base;
  }

  public Boolean isOffscreen(int width, int height) {
    return false;
  }

  public ILoCircle removeOffscreen(int width, int height) {
    return this;
  }

  public WorldScene placeAll(WorldScene scene) {
    return scene;
  }

}

class ConsLoCircle implements ILoCircle {
  Circle first;
  ILoCircle rest;

  ConsLoCircle(Circle first, ILoCircle rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoCircle moveAll() {
    return new ConsLoCircle(this.first.move(), this.rest.moveAll());
  }

  public <T> T foldr(BiFunction<Circle, T, T> f, T base) {
    return f.apply(this.first, this.rest.foldr(f, base));
  }

  public Boolean isOffscreen(int width, int height) {
    return this.foldr(new CircleIsOffscreenOrMap(width, height), false);
  }

  public ILoCircle removeOffscreen(int width, int height) {
    return this.foldr(new RemoveCircleOffScreenHelper(width, height), new MtLoCircle());
  }

  public WorldScene placeAll(WorldScene scene) {
    return this.foldr(new PlaceCircleOnAScene(), scene);
  }
}

class Game extends World {

  int width;
  int height;
  ILoCircle listOfCircles;
  int numOfCirclesOffscreenNeeded;

  Game(int width, int height, int numOfCirclesOffscreenNeeded) {
    this.width = width;
    this.height = height;
    this.listOfCircles = new MtLoCircle();
    this.numOfCirclesOffscreenNeeded = numOfCirclesOffscreenNeeded;
  }

  Game(int numOfCirclesOffscreenNeeded) {
    this(500, 500, numOfCirclesOffscreenNeeded);
  }

  public WorldScene makeScene() {
    return listOfCircles.placeAll(this.getEmptyScene());
  }

  boolean bigBang(double tickRate) {
    return bigBang(this.width, this.height, tickRate);
  }

  public World onMouseClicked(Posn mouse) {
    this.listOfCircles = new ConsLoCircle(new Utils().circle(new MyPosn(mouse)),
        this.listOfCircles);
    return this;
  }

  public World onTick() {
    this.listOfCircles = this.listOfCircles.moveAll();
    if(this.listOfCircles.isOffscreen(width, height)) {
      
    }
    return this;
  }
}

class ExampleGame {
  ExampleGame() {
  }

  Circle circle1 = new Circle(new MyPosn(230, 230), new MyPosn(0, -100), 30);
  Circle circle2 = new Circle(new MyPosn(400, 400), new MyPosn(0, -100), 40);
  Circle circle3 = new Circle(new MyPosn(700, 400), new MyPosn(-7, -4), 40);
  Circle circle4 = new Circle(new MyPosn(230, 130), new MyPosn(0, -100), 30);
  Circle circle5 = new Circle(new MyPosn(400, 300), new MyPosn(0, -100), 40);
  ILoCircle empty = new MtLoCircle();
  ILoCircle listOfCircles1 = new ConsLoCircle(circle2, new ConsLoCircle(circle1, new MtLoCircle()));
  ILoCircle listOfCircles2 = new ConsLoCircle(circle3, listOfCircles1);
  ILoCircle listOfCircles3 = new ConsLoCircle(circle5, new ConsLoCircle(circle4, new MtLoCircle()));

  boolean testMoveAll(Tester t) {
    return t.checkExpect(empty.moveAll(), empty.moveAll())
        && t.checkExpect(listOfCircles1.moveAll(), listOfCircles3);
  }

  boolean testIsOffscreen(Tester t) {
    return t.checkExpect(listOfCircles2.isOffscreen(500, 500), true)
        && t.checkExpect(empty.isOffscreen(500, 500), false);
  }

  boolean testRemoveOffscreen(Tester t) {
    return t.checkExpect(listOfCircles2.removeOffscreen(500, 500), listOfCircles1)
        && t.checkExpect(empty.removeOffscreen(500, 500), empty);
  }

  boolean testPlaceAll(Tester t) {
    return true;
  }

  boolean testBigBang(Tester t) {
    Game w = new Game(500, 500, 20);
    return w.bigBang(1.0 / 24.0);
  }
}