package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.Iterator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

/**
 * Contains tests for the {@link BoundedIterator} class.
 * This test suite focuses on verifying the behavior of the remove() method.
 */
public class BoundedIteratorTest {

    /**
     * Tests that calling remove() on the BoundedIterator successfully
     * delegates the remove call to the underlying iterator. This is valid only
     * after a call to next() has been made.
     */
    @Test
    public void removeAfterNextShouldDelegateToUnderlyingIterator() {
        // Arrange
        // 1. Create a mock for the underlying iterator that BoundedIterator will wrap.
        // We use a generic type as the element's value is not important for this test.
        @SuppressWarnings("unchecked") // Safe as we define the mock's behavior.
        final Iterator<Object> mockUnderlyingIterator = mock(Iterator.class);

        // 2. Define the mock's behavior. It should indicate it has elements.
        when(mockUnderlyingIterator.hasNext()).thenReturn(true);
        when(mockUnderlyingIterator.next()).thenReturn(new Object());

        // 3. Create the BoundedIterator instance to be tested.
        // We use an offset of 0 and a max of 10.
        final BoundedIterator<Object> boundedIterator =
            new BoundedIterator<>(mockUnderlyingIterator, 0L, 10L);

        // Act
        // First, call next() to position the iterator, which is a prerequisite for calling remove().
        boundedIterator.next();
        // Then, call the method under test.
        boundedIterator.remove();

        // Assert
        // Verify that the remove() method was called exactly once on the underlying iterator.
        // This confirms that the BoundedIterator correctly delegates the operation.
        verify(mockUnderlyingIterator, times(1)).remove();
    }
}