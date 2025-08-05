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

/**
 * Unit test for {@link CaseFormat}.
 *
 * @author Mike Bostock
 */
@GwtCompatible(emulated = true)
@NullUnmarked
public class CaseFormatTest extends TestCase {

  // Helper method to verify conversion results
  private void verifyConversion(CaseFormat source, CaseFormat target, 
                                String input, String expected) {
    assertWithMessage("%s.to(%s, '%s')", source, target, input)
        .that(source.to(target, input))
        .isEqualTo(expected);
  }

  public void testIdentity() {
    for (CaseFormat from : CaseFormat.values()) {
      assertWithMessage("%s to %s", from, from).that(from.to(from, "foo")).isSameInstanceAs("foo");
      for (CaseFormat to : CaseFormat.values()) {
        assertWithMessage("%s to %s", from, to).that(from.to(to, "")).isEmpty();
        assertWithMessage("%s to %s", from, to).that(from.to(to, " ")).isEqualTo(" ");
      }
    }
  }

  @J2ktIncompatible
  @GwtIncompatible // NullPointerTester
  public void testNullArguments() {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicStaticMethods(CaseFormat.class);
    for (CaseFormat format : CaseFormat.values()) {
      tester.testAllPublicInstanceMethods(format);
    }
  }

  public void testLowerHyphenConversions() {
    verifyConversion(LOWER_HYPHEN, LOWER_HYPHEN, "foo", "foo");
    verifyConversion(LOWER_HYPHEN, LOWER_HYPHEN, "foo-bar", "foo-bar");
    
    verifyConversion(LOWER_HYPHEN, LOWER_UNDERSCORE, "foo", "foo");
    verifyConversion(LOWER_HYPHEN, LOWER_UNDERSCORE, "foo-bar", "foo_bar");
    
    verifyConversion(LOWER_HYPHEN, LOWER_CAMEL, "foo", "foo");
    verifyConversion(LOWER_HYPHEN, LOWER_CAMEL, "foo-bar", "fooBar");
    
    verifyConversion(LOWER_HYPHEN, UPPER_CAMEL, "foo", "Foo");
    verifyConversion(LOWER_HYPHEN, UPPER_CAMEL, "foo-bar", "FooBar");
    
    verifyConversion(LOWER_HYPHEN, UPPER_UNDERSCORE, "foo", "FOO");
    verifyConversion(LOWER_HYPHEN, UPPER_UNDERSCORE, "foo-bar", "FOO_BAR");
  }

  public void testLowerUnderscoreConversions() {
    verifyConversion(LOWER_UNDERSCORE, LOWER_HYPHEN, "foo", "foo");
    verifyConversion(LOWER_UNDERSCORE, LOWER_HYPHEN, "foo_bar", "foo-bar");
    
    verifyConversion(LOWER_UNDERSCORE, LOWER_UNDERSCORE, "foo", "foo");
    verifyConversion(LOWER_UNDERSCORE, LOWER_UNDERSCORE, "foo_bar", "foo_bar");
    
    verifyConversion(LOWER_UNDERSCORE, LOWER_CAMEL, "foo", "foo");
    verifyConversion(LOWER_UNDERSCORE, LOWER_CAMEL, "foo_bar", "fooBar");
    
    verifyConversion(LOWER_UNDERSCORE, UPPER_CAMEL, "foo", "Foo");
    verifyConversion(LOWER_UNDERSCORE, UPPER_CAMEL, "foo_bar", "FooBar");
    
    verifyConversion(LOWER_UNDERSCORE, UPPER_UNDERSCORE, "foo", "FOO");
    verifyConversion(LOWER_UNDERSCORE, UPPER_UNDERSCORE, "foo_bar", "FOO_BAR");
  }

  public void testLowerCamelConversions() {
    // Basic conversions
    verifyConversion(LOWER_CAMEL, LOWER_HYPHEN, "foo", "foo");
    verifyConversion(LOWER_CAMEL, LOWER_HYPHEN, "fooBar", "foo-bar");
    
    verifyConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "foo", "foo");
    verifyConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "fooBar", "foo_bar");
    
    verifyConversion(LOWER_CAMEL, LOWER_CAMEL, "foo", "foo");
    verifyConversion(LOWER_CAMEL, LOWER_CAMEL, "fooBar", "fooBar");
    
    verifyConversion(LOWER_CAMEL, UPPER_CAMEL, "foo", "Foo");
    verifyConversion(LOWER_CAMEL, UPPER_CAMEL, "fooBar", "FooBar");
    
    verifyConversion(LOWER_CAMEL, UPPER_UNDERSCORE, "foo", "FOO");
    verifyConversion(LOWER_CAMEL, UPPER_UNDERSCORE, "fooBar", "FOO_BAR");
    
    // Special handling for acronyms
    verifyConversion(LOWER_CAMEL, LOWER_HYPHEN, "HTTP", "h-t-t-p");
    verifyConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "hTTP", "h_t_t_p");
    verifyConversion(LOWER_CAMEL, UPPER_CAMEL, "hTTP", "HTTP");
  }

  public void testUpperCamelConversions() {
    // Basic conversions
    verifyConversion(UPPER_CAMEL, LOWER_HYPHEN, "Foo", "foo");
    verifyConversion(UPPER_CAMEL, LOWER_HYPHEN, "FooBar", "foo-bar");
    
    verifyConversion(UPPER_CAMEL, LOWER_UNDERSCORE, "Foo", "foo");
    verifyConversion(UPPER_CAMEL, LOWER_UNDERSCORE, "FooBar", "foo_bar");
    
    verifyConversion(UPPER_CAMEL, LOWER_CAMEL, "Foo", "foo");
    verifyConversion(UPPER_CAMEL, LOWER_CAMEL, "FooBar", "fooBar");
    
    verifyConversion(UPPER_CAMEL, UPPER_CAMEL, "Foo", "Foo");
    verifyConversion(UPPER_CAMEL, UPPER_CAMEL, "FooBar", "FooBar");
    
    verifyConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "Foo", "FOO");
    verifyConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "FooBar", "FOO_BAR");
    
    // Special handling for acronyms and edge cases
    verifyConversion(UPPER_CAMEL, LOWER_CAMEL, "HTTP", "hTTP");
    verifyConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "HTTP", "H_T_T_P");
    verifyConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "H_T_T_P", "H__T__T__P");
  }

  public void testUpperUnderscoreConversions() {
    verifyConversion(UPPER_UNDERSCORE, LOWER_HYPHEN, "FOO", "foo");
    verifyConversion(UPPER_UNDERSCORE, LOWER_HYPHEN, "FOO_BAR", "foo-bar");
    
    verifyConversion(UPPER_UNDERSCORE, LOWER_UNDERSCORE, "FOO", "foo");
    verifyConversion(UPPER_UNDERSCORE, LOWER_UNDERSCORE, "FOO_BAR", "foo_bar");
    
    verifyConversion(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO", "foo");
    verifyConversion(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO_BAR", "fooBar");
    
    verifyConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO", "Foo");
    verifyConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO_BAR", "FooBar");
    
    verifyConversion(UPPER_UNDERSCORE, UPPER_UNDERSCORE, "FOO", "FOO");
    verifyConversion(UPPER_UNDERSCORE, UPPER_UNDERSCORE, "FOO_BAR", "FOO_BAR");
    
    // Acronym handling
    verifyConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "H_T_T_P", "HTTP");
  }

  public void testConverterToForward() {
    verifyConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO_BAR", "FooBar");
    verifyConversion(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO_BAR", "fooBar");
    verifyConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "FooBar", "FOO_BAR");
    verifyConversion(LOWER_CAMEL, UPPER_UNDERSCORE, "fooBar", "FOO_BAR");
  }

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
}