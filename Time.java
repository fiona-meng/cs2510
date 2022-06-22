import tester.Tester;

class Time {
  int hour;
  int minute;
  int second;

  Time(int hour, int minute, int second) {
    this.hour = (new Utils()).checkRange(hour, 0, 23, "Invalid hour: " + Integer.toString(hour));
    this.minute = (new Utils()).checkRange(minute, 0, 59, 
        "Invalid minute: " + Integer.toString(minute));
    this.second = (new Utils()).checkRange(second, 0, 59, 
        "Invalid second: " + Integer.toString(second));
  }

  /* Template
  Field:
  ... this.hour ...            -- int
  ... this.minute ...          -- int
  ... this.second ...          -- int
  Method:
  ... this.sameTime(Time) ...  -- boolean
   */


  //convenience constructor provides a default value of zero seconds for the time.
  Time(int hour, int minute) {
    this(hour, minute, 0);
  }


  // an hour between 1 and 12
  // minutes between 0 and 59
  // boolean isAM that is true for the morning hours
  Time(int hour, int minute, boolean isAM) {
    this(hour, minute);
    if (isAM && hour == 12) {
      this.hour = 0;
    }
    if (!isAM && hour != 12) {
      this.hour = hour + 12;
    }      
  }


  // check whether is the same time
  boolean sameTime(Time that) {
    return this.hour == that.hour 
        && this.minute == that.minute 
        && this.second == that.second;
  }
}

// enforces constraints
class Utils {
  Utils() {}

  int checkRange(int value, int low, int high, String message) {
    if (value >= low && value <= high) {
      return value;
    } else {
      throw new IllegalArgumentException(message);
    }
  }
}

class ExampleTimes {
  ExampleTimes(){}

  Time time1 = new Time(10, 30, 00); // Good time
  Time time1Same = new Time(10, 30);
  Time time2 = new Time(12, 23, true);
  Time time3 = new Time(11, 23, false);
  Time time4 = new Time(12, 30, true);
  Time time4Same = new Time(0, 30, true);
  Time time5 = new Time(12, 30, false);
  Time time6 = new Time(7, 30, false);
  Time time6Same = new Time(19, 30, 00);


  boolean testCheckConstructorExpection(Tester t) {
    return t.checkConstructorException(new IllegalArgumentException("Invalid hour: 50"), 
        "Time", 50, 20, 23) 
        && t.checkConstructorException(new IllegalArgumentException("Invalid minute: 88"), 
            "Time", 12,88,20) 
        && t.checkConstructorException(new IllegalArgumentException("Invalid second: 65"), 
            "Time", 12, 20, 65);
  }

  boolean testSameTime(Tester t) {
    return t.checkExpect(this.time1.sameTime(time1Same), true)
        && t.checkExpect(this.time2.sameTime(time3), false) 
        && t.checkExpect(this.time4.sameTime(time4Same), true) 
        && t.checkExpect(this.time4.sameTime(time5), false) 
        && t.checkExpect(this.time6.sameTime(time6Same), true);

  }
}