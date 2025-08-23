package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.text.translate.NumericEntityUnescaper}.
 */
@Deprecated
class NumericEntityUnescaperTest extends AbstractLangTest {

    /**
     * Tests that the NumericEntityUnescaper ignores incomplete numeric entities
     * when the last character is an ampersand or hash symbol.
     */
    @Test
    void testIgnoreIncompleteEntities() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertEquals("Test &", unescaper.translate("Test &"), "Should ignore when last character is &");
        assertEquals("Test &#", unescaper.translate("Test &#"), "Should ignore when last character is &#");
        assertEquals("Test &#x", unescaper.translate("Test &#x"), "Should ignore when last character is &#x");
        assertEquals("Test &#X", unescaper.translate("Test &#X"), "Should ignore when last character is &#X");
    }

    /**
     * Tests that the NumericEntityUnescaper correctly unescapes supplementary characters.
     */
    @Test
    void testUnescapeSupplementaryCharacters() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "&#68642;";
        final String expectedOutput = "\uD803\uDC22";

        final String actualOutput = unescaper.translate(input);
        assertEquals(expectedOutput, actualOutput, "Failed to unescape supplementary characters");
    }

    /**
     * Tests the behavior of the NumericEntityUnescaper with unfinished numeric entities.
     */
    @Test
    void testUnfinishedNumericEntities() {
        // Test with semicolon optional
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        String input = "Test &#x30 not test";
        String expectedOutput = "Test \u0030 not test";

        String actualOutput = unescaper.translate(input);
        assertEquals(expectedOutput, actualOutput, "Failed to handle unfinished entities with optional semicolon");

        // Test with default behavior (ignore unfinished entities)
        unescaper = new NumericEntityUnescaper();
        input = "Test &#x30 not test";
        expectedOutput = input;

        actualOutput = unescaper.translate(input);
        assertEquals(expectedOutput, actualOutput, "Failed to ignore unfinished entities with default behavior");

        // Test with error on missing semicolon
        final NumericEntityUnescaper errorUnescaper =
                new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        final String errorInput = "Test &#x30 not test";
        assertIllegalArgumentException(() -> errorUnescaper.translate(errorInput), "Should throw exception on missing semicolon");
    }
}