package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the {@link ComparatorChain} class, focusing on exception-throwing scenarios.
 */
public class ComparatorChain_ESTestTest14 {

    /**
     * Tests that calling setComparator() with an index that is out of bounds
     * for an empty chain throws an IndexOutOfBoundsException.
     */
    @Test
    public void setComparator_shouldThrowIndexOutOfBoundsException_whenIndexIsOutOfRangeForEmptyChain() {
        // Arrange: Create an empty ComparatorChain.
        final ComparatorChain<String> emptyChain = new ComparatorChain<>();
        final int invalidIndex = 2;
        // A dummy comparator is needed for the method call, but its behavior is irrelevant.
        final Comparator<String> dummyComparator = (s1, s2) -> 0;

        // Act & Assert: Verify that calling setComparator with an out-of-bounds index
        // throws the expected exception with a specific, informative message.
        final IndexOutOfBoundsException exception = assertThrows(
            IndexOutOfBoundsException.class,
            () -> emptyChain.setComparator(invalidIndex, dummyComparator)
        );

        // The expected message format from ArrayList is "Index: %d, Size: %d"
        assertEquals("Index: 2, Size: 0", exception.getMessage());
    }
}