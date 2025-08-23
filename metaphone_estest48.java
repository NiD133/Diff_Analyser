package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that isMetaphoneEqual() returns false for two strings
     * that have different Metaphone encodings.
     */
    @Test
    public void testIsMetaphoneEqualReturnsFalseForDissimilarStrings() {
        // Arrange
        Metaphone metaphone = new Metaphone();

        // Act & Assert
        // The Metaphone code for "I" is "I", while for "X" it is "S".
        // Therefore, the two strings are not considered equal.
        assertFalse("Strings with different Metaphone codes should not be equal",
                    metaphone.isMetaphoneEqual("I", "X"));
    }

    /**
     * Tests that a new Metaphone instance has the default max code length of 4.
     */
    @Test
    public void testDefaultMaxCodeLength() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        int expectedMaxCodeLen = 4;

        // Act
        int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals(expectedMaxCodeLen, actualMaxCodeLen);
    }
}