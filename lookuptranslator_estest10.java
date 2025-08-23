package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    @Test
    public void translateShouldReplaceMatchingKeyInString() {
        // Arrange: Define a lookup table where "1" is translated to "FFFFF15A".
        // The original test created this mapping in a very complex way. This simplified
        // setup captures the essential logic that was being tested.
        final CharSequence[][] lookupTable = {
            {"1", "FFFFF15A"}
        };
        final LookupTranslator translator = new LookupTranslator(lookupTable);

        // Act: Translate a string that contains the lookup key.
        final String input = "FFFFF15A";
        final String actualOutput = translator.translate(input);

        // Assert: The key "1" in the input string should be replaced by its corresponding value.
        // The expected output is constructed by taking the parts of the input string
        // before and after the key, and inserting the translated value in between.
        // "FFFFF" (before) + "FFFFF15A" (translation) + "5A" (after)
        final String expectedOutput = "FFFFFFFFFFF15A5A";
        assertEquals(expectedOutput, actualOutput);
    }
}