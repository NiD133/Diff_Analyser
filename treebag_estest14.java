package org.apache.commons.collections4.bag;

import org.junit.Test;
import java.util.Comparator;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

/**
 * Contains tests for the {@link TreeBag#comparator()} method.
 */
public class TreeBagComparatorTest {

    /**
     * Tests that the comparator() method returns the same comparator instance
     * that was provided in the constructor.
     */
    @Test
    public void shouldReturnTheComparatorUsedInConstructor() {
        // Arrange: Create a mock comparator to be used by the TreeBag.
        @SuppressWarnings("unchecked") // Safe for mock creation of a generic type
        final Comparator<Object> expectedComparator = mock(Comparator.class);
        final TreeBag<Object> bag = new TreeBag<>(expectedComparator);

        // Act: Retrieve the comparator from the bag.
        final Comparator<? super Object> actualComparator = bag.comparator();

        // Assert: The retrieved comparator should be the exact same instance
        // as the one passed to the constructor.
        assertSame("The returned comparator should be the same instance as the one provided at construction.",
                expectedComparator, actualComparator);
    }
}