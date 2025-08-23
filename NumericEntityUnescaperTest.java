/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link org.apache.commons.lang3.text.translate.NumericEntityUnescaper}.
 *
 * The tests focus on:
 * - Incomplete/dangling numeric entity starts (should leave input unchanged).
 * - Supplementary code points (beyond BMP) are correctly unescaped.
 * - Behavior when the terminating semicolon is missing depending on configured options.
 */
@Deprecated
class NumericEntityUnescaperTest extends AbstractLangTest {

    @ParameterizedTest(name = "Leaves input unchanged for dangling tail: \"{0}\"")
    @ValueSource(strings = {"&", "&#", "&#x", "&#X"})
    @DisplayName("Input ending with an incomplete numeric entity should be left unchanged")
    void leavesInputUnchangedForIncompleteEntityTails(final String tail) {
        // Given
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "Test " + tail;

        // When
        final String result = unescaper.translate(input);

        // Then
        assertEquals(input, result);
    }

    @Test
    @DisplayName("Correctly unescapes a supplementary code point")
    void unescapesSupplementaryCodePoint() {
        // Given
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "&#68642;"; // decimal for U+10C22

        // Expected: String from the supplementary code point U+10C22
        final int codePoint = 0x10C22;
        final String expected = new String(Character.toChars(codePoint));

        // When
        final String result = unescaper.translate(input);

        // Then
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("When semicolon is optional, unfinished entity is unescaped")
    void unescapesWhenSemicolonIsOptional() {
        // Given
        final NumericEntityUnescaper unescaper =
                new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        final String input = "Test &#x30 not test";

        // Then
        assertEquals("Test 0 not test", unescaper.translate(input));
    }

    @Test
    @DisplayName("When semicolon is required (default), unfinished entity is left unchanged")
    void leavesUnfinishedEntityWhenSemicolonIsRequired() {
        // Given
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper(); // default: semiColonRequired
        final String input = "Test &#x30 not test";

        // Then
        assertEquals(input, unescaper.translate(input));
    }

    @Test
    @DisplayName("When configured to error, missing semicolon causes an exception")
    void throwsWhenSemicolonIsMissingAndErrorOptionSet() {
        // Given
        final NumericEntityUnescaper unescaper =
                new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        final String input = "Test &#x30 not test";

        // Then
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input));
    }
}