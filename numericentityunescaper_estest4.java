package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.IOException;

/**
 * Unit tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Tests that the translate method correctly handles input that is not a numeric entity.
     * When the character sequence at the given index does not start with the entity
     * prefix ('&'), the method should not consume any characters and return 0.
     */
    @Test
    public void testTranslateShouldReturnZeroForNonEntityInput() throws IOException {
        // Arrange: Create a default unescaper and an input string that is not an entity.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final StringWriter writer = new StringWriter();
        final String input = "This is a test string.";

        // Act: Attempt to translate from an index that does not start an entity.
        // The character at index 0 is 'T', not '&'.
        final int charactersConsumed = unescaper.translate(input, 0, writer);

        // Assert: Verify that no characters were consumed and nothing was written.
        assertEquals("Should not consume any characters if the input is not an entity", 0, charactersConsumed);
        assertEquals("The writer should remain empty", "", writer.toString());
    }
}