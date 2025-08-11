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

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link org.apache.commons.lang3.text.translate.NumericEntityUnescaper}.
 */
@Deprecated
@SuppressWarnings("deprecation") // The class under test is deprecated
class NumericEntityUnescaperTest extends AbstractLangTest {

    @Test
    void shouldUnescapeDecimalEntityForSupplementaryCharacter() {
        // Arrange
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "&#68642;";
        final String expected = "\uD803\uDC22"; // U+10C42

        // Act
        final String result = unescaper.translate(input);

        // Assert
        assertEquals(expected, result, "Failed to unescape a supplementary character.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test &", "Test &#", "Test &#x", "Test &#X"})
    void shouldNotTranslateIncompleteEntityAtEndOfInput(final String incompleteEntity) {
        // Arrange
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Act
        final String result = unescaper.translate(incompleteEntity);

        // Assert
        // The unescaper should leave the malformed input unchanged.
        assertEquals(incompleteEntity, result);
    }

    @Test
    void shouldUnescapeEntityWhenSemicolonIsOptional() {
        // Arrange
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        final String inputWithMissingSemicolon = "Test &#x30 not test"; // &#x30 is '0'
        final String expected = "Test 0 not test";

        // Act
        final String result = unescaper.translate(inputWithMissingSemicolon);

        // Assert
        assertEquals(expected, result, "Failed to unescape entity when semicolon is optional.");
    }

    @Test
    void shouldNotUnescapeEntityWhenSemicolonIsRequiredByDefault() {
        // Arrange
        // The default constructor requires a semicolon for a valid entity.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String inputWithMissingSemicolon = "Test &#x30 not test";

        // Act
        final String result = unescaper.translate(inputWithMissingSolon);

        // Assert
        // The input should be returned unchanged because the entity is considered malformed.
        assertEquals(inputWithMissingSemicolon, result, "Should not unescape entity when semicolon is required and missing.");
    }

    @Test
    void shouldThrowExceptionForMissingSemicolonWhenConfiguredToError() {
        // Arrange
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        final String inputWithMissingSemicolon = "Test &#x30 not test";

        // Act & Assert
        assertIllegalArgumentException(
            () -> unescaper.translate(inputWithMissingSemicolon),
            "Should throw an exception for a missing semicolon when configured to do so."
        );
    }
}