package org.apache.commons.codec.language;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Metaphone class.
 */
public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        this.metaphone = new Metaphone();
    }

    /**
     * Tests that the Metaphone algorithm correctly encodes a string containing "CK".
     * According to the standard Metaphone algorithm, "CK" is treated as "K".
     */
    @Test
    public void shouldEncodeStringWithCKAsK() {
        // Arrange
        final String input = "CKJ";
        // The expected encoding is "KJ" because "CK" -> "K" and "J" -> "J".
        // The original test asserted "XJ", which appears to be incorrect.
        final String expectedEncoding = "KJ";

        // Act
        final String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("Encoding for 'CKJ' was incorrect.", expectedEncoding, actualEncoding);
    }

    /**
     * Tests that a new Metaphone instance is created with the default maximum code length.
     */
    @Test
    public void shouldCreateInstanceWithDefaultMaxCodeLength() {
        // Arrange
        final int expectedDefaultLength = 4;

        // Act
        final int actualLength = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4.", expectedDefaultLength, actualLength);
    }
}