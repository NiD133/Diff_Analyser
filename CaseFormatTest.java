package com.google.common.base;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import com.google.common.testing.SerializableTester;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit test for {@link CaseFormat}.
 * Tests conversion between different case formats.
 */
@GwtCompatible(emulated = true)
@NullUnmarked
public class CaseFormatTest extends TestCase {

  /**
   * Tests that converting a string to the same format returns the original string.
   */
  public void testIdentity() {
    for (CaseFormat from : CaseFormat.values()) {
      assertWithMessage("%s to %s", from, from)
          .that(from.to(from, "foo"))
          .isSameInstanceAs("foo");
      for (CaseFormat to : CaseFormat.values()) {
        assertWithMessage("%s to %s", from, to)
            .that(from.to(to, ""))
            .isEmpty();
        assertWithMessage("%s to %s", from, to)
            .that(from.to(to, " "))
            .isEqualTo(" ");
      }
    }
  }

  /**
   * Tests that null arguments are handled correctly.
   */
  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNullArguments() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(CaseFormat.class);
    for (CaseFormat format : CaseFormat.values()) {
      tester.testAllPublicInstanceMethods(format);
    }
  }

  // Section: Lower Hyphen Tests
  public void testLowerHyphenConversions() {
    assertConversion(LOWER_HYPHEN, LOWER_HYPHEN, "foo", "foo");
    assertConversion(LOWER_HYPHEN, LOWER_HYPHEN, "foo-bar", "foo-bar");
    assertConversion(LOWER_HYPHEN, LOWER_UNDERSCORE, "foo-bar", "foo_bar");
    assertConversion(LOWER_HYPHEN, LOWER_CAMEL, "foo-bar", "fooBar");
    assertConversion(LOWER_HYPHEN, UPPER_CAMEL, "foo-bar", "FooBar");
    assertConversion(LOWER_HYPHEN, UPPER_UNDERSCORE, "foo-bar", "FOO_BAR");
  }

  // Section: Lower Underscore Tests
  public void testLowerUnderscoreConversions() {
    assertConversion(LOWER_UNDERSCORE, LOWER_HYPHEN, "foo_bar", "foo-bar");
    assertConversion(LOWER_UNDERSCORE, LOWER_UNDERSCORE, "foo_bar", "foo_bar");
    assertConversion(LOWER_UNDERSCORE, LOWER_CAMEL, "foo_bar", "fooBar");
    assertConversion(LOWER_UNDERSCORE, UPPER_CAMEL, "foo_bar", "FooBar");
    assertConversion(LOWER_UNDERSCORE, UPPER_UNDERSCORE, "foo_bar", "FOO_BAR");
  }

  // Section: Lower Camel Tests
  public void testLowerCamelConversions() {
    assertConversion(LOWER_CAMEL, LOWER_HYPHEN, "fooBar", "foo-bar");
    assertConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "fooBar", "foo_bar");
    assertConversion(LOWER_CAMEL, LOWER_CAMEL, "fooBar", "fooBar");
    assertConversion(LOWER_CAMEL, UPPER_CAMEL, "fooBar", "FooBar");
    assertConversion(LOWER_CAMEL, UPPER_UNDERSCORE, "fooBar", "FOO_BAR");
  }

  // Section: Upper Camel Tests
  public void testUpperCamelConversions() {
    assertConversion(UPPER_CAMEL, LOWER_HYPHEN, "FooBar", "foo-bar");
    assertConversion(UPPER_CAMEL, LOWER_UNDERSCORE, "FooBar", "foo_bar");
    assertConversion(UPPER_CAMEL, LOWER_CAMEL, "FooBar", "fooBar");
    assertConversion(UPPER_CAMEL, UPPER_CAMEL, "FooBar", "FooBar");
    assertConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "FooBar", "FOO_BAR");
  }

  // Section: Upper Underscore Tests
  public void testUpperUnderscoreConversions() {
    assertConversion(UPPER_UNDERSCORE, LOWER_HYPHEN, "FOO_BAR", "foo-bar");
    assertConversion(UPPER_UNDERSCORE, LOWER_UNDERSCORE, "FOO_BAR", "foo_bar");
    assertConversion(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO_BAR", "fooBar");
    assertConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO_BAR", "FooBar");
    assertConversion(UPPER_UNDERSCORE, UPPER_UNDERSCORE, "FOO_BAR", "FOO_BAR");
  }

  // Section: Converter Tests
  public void testConverterToForward() {
    assertConverter(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO_BAR", "FooBar");
    assertConverter(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO_BAR", "fooBar");
    assertConverter(UPPER_CAMEL, UPPER_UNDERSCORE, "FooBar", "FOO_BAR");
    assertConverter(LOWER_CAMEL, UPPER_UNDERSCORE, "fooBar", "FOO_BAR");
  }

  public void testConverterToBackward() {
    assertReverseConverter(UPPER_UNDERSCORE, UPPER_CAMEL, "FooBar", "FOO_BAR");
    assertReverseConverter(UPPER_UNDERSCORE, LOWER_CAMEL, "fooBar", "FOO_BAR");
    assertReverseConverter(UPPER_CAMEL, UPPER_UNDERSCORE, "FOO_BAR", "FooBar");
    assertReverseConverter(LOWER_CAMEL, UPPER_UNDERSCORE, "FOO_BAR", "fooBar");
  }

  public void testConverter_nullConversions() {
    for (CaseFormat outer : CaseFormat.values()) {
      for (CaseFormat inner : CaseFormat.values()) {
        assertThat(outer.converterTo(inner).convert(null)).isNull();
        assertThat(outer.converterTo(inner).reverse().convert(null)).isNull();
      }
    }
  }

  public void testConverter_toString() {
    assertThat(LOWER_HYPHEN.converterTo(UPPER_CAMEL).toString())
        .isEqualTo("LOWER_HYPHEN.converterTo(UPPER_CAMEL)");
  }

  public void testConverter_serialization() {
    for (CaseFormat outer : CaseFormat.values()) {
      for (CaseFormat inner : CaseFormat.values()) {
        SerializableTester.reserializeAndAssert(outer.converterTo(inner));
      }
    }
  }

  // Helper method for asserting conversions
  private void assertConversion(CaseFormat from, CaseFormat to, String input, String expected) {
    assertThat(from.to(to, input)).isEqualTo(expected);
  }

  // Helper method for asserting converter forward conversions
  private void assertConverter(CaseFormat from, CaseFormat to, String input, String expected) {
    assertThat(from.converterTo(to).convert(input)).isEqualTo(expected);
  }

  // Helper method for asserting converter reverse conversions
  private void assertReverseConverter(CaseFormat from, CaseFormat to, String input, String expected) {
    assertThat(from.converterTo(to).reverse().convert(input)).isEqualTo(expected);
  }
}