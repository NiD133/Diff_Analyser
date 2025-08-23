package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link BoundedIterator}.
 * This test case focuses on constructor validation.
 */
// Note: The original class name "BoundedIterator_ESTestTest12" was kept,
// but a more descriptive name like "BoundedIteratorTest" is recommended.
public class BoundedIterator_ESTestTest12 extends BoundedIterator_ESTest_scaffolding {

    /**
     * Tests that the BoundedIterator constructor throws an IllegalArgumentException
     * when the 'max' parameter is negative.
     */
    @Test
    public void constructorShouldThrowIllegalArgumentExceptionForNegativeMax() {
        // Arrange: Define parameters for the constructor.
        // The iterator can be null because the validation for 'max' occurs before the iterator is used.
        final Iterator<Object> nullIterator = null;
        final long offset = 0L;
        final long negativeMax = -1L;

        // Act & Assert: Attempt to create the iterator and verify the resulting exception.
        try {
            new BoundedIterator<>(nullIterator, offset, negativeMax);
            fail("Expected an IllegalArgumentException to be thrown due to the negative 'max' parameter.");
        } catch (final IllegalArgumentException e) {
            // Assert: Verify that the exception message is correct.
            assertEquals("Max parameter must not be negative.", e.getMessage());
        }
    }
}