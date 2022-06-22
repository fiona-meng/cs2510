import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
// and predefined colors (Color.RED, Color.GRAY, etc.)

class MyPosn extends Posn {

  // standard constructor
  MyPosn(int x, int y) {
    super(x, y);
  }

  // constructor to convert from a Posn to a MyPosn
  MyPosn(Posn p) {
    this(p.x, p.y);
  }
  
  public MyPosn add(MyPosn p) {
    return new MyPosn(this.x + p.x, this.y + p.y);
  }
  
  public boolean isOffscreen(int width, int height) {
    return this.x < width
        && this.y < height;
  }
}
interface ILoCircle{

}

class MtLCircle implements ILoCircle{
  MtLCircle(){}
  
}

class ConsLoCircle implements ILoCircle{
  Circle first;
  ILoCircle rest;
  
  ConsLoCircle(Circle first, ILoCircle rest){
    this.first = first;
    this.rest = rest;
  }
}

class Circle {
  MyPosn position; // in pixels
  MyPosn velocity; // in pixels/tick
  int radius;

  Circle(MyPosn position, MyPosn velocity,  int radius){
    this.position = position;
    this.velocity = velocity;
    this.radius = radius;
  }

}

abstract class World implements ILoCircle{
  
  
  public WorldEnd worldEnds() {
    if (theGameIsOver) {
      return new WorldEnd(true, new OverlayImages(this.makeScene(),
          new TextImage(new Posn(20, 20), "Game over", 
              20, 3, new Color(100, 0, 200))));
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }
}

class ILoCircleExample{
  ILoCircleExample(){}
  
  MyPosn position1 = new MyPosn(10, 20);
  MyPosn position2 = new MyPosn(20, 30);
  MyPosn position3 = new MyPosn(40, 60);
  MyPosn velocity1 = new MyPosn(1, 1);
  MyPosn velocity2 = new MyPosn(2, 3);
  Circle circle1 = new Circle(position1, velocity1, 5);
  Circle circle2 = new Circle(position2, velocity1, 5);
  Circle circle3 = new Circle(position2, velocity2, 5);
  Circle circle4 = new Circle(position3, velocity2, 5); 
  ILoCircle list1 = new ConsLoCircle(circle1, new ConsLoCircle(circle2, 
      new ConsLoCircle(circle3, new ConsLoCircle(circle4, new MtLCircle()))));
  
}