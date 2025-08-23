package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

/**
 * Contains tests for the {@link FixedOrderComparator}.
 * This improved test focuses on verifying the default behavior of the comparator
 * when initialized with a list of items.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that a FixedOrderComparator, when constructed with a list of items,
     * defaults to the EXCEPTION behavior for handling unknown objects.
     */
    @Test
    public void shouldDefaultToExceptionBehaviorForUnknownObjectsWhenCreatedWithList() {
        // Arrange: Create a list of objects to define the comparison order.
        // The specific content of the list is not important for this test,
        // only that we use the list-based constructor.
        List<Object> knownObjects = Collections.singletonList(new Object());

        // Act: Instantiate the comparator with the list of known objects.
        FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(knownObjects);

        // Assert: Verify that the default behavior for handling unknown objects is EXCEPTION.
        final FixedOrderComparator.UnknownObjectBehavior expectedBehavior =
                FixedOrderComparator.UnknownObjectBehavior.EXCEPTION;
        final FixedOrderComparator.UnknownObjectBehavior actualBehavior =
                comparator.getUnknownObjectBehavior();
        
        assertEquals("The default unknown object behavior should be EXCEPTION",
                     expectedBehavior,
                     actualBehavior);
    }
}