package com.google.common.base;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
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
 * Tests for CaseFormat conversions and its Converter.
 *
 * Organized by "source format" to show, at a glance, how a given format converts to all others.
 * Helper methods provide consistent, descriptive failure messages and reduce repetition.
 */
@GwtCompatible(emulated = true)
@NullUnmarked
public class CaseFormatTest extends TestCase {

  // ---------------------------
  // Generic behavior smoke tests
  // ---------------------------

  public void testIdentityAndTrivialInputs() {
    for (CaseFormat from : CaseFormat.values()) {
      // Identity: the same instance should be returned for an unmodified string.
      assertWithMessage("%s -> %s should return same instance for identical string", from, from)
          .that(from.to(from, "foo"))
          .isSameInstanceAs("foo");

      // Trivial inputs: empty string and single space are preserved across all conversions.
      for (CaseFormat to : CaseFormat.values()) {
        assertWithMessage("Empty string: %s -> %s", from, to)
            .that(from.to(to, ""))
            .isEmpty();
        assertWithMessage("Whitespace passthrough: %s -> %s", from, to)
            .that(from.to(to, " "))
            .isEqualTo(" ");
      }
    }
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNullArguments_areRejected() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(CaseFormat.class);
    for (CaseFormat format : CaseFormat.values()) {
      tester.testAllPublicInstanceMethods(format);
    }
  }

  // ---------------------------
  // LOWER_HYPHEN source format
  // ---------------------------

  public void testLowerHyphen_conversions() {
    // -> LOWER_HYPHEN
    assertConv(LOWER_HYPHEN, LOWER_HYPHEN, "foo", "foo");
    assertConv(LOWER_HYPHEN, LOWER_HYPHEN, "foo-bar", "foo-bar");

    // -> LOWER_UNDERSCORE
    assertConv(LOWER_HYPHEN, LOWER_UNDERSCORE, "foo", "foo");
    assertConv(LOWER_HYPHEN, LOWER_UNDERSCORE, "foo-bar", "foo_bar");

    // -> LOWER_CAMEL
    assertConv(LOWER_HYPHEN, LOWER_CAMEL, "foo", "foo");
    assertConv(LOWER_HYPHEN, LOWER_CAMEL, "foo-bar", "fooBar");

    // -> UPPER_CAMEL
    assertConv(LOWER_HYPHEN, UPPER_CAMEL, "foo", "Foo");
    assertConv(LOWER_HYPHEN, UPPER_CAMEL, "foo-bar", "FooBar");

    // -> UPPER_UNDERSCORE
    assertConv(LOWER_HYPHEN, UPPER_UNDERSCORE, "foo", "FOO");
    assertConv(LOWER_HYPHEN, UPPER_UNDERSCORE, "foo-bar", "FOO_BAR");
  }

  // ---------------------------
  // LOWER_UNDERSCORE source format
  // ---------------------------

  public void testLowerUnderscore_conversions() {
    // -> LOWER_HYPHEN
    assertConv(LOWER_UNDERSCORE, LOWER_HYPHEN, "foo", "foo");
    assertConv(LOWER_UNDERSCORE, LOWER_HYPHEN, "foo_bar", "foo-bar");

    // -> LOWER_UNDERSCORE
    assertConv(LOWER_UNDERSCORE, LOWER_UNDERSCORE, "foo", "foo");
    assertConv(LOWER_UNDERSCORE, LOWER_UNDERSCORE, "foo_bar", "foo_bar");

    // -> LOWER_CAMEL
    assertConv(LOWER_UNDERSCORE, LOWER_CAMEL, "foo", "foo");
    assertConv(LOWER_UNDERSCORE, LOWER_CAMEL, "foo_bar", "fooBar");

    // -> UPPER_CAMEL
    assertConv(LOWER_UNDERSCORE, UPPER_CAMEL, "foo", "Foo");
    assertConv(LOWER_UNDERSCORE, UPPER_CAMEL, "foo_bar", "FooBar");

    // -> UPPER_UNDERSCORE
    assertConv(LOWER_UNDERSCORE, UPPER_UNDERSCORE, "foo", "FOO");
    assertConv(LOWER_UNDERSCORE, UPPER_UNDERSCORE, "foo_bar", "FOO_BAR");
  }

  // ---------------------------
  // LOWER_CAMEL source format
  // ---------------------------

  public void testLowerCamel_conversions() {
    // -> LOWER_HYPHEN
    assertConv(LOWER_CAMEL, LOWER_HYPHEN, "foo", "foo");
    assertConv(LOWER_CAMEL, LOWER_HYPHEN, "fooBar", "foo-bar");
    assertConv(LOWER_CAMEL, LOWER_HYPHEN, "HTTP", "h-t-t-p"); // acronym split

    // -> LOWER_UNDERSCORE
    assertConv(LOWER_CAMEL, LOWER_UNDERSCORE, "foo", "foo");
    assertConv(LOWER_CAMEL, LOWER_UNDERSCORE, "fooBar", "foo_bar");
    assertConv(LOWER_CAMEL, LOWER_UNDERSCORE, "hTTP", "h_t_t_p"); // irregular capitalization

    // -> LOWER_CAMEL
    assertConv(LOWER_CAMEL, LOWER_CAMEL, "foo", "foo");
    assertConv(LOWER_CAMEL, LOWER_CAMEL, "fooBar", "fooBar");

    // -> UPPER_CAMEL
    assertConv(LOWER_CAMEL, UPPER_CAMEL, "foo", "Foo");
    assertConv(LOWER_CAMEL, UPPER_CAMEL, "fooBar", "FooBar");
    assertConv(LOWER_CAMEL, UPPER_CAMEL, "hTTP", "HTTP"); // acronym preservation

    // -> UPPER_UNDERSCORE
    assertConv(LOWER_CAMEL, UPPER_UNDERSCORE, "foo", "FOO");
    assertConv(LOWER_CAMEL, UPPER_UNDERSCORE, "fooBar", "FOO_BAR");
  }

  // ---------------------------
  // UPPER_CAMEL source format
  // ---------------------------

  public void testUpperCamel_conversions() {
    // -> LOWER_HYPHEN
    assertConv(UPPER_CAMEL, LOWER_HYPHEN, "Foo", "foo");
    assertConv(UPPER_CAMEL, LOWER_HYPHEN, "FooBar", "foo-bar");

    // -> LOWER_UNDERSCORE
    assertConv(UPPER_CAMEL, LOWER_UNDERSCORE, "Foo", "foo");
    assertConv(UPPER_CAMEL, LOWER_UNDERSCORE, "FooBar", "foo_bar");

    // -> LOWER_CAMEL
    assertConv(UPPER_CAMEL, LOWER_CAMEL, "Foo", "foo");
    assertConv(UPPER_CAMEL, LOWER_CAMEL, "FooBar", "fooBar");
    assertConv(UPPER_CAMEL, LOWER_CAMEL, "HTTP", "hTTP"); // edge case from original tests

    // -> UPPER_CAMEL
    assertConv(UPPER_CAMEL, UPPER_CAMEL, "Foo", "Foo");
    assertConv(UPPER_CAMEL, UPPER_CAMEL, "FooBar", "FooBar");

    // -> UPPER_UNDERSCORE
    assertConv(UPPER_CAMEL, UPPER_UNDERSCORE, "Foo", "FOO");
    assertConv(UPPER_CAMEL, UPPER_UNDERSCORE, "FooBar", "FOO_BAR");
    assertConv(UPPER_CAMEL, UPPER_UNDERSCORE, "HTTP", "H_T_T_P");
    assertConv(UPPER_CAMEL, UPPER_UNDERSCORE, "H_T_T_P", "H__T__T__P"); // separators preserved
  }

  // ---------------------------
  // UPPER_UNDERSCORE source format
  // ---------------------------

  public void testUpperUnderscore_conversions() {
    // -> LOWER_HYPHEN
    assertConv(UPPER_UNDERSCORE, LOWER_HYPHEN, "FOO", "foo");
    assertConv(UPPER_UNDERSCORE, LOWER_HYPHEN, "FOO_BAR", "foo-bar");

    // -> LOWER_UNDERSCORE
    assertConv(UPPER_UNDERSCORE, LOWER_UNDERSCORE, "FOO", "foo");
    assertConv(UPPER_UNDERSCORE, LOWER_UNDERSCORE, "FOO_BAR", "foo_bar");

    // -> LOWER_CAMEL
    assertConv(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO", "foo");
    assertConv(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO_BAR", "fooBar");

    // -> UPPER_CAMEL
    assertConv(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO", "Foo");
    assertConv(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO_BAR", "FooBar");
    assertConv(UPPER_UNDERSCORE, UPPER_CAMEL, "H_T_T_P", "HTTP"); // acronym collapse

    // -> UPPER_UNDERSCORE
    assertConv(UPPER_UNDERSCORE, UPPER_UNDERSCORE, "FOO", "FOO");
    assertConv(UPPER_UNDERSCORE, UPPER_UNDERSCORE, "FOO_BAR", "FOO_BAR");
  }

  // ---------------------------
  // Converter behavior
  // ---------------------------

  public void testConverter_forward() {
    assertThat(UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).convert("FOO_BAR")).isEqualTo("FooBar");
    assertThat(UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).convert("FOO_BAR")).isEqualTo("fooBar");
    assertThat(UPPER_CAMEL.converterTo(UPPER_UNDERSCORE).convert("FooBar")).isEqualTo("FOO_BAR");
    assertThat(LOWER_CAMEL.converterTo(UPPER_UNDERSCORE).convert("fooBar")).isEqualTo("FOO_BAR");
  }

  public void testConverter_backward() {
    assertThat(UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).reverse().convert("FooBar"))
        .isEqualTo("FOO_BAR");
    assertThat(UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).reverse().convert("fooBar"))
        .isEqualTo("FOO_BAR");
    assertThat(UPPER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse().convert("FOO_BAR"))
        .isEqualTo("FooBar");
    assertThat(LOWER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse().convert("FOO_BAR"))
        .isEqualTo("fooBar");
  }

  public void testConverter_nullConversions() {
    for (CaseFormat source : CaseFormat.values()) {
      for (CaseFormat target : CaseFormat.values()) {
        assertThat(source.converterTo(target).convert(null)).isNull();
        assertThat(source.converterTo(target).reverse().convert(null)).isNull();
      }
    }
  }

  public void testConverter_toString() {
    assertThat(LOWER_HYPHEN.converterTo(UPPER_CAMEL).toString())
        .isEqualTo("LOWER_HYPHEN.converterTo(UPPER_CAMEL)");
  }

  public void testConverter_serialization() {
    for (CaseFormat source : CaseFormat.values()) {
      for (CaseFormat target : CaseFormat.values()) {
        SerializableTester.reserializeAndAssert(source.converterTo(target));
      }
    }
  }

  // ---------------------------
  // Helpers
  // ---------------------------

  private static void assertConv(CaseFormat from, CaseFormat to, String input, String expected) {
    assertWithMessage("Convert %s -> %s for input '%s'", from, to, input)
        .that(from.to(to, input))
        .isEqualTo(expected);
  }
}