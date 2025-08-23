package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertThrows;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * Contains tests for the {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that calling next() on an iterator that was constructed with a null
     * root iterator correctly throws a NoSuchElementException.
     */
    @Test
    public void testNextThrowsExceptionWhenConstructedWithNullIterator() {
        // Arrange: Create an ObjectGraphIterator with a null root iterator,
        // which signifies that the iterator is empty from the start.
        final Iterator<?> nullRootIterator = null;
        final ObjectGraphIterator<Object> emptyIterator = new ObjectGraphIterator<>(nullRootIterator);

        // Act & Assert: Verify that calling next() on this empty iterator
        // throws a NoSuchElementException.
        assertThrows(NoSuchElementException.class, () -> emptyIterator.next());
    }
}