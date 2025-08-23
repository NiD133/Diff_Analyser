package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting a null char array returns null,
     * maintaining the null-safe behavior of the method.
     */
    @Test
    public void sort_withNullCharArray_shouldReturnNull() {
        // Act: Call the sort method with a null array.
        // The explicit cast is necessary to resolve method overload ambiguity.
        final char[] result = ArraySorter.sort((char[]) null);

        // Assert: Verify that the result is null.
        assertNull(result);
    }
}