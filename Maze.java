import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

class Vertex {
  int x;
  int y;
  boolean hasBeen;
  boolean partofPath;
  ArrayList<Edge> allEdges;

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.allEdges = new ArrayList<Edge>();
    this.hasBeen = false;
    this.partofPath = false;
  }
  

  // uniquely-identifying feature of each vertex;
  int uniqueIdentify() {
    return 1000 * y + x;
  }
}

class Edge {
  Vertex from;
  Vertex to;
  int weight;

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }
}

interface Predicate<T> {
  boolean apply(T t);
}

interface IComparator<T> {
  boolean apply(T t1, T t2);
}

class CompareEdges implements IComparator<Edge> {
  public boolean apply(Edge e1, Edge e2) {
    return e1.weight < e2.weight;
  }
}

class Stack<T> {
  Deque<T> contents;

  Stack() {
    this.contents = new Deque<T>();
  }

  Stack(IList<T> ts) {
    this.contents = new Deque<T>();
    for (T t : ts) {
      contents.addAtTail(t);
    }
  }

  // adds an item to the beginning of the stack;
  void push(T item) {
    contents.addAtHead(item);
  }

  // determines if the stack is empty;
  boolean isEmpty() {
    return contents.size() == 0;
  }

  // removes and returns the head of the stack;
  T pop() {
    return contents.removeFromHead();
  }
}

class Queue<T> {
  Deque<T> contents;

  Queue() {
    this.contents = new Deque<T>();
  }

  Queue(IList<T> ts) {
    contents = new Deque<T>();
    for (T t : ts) {
      contents.addAtTail(t);
    }
  }

  // adds an item to the tail of the queue;
  void pushQ(T item) {
    contents.addAtTail(item);
  }

  // determines if the queue are empty;
  boolean isEmpty() {
    return contents.size() == 0;
  }

  // removes and returns the tail of the queue;
  T popQ() {
    return contents.removeFromTail();
  }
}

interface IList<T> extends Iterable<T> {
  // determines if it's empty;
  boolean isEmpty();

  // returns the length of the list;
  int length();

  // iterates over the list;
  Iterator<T> iterator();

  // adds an item to the list;
  IList<T> add(T t);

  // get the item at the given index;
  T get(int i);

  // combines the list with the given list;
  IList<T> append(IList<T> l);

  // determines if this list has a next item;
  boolean hasNext();

  // get the current item;
  T getItem();

  // returns the rest of the list;
  IList<T> getNext();
}

class Empty<T> implements IList<T>, Iterable<T> {
  // returns the length of the list;
  public int length() {
    return 0;
  }

  // determines if it's empty;
  public boolean isEmpty() {
    return true;
  }

  // iterates over the list;
  public Iterator<T> iterator() {
    return new ListIterator<T>(this);
  }

  // adds an item to the list;
  public IList<T> add(T t) {
    return new Cons<T>(t, new Empty<T>());
  }

  // get the item at the given index;
  public T get(int i) {
    return null;
  }

  // combines the list with the given list;
  public IList<T> append(IList<T> l) {
    return l;
  }

  // determines if this list has a next item;
  public boolean hasNext() {
    return false;
  }

  // get the current item;
  public T getItem() {
    return null;
  }

  // returns the rest of the list;
  public IList<T> getNext() {
    throw new UnsupportedOperationException("cannot found the next");
  }
}

class Cons<T> implements IList<T>, Iterable<T> {
  T first;
  IList<T> rest;

  Cons(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // returns the length of the list;
  public int length() {
    return 1 + this.rest.length();
  }

  // determines if it's empty;
  public boolean isEmpty() {
    return false;
  }

  // iterates over the list;
  public Iterator<T> iterator() {
    return new ListIterator<T>(this);
  }

  // adds an item to the list;
  public IList<T> add(T t) {
    if (rest.isEmpty()) {
      rest = new Cons<T>(t, new Empty<T>());
    } else {
      rest = rest.add(t);
    }
    return this;
  }

  // get the item at the given index;
  public T get(int i) {
    if (i == 0) {
      return this.first;
    } else {
      return this.rest.get(i--);
    }
  }

  // combines the list with the given list;
  public IList<T> append(IList<T> l) {
    if (rest.isEmpty()) {
      rest = l;
    } else {
      rest.append(l);
    }
    return this;
  }

  // determines if this list has a next item;
  public boolean hasNext() {
    return true;
  }

  // get the current item;
  public T getItem() {
    return this.first;
  }

  // returns the rest of the list;
  public IList<T> getNext() {
    return this.rest;
  }
}

class ListIterator<T> implements Iterator<T> {
  IList<T> l;

  ListIterator(IList<T> list) {
    this.l = list;
  }

  // determines if there is a next item in the iterator;
  public boolean hasNext() {
    return l.hasNext();
  }

  // returns the next item of the iterator and moves it to the following item;
  public T next() {
    T t = this.l.getItem();
    this.l = this.l.getNext();
    return t;
  }

  // a not needed method in this scenario;
  public void remove() {
    throw new UnsupportedOperationException("cannot remove");
  }
}

class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> s) {
    this.header = s;
  }

  // iterates;
  public Iterator<T> iterator() {
    return new DequeIterator<T>(this.header);
  }

  // counts the number of nodes in a deque;
  int size() {
    return this.header.sizeHelper();
  }

  // puts the given item at the front of the deque;
  void addAtHead(T t) {
    Node<T> addt = new Node<T>(t);
    addt.add(this.header, this.header.next);
  }

  // puts the given item at the end of the deque;
  void addAtTail(T t) {
    Node<T> addt = new Node<T>(t);
    addt.add(this.header.prev, this.header);
  }

  // removes the first node from the deque;
  T removeFromHead() {
    return this.header.next.remove();
  }

  // removes the last node from the deque;
  T removeFromTail() {
    return this.header.prev.remove();
  }

  // if the given predicate is true, produces the first node of the deque;
  ANode<T> find(Predicate<T> pred) {
    return this.header.next.findHelper(pred);
  }

  // removes the given node from the deque;
  void removeNode(ANode<T> node) {
    if (node != this.header) {
      node.remove();
    }
  }
}

class DequeIterator<T> implements Iterator<T> {
  ANode<T> d;

  DequeIterator(ANode<T> deque) {
    this.d = deque;
  }

  // returns true if there's at least one value left in this iterator
  public boolean hasNext() {
    return d.hasNext();
  }

  // returns the next value and advances the iterator
  public T next() {
    T t = this.d.next.getData();
    this.d = this.d.next;
    return t;
  }

  // a not needed method in this scenario;
  public void remove() {
    throw new UnsupportedOperationException("cannot remove");
  }
}

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // counts how many nodes;
  int sizeHelper(ANode<T> s) {
    if (this.next.equals(s)) {
      return 1;
    } else {
      return 1 + this.next.sizeHelper(s);
    }
  }

  // removes this node from the deque
  T remove() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
    return this.getData();
  }

  // helps to find a node that meets the given predicate;
  ANode<T> findHelper(Predicate<T> p) {
    return this;
  }

  // helps to remove a node from the given deque;
  void removeNode(ANode<T> a) {
    if (this.equals(a)) {
      this.remove();
    } else {
      this.next.removeNode(a);
    }
  }

  // returns the data for this node;
  T getData() {
    return null;
  }

  // determines whether this node has a next value;
  boolean hasNext() {
    return !next.isSentinel();
  }

  // determines if this node is a sentinel;
  boolean isSentinel() {
    return false;
  }
}

class Sentinel<T> extends ANode<T> {
  Sentinel() {
    this.prev = this;
    this.next = this;
  }

  // counts how many nodes;
  int sizeHelper() {
    if (this.next.equals(this)) {
      return 0;
    }
    return this.next.sizeHelper(this);
  }

  // removes this ANode<T> if this is a node;
  public T remove() {
    throw new RuntimeException("cannot remove from an empty deque");
  }

  // finds the first node that produces true with the given predicate;
  public ANode<T> findHelper(Predicate<T> pred) {
    return this.next.findHelper(pred);
  }

  // adds the given node to the head;
  T addAtHead(T t) {
    new Node<T>(t, this.next, this);
    return this.next.getData();
  }

  // adds the given node to the tail;
  T addAtTail(T t) {
    new Node<T>(t, this, this.prev);
    return this.next.getData();
  }

  // removes a node from the head of the deque;
  T removeFromHead() {
    return this.next.remove();
  }

  // removes a node from the tail of the deque;
  T removeFromTail() {
    return this.prev.remove();
  }

  // determines if this node is a sentinel;
  boolean isSentinel() {
    return true;
  }
}

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.prev = null;
    this.next = null;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    this.data = data;
    if ((prev == null) || (next == null)) {
      throw new IllegalArgumentException("The node is null");
    }
    this.prev = prev;
    this.next = next;
    this.prev.next = this;
    this.next.prev = this;
  }

  // adds this node at the front of the deque list;
  void add(ANode<T> prev, ANode<T> next) {
    prev.next = this;
    next.prev = this;
    this.prev = prev;
    this.next = next;
  }

  // removes this ANode<T> if this is a node;
  public T remove() {
    ANode<T> prev = this.prev;
    ANode<T> next = this.next;
    prev.next = next;
    next.prev = prev;
    return this.data;
  }

  // finds the first node that produces true with the given predicate;
  public ANode<T> findHelper(Predicate<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    } else {
      return this.next.findHelper(pred);
    }
  }
}

abstract class Search {
  HashMap<Integer, Vertex> cameFromEdge;

  // initializes the path;
  void restart(HashMap<Integer, Vertex> h, Vertex next) {
    while (h.containsKey(next.uniqueIdentify())) {
      next.partofPath = true;
      next = h.get(next.uniqueIdentify());
    }
  }
}

class BreadthFirst extends Search {
  Queue<Vertex> worklist;

  BreadthFirst(IList<Vertex> list) {
    this.worklist = new Queue<Vertex>();
    worklist.pushQ(list.getItem());
    list.getItem().hasBeen = true;
    cameFromEdge = new HashMap<Integer, Vertex>();
  }

  // determines if we have a next vertex in out list;
  public boolean hasNext() {
    return !worklist.isEmpty();
  }

  // iterates;
  public Queue<Vertex> next() {
    Vertex u = worklist.popQ();
    for (Edge e : u.allEdges) {
      if (!e.to.hasBeen) {
        cameFromEdge.put(e.to.uniqueIdentify(), e.from);
        if (e.to.x == Maze.WIDTH - 1 && e.to.y == Maze.HEIGHT - 1) {
          restart(cameFromEdge, e.to);
          worklist = new Queue<Vertex>();
        } else {
          e.to.hasBeen = true;
          worklist.pushQ(e.to);
        }
      }
    }
    return worklist;
  }
}

class DepthFirst extends Search {
  Stack<Vertex> worklist;

  DepthFirst(IList<Vertex> list) {
    this.worklist = new Stack<Vertex>();
    worklist.push(list.getItem());
    list.getItem().hasBeen = true;
    cameFromEdge = new HashMap<Integer, Vertex>();
  }

  // determines if we have the next vertex in the list;
  public boolean hasNext() {
    return !worklist.isEmpty();
  }

  // iterates;
  public Stack<Vertex> next() {
    Vertex u = worklist.pop();
    for (Edge e : u.allEdges) {
      if (!e.to.hasBeen) {
        cameFromEdge.put(e.to.uniqueIdentify(), e.from);
        if (e.to.x == Maze.WIDTH - 1 && e.to.y == Maze.HEIGHT - 1) {
          restart(cameFromEdge, e.to);
          worklist = new Stack<Vertex>();
        } else {
          worklist.push(u);
          e.to.hasBeen = true;
          worklist.push(e.to);
          break;
        }
      }
    }
    return worklist;
  }
}

class Player extends Search {
  Vertex current;
  boolean finished;

  Player(IList<Vertex> list) {
    current = list.getItem();
    cameFromEdge = new HashMap<Integer, Vertex>();
    finished = false;
  }

  // determines if we are finished;
  public boolean hasNext() {
    return !finished;
  }

  // moves the edge;
  public Vertex move(boolean b, Edge e) {
    if (b) {
      current.hasBeen = true;
      current.partofPath = false;
      if (!e.to.hasBeen) {
        cameFromEdge.put(e.to.uniqueIdentify(), e.from);
      }
      if (e.to.x == Maze.WIDTH - 1 && e.to.y == Maze.HEIGHT - 1) {
        restart(cameFromEdge, e.to);
      } else {
        current = e.to;
        current.partofPath = true;
      }
    }
    return current;
  }

  // moves to the left;
  public Vertex moveLeft() {
    for (Edge e : current.allEdges) {
      move(e.to.x == current.x - 1, e);
    }
    return current;
  }

  // moves to the right;
  public Vertex moveRight() {
    for (Edge e : current.allEdges) {
      move(e.to.x == current.x + 1, e);
    }
    return current;
  }

  // moves to bottom;
  public Vertex moveBottom() {
    for (Edge e : current.allEdges) {
      move(e.to.y == current.y + 1, e);
    }
    return current;
  }

  // moves to top;
  public Vertex moveTop() {
    for (Edge e : current.allEdges) {
      move(e.to.y == current.y - 1, e);
    }
    return current;
  }
}

class Maze extends World {
  static final int WIDTH = 100;
  static final int HEIGHT = 60;
  static final int SIZE = 10;
  boolean breadthfirst;
  boolean depthfirst;
  boolean manual;
  BreadthFirst bf;
  DepthFirst df;
  Player p;
  IList<Vertex> vertices;
  IList<Edge> edges;

  Maze() {
    init();
  }

  // initializes the game;
  void init() {
    ArrayList<ArrayList<Vertex>> v = initVertices();
    ArrayList<Edge> allEdges = getEdges(v);
    v = kruskalVertice(v);
    edges = getWalls(v, allEdges);
    vertices = new Empty<Vertex>();
    for (ArrayList<Vertex> vList : v) {
      for (Vertex vt : vList) {
        vertices = vertices.add(vt);
      }
    }
    breadthfirst = false;
    depthfirst = false;
    manual = false;
    bf = new BreadthFirst(vertices);
    df = new DepthFirst(vertices);
    p = new Player(vertices);
  }

  // draws the game;
  public WorldScene makeScene() {
    WorldScene w = new WorldScene(WIDTH * SIZE, HEIGHT * SIZE);
    for (Vertex v : vertices) {
      Color col = colorVertex(v);
      w.placeImageXY(new RectangleImage(SIZE, SIZE, OutlineMode.SOLID, col),
          (v.x * SIZE) + (SIZE * 1 / 2),
          (v.y * SIZE) + (SIZE * 1 / 2));
    }
    for (Edge e : edges) {
      if (e.to.x == e.from.x) {
        w.placeImageXY(new RectangleImage(SIZE, SIZE / 10, OutlineMode.SOLID, Color.black),
            (e.to.x * SIZE) + (SIZE * 1 / 2), ((e.to.y + e.from.y) * SIZE / 2) + (SIZE * 1 / 2));
      } else {
        w.placeImageXY(new RectangleImage(SIZE / 10, SIZE, OutlineMode.SOLID, Color.black),
            ((e.to.x + e.from.x) * SIZE / 2) + (SIZE * 1 / 2), (e.to.y * SIZE) + (SIZE * 1 / 2));
      }
    }
    return w;
  }

  // makes the walls for the game;
  IList<Edge> getWalls(ArrayList<ArrayList<Vertex>> v, ArrayList<Edge> all) {
    IList<Edge> w = new Empty<Edge>();
    for (Edge e : all) {
      boolean valid = true;
      for (ArrayList<Vertex> l : v) {
        for (Vertex vt : l) {
          for (Edge e2 : vt.allEdges) {
            if (e.equals(e2) || (e.to == e2.from && e.from == e2.to)) {
              valid = false;
            }
          }
        }
      }
      if (valid) {
        w = w.add(e);
      }
    }
    return w;
  }

  // returns a list of edges;
  ArrayList<Edge> getEdges(ArrayList<ArrayList<Vertex>> v) {
    ArrayList<Edge> all = new ArrayList<Edge>();
    for (ArrayList<Vertex> verts : v) {
      for (Vertex vt : verts) {
        for (Edge ed : vt.allEdges) {
          all.add(ed);
        }
      }
    }
    return all;
  }

  // returns with the starting vertices;
  ArrayList<ArrayList<Vertex>> initVertices() {
    ArrayList<ArrayList<Vertex>> vertices = new ArrayList<ArrayList<Vertex>>();
    for (int x = 0; x < WIDTH; x++) {
      ArrayList<Vertex> temp = new ArrayList<Vertex>();
      for (int y = 0; y < HEIGHT; y++) {
        temp.add(new Vertex(x, y));
      }
      vertices.add(temp);
    }
    Random r = new Random();
    for (ArrayList<Vertex> vList : vertices) {
      for (Vertex v : vList) {
        if (v.x != 0) {
          v.allEdges.add(new Edge(v, vertices.get(v.x - 1).get(v.y), r.nextInt(1000)));
        }
        if (v.x != WIDTH - 1) {
          v.allEdges.add(new Edge(v, vertices.get(v.x + 1).get(v.y), r.nextInt(1000)));
        }
        if (v.y != 0) {
          v.allEdges.add(new Edge(v, vertices.get(v.x).get(v.y - 1), r.nextInt(1000)));
        }
        if (v.y != HEIGHT - 1) {
          v.allEdges.add(new Edge(v, vertices.get(v.x).get(v.y + 1), r.nextInt(1000)));
        }
      }
    }
    return vertices;
  }

  // returns with the list of the edges, depending on their weights;
  ArrayList<ArrayList<Vertex>> kruskalVertice(ArrayList<ArrayList<Vertex>> v) {
    ArrayList<Edge> allEdges = getEdges(v);
    for (ArrayList<Vertex> i : v) {
      for (Vertex j : i) {
        j.allEdges = new ArrayList<Edge>();
      }
    }
    int totalCells = HEIGHT * WIDTH;
    IList<Edge> sT = new Empty<Edge>();
    ArrayList<Edge> allEdgesSorted = sort(allEdges);
    HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
    for (int i = 0; i <= (1000 * HEIGHT) + WIDTH; i++) {
      hash.put(i, i);
    }
    ArrayList<Edge> l = allEdgesSorted;
    while (sT.length() < totalCells - 1) {
      Edge e = l.get(0);
      if (this.find(hash, e.to.uniqueIdentify()) != this.find(hash, e.from.uniqueIdentify())) {
        sT = sT.add(e);
        e.from.allEdges.add(e);
        e.to.allEdges.add(new Edge(e.to, e.from, e.weight));
        int temp = (find(hash, e.to.uniqueIdentify()));
        hash.remove(find(hash, e.to.uniqueIdentify()));
        hash.put(temp, find(hash, e.from.uniqueIdentify()));
      }
      l.remove(0);
    }
    return v;
  }

  // finds the representative of the hash map;
  int find(HashMap<Integer, Integer> hashmap, int x) {
    if (hashmap.get(x) == x) {
      return x;
    } else {
      return find(hashmap, hashmap.get(x));
    }
  }

  // returns with the sorted array list;
  ArrayList<Edge> sort(ArrayList<Edge> l) {
    if (l.size() <= 1) {
      return l;
    }
    ArrayList<Edge> l1 = new ArrayList<Edge>();
    ArrayList<Edge> l2 = new ArrayList<Edge>();
    for (int i = 0; i < l.size() / 2; i++) {
      l1.add(l.get(i));
    }
    for (int i = l.size() / 2; i < l.size(); i++) {
      l2.add(l.get(i));
    }
    l1 = sort(l1);
    l2 = sort(l2);
    return union(l1, l2);
  }

  // combines the two given array lists;
  ArrayList<Edge> union(ArrayList<Edge> l1, ArrayList<Edge> l2) {
    ArrayList<Edge> l3 = new ArrayList<Edge>();
    IComparator<Edge> c = new CompareEdges();
    while (l1.size() > 0 && l2.size() > 0) {
      if (c.apply(l1.get(0), l2.get(0))) {
        l3.add(l1.get(0));
        l1.remove(0);
      } else {
        l3.add(l2.get(0));
        l2.remove(0);
      }
    }
    while (l1.size() > 0) {
      l3.add(l1.get(0));
      l1.remove(0);
    }
    while (l2.size() > 0) {
      l3.add(l2.get(0));
      l2.remove(0);
    }
    return l3;
  }

  // returns with the color of the given vertex;
  Color colorVertex(Vertex v) {
    if (v.x == WIDTH - 1 && v.y == HEIGHT - 1) {
      return Color.red;
    } else if (v.partofPath) {
      return Color.pink;
    } else if (v.x == 0 && v.y == 0) {
      return Color.yellow;
    } else if (v.hasBeen) {
      return Color.blue;
    } else {
      return Color.white;
    }
  }

  // changes the game every tick;
  public void onTick() {
    if (breadthfirst) {
      if (bf.hasNext()) {
        bf.next();
      }
    }
    if (depthfirst) {
      if (df.hasNext()) {
        df.next();
      }
    }
  }

  // enables the key events;
  public void onKeyEvent(String ke) {
    if (ke.equals("b")) {
      breadthfirst = true;
      depthfirst = false;
      manual = false;
      reset();
    } else if (ke.equals("d")) {
      breadthfirst = false;
      depthfirst = true;
      manual = false;
      reset();
    } else if (ke.equals("m")) {
      breadthfirst = false;
      depthfirst = false;
      manual = true;
      reset();
    } else if (ke.equals("r")) {
      init();
    } else if (manual) {
      if (p.hasNext()) {
        if (ke.equals("left")) {
          p.moveLeft();
        } else if (ke.equals("up")) {
          p.moveTop();
        } else if (ke.equals("right")) {
          p.moveRight();
        } else if (ke.equals("down")) {
          p.moveBottom();
        }
      }
    }
  }

  // resets our game, but not our maze
  public void reset() {
    for (Vertex v : vertices) {
      v.partofPath = false;
      v.hasBeen = false;
    }
    bf = new BreadthFirst(vertices);
    df = new DepthFirst(vertices);
    p = new Player(vertices);
  }
}

class ExamplesMaze {
  ExamplesMaze() {
  }

  Sentinel<String> sent1;
  Sentinel<Integer> sent2;

  ANode<String> node1;
  ANode<String> node2;
  ANode<String> node3;
  ANode<String> node4;

  ANode<Integer> node5;
  ANode<Integer> node6;
  ANode<Integer> node7;
  ANode<Integer> node8;

  Deque<String> deque1;
  Deque<String> deque2;
  Deque<Integer> deque3;
  
  Maze maze;
  Edge e1;
  Edge e2;
  Edge e3;
  Edge e4;
  Edge e5;
  
  Vertex v1;
  Vertex v2;
  Vertex v3;
  IList<Vertex> lv;
  Player p;
  
  
  
  
  
  void initData() {
    this.sent1 = new Sentinel<String>();
    this.sent2 = new Sentinel<Integer>();

    this.node1 = new Node<String>("abc", sent1, sent1);
    this.node2 = new Node<String>("bcd", node1, sent1);
    this.node3 = new Node<String>("cde", node2, sent1);
    this.node4 = new Node<String>("def", node3, sent1);

    this.node5 = new Node<Integer>(012, sent2, sent2);
    this.node6 = new Node<Integer>(123, node5, sent2);
    this.node7 = new Node<Integer>(234, node6, sent2);
    this.node8 = new Node<Integer>(345, node7, sent2);

    this.deque1 = new Deque<String>();
    this.deque2 = new Deque<String>(this.sent1);
    this.deque3 = new Deque<Integer>(this.sent2);
    
    this.maze = new Maze();

    this.e1 = new Edge(null, null, 10);
    this.e2 = new Edge(null, null, 23);
    this.e3 = new Edge(null, null, 50);
    this.e4 = new Edge(null, null, 35);
    this.e5 = new Edge(null, null, 15);
    
    this.v1 = new Vertex(2, 3);
    this.v2 = new Vertex(5, 4);
    this.v3 = new Vertex(6, 5);

    this.lv = new Cons<Vertex>(this.v1, new Cons<Vertex>(this.v2, new Cons<Vertex>(this.v3, 

        new Empty<Vertex>())));
    

    this.p = new Player(this.lv);
  }

  void testsize(Tester t) {
    this.initData();
    t.checkExpect(this.deque1.size(), 0);
    t.checkExpect(this.deque2.size(), 4);
    t.checkExpect(this.deque3.size(), 4);
  }

  void testaddAtHead(Tester t) {
    this.initData();
    this.deque2.addAtHead("efg");
    t.checkExpect(this.deque2.size(), 5);
  }

  void testaddAtTail(Tester t) {
    this.initData();
    this.deque1.addAtTail("def");
    this.deque1.addAtTail("cde");
    this.deque1.addAtTail("bcd");
    this.deque1.addAtTail("abc");
    t.checkExpect(this.deque1.size(), 4);
    t.checkExpect(this.deque1, this.deque2);
  }

  void testRemoveFromHead(Tester t) {
    initData();
    this.deque2.removeFromHead();
    t.checkExpect(this.deque2.size(), 3);
    this.deque2.removeFromHead();
    t.checkExpect(this.deque2.size(), 2);
    this.deque2.removeFromHead();
    t.checkExpect(this.deque2.size(), 1);
  }

  void testRemoveFromTail(Tester t) {
    initData();
    this.deque2.removeFromTail();
    t.checkExpect(this.deque2.size(), 3);
    this.deque2.removeFromTail();
    t.checkExpect(this.deque2.size(), 2);
    this.deque2.removeFromTail();
    t.checkExpect(this.deque2.size(), 1);
  }

  void testremoveNode(Tester t) {
    initData();
  }

  void testremove(Tester t) {
    initData();
    t.checkException(new RuntimeException("cannot remove from an empty deque"), this.sent1, "remove");
    this.node1.remove();
    t.checkExpect(this.deque2.size(), 3);
  }

  void testsizeHelper(Tester t) {
    initData();
    t.checkExpect(this.sent1.sizeHelper(), 4);
  }

  void testadd(Tester t) {
    IList<String> s = new Empty<String>();
    t.checkExpect(s.add("hi"), new Cons<String>("hi", new Empty<String>()));
    t.checkExpect(s.add("hi").add("hi2"), new Cons<String>("hi", new Cons<String>("hi2",
        new Empty<String>())));
  }

  void testlength(Tester t) {
    IList<String> mt = new Empty<String>();
    IList<String> l1 = new Cons<String>("One", new Cons<String>("Two", 
        new Cons<String>("Three", mt)));
    t.checkExpect(mt.length(), 0);
    t.checkExpect(l1.length(), 3);
  }

  void testappend(Tester t) {
    IList<String> mt = new Empty<String>();
    IList<String> l1 = new Cons<String>("One", new Cons<String>("Two", 
        new Cons<String>("Three", mt)));
    t.checkExpect(mt.append(l1), l1);
    t.checkExpect(l1.append(mt), l1);
  }

  void testgetItem(Tester t) {
    IList<String> mt = new Empty<String>();
    IList<String> l1 = new Cons<String>("One", new Cons<String>("Two", 
        new Cons<String>("Three", mt)));
    t.checkExpect(mt.getItem(), null);
    t.checkExpect(l1.getItem(), "One");
  }

  void testgetNext(Tester t) {
    IList<String> mt = new Empty<String>();
    IList<String> l1 = new Cons<String>("One", new Cons<String>("Two", 
        new Cons<String>("Three", mt)));
    t.checkExpect(l1.getNext(), new Cons<String>("Two", new Cons<String>("Three", mt)));
  }

  void testuniqueIdentify(Tester t) {
    Vertex v1 = new Vertex(3, 7);
    Vertex v2 = new Vertex(0, 0);
    t.checkExpect(v1.uniqueIdentify(), 7003);
    t.checkExpect(v2.uniqueIdentify(), 0);
  }

  void testMaze(Tester t) {
    initData();
    t.checkExpect(maze.vertices.length(), Maze.WIDTH * Maze.HEIGHT);
    HashMap<Integer, Integer> h = new HashMap<Integer, Integer>();
    h.put(1, 1);
    h.put(2, 1);
    t.checkExpect(maze.find(h, 1), 1);
    t.checkExpect(maze.find(h, 2), 1);
  }

  void testSort(Tester t) {
    initData();
    ArrayList<Edge> unsorted = new ArrayList<Edge>();
    ArrayList<Edge> sorted = new ArrayList<Edge>();

    unsorted.add(e1);
    unsorted.add(e2);
    unsorted.add(e3);
    unsorted.add(e4);
    unsorted.add(e5);

    sorted.add(e1);
    sorted.add(e5);
    sorted.add(e2);
    sorted.add(e4);
    sorted.add(e3);

    t.checkExpect(maze.sort(unsorted), sorted);
  }
  
  
  void testMoveLeft(Tester t) {
    initData();
    t.checkExpect(this.p.moveLeft(), new Vertex(2, 3));
  }
  
  void testMoveRight(Tester t) {
    initData();
    t.checkExpect(this.p.moveRight(), new Vertex(2, 3));
  }
  
  void testMoveBottom(Tester t) {
    initData();
    t.checkExpect(this.p.moveBottom(), new Vertex(2, 3));
  }
  
  void testMoveTop(Tester t) {
    initData();
    t.checkExpect(this.p.moveTop(), new Vertex(2, 3));
  }
  

  //

//  void testGame(Tester t) {
//    Maze m = new Maze();
//    m.bigBang(Maze.WIDTH * Maze.SIZE, 
//        Maze.HEIGHT * Maze.SIZE, 0.005);
//  }
}

