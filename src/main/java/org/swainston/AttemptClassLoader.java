package org.swainston;

public class AttemptClassLoader extends ClassLoader {
  byte[] bytes;

  public AttemptClassLoader(byte[] bytes) {
    this.bytes = bytes;
  }

  @Override
  protected Class<?> findClass(String name) {
    return defineClass(name, bytes, 0, bytes.length);
  }
}

