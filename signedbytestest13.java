package com.google.common.primitives;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for null-handling in {@link SignedBytes}.
 */
@RunWith(JUnit4.class)
public class SignedBytesNullsTest {

  @Test
  @J2ktIncompatible // NullPointerTester
  @GwtIncompatible
  public void publicStaticMethods_rejectNullParameters() {
    // NullPointerTester checks that all public static methods of a class throw
    // NullPointerException when passed null for any non-@Nullable parameter.
    new NullPointerTester().testAllPublicStaticMethods(SignedBytes.class);
  }
}