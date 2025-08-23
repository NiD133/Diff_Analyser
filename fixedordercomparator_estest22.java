package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

/**
 * This test class contains tests for the {@link FixedOrderComparator}.
 * This specific test case verifies the reflexive property of the equals method.
 */
// The original class name and inheritance are preserved to maintain compatibility
// with the existing test suite structure, which may be tool-generated.
public class FixedOrderComparator_ESTestTest22 extends FixedOrderComparator_ESTest_scaffolding {

    /**
     * Tests that a FixedOrderComparator instance is equal to itself,
     * fulfilling the reflexive contract of the Object.equals() method.
     */
    @Test(timeout = 4000)
    public void equals_shouldReturnTrue_whenComparingAnInstanceToItself() {
        // Arrange: Create a comparator. The ordering list can be empty as its
        // content is irrelevant for this test of object identity.
        final List<Object> emptyOrder = Collections.emptyList();
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>(emptyOrder);

        // Act & Assert: An object must always be equal to itself.
        assertTrue("A comparator instance must be equal to itself.", comparator.equals(comparator));
    }
}