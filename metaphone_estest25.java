package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly handles the "PH" combination by
     * encoding it as "F". This test also verifies that leading non-alphabetic
     * characters are ignored during the encoding process.
     */
    @Test
    public void testMetaphoneEncodesPHAsFAndIgnoresLeadingNonAlphabeticChar() {
        // Arrange: Create a Metaphone instance and define the input string.
        // The input "&pH" contains a non-alphabetic character and the "ph" digraph,
        // which should be treated as "F".
        final Metaphone metaphone = new Metaphone();
        final String input = "&pH";
        final String expectedCode = "F";

        // Act: Generate the Metaphone code for the input string.
        final String actualCode = metaphone.metaphone(input);

        // Assert: Verify that the generated code is correct and the object's state is as expected.
        assertEquals("The 'PH' combination should be encoded as 'F'", expectedCode, actualCode);
        
        // This assertion also confirms that the default max code length is not altered by the encoding process.
        assertEquals("Default max code length should be 4", 4, metaphone.getMaxCodeLen());
    }
}