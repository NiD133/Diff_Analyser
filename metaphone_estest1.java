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
        this.metaphone = new Metaphone();
    }

    /**
     * Tests that a new Metaphone instance is configured with the correct
     * default maximum code length.
     */
    @Test
    public void shouldHaveDefaultMaxCodeLengthOfFour() {
        // Arrange
        final int expectedMaxCodeLen = 4;

        // Act
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4", expectedMaxCodeLen, actualMaxCodeLen);
    }

    /**
     * Tests a basic encoding case to ensure the algorithm produces the correct output.
     */
    @Test
    public void shouldEncodeWordCorrectly() {
        // Arrange
        final String input = "acw";
        final String expectedCode = "AK";

        // Act
        final String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("The Metaphone code for 'acw' should be 'AK'", expectedCode, actualCode);
    }
}