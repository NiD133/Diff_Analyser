package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.NoSuchElementException;

/**
 * Tests for {@link FilterListIterator}.
 * This class focuses on the behavior of the iterator when it is not properly initialized.
 */
public class FilterListIteratorTest {

    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowNoSuchElementExceptionWhenIteratorNotSet() {
        // Arrange: Create a FilterListIterator using the default constructor,
        // which leaves the underlying iterator as null.
        FilterListIterator<Object> emptyFilterIterator = new FilterListIterator<>();

        // Act: Calling next() on an iterator without a backing ListIterator
        // should fail.
        emptyFilterIterator.next();
    }
}