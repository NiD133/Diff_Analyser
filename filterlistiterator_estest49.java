package org.apache.commons.collections4.iterators;

import org.junit.Test;

/**
 * Tests for {@link FilterListIterator}.
 * This test focuses on the behavior of the remove() method.
 */
public class FilterListIteratorTest {

    @Test(expected = UnsupportedOperationException.class)
    public void removeShouldThrowUnsupportedOperationException() {
        // Given: A default FilterListIterator instance
        FilterListIterator<Object> iterator = new FilterListIterator<>();

        // When: The remove() method is called
        iterator.remove();

        // Then: An UnsupportedOperationException is thrown (verified by the @Test annotation)
    }
}