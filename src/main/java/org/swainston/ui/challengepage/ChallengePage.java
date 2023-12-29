package org.swainston.ui.challengepage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.opentest4j.AssertionFailedError;
import org.swainston.*;
import org.swainston.database.MySQLAttemptsStore;
import org.swainston.database.MySQLChallengeStore;
import org.swainston.ui.homepage.HomePage;

import java.util.Optional;

/**
 * This class handles the challenge page and the submitted in the challenge page.
 */
public class ChallengePage extends WebPage implements AuthenticatedWebPage {

  private static final long serialVersionUID = 1L;

  private final ChallengeStore challengeStore;
  private final AttemptsStore attemptsStore;

  /**
   * Constructs the challenge page and layout.
   */
  public ChallengePage() {
    challengeStore = new MySQLChallengeStore();
    attemptsStore = new MySQLAttemptsStore();
    layout();
  }

  /**
   * Holds all the code required to run the page,
   * process the students' code and return feedback to the student
   */
  private void layout() {
    String user = getMySession().getUser();

    // Optional variables are used to ensure that no null variables are able to leak into code
    Optional<Challenge> nextChallenge = challengeStore.getNextChallenge(user);

    // Checks that the optional is not empty
    if (nextChallenge.isEmpty()) {
      // Theoretically unreachable
      throw new AssertionFailedError("Unable to find next challenge for user.");
    }

    // Get the challenge on the basis that it is not empty
    Challenge challenge = nextChallenge.get();
    Optional<Attempt> attempt = attemptsStore.getAttempt(user, challenge.getId());
    if (attempt.isEmpty()) {
      MySQLAttemptsStore.loadDefault(user, challenge);
    }

    add(new Label("title", new PropertyModel<>(challenge, "title")));
    add(new Label("instructions", new PropertyModel<>(challenge, "description")));
    add(new Link<Void>("go-home") {
      @Override
      public void onClick() {
        setResponsePage(HomePage.class);
      }
    });
    add(new Link<Void>("sign-out") {
      @Override
      public void onClick() {
        getMySession().signOut();
        setResponsePage(HomePage.class);
      }
    });

    add(new ChallengePageForm(user, challenge, attemptsStore));
  }


  private SignInSession getMySession() {
    return (SignInSession) getSession();
  }

}
