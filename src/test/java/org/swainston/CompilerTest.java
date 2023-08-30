package org.swainston;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {

  private Compiler.Result result;

  @BeforeEach
  void setUp() {
    result = new Compiler.Result("foo");
  }

  @AfterEach
  void tearDown() {
  }

//  @Test
//  void checkCompiles() {
//    assertTrue(false);
//  }

  @Test
  void testGetErrors() {
    List<String> errors = new ArrayList<>();
    errors.add("foo");
    errors.add("bar");
    errors.add("aaa");
    result.setErrors(errors);

    assertEquals(errors, result.getErrors());
  }

  @Test
  void testGetErrors_unset() {
    List<String> errors = new ArrayList<>();
//    errors.add("foo");
//    errors.add("bar");
//    errors.add("aaa");
//    result.setErrors(errors);

    assertNull(result.getErrors());
  }
}