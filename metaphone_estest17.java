package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Metaphone class.
 */
public class MetaphoneTest {

    /**
     * Tests the Metaphone rule where 'B' is silent if it's at the end of a word and after an 'M'.
     * For example, the word "MB" should be encoded as "M".
     */
    @Test
    public void shouldEncodeWordEndingInMbAsM() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "MB";
        final String expectedMetaphone = "M";

        // Act
        final String actualMetaphone = metaphone.metaphone(input);

        // Assert
        assertEquals(expectedMetaphone, actualMetaphone);
    }
}