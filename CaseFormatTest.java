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
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for {@link CaseFormat}.
 *
 * @author Mike Bostock
 */
@GwtCompatible(emulated = true)
@NullUnmarked
@RunWith(JUnit4.class)
public class CaseFormatTest {

  @Test
  public void testIdentityConversion() {
    for (CaseFormat format : CaseFormat.values()) {
      assertWithMessage("identity conversion for %s", format)
          .that(format.to(format, "foo"))
          .isSameInstanceAs("foo");
    }
  }

  @Test
  public void testSpecialStringConversions() {
    for (CaseFormat from : CaseFormat.values()) {
      for (CaseFormat to : CaseFormat.values()) {
        assertWithMessage("from %s to %s", from, to).that(from.to(to, "")).isEmpty();
        assertWithMessage("from %s to %s", from, to).that(from.to(to, " ")).isEqualTo(" ");
      }
    }
  }

  @Test
  public void testLowerHyphenToOthers() {
    assertThat(LOWER_HYPHEN.to(LOWER_HYPHEN, "foo-bar")).isEqualTo("foo-bar");
    assertThat(LOWER_HYPHEN.to(LOWER_UNDERSCORE, "foo-bar")).isEqualTo("foo_bar");
    assertThat(LOWER_HYPHEN.to(LOWER_CAMEL, "foo-bar")).isEqualTo("fooBar");
    assertThat(LOWER_HYPHEN.to(UPPER_CAMEL, "foo-bar")).isEqualTo("FooBar");
    assertThat(LOWER_HYPHEN.to(UPPER_UNDERSCORE, "foo-bar")).isEqualTo("FOO_BAR");
  }

  @Test
  public void testLowerUnderscoreToOthers() {
    assertThat(LOWER_UNDERSCORE.to(LOWER_UNDERSCORE, "foo_bar")).isEqualTo("foo_bar");
    assertThat(LOWER_UNDERSCORE.to(LOWER_HYPHEN, "foo_bar")).isEqualTo("foo-bar");
    assertThat(LOWER_UNDERSCORE.to(LOWER_CAMEL, "foo_bar")).isEqualTo("fooBar");
    assertThat(LOWER_UNDERSCORE.to(UPPER_CAMEL, "foo_bar")).isEqualTo("FooBar");
    assertThat(LOWER_UNDERSCORE.to(UPPER_UNDERSCORE, "foo_bar")).isEqualTo("FOO_BAR");
  }

  @Test
  public void testLowerCamelToOthers() {
    assertThat(LOWER_CAMEL.to(LOWER_CAMEL, "fooBar")).isEqualTo("fooBar");
    assertThat(LOWER_CAMEL.to(LOWER_HYPHEN, "fooBar")).isEqualTo("foo-bar");
    assertThat(LOWER_CAMEL.to(LOWER_UNDERSCORE, "fooBar")).isEqualTo("foo_bar");
    assertThat(LOWER_CAMEL.to(UPPER_CAMEL, "fooBar")).isEqualTo("FooBar");
    assertThat(LOWER_CAMEL.to(UPPER_UNDERSCORE, "fooBar")).isEqualTo("FOO_BAR");

    // Edge cases for acronyms
    assertThat(LOWER_CAMEL.to(LOWER_HYPHEN, "HTTP")).isEqualTo("h-t-t-p");
    assertThat(LOWER_CAMEL.to(LOWER_UNDERSCORE, "hTTP")).isEqualTo("h_t_t_p");
    assertThat(LOWER_CAMEL.to(UPPER_CAMEL, "hTTP")).isEqualTo("HTTP");
  }

  @Test
  public void testUpperCamelToOthers() {
    assertThat(UPPER_CAMEL.to(UPPER_CAMEL, "FooBar")).isEqualTo("FooBar");
    assertThat(UPPER_CAMEL.to(LOWER_HYPHEN, "FooBar")).isEqualTo("foo-bar");
    assertThat(UPPER_CAMEL.to(LOWER_UNDERSCORE, "FooBar")).isEqualTo("foo_bar");
    assertThat(UPPER_CAMEL.to(LOWER_CAMEL, "FooBar")).isEqualTo("fooBar");
    assertThat(UPPER_CAMEL.to(UPPER_UNDERSCORE, "FooBar")).isEqualTo("FOO_BAR");

    // Edge cases for acronyms
    assertThat(UPPER_CAMEL.to(LOWER_CAMEL, "HTTP")).isEqualTo("hTTP");
    assertThat(UPPER_CAMEL.to(UPPER_UNDERSCORE, "HTTP")).isEqualTo("H_T_T_P");
    assertThat(UPPER_CAMEL.to(UPPER_UNDERSCORE, "H_T_T_P")).isEqualTo("H__T__T__P");
  }

  @Test
  public void testUpperUnderscoreToOthers() {
    assertThat(UPPER_UNDERSCORE.to(UPPER_UNDERSCORE, "FOO_BAR")).isEqualTo("FOO_BAR");
    assertThat(UPPER_UNDERSCORE.to(LOWER_HYPHEN, "FOO_BAR")).isEqualTo("foo-bar");
    assertThat(UPPER_UNDERSCORE.to(LOWER_UNDERSCORE, "FOO_BAR")).isEqualTo("foo_bar");
    assertThat(UPPER_UNDERSCORE.to(LOWER_CAMEL, "FOO_BAR")).isEqualTo("fooBar");
    assertThat(UPPER_UNDERSCORE.to(UPPER_CAMEL, "FOO_BAR")).isEqualTo("FooBar");

    // Edge case for acronyms
    assertThat(UPPER_UNDERSCORE.to(UPPER_CAMEL, "H_T_T_P")).isEqualTo("HTTP");
  }

  @Test
  public void testConverter_forward() {
    assertThat(UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).convert("FOO_BAR")).isEqualTo("FooBar");
    assertThat(UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).convert("FOO_BAR")).isEqualTo("fooBar");
    assertThat(UPPER_CAMEL.converterTo(UPPER_UNDERSCORE).convert("FooBar")).isEqualTo("FOO_BAR");
    assertThat(LOWER_CAMEL.converterTo(UPPER_UNDERSCORE).convert("fooBar")).isEqualTo("FOO_BAR");
  }

  @Test
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

  @Test
  public void testConverter_nullConversions() {
    for (CaseFormat outer : CaseFormat.values()) {
      for (CaseFormat inner : CaseFormat.values()) {
        assertThat(outer.converterTo(inner).convert(null)).isNull();
        assertThat(outer.converterTo(inner).reverse().convert(null)).isNull();
      }
    }
  }

  @Test
  public void testConverter_toString() {
    assertThat(LOWER_HYPHEN.converterTo(UPPER_CAMEL).toString())
        .isEqualTo("LOWER_HYPHEN.converterTo(UPPER_CAMEL)");
  }

  @Test
  public void testConverter_serialization() {
    for (CaseFormat outer : CaseFormat.values()) {
      for (CaseFormat inner : CaseFormat.values()) {
        SerializableTester.reserializeAndAssert(outer.converterTo(inner));
      }
    }
  }

  @Test
  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNullArguments() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(CaseFormat.class);
    for (CaseFormat format : CaseFormat.values()) {
      tester.testAllPublicInstanceMethods(format);
    }
  }
}