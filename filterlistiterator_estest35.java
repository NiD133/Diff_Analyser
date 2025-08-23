package org.apache.commons.collections4.iterators;

import org.junit.Test;

/**
 * Tests for {@link FilterListIterator}.
 * This class demonstrates a clear and focused way to test its behavior.
 */
public class FilterListIteratorTest {

    /**
     * Verifies that the remove() method is not supported and consistently
     * throws an UnsupportedOperationException.
     * <p>
     * The {@link FilterListIterator#remove()} operation is explicitly not
     * implemented, and this test ensures that contract is met.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeShouldThrowUnsupportedOperationException() {
        // Arrange: Create a simple FilterListIterator instance.
        // The internal state or wrapped iterator is irrelevant, as remove()
        // is unconditionally unsupported.
        final FilterListIterator<Object> iterator = new FilterListIterator<>();

        // Act: Attempt to call the remove() method.
        iterator.remove();

        // Assert: The test expects an UnsupportedOperationException,
        // which is handled by the @Test(expected=...) annotation.
    }
}