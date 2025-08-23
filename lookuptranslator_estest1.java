package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    @Test
    public void shouldNotTranslateWhenInputDoesNotContainAnyLookupKeys() {
        // Arrange: Define a simple and clear lookup table.
        final CharSequence[][] lookupTable = {
            {"alpha", "α"},
            {"beta", "β"}
        };
        final LookupTranslator translator = new LookupTranslator(lookupTable);

        final String input = "The quick brown fox jumps over the lazy dog.";

        // Act: Translate the input string.
        final String result = translator.translate(input);

        // Assert: The string should remain unchanged because none of the keys ("alpha", "beta") were found.
        assertEquals(input, result);
    }
}