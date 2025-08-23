package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.Iterator;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Contains tests for the BoundedIterator class.
 */
public class BoundedIteratorTest {

    /**
     * Tests that two distinct BoundedIterator instances are not considered equal.
     *
     * BoundedIterator does not override the default Object.equals() method, which
     * checks for reference equality. This test confirms that behavior by creating
     * two separate instances and asserting they are not equal.
     */
    @Test
    public void twoDistinctInstancesShouldNotBeEqual() {
        // Arrange
        // Create a mock underlying iterator. Its behavior is not critical for this test,
        // but it must be valid for the BoundedIterator constructor to succeed.
        @SuppressWarnings("unchecked")
        final Iterator<String> mockIterator = mock(Iterator.class);
        when(mockIterator.hasNext()).thenReturn(true);
        when(mockIterator.next()).thenReturn("any element");

        // Create two different BoundedIterator instances.
        final BoundedIterator<String> iterator1 = new BoundedIterator<>(mockIterator, 1L, 10L);
        final BoundedIterator<String> iterator2 = new BoundedIterator<>(iterator1, 5L, 2L);

        // Act & Assert
        // Verify that the two distinct instances are not equal, as per default Object.equals() behavior.
        assertNotEquals(iterator1, iterator2);
    }
}