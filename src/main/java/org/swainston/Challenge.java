package org.swainston;

public class Challenge {

  private Integer id;
  private String title;
  private String description;
  private String solutionTemplate;

  public String getSolutionTemplate() {
    return solutionTemplate;
  }

  public void setSolutionTemplate(String solutionTemplate) {
    this.solutionTemplate = solutionTemplate;
  }

  public Challenge() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
