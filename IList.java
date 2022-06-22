import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import tester.Tester;

interface IList<T> {
  // finds out the items that meet the requirements;
  IList<T> filter(Predicate<T> pred);
  
  // creates a new list of the items after conversion;
  <U> IList<U> map(Function<T,U> converter);
  
  // creates a new list of the items that meet the requirements;
  <U> U fold(BiFunction<T,U,U> converter,U initial);  
}

class MtList<T> implements IList<T> {
  MtList() {}

  @Override
  public IList<T> filter(Predicate<T> pred) {
    // finds out the items that meet the requirements;
    // TODO Auto-generated method stub
    return new MtList<T>();
  }

  @Override
  // creates a new list of the items after conversion;
  public <U> IList<U> map(Function<T, U> converter) {
    // TODO Auto-generated method stub
    return new MtList<U>();
  }

  @Override
  // creates a new list of the items that meet the requirements;
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    // TODO Auto-generated method stub
    return initial;
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  ConsList(T first,IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
  @Override
  // finds out the items that meet the requirements;
  public IList<T> filter(Predicate<T> pred) {
    // TODO Auto-generated method stub
    if (pred.apply(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    else {
      return this.rest.filter(pred);
    }
  }

  @Override
  // creates a new list of the items after conversion;
  public <U> IList<U> map(Function<T, U> converter) {
    // TODO Auto-generated method stub
    return new ConsList<U>(converter.apply(this.first), this.rest.map(converter));
  }

  @Override
  // creates a new list of the items that meet the requirements;
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    // TODO Auto-generated method stub
    return converter.apply(this.first, this.rest.fold(converter,initial));
  }
}


class Month  {
  String string;
  
  Month(String string) {
    this.string = string;
  }
  
  // returns with the months that start with a T;
  boolean containsT() {
    return this.string.contains("T");
  }
  
  // returns with the months that end with er;
  boolean containsER() {
    return this.string.endsWith("er");
  }
  
  // returns with the month with its first three letter;
  String monthAbb() {
    return this.string.substring(0, 2);
  }
}

//class isT implement IPredicate<String>

class MonthPred implements Predicate<Month> {
  MonthPred() {}
  
  // returns with the months that start with a T;
  public boolean apply(Month month) {
    return month.containsT();
  }
  
  @Override
  public boolean test(Month t) {
    // TODO Auto-generated method stub
    return false;
  }
}

class MonthER implements BiFunction<Month, Integer, Integer> {
  // returns with the months that end with er;
  public Integer apply(Month month, Integer acc) {
    if (month.containsER()) {
      return 1 + acc;
    } else {
      return 0;
    }
  }
}

class AbbMonth implements Function<Month, String> {
  // returns with the month with its first three letter;
  public String apply(Month month) {
    return month.monthAbb();
  }
}

class convolve implements BiFunction<Integer, String, String> {
  public String
}

class ExamplesLists{
  ExamplesLists() {}
  
  IList<String> monthlist = new ConsList<String>("January", new ConsList<String>("February",
      new ConsList<String>("March", new ConsList<String>("April", new ConsList<String>(
          "May", new ConsList<String>("June", new ConsList<String>("July", new ConsList<String>(
              "August", new ConsList<String>("September", new ConsList<String>("October",
                  new ConsList<String>("November", new ConsList<String>("December",
                      new MtList<String>()))))))))))));
  
  IList<String> monthmapExpect = new ConsList<String>("Jan", new ConsList<String>("Feb",
      new ConsList<String>("Mar", new ConsList<String>("Apr", new ConsList<String>(
          "May", new ConsList<String>("Jun", new ConsList<String>("Jul", new ConsList<String>(
              "Aug", new ConsList<String>("Sep", new ConsList<String>("Oct",
                  new ConsList<String>("Nov", new ConsList<String>("Dec",
                      new MtList<String>()))))))))))));
  
  IList<Integer> monthInt = new ConsList<Integer>(1, new ConsList<Integer>(2,
      new ConsList<Integer>(3, new ConsList<Integer>(4, new ConsList<Integer>(
          5, new ConsList<Integer>(6, new ConsList<Integer>(7, new ConsList<Integer>(
              8, new ConsList<Integer>(9, new ConsList<Integer>(10,
                  new ConsList<Integer>(11, new ConsList<Integer>(12,
                      new MtList<Integer>()))))))))))));
  
  IList<String> convolveExpect = new ConsList<String>("1,anuary", new ConsList<String>("2,February",
      new ConsList<String>("3,March", new ConsList<String>("4,April", new ConsList<String>(
          "5,May", new ConsList<String>("6,June", new ConsList<String>("7,July", new ConsList<String>(
              "8,August", new ConsList<String>("9,September", new ConsList<String>("10,October",
                  new ConsList<String>("11,November", new ConsList<String>("12,December",
                      new MtList<String>()))))))))))));
  
  
  Predicate<Month> haveT = new MonthPred();

  boolean testfilter(Tester t) {
    return t.checkExpect(this.monthlist.filter(this.haveT), new MtList<String>());
  }
  
  boolean testfold(Tester t) {
    return t.checkExpect(monthlist.fold(new MonthER()), 4);
  }
  
  boolean testmap(Tester t) {
    return t.checkExpect(this.monthlist.map(null), monthmapExpect);
  }
  
  boolean testConvolve(Tester t) {
    return t.checkExpect(this.monthlist.convolve(monthlist, monthInt), convolveExpect);
  }
  
  
}