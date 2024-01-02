package org.swainston.tests.UpperCaseChallenge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class UpperCaseChallengeImplTest {

  private UpperCaseChallenge upperCaseChallenge = new UpperCaseChallengeImpl();

  /**
   * Allows other classes to parse the upperCaseChallenge to this class
   *
   * @param upperCaseChallenge the student challenge object
   */
  public void setUpperCaseChallenge(UpperCaseChallenge upperCaseChallenge) {
    this.upperCaseChallenge = upperCaseChallenge;
  }

  /**
   * Tests to see whether a string with random upper case chars is converted to lower case chars
   */
  @Test
  public void upperCaseConverter_all_chars() {
    String test = "AbcDeFghIjKlmNopQrsTuVwXYZ";
    String expected = "abcdefghijklmnopqrstuvwxyz";
    String actual = upperCaseChallenge.upperCaseConverter(test);
    Assertions.assertEquals(expected, actual);
  }

  /**
   * Tests whether given numbers, it does not alter the string at all
   */
  @Test
  public void upperCaseConverter_all_num() {
    String test = "23748923749";
    String expected = "23748923749";
    String actual = upperCaseChallenge.upperCaseConverter(test);
    Assertions.assertEquals(expected, actual);
  }

  /**
   * Tests to see whether given a null value, the code throws a NullPointerException
   */
  @Test
  public void upperCaseConverter_null() {
    Executable executable = () -> upperCaseChallenge.upperCaseConverter(null);
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  /**
   * Tests whether to see a string of spaces is kept the same and not changed
   */
  @Test
  public void upperCaseConverter_all_spaces() {
    String test = "    ";
    String expected = "    ";
    String actual = upperCaseChallenge.upperCaseConverter(test);
    Assertions.assertEquals(expected, actual);
  }

  /**
   * Tests whether given an empty string, it returns an empty string again
   */
  @Test
  public void upperCaseConverter_empty_string() {
    String test = "";
    String expected = "";
    String actual = upperCaseChallenge.upperCaseConverter(test);
    Assertions.assertEquals(expected, actual);
  }
}