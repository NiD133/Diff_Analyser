package com.google.common.reflect;

import static org.junit.Assume.assumeTrue;

import com.google.common.testing.NullPointerTester;
import java.lang.reflect.Method;
import org.junit.Test;

/**
 * Tests for the null-related contracts of the {@link Parameter} class's public methods.
 */
public class ParameterTestTest1 {

  // A helper method to obtain a Parameter instance for testing via reflection.
  @SuppressWarnings("unused")
  private void methodWithParameters(int anInt, String aString) {}

  /**
   * Checks if {@code java.lang.reflect.AnnotatedType} is available in the current runtime.
   *
   * @return {@code true} if the class is available, {@code false} otherwise.
   */
  private static boolean isAnnotatedTypeAvailable() {
    try {
      Class.forName("java.lang.reflect.AnnotatedType");
      return true;
    } catch (ClassNotFoundException e) {
      // This class is not available on older JDKs or Android.
      return false;
    }
  }

  @Test
  public void testPublicMethods_nullPointerContracts() throws Exception {
    /*
     * On platforms where AnnotatedType is not available (like Android), loading Parameter.class
     * fails with a NoClassDefFoundError because its public method getAnnotatedType() refers to the
     * missing class. NullPointerTester would then crash when it calls getDeclaredMethods() on
     * Parameter.class. We skip this test on such platforms.
     */
    assumeTrue(
        "Skipping test: java.lang.reflect.AnnotatedType is not available on this platform.",
        isAnnotatedTypeAvailable());

    // Get a representative Parameter instance to test. Any instance will do.
    Method method =
        ParameterTestTest1.class.getDeclaredMethod("methodWithParameters", int.class, String.class);
    Parameter representativeParameter = Invokable.from(method).getParameters().get(0);

    // Verify that all public methods of Parameter properly handle null inputs.
    new NullPointerTester().testAllPublicInstanceMethods(representativeParameter);
  }
}