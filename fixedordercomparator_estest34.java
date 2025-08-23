package org.apache.commons.collections4.comparators;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This test verifies the behavior of the FixedOrderComparator when comparing an unknown object.
 * It replaces an auto-generated test that was difficult to understand.
 */
public class FixedOrderComparator_ESTestTest34 { // Note: Class name kept for context

    /**
     * Tests that compare() throws an IllegalArgumentException by default
     * when the first object to compare is not in the fixed order.
     */
    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenComparingUnknownFirstObject() {
        // Arrange: Create a comparator with a defined order of known objects.
        final String[] knownOrder = {"Mercury", "Venus", "Earth"};
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(knownOrder);

        final String knownObject = "Venus";
        final String unknownObject = "Mars"; // This object is not in the known order.

        // Act & Assert: Expect an exception when comparing the unknown object.
        try {
            comparator.compare(unknownObject, knownObject);
            fail("Expected an IllegalArgumentException because 'Mars' is an unknown object.");
        } catch (final IllegalArgumentException e) {
            // Verify the exception message clearly identifies the unknown object.
            final String expectedMessage = "Attempting to compare unknown object " + unknownObject;
            assertTrue(
                "The exception message should indicate which object is unknown.",
                e.getMessage().contains(expectedMessage)
            );
        }
    }
}