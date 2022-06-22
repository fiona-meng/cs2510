import tester.Tester;

interface IVehicle {
  // computes the total revenue of this Vehicle
  double totalRevenue();

  // computes the cost of fully fueling this Vehicle
  double fuelCost();

  // computes the per-passenger profit of this Vehicle
  double perPassengerProfit();

  // produce a String that shows the name and passenger capacity of this Vehicle
  String format();

  // is this IVehicle the same as that one?
  boolean sameVehicle(IVehicle that);

  //is this Train the same as that one?
  boolean sameTrain(Train other);

  //is this Bus the same as that one?
  boolean sameBus(Bus other);

  //is this Airplane the same as that one?
  boolean sameAirplane(Airplane other);
}

abstract class AVehicle implements IVehicle {
  String name;
  int passengerCapacity; // per passenger fare
  double fare;
  int fuelCapacity; 

  AVehicle(String name, int passengerCapacity, double fare, int fuelCapacity) {
    this.name = name;
    this.passengerCapacity = passengerCapacity;
    this.fare = fare;
    this.fuelCapacity = fuelCapacity;

    /* Template:
     * Fields:
     * ... this.name ...                   - String
     * ... this.passengerCapacity ...      - int
     * ... this.fare ...                   - double
     * ... this.fuelCapacity ...           - int
     * Methods:
     * ... totalRevenue() ...              - double
     * ... fuelCost() ...                  - double
     * ... perPassengerProfit() ...        - double
     * ... format() ...                    - String
     * ... sameVehicle(IVehicle) ...       - boolean
     * ... sameBus(Bus) ...                - boolean
     * ... sameTrain(Train) ...            - boolean
     * ... sameAirplane(Airplane) ...      - boolean
     */
  }

  // computes the total revenue of operating this Airplane
  public double totalRevenue() {
    return this.passengerCapacity * this.fare;
  }

  // computes the cost of fully fueling this Airplane
  public double fuelCost() {
    return this.fuelCapacity * 2.55;
  }

  // computes the per-passenger profit of this Airplane
  public double perPassengerProfit() {
    return (this.totalRevenue() - this.fuelCost()) / this.passengerCapacity;
  }

  // produce a String that shows the name and passenger capacity of this
  // Airplane
  public String format() {
    return this.name + ", " + this.passengerCapacity + ".";
  }

  //is this Train the same as that one?
  public boolean sameTrain(Train other) {
    return false;
  }

  //is this Bus the same as that one?
  public boolean sameBus(Bus other) {
    return false;
  }

  //is this Airplane the same as that one?
  public boolean sameAirplane(Airplane other) {
    return false;
  }

}

class Airplane extends AVehicle {
  // gallons of fuel (kerosene @ 1.94/gallon)
  String code; // ICAO type designator
  boolean isWideBody; // twin-aisle aircraft

  Airplane(String name, int passengerCapacity, double fare, int fuelCapacity, String code,
      boolean isWideBody) {
    super(name, passengerCapacity, fare, fuelCapacity);
    this.code = code;
    this.isWideBody = isWideBody;
  }

  /* Template:
   * Fields:
   * ... this.name ...                   - String
   * ... this.passengerCapacity ...      - int
   * ... this.fare ...                   - double
   * ... this.fuelCapacity ...           - int
   * ... this.code ...                   - String
   * ... this.isWideBody ...             - boolean
   * Methods:
   * ... this.totalRevenue() ...         - double
   * ... this.fuelCost() ...             - double
   * ... this.perPassengerProfit() ...   - double
   * ... this.format() ...               - String
   * ... this.sameVehicle(IVehicle) ...  - boolean
   * ... this.sameBus(Bus) ...           - boolean
   * ... this.sameTrain(Train) ...       - boolean
   * ... this.sameAirplane(Airplane) ... - boolean
   * 
   * Methods on parameters:
   * ... that.sameAirplane(IVehicle) ... -boolean
   */

  // computes the cost of fully fueling this Airplane
  public double fuelCost() {
    return this.fuelCapacity * 1.94;
  }
  
  public boolean isAirplane() {
    return true;
  }

  // is this Airplane the same as that IVehicle?
  public boolean sameVehicle(IVehicle that) {
    return that.sameAirplane(this);
  }

  //is this Airplane the same as that Airplane?
  public boolean sameAirplane(Airplane otherAirplane) {
    return this.name == otherAirplane.name
        && this.passengerCapacity == otherAirplane.passengerCapacity
        && this.fare == otherAirplane.fare
        && this.fuelCapacity == otherAirplane.fuelCapacity
        && this.code == otherAirplane.code
        && this.isWideBody == otherAirplane.isWideBody;
  }
 
  
} 
  


class Train extends AVehicle {
  // gallons of fuel (diesel @ 2.55/gallon)
  int numberOfCars; // cars per trainset
  int gauge; // track gauge in millimeters

  Train(String name, int passengerCapacity, double fare, int fuelCapacity, int numberOfCars,
      int gauge) {
    super(name, passengerCapacity, fare, fuelCapacity);
    this.numberOfCars = numberOfCars;
    this.gauge = gauge;
  }

  /* Template:
   * Fields:
   * ... this.name ...                 - String
   * ... this.passengerCapacity ...    - int
   * ... this.fare ...                 - double
   * ... this.fuelCapacity ...         - int
   * ... this.numberOfCars ...         - int
   * ... this.guage ...                - int
   * Methods:
   * ... totalRevenue() ...            - double
   * ... fuelCost() ...                - double
   * ... perPassengerProfit() ...      - double
   * ... format() ...                  - String
   * ... sameVehicle(IVehicle) ...     - boolean
   * ... sameBus(Bus) ...              - boolean
   * ... sameTrain(Train) ...          - boolean
   * ... sameAirplane(Airplane) ...    - boolean
   * 
   * Methods on parameters:
   * ... that.sameTrain(IVehicle) ...  -boolean
   */

  // is this Train the same as that IVehicle?
  public boolean sameVehicle(IVehicle that) {
    return that.sameTrain(this);
  }

  //is this Train the same as that Train?
  public boolean sameTrain(Train otherTrain) {
    return this.name == otherTrain.name
        && this.passengerCapacity == otherTrain.passengerCapacity
        && this.fare == otherTrain.fare
        && this.fuelCapacity == otherTrain.fuelCapacity
        && this.numberOfCars == otherTrain.numberOfCars
        && this.gauge == otherTrain.gauge;
  }

}

class Bus extends AVehicle {
  // gallons of fuel (diesel @ 2.55/gallon)
  int length; // length in feet

  Bus(String name, int passengerCapacity, double fare, int fuelCapacity, int length) {
    super(name, passengerCapacity, fare, fuelCapacity);
    this.length = length;
  }

  /* Template:
   * Fields:
   * ... this.name ...                 - String
   * ... this.passengerCapacity ...    - int
   * ... this.fare ...                 - double
   * ... this.fuelCapacity ...         - int
   * ... this.length ...               - int
   * Methods:
   * ... totalRevenue() ...            - double
   * ... fuelCost() ...                - double
   * ... perPassengerProfit() ...      - double
   * ... format() ...                  - String
   * ... sameVehicle(IVehicle) ...     - boolean  
   * ... sameBus(Bus) ...              - boolean
   * ... sameTrain(Train) ...          - boolean
   * ... sameAirplane(Airplane) ...    - boolean
   * 
   * Methods on parameters:
   * ... that.sameBus(IVehicle) ...    -boolean
   */

  // is this Bus the same as that IVehicle?
  public boolean sameVehicle(IVehicle that) {
    return that.sameBus(this);
  }

  //is this Bus the same as that Bus?
  public boolean sameBus(Bus otherBus) {
    return this.name == otherBus.name
        && this.passengerCapacity == otherBus.passengerCapacity
        && this.fare == otherBus.fare
        && this.fuelCapacity == otherBus.fuelCapacity
        && this.length == otherBus.length;
  }

  
}

class ExamplesVehicle {
  IVehicle dreamliner = new Airplane("Boeing 787", 242, 835.0, 33340, "B788", false);
  IVehicle commuterRail = new Train("MPI HSP46", 500, 11.50, 2000, 6, 1435);
  IVehicle silverLine = new Bus("Neoplan AN460LF", 77, 1.70, 100, 60);
  IVehicle airplane = new Airplane("Airplane", 380, 1000.0, 50000, "M910", true);
  IVehicle train = new Train("Train", 300, 10.75, 4000, 12, 2870);
  IVehicle bus = new Bus("Bus", 50, 0.5, 70, 45);

  // testing total revenue method
  boolean testTotalRevenue(Tester t) {
    return t.checkInexact(this.dreamliner.totalRevenue(), 242 * 835.0, .0001)
        && t.checkInexact(this.commuterRail.totalRevenue(), 500 * 11.5, .0001)
        && t.checkInexact(this.silverLine.totalRevenue(), 77 * 1.7, 0.001)
        && t.checkInexact(this.airplane.totalRevenue(), 380 * 1000.0, 0.001)
        && t.checkInexact(this.train.totalRevenue(), 300 * 10.75, 0.001)
        && t.checkInexact(this.bus.totalRevenue(), 50 * 0.5, 0.001);
  }

  boolean testfuelCost(Tester t) {
    return t.checkInexact(this.dreamliner.fuelCost(), 33340 * 1.94, .0001)
        && t.checkInexact(this.commuterRail.fuelCost(), 2000 * 2.55, .0001)
        && t.checkInexact(this.silverLine.fuelCost(), 100 * 2.55, 0.001)
        && t.checkInexact(this.airplane.fuelCost(), 50000 * 1.94, 0.001)
        && t.checkInexact(this.train.fuelCost(), 4000 * 2.55, 0.001)
        && t.checkInexact(this.bus.fuelCost(), 70 * 2.55, 0.001);
  }

  boolean testperPassengerProfit(Tester t) {
    return t.checkInexact(this.dreamliner.perPassengerProfit(),
        (242 * 835.0 - 33340 * 1.94) / 242, .0001)
        && t.checkInexact(this.commuterRail.perPassengerProfit(),
            (500 * 11.5 - 2000 * 2.55) / 500, .0001)
        && t.checkInexact(this.silverLine.perPassengerProfit(),
            (77 * 1.7 - 100 * 2.55) / 77, 0.001)
        && t.checkInexact(this.airplane.perPassengerProfit(),
            (380 * 1000.0 - 50000 * 1.94) / 380, 0.001)
        && t.checkInexact(this.train.perPassengerProfit(),
            (300 * 10.75 - 4000 * 2.55) / 300, 0.001)
        && t.checkInexact(this.bus.perPassengerProfit(), (50 * 0.5 - 70 * 2.55) / 50, 0.001);
  }

  boolean testperformat(Tester t) {
    return t.checkExpect(this.dreamliner.format(), "Boeing 787, 242.")
        && t.checkExpect(this.commuterRail.format(), "MPI HSP46, 500.")
        && t.checkExpect(this.silverLine.format(), "Neoplan AN460LF, 77.")
        && t.checkExpect(this.airplane.format(), "Airplane, 380.")
        && t.checkExpect(this.train.format(), "Train, 300.")
        && t.checkExpect(this.bus.format(), "Bus, 50.");
  }

  boolean testsameVehicle(Tester t) {
    return t.checkExpect(this.dreamliner.sameVehicle(dreamliner), true)
        && t.checkExpect(this.dreamliner.sameVehicle(train), false)
        && t.checkExpect(this.dreamliner.sameVehicle(airplane), false)
        && t.checkExpect(this.commuterRail.sameVehicle(dreamliner), false)
        && t.checkExpect(this.commuterRail.sameVehicle(train), false)
        && t.checkExpect(this.silverLine.sameVehicle(silverLine), true)
        && t.checkExpect(this.silverLine.sameVehicle(bus), false)
        && t.checkExpect(this.airplane.sameVehicle(silverLine), false)
        && t.checkExpect(this.airplane.sameVehicle(airplane), true)
        && t.checkExpect(this.train.sameVehicle(train), true)
        && t.checkExpect(this.bus.sameVehicle(train), false);
  }
}