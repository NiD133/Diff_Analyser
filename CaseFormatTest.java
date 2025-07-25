/*
 * Copyright (C) 2006 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import org.junit.Test;

/**
 * Unit test for {@link CaseFormat}.
 *
 * <p>This test suite exhaustively tests the conversion between all supported CaseFormat enums.
 *
 * @author Mike Bostock
 */
@GwtCompatible(emulated = true)
@NullUnmarked
public class CaseFormatTest extends TestCase {

  @Test
  public void testIdentityConversions() {
    for (CaseFormat format : CaseFormat.values()) {
      // Converting to the same format should return the same instance (for non-empty strings).
      assertWithMessage("Converting %s to itself", format)
          .that(format.to(format, "foo"))
          .isSameInstanceAs("foo");

      // Converting an empty string should return an empty string.
      for (CaseFormat to : CaseFormat.values()) {
        assertWithMessage("Converting from %s to %s with empty string", format, to)
            .that(format.to(to, ""))
            .isEmpty();
        // Converting a string with only a space should return the same space.
          assertWithMessage("Converting from %s to %s with space", format, to)
              .that(format.to(to, " "))
              .isEqualTo(" ");
      }
    }
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  @Test
  public void testNullArgumentHandling() {
    // Tests that all public methods throw NullPointerExceptions when passed null arguments.
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(CaseFormat.class);
    for (CaseFormat format : CaseFormat.values()) {
      tester.testAllPublicInstanceMethods(format);
    }
  }

  @Test
  public void testLowerHyphenConversions() {
    assertConversion(LOWER_HYPHEN, LOWER_HYPHEN, "foo", "foo");
    assertConversion(LOWER_HYPHEN, LOWER_HYPHEN, "foo-bar", "foo-bar");

    assertConversion(LOWER_HYPHEN, LOWER_UNDERSCORE, "foo", "foo");
    assertConversion(LOWER_HYPHEN, LOWER_UNDERSCORE, "foo-bar", "foo_bar");

    assertConversion(LOWER_HYPHEN, LOWER_CAMEL, "foo", "foo");
    assertConversion(LOWER_HYPHEN, LOWER_CAMEL, "foo-bar", "fooBar");

    assertConversion(LOWER_HYPHEN, UPPER_CAMEL, "foo", "Foo");
    assertConversion(LOWER_HYPHEN, UPPER_CAMEL, "foo-bar", "FooBar");

    assertConversion(LOWER_HYPHEN, UPPER_UNDERSCORE, "foo", "FOO");
    assertConversion(LOWER_HYPHEN, UPPER_UNDERSCORE, "foo-bar", "FOO_BAR");
  }

  @Test
  public void testLowerUnderscoreConversions() {
    assertConversion(LOWER_UNDERSCORE, LOWER_HYPHEN, "foo", "foo");
    assertConversion(LOWER_UNDERSCORE, LOWER_HYPHEN, "foo_bar", "foo-bar");

    assertConversion(LOWER_UNDERSCORE, LOWER_UNDERSCORE, "foo", "foo");
    assertConversion(LOWER_UNDERSCORE, LOWER_UNDERSCORE, "foo_bar", "foo_bar");

    assertConversion(LOWER_UNDERSCORE, LOWER_CAMEL, "foo", "foo");
    assertConversion(LOWER_UNDERSCORE, LOWER_CAMEL, "foo_bar", "fooBar");

    assertConversion(LOWER_UNDERSCORE, UPPER_CAMEL, "foo", "Foo");
    assertConversion(LOWER_UNDERSCORE, UPPER_CAMEL, "foo_bar", "FooBar");

    assertConversion(LOWER_UNDERSCORE, UPPER_UNDERSCORE, "foo", "FOO");
    assertConversion(LOWER_UNDERSCORE, UPPER_UNDERSCORE, "foo_bar", "FOO_BAR");
  }

  @Test
  public void testLowerCamelConversions() {
    assertConversion(LOWER_CAMEL, LOWER_HYPHEN, "foo", "foo");
    assertConversion(LOWER_CAMEL, LOWER_HYPHEN, "fooBar", "foo-bar");
    assertConversion(LOWER_CAMEL, LOWER_HYPHEN, "HTTP", "h-t-t-p");

    assertConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "foo", "foo");
    assertConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "fooBar", "foo_bar");
    assertConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "hTTP", "h_t_t_p");

    assertConversion(LOWER_CAMEL, LOWER_CAMEL, "foo", "foo");
    assertConversion(LOWER_CAMEL, LOWER_CAMEL, "fooBar", "fooBar");

    assertConversion(LOWER_CAMEL, UPPER_CAMEL, "foo", "Foo");
    assertConversion(LOWER_CAMEL, UPPER_CAMEL, "fooBar", "FooBar");
    assertConversion(LOWER_CAMEL, UPPER_CAMEL, "hTTP", "HTTP");

    assertConversion(LOWER_CAMEL, UPPER_UNDERSCORE, "foo", "FOO");
    assertConversion(LOWER_CAMEL, UPPER_UNDERSCORE, "fooBar", "FOO_BAR");
  }

  @Test
  public void testUpperCamelConversions() {
    assertConversion(UPPER_CAMEL, LOWER_HYPHEN, "Foo", "foo");
    assertConversion(UPPER_CAMEL, LOWER_HYPHEN, "FooBar", "foo-bar");

    assertConversion(UPPER_CAMEL, LOWER_UNDERSCORE, "Foo", "foo");
    assertConversion(UPPER_CAMEL, LOWER_UNDERSCORE, "FooBar", "foo_bar");

    assertConversion(UPPER_CAMEL, LOWER_CAMEL, "Foo", "foo");
    assertConversion(UPPER_CAMEL, LOWER_CAMEL, "FooBar", "fooBar");
    assertConversion(UPPER_CAMEL, LOWER_CAMEL, "HTTP", "hTTP");

    assertConversion(UPPER_CAMEL, UPPER_CAMEL, "Foo", "Foo");
    assertConversion(UPPER_CAMEL, UPPER_CAMEL, "FooBar", "FooBar");

    assertConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "Foo", "FOO");
    assertConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "FooBar", "FOO_BAR");
    assertConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "HTTP", "H_T_T_P");
    assertConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "H_T_T_P", "H__T__T__P");
  }

  @Test
  public void testUpperUnderscoreConversions() {
    assertConversion(UPPER_UNDERSCORE, LOWER_HYPHEN, "FOO", "foo");
    assertConversion(UPPER_UNDERSCORE, LOWER_HYPHEN, "FOO_BAR", "foo-bar");

    assertConversion(UPPER_UNDERSCORE, LOWER_UNDERSCORE, "FOO", "foo");
    assertConversion(UPPER_UNDERSCORE, LOWER_UNDERSCORE, "FOO_BAR", "foo_bar");

    assertConversion(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO", "foo");
    assertConversion(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO_BAR", "fooBar");

    assertConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO", "Foo");
    assertConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO_BAR", "FooBar");
    assertConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "H_T_T_P", "HTTP");

    assertConversion(UPPER_UNDERSCORE, UPPER_UNDERSCORE, "FOO", "FOO");
    assertConversion(UPPER_UNDERSCORE, UPPER_UNDERSCORE, "FOO_BAR", "FOO_BAR");
  }

  @Test
  public void testConverterToForward() {
    assertThat(UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).convert("FOO_BAR")).isEqualTo("FooBar");
    assertThat(UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).convert("FOO_BAR")).isEqualTo("fooBar");
    assertThat(UPPER_CAMEL.converterTo(UPPER_UNDERSCORE).convert("FooBar")).isEqualTo("FOO_BAR");
    assertThat(LOWER_CAMEL.converterTo(UPPER_UNDERSCORE).convert("fooBar")).isEqualTo("FOO_BAR");
  }

  @Test
  public void testConverterToBackward() {
    assertThat(UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).reverse().convert("FooBar"))
        .isEqualTo("FOO_BAR");
    assertThat(UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).reverse().convert("fooBar"))
        .isEqualTo("FOO_BAR");
    assertThat(UPPER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse().convert("FOO_BAR"))
        .isEqualTo("FooBar");
    assertThat(LOWER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse().convert("FOO_BAR"))
        .isEqualTo("fooBar");
  }

  @Test
  public void testConverterNullConversions() {
    for (CaseFormat outer : CaseFormat.values()) {
      for (CaseFormat inner : CaseFormat.values()) {
        assertThat(outer.converterTo(inner).convert(null)).isNull();
        assertThat(outer.converterTo(inner).reverse().convert(null)).isNull();
      }
    }
  }

  @Test
  public void testConverterToString() {
    assertThat(LOWER_HYPHEN.converterTo(UPPER_CAMEL).toString())
        .isEqualTo("LOWER_HYPHEN.converterTo(UPPER_CAMEL)");
  }

  @Test
  public void testConverterSerialization() {
    for (CaseFormat outer : CaseFormat.values()) {
      for (CaseFormat inner : CaseFormat.values()) {
        SerializableTester.reserializeAndAssert(outer.converterTo(inner));
      }
    }
  }

  private static void assertConversion(
      CaseFormat from, CaseFormat to, String input, String expected) {
    assertWithMessage("Converting %s to %s: %s", from, to, input)
        .that(from.to(to, input))
        .isEqualTo(expected);
  }
}