package org.apache.commons.codec.language;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    /**
     * Tests that the Metaphone algorithm correctly encodes the initial "SC" combination as "SK".
     * This is a specific rule in the Metaphone encoding standard.
     */
    @Test
    public void shouldEncodeInitialScAsSk() {
        // Arrange
        final String input = "SC";
        final String expectedEncoding = "SK";

        // Act
        final String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("The encoding for 'SC' should be 'SK'", expectedEncoding, actualEncoding);
    }

    /**
     * Tests that a new Metaphone instance has a default maximum code length of 4.
     */
    @Test
    public void shouldHaveDefaultMaxCodeLengthOf4() {
        // Assert
        assertEquals(4, metaphone.getMaxCodeLen());
    }
}