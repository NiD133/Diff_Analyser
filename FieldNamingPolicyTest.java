/*
 * Copyright (C) 2021 Google Inc.
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

package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.Test;

/**
 * Performs tests directly against {@link FieldNamingPolicy}; for integration tests see {@code
 * FieldNamingTest}.
 */
public class FieldNamingPolicyTest {
    private static final char UNDERSCORE_SEPARATOR = '_';

    // Test cases for camelCase separation
    private static final String[][] CAMEL_CASE_SEPARATION_TEST_CASES = {
        // input, expected output
        {"a", "a"},                          // Single lowercase letter
        {"ab", "ab"},                        // Two lowercase letters
        {"Ab", "Ab"},                        // Capitalized letter
        {"aB", "a_B"},                       // Single uppercase letter in middle
        {"AB", "A_B"},                       // Two uppercase letters
        {"A_B", "A__B"},                     // Existing underscores
        {"firstSecondThird", "first_Second_Third"}, // Multiple words
        {"__", "__"},                        // Only underscores
        {"_123", "_123"}                     // Underscore followed by digits
    };

    // Test cases for first letter capitalization
    private static final String[][] FIRST_LETTER_UPPERCASE_TEST_CASES = {
        // input, expected output
        {"a", "A"},                          // Single letter
        {"ab", "Ab"},                        // Two letters
        {"AB", "AB"},                        // All caps
        {"_a", "_A"},                        // Leading underscore
        {"_ab", "_Ab"},                      // Leading underscore with multiple letters
        {"__", "__"},                        // Only underscores
        {"_1", "_1"},                        // Leading underscore with digit
        {"\u2170", "\u2170"},                // Non-letter character (should not change)
        {"_\u2170", "_\u2170"},              // Non-letter with underscore
        {"\u2170a", "\u2170A"}               // Non-letter followed by letter
    };

    @Test
    public void separateCamelCase_withVariousInputs_producesCorrectOutput() {
        for (String[] testCase : CAMEL_CASE_SEPARATION_TEST_CASES) {
            String input = testCase[0];
            String expected = testCase[1];
            String actual = FieldNamingPolicy.separateCamelCase(input, UNDERSCORE_SEPARATOR);
            
            assertWithMessage("Input: '%s'", input)
                .that(actual)
                .isEqualTo(expected);
        }
    }

    @Test
    public void upperCaseFirstLetter_withVariousInputs_producesCorrectOutput() {
        for (String[] testCase : FIRST_LETTER_UPPERCASE_TEST_CASES) {
            String input = testCase[0];
            String expected = testCase[1];
            String actual = FieldNamingPolicy.upperCaseFirstLetter(input);
            
            assertWithMessage("Input: '%s'", input)
                .that(actual)
                .isEqualTo(expected);
        }
    }

    /** Upper-casing policies should be unaffected by default Locale. */
    @Test
    public void upperCasingPolicies_ignoreDefaultLocale() throws Exception {
        class Dummy {
            @SuppressWarnings("unused")
            int value;
        }

        FieldNamingPolicy[] policies = {
            FieldNamingPolicy.UPPER_CAMEL_CASE,
            FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES,
            FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES,
        };

        Field field = Dummy.class.getDeclaredField("value");
        runLocaleSensitiveTest(
            field,
            field.getName().toUpperCase(Locale.ROOT),
            policies,
            "uppercase"
        );
    }

    /** Lower-casing policies should be unaffected by default Locale. */
    @Test
    public void lowerCasingPolicies_ignoreDefaultLocale() throws Exception {
        class Dummy {
            @SuppressWarnings({"unused", "ConstantField"})
            int VALUE;
        }

        FieldNamingPolicy[] policies = {
            FieldNamingPolicy.LOWER_CASE_WITH_DASHES,
            FieldNamingPolicy.LOWER_CASE_WITH_DOTS,
            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES,
        };

        Field field = Dummy.class.getDeclaredField("VALUE");
        runLocaleSensitiveTest(
            field,
            field.getName().toLowerCase(Locale.ROOT),
            policies,
            "lowercase"
        );
    }

    /**
     * Helper method for locale-sensitive tests.
     * Sets Turkish locale (which has special case conversion rules) and verifies that
     * field naming policies produce consistent results regardless of default locale.
     */
    private void runLocaleSensitiveTest(
            Field field, 
            String expectedConversion,
            FieldNamingPolicy[] policies,
            String caseType) throws Exception {
        
        Locale originalLocale = Locale.getDefault();
        try {
            // Turkish has special case conversion rules (e.g., 'i' becomes 'İ')
            Locale turkishLocale = new Locale("tr");
            Locale.setDefault(turkishLocale);

            // Verify Turkish locale affects default case conversion
            String turkishConversion = caseType.equals("uppercase") 
                ? field.getName().toUpperCase(turkishLocale)
                : field.getName().toLowerCase(turkishLocale);
                
            assertWithMessage("Test setup: Turkish locale should produce different %s conversion", caseType)
                .that(turkishConversion)
                .isNotEqualTo(expectedConversion);

            // Verify each policy produces consistent conversion
            for (FieldNamingPolicy policy : policies) {
                String actual = policy.translateName(field);
                assertWithMessage("Policy '%s' should produce locale-independent %s conversion", policy, caseType)
                    .that(actual)
                    .isEqualTo(expectedConversion);
            }
        } finally {
            Locale.setDefault(originalLocale);
        }
    }
}