package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertNull;

import java.util.ListIterator;
import org.junit.Test;

/**
 * Unit tests for {@link FilterListIterator}.
 */
public class FilterListIteratorTest {

    @Test
    public void getListIteratorShouldReturnNullWhenCreatedWithDefaultConstructor() {
        // Arrange: Create a FilterListIterator using its no-argument constructor.
        // This constructor initializes the internal iterator to null.
        final FilterListIterator<Object> filterIterator = new FilterListIterator<>();

        // Act: Retrieve the underlying iterator.
        final ListIterator<?> wrappedIterator = filterIterator.getListIterator();

        // Assert: The underlying iterator should be null as it has not been set.
        assertNull("The wrapped iterator should be null after default construction", wrappedIterator);
    }
}