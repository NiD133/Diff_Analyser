package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for {@link MurmurHash2}.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash64 throws a StringIndexOutOfBoundsException when called with a negative 'from' index.
     * This is the expected behavior, as the method internally calls String.substring(), which enforces
     * non-negative indices.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testHash64StringWithNegativeFromIndexShouldThrowException() {
        // Define test inputs with clear names
        final String text = "any string";
        final int negativeFromIndex = -1;
        final int anyLength = 5;

        // This call is expected to throw StringIndexOutOfBoundsException
        MurmurHash2.hash64(text, negativeFromIndex, anyLength);
    }
}