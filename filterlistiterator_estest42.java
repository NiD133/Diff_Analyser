package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.ListIterator;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link FilterListIterator} focusing on its behavior when no
 * underlying iterator is configured.
 */
public class FilterListIteratorUninitializedTest {

    @Test
    public void getListIterator_whenNoIteratorIsSet_shouldReturnNull() {
        // Arrange: Create a FilterListIterator without providing an underlying iterator.
        final FilterListIterator<Object> filterListIterator = new FilterListIterator<>();

        // Act: Retrieve the underlying iterator.
        final ListIterator<?> underlyingIterator = filterListIterator.getListIterator();

        // Assert: The returned iterator should be null.
        assertNull("getListIterator() should return null when no iterator has been set.", underlyingIterator);
    }

    @Test
    public void hasNext_whenNoIteratorIsSet_shouldThrowNullPointerException() {
        // Arrange: Create a FilterListIterator without providing an underlying iterator.
        final FilterListIterator<Object> filterListIterator = new FilterListIterator<>();

        // Act & Assert: Calling hasNext() requires an underlying iterator to check.
        // Since one was never provided, a NullPointerException is expected.
        assertThrows(
            "hasNext() should throw a NullPointerException when no iterator has been set.",
            NullPointerException.class,
            filterListIterator::hasNext
        );
    }
}