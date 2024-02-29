package org.swainston;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.swainston.tests.UpperCaseChallenge.UpperCaseChallenge;
import org.swainston.tests.UpperCaseChallenge.UpperCaseChallengeImplTest;
import org.swainston.ui.challengepage.ChallengePage;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

/**
 * Students' code is compiled and tests called on it within this class
 */
public class ChallengePageForm extends Form<Attempt> {

  private final Challenge challenge;
  private final String user;
  private final AttemptsStore attemptsStore;

  /**
   * ChallengePageForm object
   *
   * @param user          the student
   * @param challenge     the challenge the student is attempting
   * @param attemptsStore the attempt store that holds the student's attempts
   */
  public ChallengePageForm(String user, Challenge challenge, AttemptsStore attemptsStore) {
    super("form-submit-code");

    this.challenge = challenge;
    this.user = user;
    this.attemptsStore = attemptsStore;

    Attempt modelObject = attemptsStore.getAttempt(user, challenge.getId()).get();
    setModel(new Model<>(modelObject));
    add(new TextArea<>("textarea-code",
        new PropertyModel<Attempt>(modelObject, "attempt")));
  }

  /**
   * The function that is run when the student presses the submit button
   */
  @Override
  protected void onSubmit() {

    TextArea<Attempt> textArea = (TextArea<Attempt>) get("textarea-code");
    IModel<Attempt> textAreaModel = textArea.getModel();

    Object attemptObject = textAreaModel.getObject();
    String studentCode = (String) attemptObject;

    String codeImplementingTest = studentCode.replace("class Challenge8",
        "class Challenge8 implements " +
            "org.swainston.tests.UpperCaseChallenge.UpperCaseChallenge");
    Attempt attempt = new Attempt();
    attempt.setAttempt(codeImplementingTest);

    System.out.println("[" + codeImplementingTest + "]");

    final Compiler.Result result = Compiler.checkCompiles(attempt);

    if (result.isCompiles()) {
      var byteArray = result.getByteArrayOutputStream().toByteArray();


      AttemptClassLoader attemptClassLoader = new AttemptClassLoader(byteArray);
      try {
        String source_name = "org.swainston.Challenge8";
        Class<?> aClass = attemptClassLoader.findClass(source_name);
        Object object = aClass.getDeclaredConstructor().newInstance();

        UpperCaseChallenge upperCaseChallenge = UpperCaseChallenge.class.cast(object);
        UpperCaseChallengeImplTest upperCaseChallengeImplTest = new UpperCaseChallengeImplTest();
        upperCaseChallengeImplTest.setUpperCaseChallenge(upperCaseChallenge);

        try {
          upperCaseChallengeImplTest.upperCaseConverter_all_num();
          upperCaseChallengeImplTest.upperCaseConverter_all_chars();
          upperCaseChallengeImplTest.upperCaseConverter_all_spaces();
          upperCaseChallengeImplTest.upperCaseConverter_empty_string();
          upperCaseChallengeImplTest.upperCaseConverter_null();
        } catch (Error e) {
          result.setErrors(Collections.singletonList(e.toString()));
        }

      } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
               InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    // Store the original code that the student submitted, not the code modified to implement the
    // test methods.
    result.setSubmission(studentCode);
    attemptsStore.setAttempt(user, challenge.getId(), result.toString());

    setResponsePage(ChallengePage.class);
  }
}
