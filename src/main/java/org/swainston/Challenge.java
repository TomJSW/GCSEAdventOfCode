package org.swainston;

/**
 * Class that holds information surrounding a challenge
 */
public class Challenge {

  private Integer id;
  private String title;
  private String description;
  private String solutionTemplate;

  /**
   *
   */
  public Challenge() {
  }

  /**
   * Fetches solution template for a challenge
   *
   * @return set template for the solution defined in the database
   */
  public String getSolutionTemplate() {
    return solutionTemplate;
  }

  /**
   * Sets the template for a certain solution
   *
   * @param solutionTemplate the template for the solution
   */
  public void setSolutionTemplate(String solutionTemplate) {
    this.solutionTemplate = solutionTemplate;
  }

  /**
   * @return id of a challenge
   */
  public Integer getId() {
    return id;
  }

  /**
   * Sets the id of a challenge
   *
   * @param id the challenge id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return title of the challenge
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of a challenge
   *
   * @param title the new title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the description of a challenge
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of a challenge
   *
   * @param description the new description
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
