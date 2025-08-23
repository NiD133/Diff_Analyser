package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link ObjectGraphIterator}.
 * This test focuses on the behavior of the next() method after the iterator has been exhausted.
 */
public class ObjectGraphIteratorTest {

    /**
     * Verifies that calling next() throws a NoSuchElementException
     * after the iterator's traversal has been completed by an explicit
     * call to the internal findNext() method.
     */
    @Test
    public void nextShouldThrowExceptionWhenIteratorIsExhaustedByFindNext() {
        // Arrange: Create an ObjectGraphIterator with an empty root iterator.
        // This means the iterator is initially exhausted.
        final Iterator<String> emptyRootIterator = Collections.emptyIterator();
        final ObjectGraphIterator<String> graphIterator = new ObjectGraphIterator<>(emptyRootIterator);

        // Act: Call the protected findNext() method with another empty iterator.
        // This simulates an internal state change that confirms the iterator is fully traversed.
        // This is a white-box test of the iterator's state machine.
        final Iterator<String> anotherEmptyIterator = Collections.emptyIterator();
        graphIterator.findNext(anotherEmptyIterator);

        // Assert: The iterator should report that it has no more elements.
        assertFalse("Iterator should be exhausted after the call to findNext", graphIterator.hasNext());

        // Assert: Calling next() on the exhausted iterator must throw an exception.
        assertThrows(NoSuchElementException.class, graphIterator::next);
    }
}