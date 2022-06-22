import tester.Tester;

interface IBook {
  // calculates how may days left until the book is overdue;
  int daysOverdue(int today);

  // determines whether the book is overdue on the given day;
  boolean isOverdue(int day);

  // return with the fine for this book, which is returned on the given day;
  int computeFine(int day);
}

abstract class ABook implements IBook {
  String title;
  int dayTaken;

  ABook(String title, int dayTaken) {
    this.title = title;
    this.dayTaken = dayTaken;
  }

  /* Template:
   * Fields:
   * ... this.title ...            --title
   * ... this.dayTaken ...         --int
   * Methods:
   * ... this.daysOverdue(int) ... --int
   * ... this.isOverdue(int) ...   --boolean
   * ... this.computeFine(int) ... --int
   */

  //calculates how may days left until the book is overdue
  public int daysOverdue(int today) {
    return today - (this.dayTaken + 14);
  }

  //determines whether the book is overdue on the given day
  public boolean isOverdue(int day) {
    return this.daysOverdue(day) > 0;

  }

  //return with the fine for this book, which is returned on the given day
  public int computeFine(int day) {
    if (this.isOverdue(day)) {
      return this.daysOverdue(day) * 10;
    } else {
      return 0;
    }
  }
}

class Book extends ABook {
  String author;

  Book(String title, String author, int dayTaken) {
    super(title, dayTaken);
    this.author = author;
  }

  /* Template:
   * Fields:
   * ... this.title ...            -- String
   * ... this.author ...           -- String
   * ... this.dayTaken ...         -- int
   * Methods:
   * ... this.daysOverdue(int) ... --int
   * ... this.isOverdue(int) ...   --boolean
   * ... this.computeFine(int) ... --int
   */
}

class RefBook extends ABook {

  RefBook(String title, int dayTaken) {
    super(title, dayTaken);
  }

  /* Template:
   * Fields:
   * ... this.title ...    - String
   * ... this.dayTaken ... - int
   * Methods:
   * ... this.daysOverdue(int) ... --int
   * ... this.isOverdue(int) ...   --boolean
   * ... this.computeFine(int) ... --int
   */


  //calculates how may days left until the book is overdue;
  public int daysOverdue(int today) {
    return today - (this.dayTaken + 2);
  }
}

class AudioBook extends ABook {
  String author;

  AudioBook(String title, String author, int dayTaken) {
    super(title, dayTaken);
    this.author = author;
  }

  /* Template:
   * Fields:
   * ... this.title ...            -- String
   * ... this.author ...           -- String
   * ... this.dayTaken ...         -- int
   * Methods:
   * ... this.daysOverdue(int) ... --int
   * ... this.isOverdue(int) ...   --boolean
   * ... this.computeFine(int) ... --int
   */


  //return with the fine for this book, which is returned on the given day
  public int computeFine(int day) {
    if (this.isOverdue(day)) {
      return this.daysOverdue(day) * 20;
    } else {
      return 0;
    }
  }
}

class ExamplesBooks {
  ExamplesBooks() {}

  IBook book1 = new Book("book1", "author1", 6999);
  IBook book2 = new Book("book2", "author2", 6950);
  IBook refbook1 = new RefBook("refbook1", 6999);
  IBook refbook2 = new RefBook("refbook2", 6980);
  IBook audiobook1 = new AudioBook("audiobook1", "author3", 6990);
  IBook audiobook2 = new AudioBook("audiobook2", "author4", 6970);

  Boolean testdaysOverdue(Tester t) {
    return t.checkExpect(this.book1.daysOverdue(7000), -13)
        && t.checkExpect(this.audiobook1.daysOverdue(7000), -4)
        && t.checkExpect(this.refbook1.daysOverdue(7000), -1)
        && t.checkExpect(this.book2.daysOverdue(7000), 36)
        && t.checkExpect(this.refbook2.daysOverdue(7000), 18)
        && t.checkExpect(this.audiobook2.daysOverdue(7000), 16);
  }

  Boolean testisOverdue(Tester t) {
    return t.checkExpect(this.book1.isOverdue(7000), false)
        && t.checkExpect(this.audiobook1.isOverdue(7000), false)
        && t.checkExpect(this.refbook1.isOverdue(7000), false)
        && t.checkExpect(this.book2.isOverdue(7000), true)
        && t.checkExpect(this.refbook2.isOverdue(7000), true)
        && t.checkExpect(this.audiobook2.isOverdue(7000), true);
  }

  Boolean testcomputeFine(Tester t) {
    return t.checkExpect(this.book1.computeFine(7000), 0)
        && t.checkExpect(this.book2.computeFine(7000), 360)
        && t.checkExpect(this.refbook1.computeFine(7000), 0)
        && t.checkExpect(this.refbook2.computeFine(7000), 180)
        && t.checkExpect(this.audiobook1.computeFine(7000), 0)
        && t.checkExpect(this.audiobook2.computeFine(7000), 320);
  }
}