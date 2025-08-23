package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    /**
     * Tests that the constructor throws a NullPointerException if the main lookup array
     * contains a null element. The constructor iterates through each entry, and a null
     * entry will cause a NullPointerException when its elements are accessed.
     */
    @Test
    public void constructorShouldThrowNPEWhenLookupArrayContainsNullEntry() {
        // Arrange: Create a lookup table where one of the lookup entries is null.
        // The constructor expects an array of [key, value] pairs.
        final CharSequence[][] lookupTableWithNullEntry = new CharSequence[][]{
                {"one", "1"}, // A valid entry
                null          // An invalid, null entry that should cause an exception
        };

        // Act & Assert: Verify that creating a LookupTranslator with this table
        // throws a NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            new LookupTranslator(lookupTableWithNullEntry);
        });
    }
}