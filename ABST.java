import tester.Tester;
import java.util.Comparator;

abstract class ABST<T> {
  Comparator<T> order;

  ABST(Comparator<T> order) {
    this.order = order;
  }

  // returns with a new binary search tree after adding the new given item;
  abstract ABST<T> insert(T t);

  // checks if this is equal to the given;
  abstract boolean present(T t);

  // returns with the item that is the leftmost of the tree;
  abstract T getLeftmost();

  // determines if it is a leaf;
  abstract boolean isLeaf();

  // determines if it is a node;
  abstract boolean isNode(); 

  // returns with all the items except the leftmost one on the tree;
  abstract ABST<T> getRight();

  // determines if this tree is the same with the given tree;
  abstract boolean sameTree(ABST<T> abst);

  // the sameTree helper to get the given tree data;
  abstract boolean sameTreeHelper(Node<T> n);

  // determines if this tree has the same data with the given tree;
  abstract boolean sameData(ABST<T> abst);

  // returns with a list that represents the sorted order of the given tree;
  abstract IList<T> buildList();

  // determine whether the tree is empty;
  abstract boolean empty();

}

class Leaf<T> extends ABST<T> {
  Leaf(Comparator<T> order) {
    super(order);
  }

  //returns with a new binary search tree after adding the new given item;
  ABST<T> insert(T t) {
    return new Node<T>(this.order, t, new Leaf<T>(this.order), new Leaf<T>(this.order));
  }

  //checks if this is equal to the given;
  boolean present(T t) {
    return false;
  }

  // determines if it is a leaf;
  boolean isLeaf() {
    return true;
  }

  // determines if it is a node;
  boolean isNode() {
    return false;
  }

  // returns with the item that is the leftmost of the tree;
  T getLeftmost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  // returns with all the items except the leftmost one on the tree;
  ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  //determine whether the tree is empty;
  boolean empty()  {
    return true;
  }

  // determines if this tree is the same with the given tree;
  boolean sameTree(ABST<T> a) {
    return a.empty();
  }

  //the sameTree helper to get the given tree data;
  boolean sameTreeHelper(Node<T> n) {
    return false;
  }

  // determines if this tree has the same data with the given tree;
  boolean sameData(ABST<T> abst) {
    return abst.empty();
  }

  // returns with a list that represents the sorted order of the given tree;
  IList<T> buildList() {
    return new MtList<T>();
  }

}

class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;

  Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }

  //returns with a new binary search tree after adding the new given item;
  ABST<T> insert(T t) {
    if (this.order.compare(t, this.data) >= 0) {
      return new Node<T>(this.order, this.data, this.left, this.right.insert(t));
    } else {
      return new Node<T>(this.order, this.data, this.left.insert(t), this.right);
    }
  }

  //checks if this is equal to the given;
  boolean present(T t) {
    if (((BooksByTitle) this.order).isAuthor()) {
      return this.data.equals(((Book) t).author) || this.left.present(t) || this.right.present(t);
    } else if (((BooksByTitle) this.order).isTitle()) {
      return this.data.equals(((Book) t).title) || this.left.present(t) || this.right.present(t);
    } else {
      return this.data.equals(((Book) t).price) || this.left.present(t) || this.right.present(t);
    }
  }

  // determines if it is a leaf;
  boolean isLeaf() {
    return false;
  }

  // determines if it is a node;
  boolean isNode() {
    return true;
  }

  // returns with the item that is the leftmost of the tree;
  T getLeftmost() {
    if (this.left.isLeaf()) {
      return this.data;
    } else {
      return this.left.getLeftmost();
    }
  }

  // returns with all the items except the leftmost one on the tree;
  ABST<T> getRight() {
    if (this.left.isLeaf()) {
      return this.right;
    } else {
      return new Node<T>(this.order, this.data, this.left.getRight(), this.right);
    }
  }

  //determine whether the tree is empty;
  public boolean empty() {
    return false;
  }

  // determines if this tree is the same with the given tree;
  public boolean sameTree(ABST<T> a) {
    if (a.empty()) {
      return false;
    }
    else {
      return a.sameTreeHelper(this);
    }
  }

  //the sameTree helper to get the given tree data;
  public boolean sameTreeHelper(Node<T> n) {
    if (n.order.compare(this.data, n.data) == 0) {
      return n.left.sameTree(this.left) && n.right.sameTree(this.right);
    }
    else {
      return false;
    }
  }

  // determines if this tree has same data with the given tree;
  public boolean sameData(ABST<T> a) {
    if (a.empty()) {
      return false;
    } else if (this.order.compare(a.getLeftmost(), this.getLeftmost()) == 0) {
      return this.getRight().sameData(a.getRight());
    }
    else {
      return false;
    }
  }

  // returns with a list that represents the sorted order of the given tree;
  IList<T> buildList() {
    return new ConsList<T>(this.getLeftmost(), this.getRight().buildList());
  }
}

interface IList<T> {

}

class MtList<T> implements IList<T> {

}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}

class Book {
  String title;
  String author;
  int price;

  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }

  public boolean sameBook(Book that) {
    return this.title == that.title 
        && this.author == that.author
        && this.price == that.price;
  }
}

class BooksByTitle implements Comparator<Book> {

  //compare the given two books;
  public int compare(Book t1, Book t2) {
    return t1.title.compareTo(t2.title);
  }

  //determines what are we comparing;
  public boolean isTitle() {
    return true;
  }

  //determines what are we comparing;
  public boolean isAuthor() {
    return false;
  }

  //determines what are we comparing;
  public boolean isPrice() {
    return false;
  }
}

class BooksByAuthor implements Comparator<Book> {


  //compare the given two books;
  public int compare(Book t1, Book t2) {
    return t1.author.compareTo(t2.author);
  }

  //determines what are we comparing;
  public boolean isTitle() {
    return false;
  }

  //determines what are we comparing;
  public boolean isAuthor() {
    return true;
  }

  //determines what are we comparing;
  public boolean isPrice() {
    return false;
  }
}

class BooksByPrice implements Comparator<Book> {


  //compare the given two books;
  public int compare(Book t1, Book t2) {
    if (t1.price < t2.price) {
      return -1;
    } else if (t1.price == t2.price) {
      return 0;
    } else {
      return 1;
    }
  }

  //determines what are we comparing;
  public boolean isTitle() {
    return false;
  }

  //determines what are we comparing;
  public boolean isAuthor() {
    return false;
  }

  //determines what are we comparing;
  public boolean isPrice() {
    return true;
  }
}

class ExamplesABST {
  ExamplesABST() {}

  // Book
  Book book1 = new Book("In Cold Blood", "Truman Capote", 17);
  Book book2 = new Book("The Great Gatsby", "F.Scott Fitzgerald", 30);
  Book book3 = new Book("Hard Times", "Walter Hill", 26);
  Book book4 = new Book("Black Thunder", "Rick Jacobson", 26);
  Book book5 = new Book("To Live", "Yu Hua", 33);
  Book books = new Book("To Live", "Yu Hua", 33);

  // Binary Search Tree
  ABST<Book> leafBook = new Leaf<Book>(this.title);

  ABST<Book> nodet = new Node<Book>(this.title, this.book1, this.leafBook, new Node<Book>(
      this.title, this.book2, this.leafBook, this.leafBook));
  ABST<Book> nodea = new Node<Book>(this.author, this.book2, this.leafBook, 
      new Node<Book>(this.author, this.book1, this.leafBook, this.leafBook));
  ABST<Book> nodep = new Node<Book>(this.price, this.book1, this.leafBook, new Node<Book>(
      this.price, this.book2, this.leafBook, this.leafBook));
  ABST<Book> nodet2 = new Node<Book>(this.title, this.book3, this.leafBook, new Node<Book>(
      this.title, this.book1, this.leafBook, this.leafBook));
  ABST<Book> nodea2 = new Node<Book>(this.author, this.book4, this.leafBook, new Node<Book>(
      this.author, this.book5, this.leafBook, this.leafBook));
  ABST<Book> nodep2 = new Node<Book>(this.price, this.book4, this.leafBook, new Node<Book>(
      this.price, this.book5, this.leafBook, this.leafBook));
  ABST<Book> nodet3 = new Node<Book>(this.title, this.book4, this.leafBook, new Node<Book>(
      this.title, this.book5, this.leafBook, this.leafBook));
  ABST<Book> nodea3 = new Node<Book>(this.title, this.book4, this.leafBook,
      new Node<Book>(this.title, this.book3, this.leafBook, new Node<Book>(
          this.title, this.book5, this.leafBook, this.leafBook)));
  ABST<Book> nodetp = new Node<Book>(this.price, this.book4, this.leafBook,
      new Node<Book>(this.price, this.book3, this.leafBook, new Node<Book>(
          this.price, this.book5, this.leafBook, this.leafBook)));
  ABST<Book> nodea3Expect = new Node<Book>(this.title, this.book3, this.leafBook, new Node<Book>(
      this.title, this.book5, this.leafBook, this.leafBook));
  ABST<Book> leafExpect = new Node<Book>(this.title, this.book3,
      this.leafBook, this.leafBook);
  ABST<Book> nodetExpect = new Node<Book>(this.title, this.book1, 
      new Node<Book>(this.title, this.book4,
          this.leafBook, this.leafBook), new Node<Book>(this.title, this.book2,
              this.leafBook, this.leafBook));
  ABST<Book> nodet2Expect = new Node<Book>(this.title, this.book4, new Node<Book>(
      this.title, this.book4, this.leafBook, this.leafBook), new Node<Book>(this.title, 
          this.book5, this.leafBook, this.leafBook));
  ABST<Book> nodetl1 = new Node<Book>(this.title, this.book4, this.leafBook,
      new Node<Book>(this.title, this.book1, new Node<Book>(
          this.title, this.book3, this.leafBook, this.leafBook), new Node<Book>(
              this.title, this.book2, this.leafBook, this.leafBook)));
  ABST<Book> nodepl2 = new Node<Book>(this.price, this.book4, this.leafBook,
      new Node<Book>(this.price, this.book5, this.leafBook, new Node<Book>(
          this.price, this.books, this.leafBook, this.leafBook)));

  // IComparator
  Comparator<Book> title = new BooksByTitle();
  Comparator<Book> author = new BooksByAuthor();
  Comparator<Book> price = new BooksByPrice();

  // IList<Book>
  IList<Book> list1 = new ConsList<Book>(this.book4, new ConsList<Book>(this.book3,
      new ConsList<Book>(this.book1, new ConsList<Book>(this.book2, new MtList<Book>()))));
  IList<Book> list2 = new ConsList<Book>(this.book4, new ConsList<Book>(this.book5,
      new ConsList<Book>(this.books, new MtList<Book>())));


  boolean testinsert(Tester t) {
    return t.checkExpect(this.nodet2.insert(this.book3), this.leafExpect)
        && t.checkExpect(this.leafBook.insert(this.book3), new Node<Book>(null, this.book3,
            this.leafBook, this.leafBook));
  }

  boolean testgetLeftmost(Tester t) {
    return t.checkExpect(this.nodetExpect.getLeftmost(), this.book4)
        && t.checkExpect(this.nodea.getLeftmost(), this.book2)
        && t.checkException(new RuntimeException("No leftmost item of an empty tree"), 
            this.leafBook, "getLeftmost");
  }

  boolean testgetRight(Tester t) {
    return t.checkExpect(this.nodea3.getRight(), nodea3Expect)
        && t.checkException(new RuntimeException("No right of an empty tree"), 
            this.leafBook, "getRight");
  }

  boolean testsameTree(Tester t) {
    return t.checkExpect(this.leafBook.sameTree(this.nodet), false)
        && t.checkExpect(this.leafBook.sameTree(this.leafBook), true)
        && t.checkExpect(this.leafBook.sameTree(this.nodep2), false)
        && t.checkExpect(this.nodep2.sameTree(this.nodep2), true);    
  }

  boolean testsameData(Tester t) {
    return t.checkExpect(this.leafBook.sameData(this.nodet), false)
        && t.checkExpect(this.leafBook.sameData(this.leafBook), true)
        && t.checkExpect(this.nodea.sameData(this.nodep), false)
        && t.checkExpect(this.nodea.sameData(this.nodea), true); 

  }

  boolean testbuildList(Tester t) {
    return t.checkExpect(this.nodetl1.buildList(), this.list1)
        && t.checkExpect(this.leafBook.buildList(), new MtList<Book>())
        && t.checkExpect(this.nodepl2.buildList(), this.list2);
  }

  boolean testpresent(Tester t) {
    return t.checkExpect(this.nodet.present(this.books), false)
        && t.checkExpect(this.leafBook.present(this.book1), false)
        && t.checkExpect(this.nodep2.present(this.books), true);
  }
}