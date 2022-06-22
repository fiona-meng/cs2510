import tester.Tester;
import java.util.function.Predicate;

class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> s) {
    this.header = s;
  }

  // count the number of nodes in a deque;
  int size() {
    return this.header.next.sizeHelper();
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

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // counts how many nodes;
  int sizeHelper() {
    return 1 + this.next.sizeHelper();
  }

  // removes this ANode<T> if this is a node;
  abstract T remove();

  // finds the first node that produces true with the given predicate;
  abstract ANode<T> findHelper(Predicate<T> pred);
}

class Sentinel<T> extends ANode<T> {
  Sentinel() {
    this.prev = this;
    this.next = this;
  }

  // counts how many nodes;
  int sizeHelper() {
    return 0;
  }

  // removes this ANode<T> if this is a node;
  public T remove() {
    throw new RuntimeException("cannot remove from an empty deque");
  }

  // finds the first node that produces true with the given predicate;
  public ANode<T> findHelper(Predicate<T> pred) {
    return this;
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
    if (pred.test(this.data)) {
      return this;
    } else {
      return this.next.findHelper(pred);
    }
  }
}

class Same implements Predicate<String> {
  String string;

  Same(String string) {
    this.string = string;
  }

  // checks if this string is equal to the given string;
  public boolean test(String s) {
    return this.string.equals(s);
  }
}

class ExamplesDeque {
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

  Same same1 = new Same("abc");
  Same same2 = new Same("abd");


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

  void testfind(Tester t) {
    initData();
    t.checkExpect(this.deque2.find(this.same1), new Node<String>("abc", this.sent1, this.sent1));
    t.checkExpect(this.deque2.find(this.same2), new Sentinel<String>());
    t.checkExpect(this.deque1.find(this.same2), new Sentinel<String>());
  }

  void testremoveNode(Tester t) {
    initData();
  }

  void testremove(Tester t) {
    initData();
    t.checkException(new RuntimeException("cannot remove from an empty deque"),
        this.sent1, "remove");
    this.node1.remove();
    t.checkExpect(this.deque2.size(), 3);
  }

  void testsizeHelper(Tester t) {
    initData();
    t.checkExpect(this.sent1.sizeHelper(), 0);
    t.checkExpect(this.node1.sizeHelper(), 1);
    t.checkExpect(this.node2.sizeHelper(), 2);
  }

  void testfindHelper(Tester t) {
    initData();
    t.checkExpect(this.sent1.findHelper(this.same1), this.sent1);
    t.checkExpect(this.node1.findHelper(this.same1), this.node1);
    t.checkExpect(this.node2.findHelper(this.same1), this.node1);
  }
}
