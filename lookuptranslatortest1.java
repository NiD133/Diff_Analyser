package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LookupTranslator}.
 */
// Class name improved for clarity and to follow standard naming conventions.
// The original "TestTest1" suffix was redundant.
public class LookupTranslatorTest extends AbstractLangTest {

    @Test
    void translate_whenMatchFound_shouldWriteTranslationAndReturnConsumedLength() throws IOException {
        // Arrange
        // Define a lookup table to translate "one" into "two".
        final CharSequence[][] lookupTable = {{"one", "two"}};
        final LookupTranslator translator = new LookupTranslator(lookupTable);
        final StringWriter writer = new StringWriter();
        final String input = "one";

        // Act
        // Attempt to translate the input string from the beginning (index 0).
        final int consumedChars = translator.translate(input, 0, writer);

        // Assert
        // The method should consume the entire input "one", which has a length of 3.
        assertEquals(3, consumedChars, "The number of consumed characters should be the length of the matched input 'one'.");
        
        // The writer should contain the translated value "two".
        final String translatedOutput = writer.toString();
        assertEquals("two", translatedOutput, "The translated output should be 'two'.");
    }
}