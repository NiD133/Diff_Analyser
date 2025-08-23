package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class, focusing on specific encoding rules and configurations.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly encodes a complex string involving
     * multiple transformation rules.
     *
     * <p>The transformation of "WHeee`OhCYD" to "WST" demonstrates the following rules:</p>
     * <ul>
     *   <li><b>'WH'</b> at the start of a word is encoded as 'W'.</li>
     *   <li><b>Vowels</b> ('e', 'o') after the first letter are ignored.</li>
     *   <li><b>Non-alphabetic characters</b> ('`') are ignored.</li>
     *   <li><b>'CY'</b> is encoded as 'S'.</li>
     *   <li><b>'D'</b> is encoded as 'T'.</li>
     * </ul>
     */
    @Test
    public void testMetaphoneEncodingForComplexWord() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "WHeee`OhCYD";
        final String expectedEncoding = "WST";

        // Act
        final String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("Encoding should correctly apply multiple Metaphone rules", expectedEncoding, actualEncoding);
    }

    /**
     * Verifies that a new Metaphone instance is created with the default maximum
     * code length of 4.
     */
    @Test
    public void testDefaultMaxCodeLengthIsFour() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final int expectedMaxCodeLen = 4;

        // Act
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4", expectedMaxCodeLen, actualMaxCodeLen);
    }
}