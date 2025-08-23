package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link FixedOrderComparator}.
 * Note: The original test class name "FixedOrderComparator_ESTestTest2" was kept
 * to match the request, but a more conventional name would be "FixedOrderComparatorTest".
 */
public class FixedOrderComparator_ESTestTest2 {

    @Test
    public void compare_withKnownObjects_shouldReturnNegativeWhenFirstObjectPrecedesSecond() {
        // Arrange
        // Define a specific order of items. Using simple Strings makes the test case
        // easy to understand. The order includes a null element, which is valid.
        final String firstItem = "first";
        final String secondItem = null;
        final String[] fixedOrder = { firstItem, "some other item", secondItem };

        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(fixedOrder);

        // Act
        // Compare the two items. This is the first comparison, which will also lock the comparator.
        final int result = comparator.compare(firstItem, secondItem);

        // Assert
        // 1. Verify the comparison result.
        // The first item comes before the second in the defined order, so the result must be negative.
        assertEquals("A negative value is expected when obj1 < obj2", -1, result);

        // 2. Verify the side-effect.
        // The comparator should be locked against further modification after the first comparison.
        assertTrue("Comparator should be locked after compare() is called", comparator.isLocked());
    }
}