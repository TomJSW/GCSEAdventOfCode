package org.swainston.ui;

import java.util.Optional;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.opentest4j.AssertionFailedError;
import org.swainston.*;
import org.swainston.database.MySQLAttemptsStore;
import org.swainston.database.MySQLChallengeStore;

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

  private void layout() {
    String user = getMySession().getUser();
    Optional<Challenge> nextChallenge = challengeStore.getNextChallenge(user);

    if (nextChallenge.isEmpty()) {
      // this should never happen
      throw new AssertionFailedError("Unable to find next challenge for user.");
    }
    Challenge challenge = nextChallenge.get();
    Optional<Attempt> attempt = attemptsStore.getAttempt(user, challenge.getId());
    if (attempt.isEmpty()) {
      MySQLAttemptsStore.loadDefault(user, challenge);
    }
    add(new Label("title", new PropertyModel(challenge, "title")));
    add(new Label("instructions", new PropertyModel(challenge, "description")));
    add(new Link<String>("go-home", (IModel) () -> "foo") {
      @Override
      public void onClick() {
        setResponsePage(HomePage.class);
      }
    });
    add(new Link<String>("sign-out", (IModel) () -> "foo") {
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
