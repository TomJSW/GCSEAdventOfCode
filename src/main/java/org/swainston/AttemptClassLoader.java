package org.swainston;

/**
 * Class which assists the loading of students code without the need of
 * creating a new file in the project filestream
 */
public class AttemptClassLoader extends ClassLoader {

  private final byte[] bytes;

  /**
   * Takes the bytes from the students attempt and sets them to the final
   * variable above
   * @param bytes the students attempt
   */
  public AttemptClassLoader(byte[] bytes) {
    this.bytes = bytes;
  }

  /**
   * This finds a class from the binary name of the class
   * @param name
   *          The <a href="#binary-name">binary name</a> of the class
   *
   * @return an unspecified type of class
   */
  @Override
  protected Class<?> findClass(String name) {
    return defineClass(name, bytes, 0, bytes.length);
  }
}