package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ObjectGraphIterator focusing on its ability to handle nested iterators.
 *
 * Note: The original test file contained many helper classes (Forest, Tree, etc.)
 * for other test cases. They have been omitted here for clarity as they are not
 * relevant to the test case being improved.
 */
public class ObjectGraphIteratorTest {

    private List<String> list1;
    private List<String> list2;
    private List<String> list3;
    private List<Iterator<String>> nestedIterators;

    @BeforeEach
    void setUp() {
        // The test will call remove() on the iterators, so we need mutable lists.
        list1 = new ArrayList<>(List.of("One", "Two", "Three"));
        list2 = new ArrayList<>(List.of("Four"));
        list3 = new ArrayList<>(List.of("Five", "Six"));

        nestedIterators = List.of(list1.iterator(), list2.iterator(), list3.iterator());
    }

    @Test
    void whenRemovingElements_thenUnderlyingCollectionsShouldBeModified() {
        // Arrange
        // The ObjectGraphIterator is initialized with an iterator that itself yields other iterators.
        // This tests the constructor designed for handling nested iterators.
        final Iterator<Object> objectGraphIterator = new ObjectGraphIterator<>(nestedIterators.iterator());
        final List<Object> traversedElements = new ArrayList<>();
        final List<String> expectedElements = List.of("One", "Two", "Three", "Four", "Five", "Six");

        // Act
        // Traverse the entire graph, collecting elements and removing them as we go.
        while (objectGraphIterator.hasNext()) {
            traversedElements.add(objectGraphIterator.next());
            objectGraphIterator.remove();
        }

        // Assert
        // 1. Verify that all expected elements were traversed in the correct order.
        assertEquals(expectedElements, traversedElements, "Should have traversed all elements in order");

        // 2. Verify that the remove() operation emptied the underlying source lists.
        assertTrue(list1.isEmpty(), "List 1 should be empty after removal");
        assertTrue(list2.isEmpty(), "List 2 should be empty after removal");
        assertTrue(list3.isEmpty(), "List 3 should be empty after removal");

        // 3. Verify that the main iterator is now exhausted.
        assertFalse(objectGraphIterator.hasNext(), "Iterator should have no more elements");
    }
}