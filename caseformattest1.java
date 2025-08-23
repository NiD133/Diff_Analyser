package com.google.common.base;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import junit.framework.TestCase;

/**
 * Tests for edge cases in {@link CaseFormat}, such as identity, empty, and whitespace-only
 * conversions.
 */
public class CaseFormatTestTest1 extends TestCase {

  /**
   * Tests that converting a string to its own format is a no-op and returns the same
   * instance, as an optimization.
   */
  public void testIdentityConversion_returnsSameInstance() {
    for (CaseFormat format : CaseFormat.values()) {
      assertWithMessage("Converting from %s to itself should be a no-op", format)
          .that(format.to(format, "foo"))
          .isSameInstanceAs("foo");
    }
  }

  /**
   * Tests that converting an empty string between any two formats always results in an empty
   * string.
   */
  public void testConversion_withEmptyString_returnsEmptyString() {
    for (CaseFormat from : CaseFormat.values()) {
      for (CaseFormat to : CaseFormat.values()) {
        assertWithMessage("Converting an empty string from %s to %s", from, to)
            .that(from.to(to, ""))
            .isEmpty();
      }
    }
  }

  /**
   * Tests that converting a string containing only a single space between any two formats returns
   * the original single-space string.
   */
  public void testConversion_withSingleSpaceString_returnsItself() {
    for (CaseFormat from : CaseFormat.values()) {
      for (CaseFormat to : CaseFormat.values()) {
        assertWithMessage("Converting a single space from %s to %s", from, to)
            .that(from.to(to, " "))
            .isEqualTo(" ");
      }
    }
  }
}