import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;


class util {
  
  // produce new ArrayList<T> containing all the items of the given list that pass the predicate
  <T> ArrayList<T> filter(ArrayList<T> arr, Predicate<T> pred) {
    ArrayList<T> result = new ArrayList<T>();
    for (T t: arr) {
      if (pred.apply(t)) {
        result.add(t);
      } 
    }
    return result;

  }

  
  // modifies the given list to remove everything that fails the predicate
  <T> void removeExcept(ArrayList<T> arr, Predicate<T> pred) {
    ArrayList<T> otherResult = new ArrayList<T>();

    if (pred.apply(arr.get(0))) {
      otherResult.add(arr.get(0));
      arr.remove(0);
      removeExcept(arr,  pred);
    } else if (arr.size() == 0) {
      otherResult = otherResult;
    } else {
      arr.remove(0);
      removeExcept(arr,  pred);
    }
   
    
  }
  

}

interface Predicate<T> {
  boolean apply(T t);
}

class isZero implements Predicate<Integer> {

  public boolean apply(Integer i) {
    return i % 2 != 0;
  }
}

//Represents an individual tile
class Tile {
  // The number on the tile.  Use 0 to represent the hole
  int value;
  int col;
  int row;
  
  Tile(int value, int col, int row) {
    this.value = value;
    this.col = col;
    this.row = row;
  }
  
  // Draws this tile onto the background at the specified logical coordinates
  WorldImage drawAt(int col, int row, WorldImage background) {
    if (value != 0) {
      background.movePinholeTo(new Posn(col * 50, row * 50));
      return new OverlayImage(
      new TextImage(this.toString(), 18, Color.BLACK), new OverlayImage(
      new RectangleImage(50, 50, OutlineMode.OUTLINE, Color.yellow), background));
    }
    return background;
  }
}

class FifteenGame extends World {
  // represents the rows of tiles
  ArrayList<ArrayList<Tile>> tiles;
  
  FifteenGame( ArrayList<ArrayList<Tile>> tiles) {
    this.tiles = tiles;
  }
  
  // draws the list of tiles
  public WorldImage makeImage() {
    WorldImage image = new EmptyImage();
    int column = 0;
    int row = 0;
    for (ArrayList<Tile> arr : tiles) {
        for (Tile t : arr) {
          image = t.drawAt(column, row, image);
            row++;
        }
        column++;
    }
    return image;
  }
  
  //draws the game
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(500, 500);   
    scene.placeImageXY(makeImage(), 250, 250);
    return scene;
    
  }
  
  
  // handles keystrokes
  public void onKeyEvent(String k) {
    // needs to handle up, down, left, right to move the hole
    // extra: handle "u" to undo moves
    
    Tile hole = new Tile(0, 0, 0);
    if (k.equals("up")) {
      hole.row = hole.row +1; 
    }
    
    if (k.equals("down")) {
      hole.row = hole.row - 1; 
    }
    
    if (k.equals("left")) {
      hole.col = hole.col - 1; 
    }
    
    if (k.equals("right")) {
      hole.col = hole.col + 1; 
    }
   
   
  }
}

class FifteenGameExample {
  FifteenGameExample() {}

  ArrayList<Integer> list1;
  Predicate<Integer> func;

  void initData() {
    this.list1 = new ArrayList<Integer>();
    this.func = new isZero();
    list1.add(0);
    list1.add(1);
    list1.add(2);
    list1.add(3);
    list1.add(4);
    list1.add(5); 
    list1.add(6);
  }

  void testfilter(Tester t) {
    initData();
    t.checkExpect(list1.size(), 7);
    ArrayList<Integer> result = new util().filter(list1, func); 
    t.checkExpect(result.size(), 3);
    t.checkExpect(result.get(0), 1);
    t.checkExpect(result.get(1), 3);
    t.checkExpect(result.get(2), 5); 
  } 
  
  void testremoveExcept(Tester t) {
    
    initData();
    t.checkExpect(list1.size(), 7);
    new util().removeExcept(list1, func); 
    t.checkExpect(list1.size(), 3);
  }
  
//  void testGame(Tester t) {
//    FifteenGame g = new FifteenGame();
//    g.bigBang(120, 120);
//  }

 
  
}
