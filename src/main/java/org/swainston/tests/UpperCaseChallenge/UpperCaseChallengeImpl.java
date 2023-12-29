package org.swainston.tests.UpperCaseChallenge;

public class UpperCaseChallengeImpl implements UpperCaseChallenge {
  @Override
  public String upperCaseConverter(String text) {
    return text.toLowerCase();
  }
}
