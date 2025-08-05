package com.google.common.reflect;

import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit tests for the {@link Parameter} class.
 * This test suite verifies the behavior of the Parameter class, including its handling of nulls and equality.
 * 
 * <p>Note: This test suite is not compatible with Android VMs due to the absence of AnnotatedType.
 * 
 * <p>Author: Ben Yu
 */
@NullUnmarked
public class ParameterTest extends TestCase {

  /**
   * Tests that all public instance methods of the Parameter class handle null inputs correctly.
   * This test is skipped on Android VMs due to the absence of AnnotatedType.
   */
  public void testNulls() {
    if (isRunningOnAndroidVm()) {
      return; // Skip test on Android VMs
    }

    NullPointerTester nullTester = new NullPointerTester();
    for (Method method : ParameterTest.class.getDeclaredMethods()) {
      for (Parameter param : Invokable.from(method).getParameters()) {
        nullTester.testAllPublicInstanceMethods(param);
      }
    }
  }

  /**
   * Tests the equality behavior of Parameter instances.
   * Ensures that parameters are correctly grouped by equality.
   */
  public void testEquals() {
    EqualsTester equalsTester = new EqualsTester();
    for (Method method : ParameterTest.class.getDeclaredMethods()) {
      for (Parameter param : Invokable.from(method).getParameters()) {
        equalsTester.addEqualityGroup(param);
      }
    }
    equalsTester.testEquals();
  }

  /**
   * Helper method to determine if the current environment is an Android VM.
   * 
   * @return true if running on an Android VM, false otherwise.
   */
  private boolean isRunningOnAndroidVm() {
    try {
      Class.forName("java.lang.reflect.AnnotatedType");
      return false;
    } catch (ClassNotFoundException e) {
      return true;
    }
  }

  // Sample methods to provide parameters for testing
  @SuppressWarnings("unused")
  private void someMethod(int i, int j) {}

  @SuppressWarnings("unused")
  private void anotherMethod(int i, String s) {}
}