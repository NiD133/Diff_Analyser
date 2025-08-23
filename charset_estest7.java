package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSet}.
 */
public class CharSetTest {

    /**
     * Tests the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void testEqualsIsReflexive() {
        // Arrange
        CharSet asciiAlphaUpper = CharSet.ASCII_ALPHA_UPPER;

        // Act & Assert
        // An instance must be equal to itself.
        assertEquals(asciiAlphaUpper, asciiAlphaUpper);
    }
}