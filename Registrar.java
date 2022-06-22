import tester.Tester;

interface IRegistrar{

}

class Course implements IRegistrar {
  String name;
  Instructor prof;
  IList<Student> students;

  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = new MtList<Student>();
    prof.updateProf(this);
  }

  //  void hasProf(Course c) {
  //    if (c.prof == null) {
  //      throw new RuntimeException
  //      ("Course cannot be constructed since there is no available professor");
  //    }
  //  }

  // update students who take this course
  void enrollhelper(Student students) {
    this.students = new ConsList<Student>(students, this.students);
  }

}

class Instructor implements IRegistrar {
  String name;
  IList<Course> courses;

  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  // determines whether the given Student is in 
  // more than one of this Instructor’s Courses
  boolean dejavu(Student c) {
    return this.courses.containsMultiple(new HaveStudent(c), 1);
  }

  // update the course which will be taught by this instructor
  void updateProf(Course that) {
    this.courses = new ConsList<Course>(that, this.courses);
  }

}

class Student implements IRegistrar {
  String name;
  int id;
  IList<Course> courses;

  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  // update the course which will be studied by this student
  void enroll(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
    c.enrollhelper(this);
  }

  // determines whether the given Student is in any of the 
  // same classes as this Student
  boolean classmates(Student c) {
    return this.courses.anySame(c.courses, new SameCourse());
  }

}

interface IComparator<T> {
  boolean apply(T t1, T t2);
}

// determine whether given students are same
class SameStudents implements IComparator<Student> {
  public boolean apply(Student s1, Student s2) {
    return s1.id == s2.id;
  }
}

// determine whether given courses are same
class SameCourse implements IComparator<Course> {
  public boolean apply(Course c1, Course c2) {
    return c1.name.equals(c2.name)
        && c1.prof == c2.prof
        && c1.students == c2.students;
  }
}


interface IPred<T> {
  boolean apply(T t);
}

// determine whether this course have this student
class HaveStudent implements IPred<Course> {
  Student s; 

  HaveStudent(Student s) {
    this.s = s;
  }   

  public boolean apply(Course t) {
    return t.students.contains(this.s, new SameStudents());
  }
}

interface IList<T> { 
  // do these lists contain any elements that are the same?
  boolean anySame(IList<T> other, IComparator<T> comparator);

  // does this list contain the given element?
  boolean contains(T element, IComparator<T> comparator);

  // does this IList contain at least n elements that pass the test?
  boolean containsMultiple(IPred<T> test, int n);

  // how many elements pass the test?
  int containsMultipleHelper(IPred<T> test);
}

class MtList<T> implements IList<T> {

  // do these lists contain any elements that are the same?
  public boolean anySame(IList<T> other, IComparator<T> comparator) {
    return false;
  }


  // does this list contain the given element?
  public boolean contains(T element, IComparator<T> comparator) {
    return false;
  }

  //how many elements pass the test?
  public int containsMultipleHelper(IPred<T> test) {
    return 0;
  }

  //does this IList contain at least n elements that pass the test?
  public boolean containsMultiple(IPred<T> test, int n) {
    return false;
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  //do these lists contain any elements that are the same?
  public boolean anySame(IList<T> other, IComparator<T> comparator) {
    return other.contains(this.first, comparator) || this.rest.anySame(other, comparator);
  }

  //does this list contain the given element?
  public boolean contains(T element, IComparator<T> comparator) {
    return comparator.apply(this.first, element)
        || this.rest.contains(element, comparator);
  }

  //how many elements pass the test?
  public int containsMultipleHelper(IPred<T> test) {
    if (test.apply(this.first)) {
      return 1 + this.rest.containsMultipleHelper(test);
    }
    else {
      return this.rest.containsMultipleHelper(test);
    }
  }

  //does this IList contain at least n elements that pass the test?
  public boolean containsMultiple(IPred<T> test, int n) {
    return this.containsMultipleHelper(test) > n;
  }

}

class ExampleIRegistrar {
  ExampleIRegistrar() {}

  IList<Student> studentList1;
  IList<Student> studentList2;
  IList<Student> studentList3;
  Instructor teacher1;
  Instructor teacher2;
  Course course1;
  Course course2;
  Course course3;
  Course course4;
  IList<Course> courseList1;
  Student student1;
  Student student2;
  Student student3;
  Student student4;
  Student student5;

  void initData() {
    this.student1 = new Student("Tom", 123);
    this.student2 = new Student("Alice", 456);
    this.student3 = new Student("Tom", 666);
    this.student4 = new Student("Tony", 798);
    this.student5 = new Student("ZhangSan", 888);


    this.studentList1 = new ConsList<Student>(this.student1,
        new ConsList<Student>(this.student2, new ConsList<Student>(this.student3, 
            new ConsList<Student>(this.student4, 
                new ConsList<Student>(this.student5, 
                    new MtList<Student>())))));

    this.studentList2 = new ConsList<Student>(this.student4, new ConsList<Student>(this.student5, 
        new ConsList<Student>(this.student1, new MtList<Student>())));

    this.studentList3 = new ConsList<Student>(this.student3, new ConsList<Student>(this.student2, 
        new MtList<Student>()));

    this.teacher1 = new Instructor("Teacher1");
    this.teacher2 = new Instructor("Teacher2");

    this.course1 = new Course("Course1", this.teacher1);
    this.course2 = new Course("Course2", this.teacher1);
    this.course3 = new Course("Course3", this.teacher2);
    this.course4 = new Course("Course4", this.teacher2);

    this.courseList1 = new ConsList<Course>(this.course1,
        new ConsList<Course>(this.course2, new ConsList<Course>(this.course3, 
            new ConsList<Course>(this.course4, new MtList<Course>())))); 
  }

  void testenroll(Tester t) {
    this.initData();
    this.student1.enroll(course1);
    t.checkExpect(this.student1.courses, new ConsList<Course>(course1, new MtList<Course>()));
    t.checkExpect(this.course1.students, new ConsList<Student>(student1, new MtList<Student>()));
    this.student1.enroll(course2);
    t.checkExpect(this.student1.courses, new ConsList<Course>(course2, 
        new ConsList<Course>(course1, new MtList<Course>())));

  }


  void testenrollHelper(Tester t) {
    this.initData();
    this.course1.enrollhelper(student1);
    t.checkExpect(this.course1.students, new ConsList<Student>(student1,  new MtList<Student>()));
    this.course1.enrollhelper(student2);
    t.checkExpect(this.course1.students, new ConsList<Student>(student2,
        new ConsList<Student>(student1,  new MtList<Student>())));
  }


  void testupdateProf(Tester t) {
    this.initData();
    t.checkExpect(this.teacher1.courses, new ConsList<Course>(course2,  
        new ConsList<Course>(course1, new MtList<Course>())));
    t.checkExpect(this.course1.prof, teacher1);

    this.teacher1.updateProf(this.course1);
    t.checkExpect(this.teacher1.courses, new ConsList<Course>(course1, 
        new ConsList<Course>(course2,  
            new ConsList<Course>(course1, new MtList<Course>()))));

    this.teacher1.updateProf(this.course2);
    t.checkExpect(this.teacher1.courses, new ConsList<Course>(course2,
        new ConsList<Course>(course1, new ConsList<Course>(course2,  
            new ConsList<Course>(course1, new MtList<Course>())))));
  }


  void testclassmates(Tester t) {
    this.initData();
    this.student1.enroll(this.course1);
    this.student2.enroll(this.course1);
    t.checkExpect(this.course1.students, new ConsList<Student>(student2,
        new ConsList<Student>(student1,  new MtList<Student>())));
    t.checkExpect(this.student1.classmates(student2), true);
    t.checkExpect(this.student1.classmates(student3), false);         
  }

  void testdejavu(Tester t) {
    this.initData();
    this.student1.enroll(this.course1);
    this.student1.enroll(this.course2); 
    this.student2.enroll(this.course1);
    this.student1.enroll(this.course3);
    t.checkExpect(this.student1.courses, new ConsList<Course>(course3, new ConsList<Course>(course2,
        new ConsList<Course>(course1,  new MtList<Course>()))));
    t.checkExpect(this.teacher1.courses, new ConsList<Course>(course2,
        new ConsList<Course>(course1,  new MtList<Course>())));
    t.checkExpect(this.teacher1.dejavu(student1), true);
    t.checkExpect(this.teacher1.dejavu(student2), false);


  }

  boolean testapply(Tester t) {
    this.student1.enroll(this.course1);
    return t.checkExpect(new SameStudents().apply(this.student1, this.student2), false)
        && t.checkExpect(new SameCourse().apply(this.course1, this.course2), false)
        && t.checkExpect(new HaveStudent(this.student1).apply(this.course2), false)
        && t.checkExpect(new HaveStudent(this.student1).apply(this.course1), true);
  }

  void testanySame(Tester t) {
    this.initData();
    t.checkExpect(this.studentList2.anySame(this.studentList3, new SameStudents()), false);
    t.checkExpect(this.studentList1.anySame(this.studentList2, new SameStudents()), true);
  }



  //    boolean testhasProf(Tester t) {
  //  
  //      return t.checkConstructorException(new RuntimeException(
  //  
  //          "Course cannot be constructed since there is no available professor"), 
  //"Course", this.course1);
  //  
  //    }


  void testcontains(Tester t) {
    this.initData();
    t.checkExpect(this.studentList2.contains(this.student2, new SameStudents()), false);
    t.checkExpect(this.studentList1.contains(this.student2, new SameStudents()), true);
  }

  void testcontainsMultiple(Tester t) {
    this.initData();
    this.student1.enroll(this.course1);
    this.student1.enroll(this.course2);
    t.checkExpect(this.courseList1.containsMultiple(new HaveStudent(this.student1), 1), true);
  }

  void testcontainsMultipleHelper(Tester t) {
    this.initData();
    this.student1.enroll(this.course1);
    this.student1.enroll(this.course2);
    t.checkExpect(this.courseList1.containsMultipleHelper(new HaveStudent(this.student1)), 2);
  }
}



