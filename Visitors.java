import tester.Tester;

interface IArithVisitor<R> {
  R visitConst(Const c);
  
  R visitFormula(Formula f); 
}

interface IFunction<A, R> {
  R apply(A t);
}

// evaluates IArith to a Double answer
class EvalVisitor implements IArithVisitor<Double>, IFunction<IArith, Double> {
  public Double apply(IArith a) {
    return a.accept(this);
  }
  
  public Double visitConst(Const c) {
    return c.num;
  }

  public Double visitFormula(Formula f) {
    return f.fun.apply(f.left.accept(this), f.right.accept(this));
  }
}

// show the fully-parenthesized expression in Racket-like prefix notation
class PrintVisitor implements IArithVisitor<String>, IFunction<IArith, String> {
  public String apply(IArith a) {
    return a.accept(this);
  }
  
  
  public String visitConst(Const c) {
    return Double.toString(c.num);
  }

  public String visitFormula(Formula f) {
    return "(" + f.name + " " + f.left.accept(this) + " " + f.right.accept(this) + ")";
  }
}


// produces another IArith, where every Const has been doubled
class DoublerVisitor implements IArithVisitor<IArith>, IFunction<IArith, IArith> {
  public IArith apply(IArith a) {
    return a.accept(this);
  }
  
  public IArith visitConst(Const c) {
    return new Const(c.num * 2);
  }

  public IArith visitFormula(Formula f) {
    return new Formula(f.fun, f.name, f.left.accept(this), f.right.accept(this));
  }
}

// determine whether if every constant in the IArith is less than 10
class AllSmallVisitor implements IArithVisitor<Boolean>, IFunction<IArith, Boolean> {
  public Boolean apply(IArith a) {
    return a.accept(this);
  }
  
  public Boolean visitConst(Const c) {
    return c.num < 10;
  }

  public Boolean visitFormula(Formula f) {
    return f.left.accept(this) 
        && f.right.accept(this);
  }

}

interface IFunc2<A1, A2, R> {
  // apply this function to the given inputs
  R apply(A1 a1, A2 a2);
}

// divide two constant
class Div implements IFunc2<Double, Double, Double> {
  public Double apply(Double a1, Double a2) {
    return a1 / a2;
  } 
}

// plus two constant
class Plus implements IFunc2<Double, Double, Double> {
  public Double apply(Double a1, Double a2) {
    return a1 + a2;
  } 
}



interface IArith {
  <R> R accept(IArithVisitor<R> visitor);

}

class Const implements IArith {
  double num;

  Const(double num) {
    this.num = num;
  }

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

class Formula implements IArith {
  IFunc2<Double, Double, Double> fun;
  String name;
  IArith left;
  IArith right;

  Formula(IFunc2<Double, Double, Double> fun, String name, IArith left, IArith right) {
    this.fun = fun;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitFormula(this);
  }
}

class ExamplesVisitor {
  ExamplesVisitor(){}

  IArith c1 = new Const(1.0);
  IArith c2 = new Const(2.0);
  IArith c3 = new Const(1.5);
  IArith c4 = new Const(10.5);


  IArith f1 = new Formula(new Plus(), "plus", this.c1, this.c2);
  IArith f2 = new Formula(new Div(), "div", this.f1, this.c3);
  IArith f3 = new Formula(new Div(), "div", this.c1, this.c4);


  Boolean testPrintVisitor(Tester t) {
    return t.checkExpect(this.f2.accept(new PrintVisitor()), "(div (plus 1.0 2.0) 1.5)")
        && t.checkExpect(this.f1.accept(new PrintVisitor()), "(plus 1.0 2.0)")
        && t.checkExpect(this.c1.accept(new PrintVisitor()), "1.0");
  }

  Boolean testDoublerVisitor(Tester t) {
    return t.checkExpect(this.f1.accept(new DoublerVisitor()), 
        new Formula(new Plus(), "plus", new Const(2.0), new Const(4.0)))
        && t.checkExpect(this.c3.accept(new DoublerVisitor()), new Const(3.0));
  }

  Boolean testAllSmallVisitor(Tester t) {
    return t.checkExpect(this.f1.accept(new AllSmallVisitor()), true)
        && t.checkExpect(this.c1.accept(new AllSmallVisitor()), true)
        && t.checkExpect(this.c4.accept(new AllSmallVisitor()), false)
        && t.checkExpect(this.f3.accept(new AllSmallVisitor()), false);
  }
  
  Boolean testEvalVisitor(Tester t) {
    return t.checkExpect(this.c1.accept(new EvalVisitor()), 1.0)
        && t.checkExpect(this.f2.accept(new EvalVisitor()), 2.0)
        && t.checkExpect(this.f1.accept(new EvalVisitor()), 3.0);
  
  }
  
  Boolean testdiv(Tester t) {
    return t.checkExpect(new Div().apply(1.1, 1.0), 1.1)
        && t.checkExpect(new Div().apply(12.0, 6.0), 2.0);
  }
  
  Boolean testplus(Tester t) {
    return t.checkExpect(new Plus().apply(1.1, 1.0), 2.1)
        && t.checkExpect(new Plus().apply(12.0, 6.0), 18.0);
  }

}



