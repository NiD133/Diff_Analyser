package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that a new Metaphone instance is initialized with the correct
     * default maximum code length.
     */
    @Test
    public void shouldHaveDefaultMaxCodeLengthOfFourOnNewInstance() {
        // Arrange: Create a new instance of the Metaphone encoder.
        final Metaphone metaphone = new Metaphone();
        final int expectedMaxCodeLen = 4;

        // Act: Get the maximum code length from the new instance.
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert: Verify that the code length matches the expected default value.
        assertEquals("The default max code length should be 4", expectedMaxCodeLen, actualMaxCodeLen);
    }
}