package org.swainston;

import java.io.Serializable;

public class Attempt implements Serializable {

  private int id;
  private String email;
  private String attempt;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAttempt() {
    return attempt;
  }

  public void setAttempt(String attempt) {
    this.attempt = attempt;
  }
}
