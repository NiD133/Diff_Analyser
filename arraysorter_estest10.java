package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that {@code ArraySorter.sort(T[])} throws a {@code NullPointerException}
     * when the input array contains a null element. This is the expected behavior
     * because the method delegates to {@code java.util.Arrays.sort(Object[])}, which
     * does not permit null elements when using natural ordering.
     */
    @Test
    public void sortObjectArrayContainingNullShouldThrowNullPointerException() {
        // Arrange: Create an array of a comparable type that includes a null element.
        final Integer[] arrayWithNull = {42, 1, null, 7};

        // Act & Assert: Verify that sorting this array throws a NullPointerException.
        assertThrows(NullPointerException.class, () -> ArraySorter.sort(arrayWithNull));
    }
}