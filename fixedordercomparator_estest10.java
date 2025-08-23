package org.apache.commons.collections4.comparators;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link FixedOrderComparator} focusing on edge cases during construction.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that the constructor throws a StackOverflowError when given a list
     * that contains itself.
     * <p>
     * The constructor iterates through the provided items to populate an internal
     * HashMap. This process involves calling {@code hashCode()} on each item. For a
     * collection, its hashCode is derived from its elements' hashCodes. If a list
     * contains itself, calculating its hashCode results in an infinite recursion,
     * which correctly triggers a {@link StackOverflowError}.
     */
    @Test(expected = StackOverflowError.class)
    public void constructorShouldThrowStackOverflowErrorForCircularReferenceInItems() {
        // Arrange: Create a list that directly contains itself, forming a circular reference.
        final List<Object> listContainingItself = new LinkedList<>();
        listContainingItself.add(listContainingItself);

        // An array containing the problematic list.
        final Object[] itemsWithCircularReference = {"item1", "item2", listContainingItself};

        // Act & Assert:
        // Instantiating the comparator with this array should cause a StackOverflowError.
        new FixedOrderComparator<>(itemsWithCircularReference);
    }
}