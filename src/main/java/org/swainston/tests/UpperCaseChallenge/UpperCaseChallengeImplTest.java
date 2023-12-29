package org.swainston.tests.UpperCaseChallenge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class UpperCaseChallengeImplTest {

  private UpperCaseChallenge upperCaseChallenge = new UpperCaseChallengeImpl();

  public void setUpperCaseChallenge(UpperCaseChallenge upperCaseChallenge) {
    this.upperCaseChallenge = upperCaseChallenge;
  }

  @Test
  public void upperCaseConverter_all_chars() {
    String test = "AbcDeFghIjKlmNopQrsTuVwXYZ";
    String expected = "abcdefghijklmnopqrstuvwxyz";
    String actual = upperCaseChallenge.upperCaseConverter(test);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void upperCaseConverter_all_num() {
    String test = "23748923749";
    String expected = "23748923749";
    String actual = upperCaseChallenge.upperCaseConverter(test);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void upperCaseConverter_null() {
    Executable executable = () -> upperCaseChallenge.upperCaseConverter(null);
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  public void upperCaseConverter_all_spaces() {
    String test = "    ";
    String expected = "    ";
    String actual = upperCaseChallenge.upperCaseConverter(test);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void upperCaseConverter_empty_string() {
    String test = "";
    String expected = "";
    String actual = upperCaseChallenge.upperCaseConverter(test);
    Assertions.assertEquals(expected, actual);
  }
}