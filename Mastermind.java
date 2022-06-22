import tester.Tester;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javalib.worldcanvas.WorldCanvas;

interface ILoColor {
  // count how many colors are matched;
  int exactMatches(ILoColor that);

  // count how many colors are matched;
  int sameColorHelper(Color that, ILoColor other);

  // determine how many inexact matches there are in the player's answer;
  int inexactMatches(ILoColor that);

  // determines whether the list of colors contain the given color;
  boolean contain(Color that);
  
  // count how many colors there are in the list;
  int count();

  // draws all the circles with the corresponding colors;
  WorldImage drawCircle();

  // removes all given colors from the list of color;
  ILoColor remove(Color that);

  // removes the last color;
  ILoColor removeLast();

  // adds a color to the list of color;
  ILoColor add(Color that);
  
  // returns the color corresponding to the given number;
  Color numbertoColor(int number);

  // removes the same colors from the list;
  ILoColor removeSame(ILoColor that);
  
  // removes the same colors from the list;
  ILoColor removeSameHelper(Color that, ILoColor other);
  
  // determine how many inexact matches there are in the player's answer;
  int inexactMatchesHelper(ILoColor that);
  
  // determine how many inexact matches there are in the player's answer;
  int inexactMatchesHelper2(Color other, ILoColor that);
}

class MtLoColor implements ILoColor {
  // count how many colors are matched;
  public int exactMatches(ILoColor that) {
    return 0;
  }

  // count rest of colors are matched;
  public int sameColorHelper(Color that, ILoColor other) {
    return 0;
  }

  // count how many colors there are in the list;
  public int count() {
    return 0;
  }

  // draws all the circles with the corresponding colors;
  public WorldImage drawCircle() {
    return new EmptyImage();
  }

  // removes all given colors from the list of color;
  public ILoColor remove(Color that) {
    return this;
  }

  // determine how many inexact matches there are in the player's answer;
  public int inexactMatches(ILoColor that) {
    return 0;
  }

  // determines whether the list of colors contain the given color;
  public boolean contain(Color that) {
    return false;
  }

  // removes the last color;
  public ILoColor removeLast() {
    return this;
  }

  // adds a color to the list of color;
  public ILoColor add(Color that) {
    return new ConsLoColor(that, this);
  }
  
  // returns the color corresponding to the given number;
  public Color numbertoColor(int number) {
    return null;
  }
  
  // removes the same colors from the list;
  public ILoColor removeSame(ILoColor that) {
    return this;
  }
  
  // removes the same colors from the list;
  public ILoColor removeSameHelper(Color that, ILoColor other) {
    return this;
  }
  
  // determine how many inexact matches there are in the player's answer;
  public int inexactMatchesHelper(ILoColor that) {
    return 0;
  }
  
  // determine how many inexact matches there are in the player's answer;
  public int inexactMatchesHelper2(Color that, ILoColor other) {
    return 0;
  }
}

class ConsLoColor implements ILoColor {
  Color first;
  ILoColor rest;

  ConsLoColor(Color first, ILoColor rest) {
    this.first = first;
    this.rest = rest;
  }

  // count how many colors are matched;
  public int exactMatches(ILoColor that) {
    return that.sameColorHelper(this.first, this.rest);
  }

  // count how many colors are matched;
  public int sameColorHelper(Color that, ILoColor other) {
    if (this.first.equals(that)) {
      return 1 + other.exactMatches(this.rest);
    } else {
      return other.exactMatches(this.rest);
    }
  }

  // count how many colors there are in the list;
  public int count() {
    return 1 + this.rest.count();
  }

  // draws all the circles with the corresponding colors;
  public WorldImage drawCircle() {
    return new BesideImage(new CircleImage(20, "solid", this.first), this.rest.drawCircle());
  }

  // determine how many inexact matches there are in the player's answer;
  public int inexactMatches(ILoColor that) {
    return this.removeSame(that).inexactMatchesHelper(that);
  }
  
  // determine how many inexact matches there are in the player's answer;
  public int inexactMatchesHelper(ILoColor that) {
    return that.inexactMatchesHelper2(this.first, this.rest);
  }
  
  // determine how many inexact matches there are in the player's answer;
  public int inexactMatchesHelper2(Color that, ILoColor other) {
    if (this.contain(that)) {
      return 1 + other.remove(that).inexactMatchesHelper(this);
    }else {
      return other.inexactMatchesHelper(this);
    }
  }
  
  // removes the same colors from the list;
  public ILoColor removeSame(ILoColor that) {
    return that.removeSameHelper(this.first, this.rest);
  }
  
  // removes the same colors from the list;
  public ILoColor removeSameHelper(Color that, ILoColor other) {
    if(that.equals(this.first)) {
      return other.removeSame(this.rest);
    }else {
      return new ConsLoColor(that, other.removeSame(this.rest));
    }
  }

  // removes all given colors from the list of color;
  public ILoColor remove(Color that) {
    if (this.first.equals(that)) {
      return this.rest.remove(that);
    } else {
      return new ConsLoColor(this.first, this.rest.remove(that));
    }
  }

  // determines whether the list of colors contain the given color;
  public boolean contain(Color that) {
    if (this.first == that) {
      return true;
    } else {
      return this.rest.contain(that);
    }
  }
  
  //remove the last color
  public ILoColor removeLast() {
    return this.rest;
  }

  //add a color to the list of color
  public ILoColor add(Color that) {
    return new ConsLoColor(that, this);
  }
  
  // returns the color corresponding to the given number;
  public Color numbertoColor(int number) {
    if (number == 1) {
      return this.first;
    } else {
    return this.rest.numbertoColor(number - 1);
    }
  }
}

interface IConstants {
  int TILE_SIZE = 40;
  Color TILE_COLOR = new Color(128, 0, 0);
  int TEXT_SIZE = 26;
}

interface IGuess {
  //
  WorldImage draw(ILoColor answer);
}

class UnfinishedGuesses implements IGuess {
  ILoColor listofColor;
  
  UnfinishedGuesses(ILoColor listofColor) {
    this.listofColor = listofColor;
  }

  // draws the list of circles with the corresponding colors;
  public WorldImage draw(ILoColor answer) {
    return this.listofColor.drawCircle();
  }
  
  // add guesses to the unfinished guesses;
  UnfinishedGuesses addGuess(Color that) {
    return new UnfinishedGuesses(this.listofColor.add(that));
  }
 
  // determines if the guesses are finishes;
  boolean isfinished(ILoColor answer) {
    return this.listofColor.count() == answer.count();
  }
 
  // convert the unfinished guesses into finishes guesses;
  FinishedGuesses convert() {
    return new FinishedGuesses(this.listofColor);
  }
 
  // deletes the last guess from the unfinished guesses;
  ILoColor deleteLast() {
    return this.listofColor.removeLast();
  }
 
  // determines if the guesses are correct;
  boolean isCorrected(ILoColor answer) {
    return this.listofColor.exactMatches(answer) == answer.count()
        && this.listofColor.count() == answer.count();
  }
  
}

class FinishedGuesses implements IGuess {
  ILoColor listofColor;

  FinishedGuesses(ILoColor listofColor) {
    this.listofColor = listofColor;
  }

  // draws the list of circles with the corresponding colors;
  public WorldImage draw(ILoColor answer) {
    return new BesideImage(this.listofColor.drawCircle(),
        new RectangleImage(50, 20, OutlineMode.SOLID, Color.white),
        new TextImage(Integer.toString(this.listofColor.exactMatches(answer)), 50, Color.BLUE),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.white),
        new TextImage(Integer.toString(this.listofColor.inexactMatches(answer)), 50, Color.BLUE));
  }
}

interface ILoFinishedGuesses {
  // draws the list of guesses;
  WorldImage drawLoGuess(ILoColor answer);
}

class MtFinishedGuesses implements ILoFinishedGuesses {
  
  // draws the list of guesses;
  public WorldImage drawLoGuess(ILoColor answer) {
    return new EmptyImage();
  }
}

class ConsLoFinishedGuesses implements ILoFinishedGuesses {
  FinishedGuesses first;
  ILoFinishedGuesses rest;
  
  ConsLoFinishedGuesses(FinishedGuesses first, ILoFinishedGuesses rest) {
    this.first = first;
    this.rest = rest;
  }
  
  // draws the list of guesses;
  public WorldImage drawLoGuess(ILoColor answer) {
    return new AboveImage(this.first.draw(answer),
        this.rest.drawLoGuess(answer));
  }
}

class Mastermind extends World implements IConstants {
  boolean duplicateallowed;// Whether or not duplicate entries are allowed in the correct sequence;
  int length; // The length of the sequence to be guessed;
  int guesses; // The number of guesses a player is allowed;
  ILoColor listofColor; // A list of colors that the sequence could have been made from,
  // which also serves as the colors a player can guess
  ILoFinishedGuesses listofGuess;
  IGuess unfinished;
  ILoColor correctAnswer;

  Mastermind(boolean duplicateallowed, int length, int guesses, ILoColor listofColor) {
    if (duplicateallowed == false && length > listofColor.count()) {
      throw new IllegalArgumentException("Invalid length: " + Integer.toString(length));
    } else {
      this.duplicateallowed = duplicateallowed;
    }
    if (length > 0) {
      this.length = length;
    } else {
      throw new IllegalArgumentException("Invalid length: " + Integer.toString(length));
    }
    if (guesses > 0) {
      this.guesses = guesses;
    } else {
      throw new IllegalArgumentException("Invalid length: " + Integer.toString(guesses));
    }
    if (listofColor.count() > 0) {
      this.listofColor = listofColor;
    } else {
      throw new IllegalArgumentException(
          "Invalid length: " + Integer.toString(listofColor.count()));
    }
  }
  
  Mastermind(boolean duplicateallowed, int length, int guesses, ILoColor listofColor,
      ILoFinishedGuesses listofGuess, UnfinishedGuesses unfinished, ILoColor correctAnswer) {
    this.duplicateallowed = duplicateallowed;
    this.length = length;
    this.guesses = guesses;
    this.listofColor = listofColor;
    this.listofGuess = listofGuess;
    this.unfinished = unfinished;
    this.correctAnswer = correctAnswer;
  }
  
  
  // makes the first scene of the world;
//  public WorldScene makeScene() {
//    WorldScene scene = this.getEmptyScene();
//    scene.placeImageXY(this.listofColor.drawCircle(), 250, 250);
//    return scene;
//  }
  
  // draws the world;
  public WorldImage drawMastermind() {
    return new AboveImage(
        new AboveImage(new OverlayImage(new RectangleImage(TILE_SIZE * this.correctAnswer.count(),
        TILE_SIZE, OutlineMode.SOLID, TILE_COLOR),
        this.listofColor.drawCircle()),
        this.unfinished.draw(this.correctAnswer)),
        this.listofGuess.drawLoGuess(this.listofColor));
  }
  
//  // draws the corresponding color when presses a number key;
//  public World onKeyReleased(String key) {
//    if ("123456789".contains(key)) {
//      if (0 < Integer.valueOf(key) && Integer.valueOf(key) <= this.correctAnswer.count())
//      new Mastermind(this.duplicateallowed, this.length, this.guesses, this.listofColor, 
//          this.listofGuess, this.unfinished.addGuess(this.listofColor.numbertoColor(
//              Integer.valueOf(key))), this.correctAnswer);
//    } else if (key.equals("VK_ENTER")) {
//      this.listofGuess.drawLoGuess(listofColor);
//    } else if (key.equals("VK_BACKSPACE")) {
//      this.unfinished.deleteLast().drawCircle();
//    }
//    return this;
//  }
//  
//  // determines when the world ends;
//  public WorldEnd WorldEnds() {
//    if (this.unfinished.isCorrected(this.correctAnswer)) {
//      return new WorldEnd(true, this.makeFinalScene("Win!"));
//    } else if (this.guesses == 0) {
//      return new WorldEnd(false, this.makeFinalScene("Game Over - Out of Attempts"));
//    } else {
//      return new WorldEnd(false, this.makeScene());
//    }
//  }
//  
//  public WorldScene makeFinalScene(String s) {
//    WorldScene ending = this.getEmptyScene();
//    ending.placeImageXY((new TextImage(s, 100, Color.BLACK)), 250, 250);
//    return ending;
//  }
  }

class ExamplesColors {
  ExamplesColors() {
  }

  Color color1 = new Color(255, 0, 0);
  Color color2 = new Color(0, 225, 0);
  Color color3 = new Color(0, 0, 225);
  Color color4 = Color.cyan;

  ILoColor colorList1 = new ConsLoColor(color1, new ConsLoColor(
      color2, new ConsLoColor(color3, new MtLoColor())));
  ILoColor colorList2 = new ConsLoColor(color1,
      new ConsLoColor(color2, new ConsLoColor(color3, new ConsLoColor(color1, new MtLoColor()))));
  ILoColor colorList3 = new ConsLoColor(color1,
      new ConsLoColor(color3, new ConsLoColor(color2, new ConsLoColor(color1, new MtLoColor()))));
  ILoColor colorList4 = new ConsLoColor(color2,
      new ConsLoColor(color3, new ConsLoColor(color2, new ConsLoColor(color1, new MtLoColor()))));
  ILoColor colorList5 = new ConsLoColor(color3,
      new ConsLoColor(color2, new ConsLoColor(color1, new ConsLoColor(color1, new MtLoColor()))));
  ILoColor colorList6 = new ConsLoColor(color1, new ConsLoColor(color2, new ConsLoColor(color3,
      new ConsLoColor(color2, new ConsLoColor(
          color1, new ConsLoColor(color2, new MtLoColor()))))));
  ILoColor colorList7 = new ConsLoColor(color1, new ConsLoColor(color3, new ConsLoColor(color2,
      new ConsLoColor(color2, new ConsLoColor
          (color3, new ConsLoColor(color3, new MtLoColor()))))));
  ILoColor colorList8 = new ConsLoColor(color1, new ConsLoColor(color2, new ConsLoColor(
      color3, new ConsLoColor(color1, new MtLoColor()))));

  UnfinishedGuesses unGuess1 = new UnfinishedGuesses(colorList1);
  UnfinishedGuesses unGuess2 = new UnfinishedGuesses(colorList2);
  UnfinishedGuesses unGuess3 = new UnfinishedGuesses(colorList3);
  UnfinishedGuesses unGuess4 = new UnfinishedGuesses(colorList4);
  UnfinishedGuesses unGuess5 = new UnfinishedGuesses(colorList8);
  
  FinishedGuesses Guess1 = new FinishedGuesses(this.colorList1);
  FinishedGuesses Guess2 = new FinishedGuesses(this.colorList2);
  FinishedGuesses Guess3 = new FinishedGuesses(this.colorList3);
  FinishedGuesses Guess4 = new FinishedGuesses(this.colorList4);
  
  ILoFinishedGuesses guessList1 = new ConsLoFinishedGuesses(Guess2, new ConsLoFinishedGuesses(
      Guess3, new ConsLoFinishedGuesses(Guess4, new MtFinishedGuesses())));

  WorldImage drawCircleExpect1 = new BesideImage(new CircleImage(20, "solid", color1),
      new CircleImage(20, "solid", color2),
      new CircleImage(20, "solid", color3),
      new EmptyImage());
  
  WorldImage drawCircleExpect2 = new BesideImage(new CircleImage(20, "solid", color1),
      new CircleImage(20, "solid", color2),
      new CircleImage(20, "solid", color3),
      new CircleImage(20, "solid", color1),
      new EmptyImage());
  
  WorldImage drawCircleExpect3 = new BesideImage(new CircleImage(20, "solid", color1),
      new CircleImage(20, "solid", color3),
      new CircleImage(20, "solid", color2),
      new CircleImage(20, "solid", color1),
      new EmptyImage());
  
  WorldImage drawCircleExpect4 = new BesideImage(new CircleImage(20, "solid", color2),
      new CircleImage(20, "solid", color3),
      new CircleImage(20, "solid", color2),
      new CircleImage(20, "solid", color1),
      new EmptyImage());
  
  WorldImage drawGuess1 = new BesideImage(this.drawCircleExpect1,
      new RectangleImage(50, 20, OutlineMode.SOLID, Color.white),
      new TextImage("3", 50, Color.BLUE),
      new RectangleImage(20, 20, OutlineMode.SOLID, Color.white),
      new TextImage("0", 50, Color.BLUE));
  
  WorldImage drawGuess2 = new BesideImage(this.drawCircleExpect2,
      new RectangleImage(50, 20, OutlineMode.SOLID, Color.white),
      new TextImage("4", 50, Color.BLUE),
      new RectangleImage(20, 20, OutlineMode.SOLID, Color.white),
      new TextImage("0", 50, Color.BLUE));
  
  WorldImage drawGuess3 = new BesideImage(this.drawCircleExpect3,
      new RectangleImage(50, 20, OutlineMode.SOLID, Color.white),
      new TextImage("2", 50, Color.BLUE),
      new RectangleImage(20, 20, OutlineMode.SOLID, Color.white),
      new TextImage("2", 50, Color.BLUE));
  
  WorldImage drawGuess4 = new BesideImage(this.drawCircleExpect4,
      new RectangleImage(50, 20, OutlineMode.SOLID, Color.white),
      new TextImage("1", 50, Color.BLUE),
      new RectangleImage(20, 20, OutlineMode.SOLID, Color.white),
      new TextImage("2", 50, Color.BLUE));
  
  WorldImage drawLoGuess1 = new AboveImage(this.drawGuess2, 
      this.drawGuess3, 
      this.drawGuess4,
      new EmptyImage());
  
  Mastermind m1 = new Mastermind(true, 4, 3, this.colorList7,
      this.guessList1, this.unGuess1, this.colorList2);
  Mastermind m2 = new Mastermind(true, 4, 3, this.colorList7,
      this.guessList1, this.unGuess5, this.colorList2);
  Mastermind m3 = new Mastermind(true, 4, 3, this.colorList7,
      this.guessList1, this.unGuess2, this.colorList2);

  boolean testcount(Tester t) {
    return t.checkExpect(this.colorList1.count(), 3);
  }

  boolean testCheckConstructorException(Tester t) {
    return t.checkConstructorException(new IllegalArgumentException(
        "Invalid length: 4"), "Mastermind", false, 4, 3, this.colorList1)
        && t.checkConstructorException(new IllegalArgumentException(
            "Invalid length: 0"), "Mastermind", true, 4, 0, this.colorList1)
        && t.checkConstructorException(new IllegalArgumentException(
            "Invalid length: 0"), "Mastermind", true, 0, 3, this.colorList1);
  }

  boolean testdrawCircle(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return t.checkExpect(this.colorList1.drawCircle(), drawCircleExpect1)
        && c.drawScene(s.placeImageXY(colorList1.drawCircle(), 250, 250)) && c.show();
  }

  boolean testexactMatches(Tester t) {
    return t.checkExpect(this.colorList1.exactMatches(this.colorList1), 3)
        && t.checkExpect(this.colorList1.exactMatches(new MtLoColor()), 0)
        && t.checkExpect(this.colorList2.exactMatches(this.colorList3), 2);
  }

  boolean testsameColorHelper(Tester t) {
    return t.checkExpect(this.colorList1.sameColorHelper(color1, this.colorList1), 1)
        && t.checkExpect(this.colorList1.sameColorHelper(color1, new MtLoColor()), 1)
        && t.checkExpect(this.colorList1.sameColorHelper(color2, this.colorList1), 0);
  }

  boolean testinexactMatches(Tester t) {
    return t.checkExpect(this.colorList1.inexactMatches(this.colorList1), 0)
        && t.checkExpect(this.colorList1.inexactMatches(new MtLoColor()), 0)
        && t.checkExpect(this.colorList4.inexactMatches(this.colorList5), 2)
        && t.checkExpect(this.colorList6.inexactMatches(this.colorList7), 3)
        && t.checkExpect(this.colorList2.inexactMatches(this.colorList2), 0)
        && t.checkExpect(this.colorList3.inexactMatches(this.colorList2), 2)
        && t.checkExpect(this.colorList4.inexactMatches(this.colorList2), 2);
  }

  boolean testcontain(Tester t) {
    return t.checkExpect(this.colorList5.contain(this.color1), true)
        && t.checkExpect(this.colorList1.contain(this.color4), false);
  }

  boolean testremoveLast(Tester t) {
    return t.checkExpect(this.colorList5.removeLast(), new ConsLoColor(
        color2, new ConsLoColor(color1, new ConsLoColor(color1, new MtLoColor()))))
        && t.checkExpect(this.colorList1.removeLast(), new ConsLoColor(
            color2, new ConsLoColor(color3, new MtLoColor())));
  }

  boolean testremove(Tester t) {
    return t.checkExpect(this.colorList5.remove(this.color1), new ConsLoColor(
        this.color3, new ConsLoColor(this.color2, new MtLoColor())))
        && t.checkExpect(this.colorList7.remove(this.color1), new ConsLoColor(
            color3, new ConsLoColor(color2, new ConsLoColor(this.color2, new ConsLoColor(
                color3, new ConsLoColor(this.color3, new MtLoColor()))))));
  }

  boolean testadd(Tester t) {
    return t.checkExpect(this.colorList1.add(this.color1), new ConsLoColor(this.color1, 
        new ConsLoColor(this.color1, new ConsLoColor(this.color2, new ConsLoColor(this.color3, 
            new MtLoColor())))))
        && t.checkExpect(this.colorList1.add(this.color4), new ConsLoColor(this.color4, 
            new ConsLoColor(this.color1, new ConsLoColor(this.color2, new ConsLoColor(this.color3, 
                new MtLoColor())))));
  }

  boolean testdeleteLast(Tester t) {
    return t.checkExpect(this.unGuess1.deleteLast(), new ConsLoColor(
        this.color2, new ConsLoColor(this.color3, new MtLoColor())))
        && t.checkExpect(this.unGuess2.deleteLast(),new ConsLoColor(this.color2,
            new ConsLoColor(this.color3, new ConsLoColor(this.color1, new MtLoColor()))));
  }

//  boolean testaddGuess(Tester t) {
//    return t.checkExpect(this.unGuess1.addGuess(this.color1), new ConsLoColor(this.color1, 
//        new ConsLoColor(this.color1, new ConsLoColor(this.color2, new ConsLoColor(this.color3, 
//            new MtLoColor())))))
//        && t.checkExpect(this.unGuess1.addGuess(this.color4), new ConsLoColor(this.color4, 
//            new ConsLoColor(this.color1, new ConsLoColor(this.color2, new ConsLoColor(this.color3, 
//                new MtLoColor())))));
//  }
  
  boolean testisfinished(Tester t) {
    return t.checkExpect(this.unGuess3.isfinished(this.colorList4), true)
        && t.checkExpect(this.unGuess2.isfinished(this.colorList1), false);
  }
  
  boolean testisCorrected(Tester t) {
    return t.checkExpect(this.unGuess3.isCorrected(this.colorList3), true)
        && t.checkExpect(this.unGuess2.isCorrected(this.colorList1), false);
  }
  
  boolean testconvert(Tester t) {
    return t.checkExpect(this.unGuess1.convert(), Guess1);
  }
  
  boolean testdraw(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return t.checkExpect(this.Guess1.draw(colorList1), drawGuess1)
        && c.drawScene(s.placeImageXY(Guess1.draw(colorList1), 250, 250))
        && c.show();
  }
  
  boolean testdrawLoGuess(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return t.checkExpect(this.guessList1.drawLoGuess(colorList2), drawLoGuess1)
        && c.drawScene(s.placeImageXY(guessList1.drawLoGuess(colorList2), 250, 250))
        && c.show();
  }
  
  boolean testnumbertoColor(Tester t) {
    return t.checkExpect(this.colorList7.numbertoColor(1), this.color1)
        && t.checkExpect(this.colorList7.numbertoColor(2), this.color3)
        && t.checkExpect(this.colorList7.numbertoColor(3), this.color2)
        && t.checkExpect(this.colorList7.numbertoColor(4), this.color2)
        && t.checkExpect(this.colorList7.numbertoColor(5), this.color3);
  }
  
  boolean testremoveSame(Tester t) {
    return t.checkExpect(this.colorList3.removeSame(colorList2), new ConsLoColor(
        color3, new ConsLoColor(color2, new MtLoColor())))
        && t.checkExpect(this.colorList2.removeSame(colorList3), new ConsLoColor(
            color2, new ConsLoColor(color3, new MtLoColor())))
        && t.checkExpect(this.colorList7.removeSame(colorList6), new ConsLoColor(
            color3, new ConsLoColor(color2, new ConsLoColor(
                color3, new ConsLoColor(color3,new MtLoColor())))))
        && t.checkExpect(this.colorList6.removeSame(colorList7), new ConsLoColor(
            color2, new ConsLoColor(color3, new ConsLoColor(
                color1, new ConsLoColor(color2,new MtLoColor())))));
  }
  
  boolean testinexactMatchesHelper(Tester t) {
    return t.checkExpect(this.colorList1.inexactMatchesHelper(this.colorList3), 3)
        && t.checkExpect(this.colorList1.inexactMatchesHelper(this.colorList1), 3);
  }
  
  boolean testinexactMatchesHelper2(Tester t) {
    return t.checkExpect(this.colorList1.inexactMatchesHelper(this.colorList3), 3)
        && t.checkExpect(this.colorList1.inexactMatchesHelper(this.colorList1), 3);
  }
 
  
  boolean testremoveSameHelper(Tester t) {
    return t.checkExpect(this.colorList3.removeSameHelper(this.color1, this.colorList2),
        new ConsLoColor(color1, new ConsLoColor(color3, new MtLoColor())));
  }
  
//  boolean testonKeyReleased(Tester t) {
//    return t.checkExpect(this.m1.onKeyReleased("1"), this.m1);
////        && t.checkExpect(this.m2.onKeyReleased("VK_BACKSPACE"), this.m1)
////        && t.checkExpect(this.m1.onKeyReleased("VK_ENTER"), this.4);
//  }
//  boolean testdrawMastermind(Tester t) {
//    return t.checkExpect(this.m1.onKeyReleased("1"), new AboveImage(
//        new AboveImage(new OverlayImage(new RectangleImage(160 * this.colorList2.count(),
//        160, OutlineMode.SOLID, new Color(128, 0, 0)),
//        this.colorList7.drawCircle()), this.drawLoGuess1,
//        this.drawLoGuess1)));
//  }
  
  boolean testcontains(Tester t) {
    return t.checkExpect("123456789".contains("8"), true);
  }
}
