import java.util.*;
import tester.Tester;
/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */

class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet = 
      new ArrayList<Character>(Arrays.asList(
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
          'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
          't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random();

  // Create a new instance of the encoder/decoder with a new permutation code 
  PermutationCode() {
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code 
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  //Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> copy = new ArrayList<Character>(Arrays.asList(
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 
        'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
    return this.initEncoderHelper(new ArrayList<Character>(26), copy);
  }


  // adds a random character to the encoder list from
  // the random permutation until the permutation is empty;
  ArrayList<Character> initEncoderHelper(ArrayList<Character> init, ArrayList<Character> source) {
    if (source.size() == 0) {
      return this.code;
    } else {
      int i = this.rand.nextInt(source.size());
      this.code.add((source.get(i)));
      source.remove(i);
      return this.initEncoderHelper(code, source);
    }
  }


  // produce an encoded String from the given String
  String encode(String source) {
    return this.encodeHelper(source, ""); //you should complete this method
  }

  //adds the encoded version of each character in the source string to the encode
  // string;
  String encodeHelper(String source, String answer) {
    if (source.equals("")) {
      return answer;
    } else {
      Character first = source.charAt(0);
      String rest = source.substring(1);
      answer = answer.concat(this.code.get(this.alphabet.indexOf(first)).toString());
      return this.encodeHelper(rest, answer);
    }
  }

  // produce a decoded String from the given String
  String decode(String code) {
    return this.decodeHelper(code, ""); //you should complete this method
  }

  //adds the decoded version of each character in the code string to the answer string;
  String decodeHelper(String code, String answer) {
    if (code.equals("")) {
      return answer;
    } else {
      Character first = code.charAt(0);
      String rest = code.substring(1);
      answer = answer.concat(this.alphabet.get(this.code.indexOf(first)).toString());
      return this.decodeHelper(rest, answer);
    } 
  }

}


class ExamplesPermutationCode {
  ExamplesPermutationCode() {
  }

  ArrayList<Character> code1 = new ArrayList<Character>(Arrays.asList(
      'e', 'd', 'c', 'b', 'a', 'j', 'i', 'h', 'g', 'f',
      'o', 'n', 'm', 'l', 'k', 's', 'r', 'q', 'p', 'w', 
      'v', 'u', 't', 'z', 'y', 'x'));
  ArrayList<Character> code2 = new ArrayList<Character>(Arrays.asList(
      'e', 'b', 'a', 'j', 'i', 'h', 'g', 'f',
      'o', 'n', 'm', 'l', 'k', 'q', 'p', 'w', 
      'v', 'u', 't', 'z', 'x'));
  ArrayList<Character> code3 = new ArrayList<Character>(Arrays.asList(
      'a', 'j', 'i', 'h', 'g', 'f',
      'o', 'n', 'l', 'k', 's', 'q', 'p', 'w'));
  ArrayList<Character> code4 = new ArrayList<Character>();

  String decoded1 = "abc";
  String encoded1 = "edc";
  String decoded2 = "xyz";
  String encoded2 = "zyx";

  PermutationCode p1 = new PermutationCode();
  PermutationCode p2 = new PermutationCode(this.code1);
  PermutationCode p3 = new PermutationCode(this.code2);


  boolean testdecode(Tester t) {
    return t.checkExpect(this.p2.decode(this.encoded1), this.decoded1)
        && t.checkExpect(this.p2.decode(this.encoded1), this.decoded1)
        && t.checkExpect(this.p2.decode(""), "");
  }

  boolean testdecodeHelper(Tester t) {
    return t.checkExpect(this.p2.decodeHelper(this.decoded1, "bcd"), "bcdedc")
        && t.checkExpect(this.p2.decodeHelper("", "abd"), "abd");
  }

  boolean testencode(Tester t) {
    return t.checkExpect(this.p2.encode(this.decoded1), this.encoded1)
        && t.checkExpect(this.p2.encode(this.decoded2), this.encoded2)
        && t.checkExpect(this.p2.encode(""), "");
  }

  boolean testencodeHelper(Tester t) {
    return t.checkExpect(this.p2.encodeHelper(this.encoded1, "bcd"), "bcdabc")
        && t.checkExpect(this.p2.encodeHelper("", "abd"), "abd");
  }

  boolean testinitEncoder(Tester t) {
    return t.checkExpect(this.p2.initEncoder().size(), 52)
        && t.checkExpect(this.p1.initEncoder().size(), 52)
        && t.checkExpect(this.p3.initEncoder().size(), 47)
        && t.checkExpect(this.p1.initEncoder().size(), 78);
  }

  boolean testinitEncoderHelper(Tester t) {
    return t.checkExpect(this.p2.initEncoderHelper(this.code1, this.code4).size(), 52)
        && t.checkExpect(this.p2.initEncoderHelper(this.code1, this.code2).size(), 99);
  }



}
