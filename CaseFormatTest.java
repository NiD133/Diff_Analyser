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
 * Tests case format conversions between different naming conventions:
 * - LOWER_HYPHEN: "foo-bar" (kebab-case)
 * - LOWER_UNDERSCORE: "foo_bar" (snake_case)
 * - LOWER_CAMEL: "fooBar" (camelCase)
 * - UPPER_CAMEL: "FooBar" (PascalCase)
 * - UPPER_UNDERSCORE: "FOO_BAR" (CONSTANT_CASE)
 *
 * @author Mike Bostock
 */
@GwtCompatible(emulated = true)
@NullUnmarked
public class CaseFormatTest extends TestCase {

    // Test data representing common conversion scenarios
    private static final class TestData {
        final String singleWord = "foo";
        final String twoWords = "foo-bar"; // Using hyphen as canonical separator for test input
        final String acronym = "HTTP";
        final String acronymMixed = "hTTP";
        final String edgeCase = "H_T_T_P"; // Contains underscores in UpperCamel input
    }

    private static final TestData TEST_DATA = new TestData();

    /**
     * Tests that converting from a format to itself returns the same instance for optimization,
     * and that all formats handle empty strings and whitespace consistently.
     */
    public void testIdentityConversions() {
        for (CaseFormat format : CaseFormat.values()) {
            // Identity conversion should return same instance for performance
            assertWithMessage("Identity conversion for %s should return same instance", format)
                    .that(format.to(format, TEST_DATA.singleWord))
                    .isSameInstanceAs(TEST_DATA.singleWord);

            // All formats should handle empty strings and whitespace consistently
            for (CaseFormat targetFormat : CaseFormat.values()) {
                assertWithMessage("Empty string conversion from %s to %s", format, targetFormat)
                        .that(format.to(targetFormat, ""))
                        .isEmpty();
                assertWithMessage("Whitespace conversion from %s to %s", format, targetFormat)
                        .that(format.to(targetFormat, " "))
                        .isEqualTo(" ");
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

    // ========== LOWER_HYPHEN (kebab-case) Conversions ==========

    public void testLowerHyphenConversions() {
        // To LOWER_HYPHEN (identity)
        assertConversion(LOWER_HYPHEN, LOWER_HYPHEN, "foo", "foo");
        assertConversion(LOWER_HYPHEN, LOWER_HYPHEN, "foo-bar", "foo-bar");

        // To LOWER_UNDERSCORE
        assertConversion(LOWER_HYPHEN, LOWER_UNDERSCORE, "foo", "foo");
        assertConversion(LOWER_HYPHEN, LOWER_UNDERSCORE, "foo-bar", "foo_bar");

        // To LOWER_CAMEL
        assertConversion(LOWER_HYPHEN, LOWER_CAMEL, "foo", "foo");
        assertConversion(LOWER_HYPHEN, LOWER_CAMEL, "foo-bar", "fooBar");

        // To UPPER_CAMEL
        assertConversion(LOWER_HYPHEN, UPPER_CAMEL, "foo", "Foo");
        assertConversion(LOWER_HYPHEN, UPPER_CAMEL, "foo-bar", "FooBar");

        // To UPPER_UNDERSCORE
        assertConversion(LOWER_HYPHEN, UPPER_UNDERSCORE, "foo", "FOO");
        assertConversion(LOWER_HYPHEN, UPPER_UNDERSCORE, "foo-bar", "FOO_BAR");
    }

    // ========== LOWER_UNDERSCORE (snake_case) Conversions ==========

    public void testLowerUnderscoreConversions() {
        // To LOWER_HYPHEN
        assertConversion(LOWER_UNDERSCORE, LOWER_HYPHEN, "foo", "foo");
        assertConversion(LOWER_UNDERSCORE, LOWER_HYPHEN, "foo_bar", "foo-bar");

        // To LOWER_UNDERSCORE (identity)
        assertConversion(LOWER_UNDERSCORE, LOWER_UNDERSCORE, "foo", "foo");
        assertConversion(LOWER_UNDERSCORE, LOWER_UNDERSCORE, "foo_bar", "foo_bar");

        // To LOWER_CAMEL
        assertConversion(LOWER_UNDERSCORE, LOWER_CAMEL, "foo", "foo");
        assertConversion(LOWER_UNDERSCORE, LOWER_CAMEL, "foo_bar", "fooBar");

        // To UPPER_CAMEL
        assertConversion(LOWER_UNDERSCORE, UPPER_CAMEL, "foo", "Foo");
        assertConversion(LOWER_UNDERSCORE, UPPER_CAMEL, "foo_bar", "FooBar");

        // To UPPER_UNDERSCORE
        assertConversion(LOWER_UNDERSCORE, UPPER_UNDERSCORE, "foo", "FOO");
        assertConversion(LOWER_UNDERSCORE, UPPER_UNDERSCORE, "foo_bar", "FOO_BAR");
    }

    // ========== LOWER_CAMEL (camelCase) Conversions ==========

    public void testLowerCamelConversions() {
        // To LOWER_HYPHEN
        assertConversion(LOWER_CAMEL, LOWER_HYPHEN, "foo", "foo");
        assertConversion(LOWER_CAMEL, LOWER_HYPHEN, "fooBar", "foo-bar");
        assertConversion(LOWER_CAMEL, LOWER_HYPHEN, "HTTP", "h-t-t-p"); // All caps becomes individual letters

        // To LOWER_UNDERSCORE
        assertConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "foo", "foo");
        assertConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "fooBar", "foo_bar");
        assertConversion(LOWER_CAMEL, LOWER_UNDERSCORE, "hTTP", "h_t_t_p"); // Mixed case acronym

        // To LOWER_CAMEL (identity)
        assertConversion(LOWER_CAMEL, LOWER_CAMEL, "foo", "foo");
        assertConversion(LOWER_CAMEL, LOWER_CAMEL, "fooBar", "fooBar");

        // To UPPER_CAMEL
        assertConversion(LOWER_CAMEL, UPPER_CAMEL, "foo", "Foo");
        assertConversion(LOWER_CAMEL, UPPER_CAMEL, "fooBar", "FooBar");
        assertConversion(LOWER_CAMEL, UPPER_CAMEL, "hTTP", "HTTP"); // Mixed case becomes proper acronym

        // To UPPER_UNDERSCORE
        assertConversion(LOWER_CAMEL, UPPER_UNDERSCORE, "foo", "FOO");
        assertConversion(LOWER_CAMEL, UPPER_UNDERSCORE, "fooBar", "FOO_BAR");
    }

    // ========== UPPER_CAMEL (PascalCase) Conversions ==========

    public void testUpperCamelConversions() {
        // To LOWER_HYPHEN
        assertConversion(UPPER_CAMEL, LOWER_HYPHEN, "Foo", "foo");
        assertConversion(UPPER_CAMEL, LOWER_HYPHEN, "FooBar", "foo-bar");

        // To LOWER_UNDERSCORE
        assertConversion(UPPER_CAMEL, LOWER_UNDERSCORE, "Foo", "foo");
        assertConversion(UPPER_CAMEL, LOWER_UNDERSCORE, "FooBar", "foo_bar");

        // To LOWER_CAMEL
        assertConversion(UPPER_CAMEL, LOWER_CAMEL, "Foo", "foo");
        assertConversion(UPPER_CAMEL, LOWER_CAMEL, "FooBar", "fooBar");
        assertConversion(UPPER_CAMEL, LOWER_CAMEL, "HTTP", "hTTP"); // Proper acronym becomes mixed case

        // To UPPER_CAMEL (identity)
        assertConversion(UPPER_CAMEL, UPPER_CAMEL, "Foo", "Foo");
        assertConversion(UPPER_CAMEL, UPPER_CAMEL, "FooBar", "FooBar");

        // To UPPER_UNDERSCORE
        assertConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "Foo", "FOO");
        assertConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "FooBar", "FOO_BAR");
        assertConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "HTTP", "H_T_T_P"); // Acronym becomes individual letters
        assertConversion(UPPER_CAMEL, UPPER_UNDERSCORE, "H_T_T_P", "H__T__T__P"); // Existing underscores are preserved with double underscores
    }

    // ========== UPPER_UNDERSCORE (CONSTANT_CASE) Conversions ==========

    public void testUpperUnderscoreConversions() {
        // To LOWER_HYPHEN
        assertConversion(UPPER_UNDERSCORE, LOWER_HYPHEN, "FOO", "foo");
        assertConversion(UPPER_UNDERSCORE, LOWER_HYPHEN, "FOO_BAR", "foo-bar");

        // To LOWER_UNDERSCORE
        assertConversion(UPPER_UNDERSCORE, LOWER_UNDERSCORE, "FOO", "foo");
        assertConversion(UPPER_UNDERSCORE, LOWER_UNDERSCORE, "FOO_BAR", "foo_bar");

        // To LOWER_CAMEL
        assertConversion(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO", "foo");
        assertConversion(UPPER_UNDERSCORE, LOWER_CAMEL, "FOO_BAR", "fooBar");

        // To UPPER_CAMEL
        assertConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO", "Foo");
        assertConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "FOO_BAR", "FooBar");
        assertConversion(UPPER_UNDERSCORE, UPPER_CAMEL, "H_T_T_P", "HTTP"); // Individual letters become proper acronym

        // To UPPER_UNDERSCORE (identity)
        assertConversion(UPPER_UNDERSCORE, UPPER_UNDERSCORE, "FOO", "FOO");
        assertConversion(UPPER_UNDERSCORE, UPPER_UNDERSCORE, "FOO_BAR", "FOO_BAR");
    }

    // ========== Converter Tests ==========

    public void testConverterForwardConversions() {
        assertThat(UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).convert("FOO_BAR")).isEqualTo("FooBar");
        assertThat(UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).convert("FOO_BAR")).isEqualTo("fooBar");
        assertThat(UPPER_CAMEL.converterTo(UPPER_UNDERSCORE).convert("FooBar")).isEqualTo("FOO_BAR");
        assertThat(LOWER_CAMEL.converterTo(UPPER_UNDERSCORE).convert("fooBar")).isEqualTo("FOO_BAR");
    }

    public void testConverterReverseConversions() {
        assertThat(UPPER_UNDERSCORE.converterTo(UPPER_CAMEL).reverse().convert("FooBar"))
                .isEqualTo("FOO_BAR");
        assertThat(UPPER_UNDERSCORE.converterTo(LOWER_CAMEL).reverse().convert("fooBar"))
                .isEqualTo("FOO_BAR");
        assertThat(UPPER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse().convert("FOO_BAR"))
                .isEqualTo("FooBar");
        assertThat(LOWER_CAMEL.converterTo(UPPER_UNDERSCORE).reverse().convert("FOO_BAR"))
                .isEqualTo("fooBar");
    }

    public void testConverterNullHandling() {
        for (CaseFormat sourceFormat : CaseFormat.values()) {
            for (CaseFormat targetFormat : CaseFormat.values()) {
                assertThat(sourceFormat.converterTo(targetFormat).convert(null)).isNull();
                assertThat(sourceFormat.converterTo(targetFormat).reverse().convert(null)).isNull();
            }
        }
    }

    public void testConverterToStringRepresentation() {
        assertThat(LOWER_HYPHEN.converterTo(UPPER_CAMEL).toString())
                .isEqualTo("LOWER_HYPHEN.converterTo(UPPER_CAMEL)");
    }

    public void testConverterSerialization() {
        for (CaseFormat sourceFormat : CaseFormat.values()) {
            for (CaseFormat targetFormat : CaseFormat.values()) {
                SerializableTester.reserializeAndAssert(sourceFormat.converterTo(targetFormat));
            }
        }
    }

    // ========== Helper Methods ==========

    /**
     * Helper method to make conversion assertions more readable and reduce duplication.
     *
     * @param sourceFormat the source case format
     * @param targetFormat the target case format
     * @param input the input string in source format
     * @param expectedOutput the expected output string in target format
     */
    private static void assertConversion(CaseFormat sourceFormat, CaseFormat targetFormat,
                                         String input, String expectedOutput) {
        assertWithMessage("Converting '%s' from %s to %s", input, sourceFormat, targetFormat)
                .that(sourceFormat.to(targetFormat, input))
                .isEqualTo(expectedOutput);
    }
}