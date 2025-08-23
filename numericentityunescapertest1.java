package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
// The class name has been improved from "NumericEntityUnescaperTestTest1"
// to the standard convention "NumericEntityUnescaperTest".
public class NumericEntityUnescaperTest extends AbstractLangTest {

    private NumericEntityUnescaper unescaper;

    @BeforeEach
    void setUp() {
        // The unescaper is created with default options (semicolon required).
        unescaper = new NumericEntityUnescaper();
    }

    @DisplayName("Should not translate incomplete numeric entities at the end of the input")
    @ParameterizedTest(name = "Input: \"{0}\"")
    @ValueSource(strings = {
        "Test &",    // Ampersand alone
        "Test &#",   // Incomplete decimal entity
        "Test &#x",  // Incomplete hex entity (lowercase)
        "Test &#X"   // Incomplete hex entity (uppercase)
    })
    void translateShouldIgnoreIncompleteEntityAtEndOfInput(final String input) {
        // Act
        final String result = unescaper.translate(input);

        // Assert
        // The unescaper should not change the input string if the entity is incomplete.
        assertEquals(input, result);
    }
}