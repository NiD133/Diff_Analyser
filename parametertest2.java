package com.google.common.reflect;

import com.google.common.testing.EqualsTester;
import java.lang.reflect.Method;
import junit.framework.TestCase;

/**
 * Tests for {@link Parameter}'s {@code equals} and {@code hashCode} methods.
 */
public class ParameterTestTest2 extends TestCase {

  // Helper methods for the test to reflect upon.
  @SuppressWarnings("unused")
  private void someMethod(int firstParam, int secondParam) {}

  @SuppressWarnings("unused")
  private void anotherMethod(int firstParam, String secondParam) {}

  public void testEqualsAndHashCode() {
    // Arrange: Get Method objects for our helper methods.
    // Using getDeclaredMethod is robust and makes the test's dependencies explicit.
    Method someMethod = getMethod("someMethod", int.class, int.class);
    Method anotherMethod = getMethod("anotherMethod", int.class, String.class);

    // Act: Create Invokable objects from the methods.
    Invokable<?, ?> someMethodInvokable = Invokable.from(someMethod);
    Invokable<?, ?> anotherMethodInvokable = Invokable.from(anotherMethod);

    // Assert: Verify the equals and hashCode contract for Parameter.
    new EqualsTester()
        // Group 1: A parameter is equal to another instance representing the same parameter.
        // This is the first parameter of someMethod(int, int).
        .addEqualityGroup(
            someMethodInvokable.getParameters().get(0),
            Invokable.from(someMethod).getParameters().get(0))

        // Group 2: A different parameter from the same method is not equal.
        // This is the second parameter of someMethod(int, int).
        .addEqualityGroup(someMethodInvokable.getParameters().get(1))

        // Group 3: A parameter from a different method is not equal, even at the same position.
        // This is the first parameter of anotherMethod(int, String).
        .addEqualityGroup(anotherMethodInvokable.getParameters().get(0))
        .testEquals();
  }

  private static Method getMethod(String name, Class<?>... parameterTypes) {
    try {
      return ParameterTestTest2.class.getDeclaredMethod(name, parameterTypes);
    } catch (NoSuchMethodException e) {
      throw new AssertionError(e);
    }
  }
}