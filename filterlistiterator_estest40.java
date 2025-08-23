package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link FilterListIterator}.
 */
public class FilterListIteratorTest {

    /**
     * Tests that the set() method is not supported and throws an exception.
     * The set() operation is explicitly unsupported because filtering makes it
     * complex to determine the correct underlying element to modify.
     */
    @Test
    public void setShouldThrowUnsupportedOperationException() {
        // Arrange: Create a FilterListIterator.
        // No underlying iterator or predicate is needed as set() should fail immediately.
        final FilterListIterator<Object> iterator = new FilterListIterator<>();
        final Object newElement = "some-new-element";

        // Act & Assert: Verify that calling set() throws UnsupportedOperationException.
        final UnsupportedOperationException thrown = assertThrows(
            UnsupportedOperationException.class,
            () -> iterator.set(newElement)
        );

        // Assert: Check if the exception message is as expected.
        assertEquals("FilterListIterator.set(Object) is not supported.", thrown.getMessage());
    }
}