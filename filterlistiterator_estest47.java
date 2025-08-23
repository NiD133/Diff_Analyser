package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.ListIterator;

/**
 * Contains tests for the {@link FilterListIterator} class, focusing on specific behaviors.
 */
public class FilterListIteratorTest {

    /**
     * Tests that calling forEachRemaining with a null consumer throws a NullPointerException.
     * This behavior is inherited from the default implementation in the java.util.Iterator interface.
     */
    @Test(expected = NullPointerException.class)
    public void forEachRemainingShouldThrowNullPointerExceptionWhenConsumerIsNull() {
        // Arrange: Create a FilterListIterator.
        // The underlying iterator can be null for this test because the check for the
        // null consumer argument happens before the underlying iterator is accessed.
        FilterListIterator<Object> iterator = new FilterListIterator<>((ListIterator<?>) null);

        // Act: Call the method under test with a null argument.
        // The @Test(expected=...) annotation will handle the assertion.
        iterator.forEachRemaining(null);
    }
}