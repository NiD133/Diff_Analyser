package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Contains tests for the {@link FixedOrderComparator} class, focusing on its modification methods.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that addAsEqual() throws an IllegalArgumentException when attempting
     * to add a new object as equal to an object that is not already known to the comparator.
     */
    @Test
    public void addAsEqualShouldThrowExceptionWhenExistingObjectIsUnknown() {
        // Arrange: Create a comparator with a predefined order of known items.
        final String[] knownItems = {"A", "B", "C"};
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(knownItems);

        final String unknownObject = "X"; // This object is not in the initial list.
        final String newObject = "Y";

        // Act & Assert: Verify that calling addAsEqual with an unknown "existing" object
        // throws the expected exception.
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> comparator.addAsEqual(unknownObject, newObject)
        );

        // Further assert that the exception message is informative.
        assertTrue(
            exception.getMessage().contains(unknownObject),
            "Exception message should contain the unknown object."
        );
        assertTrue(
            exception.getMessage().contains("not known to"),
            "Exception message should state that the object is not known."
        );
    }
}