import tester.Tester;

// represents a list of Person's buddies
interface ILoBuddy {
  // checks if the list contains the given person;
  boolean contains(Person that);

  // counts how many common direct buddies there are;
  int countHelper1(Person that);

  // checks if one person in this list is a direct buddy with the given person;
  boolean extendHelper(Person that);

  // count how many items there are in the list;
  int count();
}

//represents an empty list of Person's buddies
class MTLoBuddy implements ILoBuddy {
  MTLoBuddy() {
  }

  // checks if the list contains the given person;
  public boolean contains(Person that) {
    return false;
  }

  // counts how many common direct buddies there are;
  public int countHelper1(Person that) {
    return 0;
  }

  // checks if one person in this list is a direct buddy with the given person;
  public boolean extendHelper(Person that) {
    return false;
  }

  // count how many items there are in the list;
  public int count() {
    return 0;
  }
}

//represents a list of Person's buddies
class ConsLoBuddy implements ILoBuddy {

  Person first;
  ILoBuddy rest;

  ConsLoBuddy(Person first, ILoBuddy rest) {
    this.first = first;
    this.rest = rest;
  }

  // checks if the list contains the given person;
  public boolean contains(Person that) {
    return this.first.username.equals(that.username) || this.rest.contains(that);
  }

  // counts how many common direct buddies there are;
  public int countHelper1(Person that) {
    if(that.hasDirectBuddy(this.first)) {
      return 1 + this.rest.countHelper1(that);
    } else {
      return this.rest.countHelper1(that);
    }
  }

  // checks if one person in this list is a direct buddy with the given person;
  public boolean extendHelper(Person that) {
    return this.first.hasDirectBuddy(that)
        || this.rest.extendHelper(that);   
  }

  // count how many items there are in the list;
  public int count() {
    return 1 + this.rest.count();
  }
}


// represents a Person with a user name and a list of buddies
class Person {

  String username;
  ILoBuddy buddies;

  Person(String username) {
    this.username = username;
    this.buddies = new MTLoBuddy();
  }

  // returns true if this Person has that as a direct buddy
  boolean hasDirectBuddy(Person that) {
    return this.buddies.contains(that);
  }

  // returns the number of people who will show up at the party
  // given by this person
  int partyCount() {
    return 1 + this.buddies.count();
  }

  // returns the number of people that are direct buddies
  // of both this and that person
  int countCommonBuddies(Person that) {
    return this.buddies.countHelper1(that);
  }

  // will the given person be invited to a party
  // organized by this person?
  boolean hasExtendedBuddy(Person that) {
    return this.buddies.extendHelper(that);
  }

  // adds the given person to this person's buddy list
  void addBuddy(Person buddy) {
    this.buddies = new ConsLoBuddy(buddy, this.buddies);
  }
}

//runs tests for the buddies problem
 class ExamplesBuddies {

  Person hank = new Person("Hank");
  Person bob = new Person("Bob");
  Person ed = new Person("Ed");
  Person fay = new Person("Fay");
  Person gabi = new Person("Gabi");
  Person ann = new Person("Ann");
  Person cole = new Person("Cole");
  Person dan = new Person("Dan");
  Person jan = new Person("Jan");
  Person kim = new Person("Kim");
  Person len = new Person("Len");

  ILoBuddy annbl, bobbl, colebl, danbl, edbl, faybl, gabibl, hankbl, janbl, kimbl, lenbl;

  void initData() {
    this.annbl = new ConsLoBuddy(this.bob, new ConsLoBuddy(this.cole, new MTLoBuddy()));
    this.bobbl = new ConsLoBuddy(this.ann, new ConsLoBuddy(this.ed, new ConsLoBuddy(
        this.hank, new MTLoBuddy())));
    this.colebl = new ConsLoBuddy(this.dan, new MTLoBuddy());
    this.danbl = new ConsLoBuddy(this.cole, new MTLoBuddy());
    this.edbl = new ConsLoBuddy(this.fay, new MTLoBuddy());
    this.faybl = new ConsLoBuddy(this.ed, new ConsLoBuddy(this.gabi, new MTLoBuddy()));
    this.gabibl = new ConsLoBuddy(this.ed, new ConsLoBuddy(this.fay, new MTLoBuddy()));
    this.hankbl = new MTLoBuddy();
    this.janbl = new ConsLoBuddy(this.kim, new ConsLoBuddy(this.len, new MTLoBuddy()));
    this.kimbl = new ConsLoBuddy(this.jan, new ConsLoBuddy(this.len, new MTLoBuddy()));
    this.lenbl = new ConsLoBuddy(this.jan, new ConsLoBuddy(this.kim, new MTLoBuddy()));
  }

  boolean testhasDirectBuddy(Tester t) {
    this.initData();
    this.bob.addBuddy(this.hank);
    return t.checkExpect(this.hank.hasDirectBuddy(this.bob), false)
        && t.checkExpect(this.bob.hasDirectBuddy(this.hank), true);
  }

  void testpartyCount(Tester t) {
   this.initData();
   this.bob.addBuddy(this.hank);
   t.checkExpect(this.hank.partyCount(), 1);
//   t.checkExpect(this.bob.partyCount(), 2);
  }

  boolean testcountCommonBuddies(Tester t) {
    this.initData();
    this.jan.addBuddy(this.len);
    this.jan.addBuddy(this.kim);
    this.len.addBuddy(this.kim);
    this.len.addBuddy(this.jan);
    this.kim.addBuddy(this.jan);
    this.kim.addBuddy(this.len);
    return t.checkExpect(this.jan.countCommonBuddies(this.len), 1)
        && t.checkExpect(this.len.countCommonBuddies(this.kim), 1);
  }

  boolean testhasExtendedBuddy(Tester t) {
   this.initData();
   this.bob.addBuddy(this.ed);
   this.ed.addBuddy(this.fay);
   return t.checkExpect(this.bob.hasExtendedBuddy(this.fay), true)
       && t.checkExpect(this.bob.hasExtendedBuddy(this.hank), false);
  }

  void testaddBuddy(Tester t) {
    this.initData();
    this.bob.addBuddy(this.ed);
    t.checkExpect(this.bob.hasDirectBuddy(this.ed), true);
    t.checkExpect(this.ed.hasDirectBuddy(this.bob), false);
  }

  boolean testcontains(Tester t) {
    return t.checkExpect(this.annbl.contains(this.cole), true)
        && t.checkExpect(this.annbl.contains(this.hank), false);
  }

  boolean testcountHelper1(Tester t) {
    this.initData();
    this.fay.buddies = this.faybl;
    this.ed.buddies = this.edbl;
    return t.checkExpect(this.faybl.countHelper1(this.ed), 1);
 //       && t.checkExpect(this.annbl.countHelper1(this.bobbl), 0);
  }


  boolean testextendHelper(Tester t) {
   return t.checkExpect(this.annbl.extendHelper(this.cole), false)
       && t.checkExpect(this.annbl.extendHelper(this.bob), false)
       && t.checkExpect(this.gabibl.extendHelper(this.ed), false)
       && t.checkExpect(this.annbl.extendHelper(this.hank), true);
  }

  boolean testcount(Tester t) {
    return t.checkExpect(this.annbl.count(), 2)
        && t.checkExpect(this.bobbl.count(), 3)
        && t.checkExpect(this.hankbl.count(), 0);
  }
}

