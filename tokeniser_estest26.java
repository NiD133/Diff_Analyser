package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the static unescapeEntities method in the {@link Parser} class.
 */
public class ParserEntitiesTest {

    /**
     * Verifies that a string containing an ampersand that is not part of a valid
     * HTML entity is returned unchanged.
     */
    @Test
    public void unescapeEntitiesWithInvalidReferenceReturnsOriginalString() {
        // Arrange: A string with a sequence that looks like the start of an entity ("&pr")
        // but is not a valid named character reference.
        String input = "c|jj9.a5&pr)$";
        String expected = "c|jj9.a5&pr)$";

        // Act: Call the method under test. The 'inAttribute' flag is true, but the behavior
        // is the same for this input regardless of the flag's value.
        String actual = Parser.unescapeEntities(input, true);

        // Assert: The output should be identical to the input.
        assertEquals(expected, actual);
    }
}