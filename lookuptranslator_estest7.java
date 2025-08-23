package org.apache.commons.lang3.text.translate;

import org.junit.Test;

/**
 * Tests for {@link LookupTranslator}.
 * This class focuses on specific constructor validation scenarios.
 */
// The original test class name 'LookupTranslator_ESTestTest7' is kept for context,
// but in a real-world scenario, it would be merged into a single 'LookupTranslatorTest'.
public class LookupTranslator_ESTestTest7 {

    /**
     * Verifies that the constructor throws a StringIndexOutOfBoundsException
     * when a lookup key is an empty string. The constructor attempts to access
     * the first character of each key, which is not possible for an empty string.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void constructorShouldThrowExceptionForEmptyLookupKey() {
        // Arrange: Define a lookup table with a key-value pair where the key is empty.
        final CharSequence[][] lookupTable = {
            { "", "value" }
        };

        // Act & Assert: Instantiating the translator with this table should throw
        // a StringIndexOutOfBoundsException. The expected exception is declared
        // in the @Test annotation, making the test fail if it's not thrown.
        new LookupTranslator(lookupTable);
    }
}