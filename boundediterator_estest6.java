package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the constructor of {@link BoundedIterator}.
 */
public class BoundedIteratorConstructorTest {

    /**
     * Tests that the constructor throws a NullPointerException when the decorated
     * iterator is null. The constructor contract explicitly forbids a null iterator.
     */
    @Test
    public void testConstructorThrowsNullPointerExceptionForNullIterator() {
        // GIVEN: A null iterator argument.
        // The offset and max arguments are valid but arbitrary for this test.
        final long offset = 0L;
        final long max = 10L;

        try {
            // WHEN: A BoundedIterator is constructed with a null iterator.
            new BoundedIterator<Object>(null, offset, max);
            fail("Expected a NullPointerException to be thrown for a null iterator.");
        } catch (final NullPointerException e) {
            // THEN: A NullPointerException is caught, and its message is verified.
            // The message "iterator" is expected from Objects.requireNonNull(iterator, "iterator").
            assertEquals("iterator", e.getMessage());
        }
    }
}