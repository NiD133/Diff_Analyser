package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that a new Metaphone instance is initialized with the default
     * maximum code length of 4.
     */
    @Test
    public void shouldHaveDefaultMaxCodeLengthOfFourOnInitialization() {
        // Arrange: Create a new Metaphone instance.
        Metaphone metaphone = new Metaphone();

        // Act: Get the maximum code length from the new instance.
        int maxCodeLen = metaphone.getMaxCodeLen();

        // Assert: Verify that the length is the default value of 4.
        assertEquals("Default max code length should be 4", 4, maxCodeLen);
    }
}