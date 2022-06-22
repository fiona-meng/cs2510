import java.util.ArrayList;
import tester.Tester;
import java.util.Iterator;

class ListOfLists<T> implements Iterator<T>, Iterable<Integer>{
  ArrayList<T> arraylist;

  ListOfLists(ArrayList<T> arraylist) {
    this.arraylist = arraylist;
  }

  ListOfLists() {
  }
  


  // adds a new empty Arraylist<T> to the end of the list-of-lists;
  public void addNewList() {
    ArrayList<T> newList = new ArrayList<T>();
    this.arraylist.addAll(newList);
  }

  // adds provided object to the end of the list-of-lists at the provided index;
  public void add(int index, T object) {
    this.arraylist.add(index, object);
  }

  // returns the list of this list-of-lists at the provided index;
  //  ArrayList<T> get(int index) {
  //    if (this.arraylist == null) {
  //      new IndexOutOfBoundsException("This arraylist is empty");
  //    } else {
  //      return this.arraylist.get(index);
  //    }
  //  }

  // returns the number of lists in the list-of-lists;
  public int size() {
    int num = 0;
    for (T t : arraylist) {
      num++ ;
    }
    return num;
  }

  @Override
  public boolean hasNext() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public T next() {
    // TODO Auto-generated method stub
    return null;
  }
}



class Examples {

  ArrayList<Integer> list1;
  ArrayList<String> list2;
  ArrayList<Integer> list3;
  ListOfLists<Integer> bigList1;

  void initData() {
    this.list1 = new ArrayList<Integer>();
    this.list1.add(1);
    this.list1.add(2);
    this.list1.add(3);
    this.list2 = new ArrayList<String>();
    this.list2.add("1");
    this.list2.add("2");
    this.list2.add("3");
    this.list3 = new ArrayList<Integer>();
    this.list3.add(4);
    this.list3.add(5);
    this.list3.add(6);
    this.bigList1 = new ListOfLists<Integer>(this.list1);
  }



  void testListOfLists(Tester t) {
    ListOfLists<Integer> lol = new ListOfLists<Integer>();
    //add 3 lists
    lol.addNewList();
    lol.addNewList();
    lol.addNewList();

    //add elements 1,2,3 in first list
    lol.add(0,1);
    lol.add(0,2);
    lol.add(0,3);

    //add elements 4,5,6 in second list
    lol.add(1,4);
    lol.add(1,5);
    lol.add(1,6);

    //add elements 7,8,9 in third list
    lol.add(2,7);
    lol.add(2,8);
    lol.add(2,9);

    //iterator should return elements in order 1,2,3,4,5,6,7,8,9
    int number = 1;
    for (Integer num: lol) {
        t.checkExpect(num,number);
        number = number + 1;
    }
    
    t.checkExpect(lol.size(),0);
  }


}