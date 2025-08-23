package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class, focusing on its property accessors.
 */
public class MetaphoneTest {

    /**
     * Tests that the maxCodeLen property can be set and retrieved correctly.
     * The Metaphone implementation does not validate this value, so any integer,
     * including a negative one, should be accepted by the setter and returned by the getter.
     */
    @Test
    public void shouldSetAndGetMaxCodeLen() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final int expectedMaxCodeLen = -10; // Use a simple, representative negative value

        // Act
        metaphone.setMaxCodeLen(expectedMaxCodeLen);
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("The retrieved max code length should match the value that was set.",
                     expectedMaxCodeLen, actualMaxCodeLen);
    }
}