package org.apache.commons.collections4.comparators;

import org.junit.Test;

/**
 * This test case focuses on the behavior of the FixedOrderComparator's
 * setUnknownObjectBehavior method.
 *
 * Note: This class name is retained from the original auto-generated test suite.
 * In a real-world scenario, it would be renamed to something more descriptive,
 * like FixedOrderComparatorTest.
 */
public class FixedOrderComparator_ESTestTest7 { // Retaining original class name for context

    /**
     * Tests that calling setUnknownObjectBehavior with a null argument
     * throws a NullPointerException, as specified by the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void setUnknownObjectBehaviorShouldThrowNullPointerExceptionWhenBehaviorIsNull() {
        // Arrange: Create a simple instance of the comparator.
        // The specific type parameter (e.g., String) is not important for this test.
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>();

        // Act: Attempt to set the behavior to null.
        // The @Test(expected) annotation will handle the assertion.
        comparator.setUnknownObjectBehavior(null);
    }
}