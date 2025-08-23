package com.google.common.util.concurrent;

import com.google.common.testing.NullPointerTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Nullness tests for {@link ExecutionList}.
 */
@RunWith(JUnit4.class)
public class ExecutionListNullnessTest {

  @Test
  public void publicMethods_rejectNullArguments() {
    // NullPointerTester from Guava's testing library automatically verifies that
    // all public methods of a class throw NullPointerException when passed a null
    // argument for any parameter that is not explicitly marked as @Nullable.
    new NullPointerTester().testAllPublicInstanceMethods(new ExecutionList());
  }
}