package org.apache.commons.lang3.text.translate;

import org.junit.Test;

/**
 * Tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void constructorShouldThrowExceptionForEmptyLookupKey() {
        // Arrange: Create a lookup table where one of the keys is an empty string.
        final CharSequence[][] lookupTable = {
            {"", "value"} // The key is empty, the value is arbitrary.
        };

        // Act & Assert: The constructor is expected to throw an IndexOutOfBoundsException
        // because it attempts to access the first character of the empty key to
        // populate its internal prefix set.
        new LookupTranslator(lookupTable);
    }
}