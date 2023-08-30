package org.swainston;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.swainston.test.UpperCaseChallengeImplTest;
import org.swainston.ui.ChallengePage;

import java.util.Collections;

public class ChallengePageForm extends Form<Attempt> {

  Challenge challenge;
  String user;
  AttemptsStore attemptsStore;

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


  @Override
  protected void onSubmit() {
    TextArea<Attempt> textArea = (TextArea<Attempt>) get("textarea-code");
    IModel<Attempt> pm = textArea.getModel();

    Object attemptObject = pm.getObject();
    String code = (String) attemptObject;
    Attempt attempt = new Attempt();
    attempt.setAttempt(code);

    Compiler.Result result = Compiler.checkCompiles(attempt);
    if (result.isCompiles()) {
      var bytes = result.getByteArrayOutputStream().toByteArray();
      AttemptClassLoader attemptClassLoader = new AttemptClassLoader(bytes);
      try {
        Class<?> aClass = attemptClassLoader.findClass("org.swainston.tom");
        Object object = aClass.newInstance();
        System.out.println(object);

        UpperCaseChallenge ifc = UpperCaseChallenge.class.cast(object);

        UpperCaseChallengeImplTest upperCaseChallengeImplTest = new UpperCaseChallengeImplTest();

        upperCaseChallengeImplTest.setUpperCaseChallenge(ifc);

        try {
          upperCaseChallengeImplTest.upperCaseConverter_all_num();
          upperCaseChallengeImplTest.upperCaseConverter_all_chars();
          upperCaseChallengeImplTest.upperCaseConverter_all_spaces();
          upperCaseChallengeImplTest.upperCaseConverter_empty_string();
          upperCaseChallengeImplTest.upperCaseConverter_null();
        } catch (Error e) {
          result.setErrors(Collections.singletonList(e.toString()));
        }

      } catch (InstantiationException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    attemptsStore.setAttempt(user, challenge.getId(), result.toString());

    setResponsePage(ChallengePage.class);
  }
}
