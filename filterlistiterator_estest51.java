package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for {@link FilterListIterator}.
 */
public class FilterListIteratorTest {

    /**
     * Tests that the add() method is unsupported.
     * <p>
     * The {@link FilterListIterator} is a read-only decorator for an iterator and
     * does not support modification operations like add(). This test ensures that
     * calling add() throws an {@link UnsupportedOperationException}.
     */
    @Test
    public void addShouldThrowUnsupportedOperationException() {
        // Arrange: Create a FilterListIterator.
        // No underlying iterator or predicate is needed to test this behavior.
        final FilterListIterator<Integer> iterator = new FilterListIterator<>();

        // Act & Assert: Verify that calling add() throws the expected exception.
        final UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> iterator.add(42)); // The element being added is irrelevant.

        // Assert: Check that the exception message is correct.
        final String expectedMessage = "FilterListIterator.add(Object) is not supported.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}