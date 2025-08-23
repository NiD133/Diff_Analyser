package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    @Test
    public void shouldReturnDefaultMaxCodeLengthOnNewInstance() {
        // Arrange
        // The Metaphone class documentation specifies a default max code length of 4.
        Metaphone metaphone = new Metaphone();
        int expectedMaxCodeLen = 4;

        // Act
        int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("A new Metaphone instance should have the default max code length.",
                     expectedMaxCodeLen, actualMaxCodeLen);
    }
}