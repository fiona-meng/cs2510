import java.util.ArrayList;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;



//Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;



  Cell(int x, int y, int usedColor, boolean flooded) {
    this.x = x;
    this.y = y;
    Random rand = new Random();

    ArrayList<Color> colorList = new ArrayList<Color>();
    colorList.add(Color.red);
    colorList.add(Color.blue);
    colorList.add(Color.green);
    colorList.add(Color.black);
    colorList.add(Color.yellow);
    colorList.add(Color.pink);
    colorList.add(Color.cyan);
    colorList.add(Color.gray);

    int i = rand.nextInt(usedColor);
    this.color = colorList.get(i);
    this.flooded = flooded;
  }

  Cell(int x, int y, Color color, boolean flooded, Cell left, Cell top, Cell right,
      Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  // draw the cell or square with size 30 and given color
  WorldImage draw() {
    return new RectangleImage(30, 30, OutlineMode.SOLID, this.color);
  }

  // change color to given color
  void updateColor(Color newColor) {
    this.color = newColor;
  }

  // change the adjacent cells' color if the color is same as the top left cell 
  void updateneighbors(Color color) {
    if (this.left != null
        && !this.left.flooded
        && this.left.color.equals(color)) {
      this.left.flooded = true;
    }
    if (this.top != null
        && !this.top.flooded 
        && this.top.color.equals(color)) {
      this.top.flooded = true;
    }
    if (this.right != null
        && !this.right.flooded
        && this.right.color.equals(color)) {
      this.right.flooded = true;
    }
    if (this.bottom != null
        && !this.bottom.flooded 
        && this.bottom.color.equals(color)) {
      this.bottom.flooded = true;
    }
  }



}





class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<Cell> board;
  int boardSize;
  int colorSize;
  int chance;
  int click = 0;
  int time = 0;

  FloodItWorld(int colorSize, int boardSize) {
    this.boardSize = boardSize;
    this.colorSize = colorSize;  
    makeBoard(this.boardSize);
    if (boardSize < 5) {
      this.chance = 12;
    } else {
      this.chance = 30;
    }
  }

  // create a two-dimensional grid of these Cells to represent the board
  void makeBoard(int size) {
    board = new ArrayList<Cell>();
    for (int i = 0; i < size; i = i + 1) {
      for (int k = 0; k < size; k = k + 1) {
        if (i == 0 && k == 0) {
          board.add(new Cell(i, k, this.colorSize, true));
        } else {
          board.add(new Cell(i, k, this.colorSize, false));
        }
      }
    }

    for (int i = 0; i < board.size(); i = i + 1) {
      Cell c = board.get(i);
      if (c.x == 0) {
        c.left = null;
      } else {
        c.left = board.get(i - size);
      }

      if (c.x == size - 1) {
        c.right = null;
      }
      else {
        c.right = board.get(i + size);
      }

      if (c.y == 0) {
        c.top = null;
      } 
      else {
        c.top = board.get(i - 1);
      }
      if (c.y == size - 1) {
        c.bottom = null;
      } 
      else {
        c.bottom = board.get(i + 1);
      }
    }


  }

  // make the scene with the board, game name, score, and whether win the game
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(2000, 2000);

    for (Cell c: board) {
      scene.placeImageXY(c.draw(),  80 + 30 * c.x, 80 + 30 * c.y);
    }

    scene.placeImageXY(new TextImage(Integer.toString(click), 30, Color.black), 400, 400);
    scene.placeImageXY(new TextImage("Flood-It", 30, Color.pink), 180, 50);
    scene.placeImageXY(new TextImage("/", 30, Color.black), 430, 400);
    scene.placeImageXY(new TextImage(Integer.toString(chance), 30, Color.black), 460, 400);
    scene.placeImageXY(new TextImage("Time: " + time, 30, Color.black), 400, 450);

    if (click >= chance && !isWin()) {
      scene.placeImageXY(new TextImage("You Lose", 30, Color.red), 250, 400);
    }

    if (click <= chance && isWin()) {
      scene.placeImageXY(new TextImage("You Win", 30, Color.red), 250, 400);
    }

    return scene;
  }


  // check whether win the game by checking whether all the cells are flooded
  boolean isWin() {
    boolean win = true;
    for (Cell c: board) {
      win = win && c.flooded;
    }
    return win;
  }

  // change flooded cell's color
  public void updateWorld() {
    for (int i = 0; i < board.size(); i = i + 1) {
      Cell cell = board.get(i);
      if (cell.flooded) {
        cell.color = this.board.get(0).color;
        cell.updateneighbors(this.board.get(0).color);
      }
      makeScene();
    }


  }



  // mouse click event 
  // determine whether cell is clicked
  public Cell determineCell(Posn pos) {
    Cell cell = null;
    for (Cell c: board) {
      if ((c.x <= ((pos.x - 71) / 30)) 
          && (((pos.x - 71) / 30) <= c.x )
          && (c.y <= ((pos.y - 71) / 30)) 
          && (((pos.y - 71) / 30) <= c.y )) {
        cell = c;
      }
    }
    return cell;
  }

  // update color of the top left cell
  public void updateClick(Cell cell) {
    if (cell != null) {
      Cell c = board.get(0);
      c.color = cell.color;
      board.set(0, c);
    }
  }


  // the region of mouse click event 
  public void onMouseClicked(Posn pos) {
    if (pos.x < 70 || pos.x > (boardSize * 30 + 70)
        || (pos.y < 70) 
        || pos.y > (boardSize * 30 + 70)) {
      return;
    } else {
      this.updateClick(this.determineCell(pos));
      this.updateWorld();
      click++;
    }
  }

  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.board = new ArrayList<Cell>();
      click = 0;
      makeBoard(boardSize);
    }

  }

  public void onTick() {
    time++;
  }



}



class ExampleFloodItWorld {
  ExampleFloodItWorld() {}

  Cell c1;
  Cell c2;
  Cell c3;
  Cell c4;
  FloodItWorld f1;

  Cell cc1;
  Cell cc2;
  Cell cc3;
  Cell cc4;
  FloodItWorld f2;





  void initData() {
    c1 = new Cell(0, 0, Color.pink, true, null, null, c3, c2);
    c2 = new Cell(0, 1, Color.yellow, false, null, c1, c4, c3);
    c3 = new Cell(1, 0, Color.green, false, c1, null, null, c4);
    c4 = new Cell(1, 1, Color.yellow, false, null, c3, null, null);
    c1.bottom = c2;
    c1.right = c3;
    c2.top = c1;
    c2.right = c4;
    c3.left = c1;
    c3.bottom = c4;
    c4.top = c3;
    c4.left = c2;

    this.f1 =  new FloodItWorld(1, 1);
    f1.board.add(c1);
    f1.board.add(c2);
    f1.board.add(c3);
    f1.board.add(c4);

    cc1 = new Cell(0, 0, Color.pink, true, null, null, c3, c2);
    cc2 = new Cell(0, 1, Color.yellow, true, null, c1, c4, c3);
    cc3 = new Cell(1, 0, Color.green, true, c1, null, null, c4);
    cc4 = new Cell(1, 1, Color.yellow, true, null, c3, null, null);

    this.f2 =  new FloodItWorld(2, 2);
    f2.board = new ArrayList<Cell>();
    f2.board.add(cc1);
    f2.board.add(cc2);
    f2.board.add(cc3);
    f2.board.add(cc4);

  }

  void testdraw(Tester t) {
    initData();
    t.checkExpect(c1.draw(), new RectangleImage(30, 30, OutlineMode.SOLID, Color.pink));
    t.checkExpect(c2.draw(), new RectangleImage(30, 30, OutlineMode.SOLID, Color.yellow));
    t.checkExpect(c3.draw(), new RectangleImage(30, 30, OutlineMode.SOLID, Color.green));
    t.checkExpect(c4.draw(), new RectangleImage(30, 30, OutlineMode.SOLID, Color.yellow));
  }

  void testupdateColor(Tester t) {
    initData();
    c1.updateColor(Color.green);
    t.checkExpect(c1.color, Color.green);
    c1.updateColor(Color.yellow);
    t.checkExpect(c1.color, Color.yellow);
    c2.updateColor(Color.black);
    t.checkExpect(c2.color, Color.black);
  }

  void testupdateneighbors(Tester t) {
    initData();
    t.checkExpect(c2.flooded, false);
    t.checkExpect(c2.color.equals(Color.yellow), true);
    c1.updateneighbors(Color.yellow);
    t.checkExpect(this.c1.bottom.flooded, true);
    t.checkExpect(this.c1.bottom, c2);
  }

  void testisWin(Tester t) {
    initData();
    t.checkExpect(f1.isWin(), false);
  }

  void testupdateWorld(Tester t) {
    initData();
    f2.updateWorld();
    t.checkExpect(f2.board.get(1).color, f2.board.get(0).color);
    t.checkExpect(f2.board.get(2).color, f2.board.get(0).color);
    t.checkExpect(f2.board.get(3).color, f2.board.get(0).color);

  }


  void testupdateClick(Tester t) {
    initData();
    this.f1.updateClick(c2);
    t.checkExpect(f1.board.get(0).color, Color.yellow);
    this.f1.updateClick(c3);
    t.checkExpect(f1.board.get(0).color, Color.green);
  }



  void testonKeyEvent(Tester t) {

    initData();
    ArrayList<Cell> list = new ArrayList<Cell>();
    list = f1.board;
    f1.onKeyEvent("a");
    t.checkExpect(f1.board.equals(list), true);
    f1.onKeyEvent("r");
    t.checkExpect(f1.board.equals(list), false);

  }



  void testmakeBoard(Tester t) {
    initData();
    f1.makeBoard(2);
    t.checkExpect(f1.board.size(), 4);
    t.checkExpect(f1.board.get(0).x, 0);
    t.checkExpect(f1.board.get(0).y, 0);
    t.checkExpect(f1.board.get(1).x, 0);
    t.checkExpect(f1.board.get(1).y, 1);
    t.checkExpect(f1.board.get(2).x, 1);
    t.checkExpect(f1.board.get(2).y, 0);
    t.checkExpect(f1.board.get(3).x, 1);
    t.checkExpect(f1.board.get(3).y, 1);
  }




  void testdetermineCell(Tester t) {
    initData();
    t.checkExpect(f1.determineCell(new Posn(90,90)), c1);
    t.checkExpect(f1.determineCell(new Posn(90,120)), c2);
    t.checkExpect(f1.determineCell(new Posn(120,90)), c3);
    t.checkExpect(f1.determineCell(new Posn(120,120)), c4);

  }


  void testmakeScene(Tester t) {
    initData();
    WorldScene testscene = new WorldScene(2000, 2000);
    testscene.placeImageXY(c1.draw(), 80, 80);
    testscene.placeImageXY(c2.draw(), 80, 110);
    testscene.placeImageXY(c3.draw(), 110, 80);
    testscene.placeImageXY(c4.draw(), 110, 110);
    testscene.placeImageXY(new TextImage("0", 30, Color.black), 400, 400);
    testscene.placeImageXY(new TextImage("Flood-It", 30, Color.pink), 180, 50);
    testscene.placeImageXY(new TextImage("/", 30, Color.black), 430, 400);
    testscene.placeImageXY(new TextImage("0", 30, Color.black), 460, 400);
    testscene.placeImageXY(new TextImage("Time: 0", 30, Color.black), 400, 450);
    //t.checkExpect(f1.makeScene(), testscene);

  }

  void testonMouseClicked(Tester t) {
    initData();
    t.checkExpect(f2.board.get(0), cc1);
    t.checkExpect(f2.click, 0);
    f2.onMouseClicked(new Posn(90, 90));
    t.checkExpect(f2.click, 1);
    t.checkExpect(f2.board.get(1).color, Color.pink);
    t.checkExpect(f2.board.get(2).color, Color.pink);
    t.checkExpect(f2.board.get(3).color, Color.pink);   
    f1.onMouseClicked(new Posn(100, 100));
    t.checkExpect(f1.click, 1);
    f1.onMouseClicked(new Posn(100, 100));
    t.checkExpect(f1.click, 2);

  }


  void testGame(Tester t) {
    FloodItWorld g = new FloodItWorld(7, 7);
    g.bigBang(500, 500);
  }
}


