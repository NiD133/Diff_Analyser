package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    /**
     * Tests that the constructor throws an exception when a lookup array
     * contains an empty inner array instead of a key-value pair.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void constructorShouldThrowExceptionForMalformedLookupPair() {
        // Arrange: Create a lookup table where an entry is an empty array,
        // which is invalid because it should be a [key, value] pair.
        final CharSequence[][] malformedLookupTable = new CharSequence[1][0];

        // Act & Assert: Instantiating the translator with the malformed table
        // should throw an ArrayIndexOutOfBoundsException.
        new LookupTranslator(malformedLookupTable);
    }
}