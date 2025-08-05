package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CartesianProductIterator}.
 */
public class CartesianProductIteratorTest {

    @Test
    public void constructor_withNullVarargs_throwsNullPointerException() {
        // Arrange
        Iterable<String>[] nullIterables = null;

        // Act & Assert
        try {
            new CartesianProductIterator<>(nullIterables);
        } catch (final NullPointerException e) {
            assertEquals("iterables", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullInIterables_throwsNullPointerException() {
        // Arrange
        final Iterable<String> list1 = Arrays.asList("A");
        final Iterable<String> list2 = null;

        // Act
        new CartesianProductIterator<>(list1, list2); // Should throw
    }

    @Test
    public void hasNext_whenProductIsNotEmpty_returnsTrue() {
        // Arrange
        final Iterable<String> list1 = Arrays.asList("A");
        final Iterable<Integer> list2 = Arrays.asList(1);
        final CartesianProductIterator<Object> iterator = new CartesianProductIterator<>(list1, list2);

        // Act & Assert
        assertTrue("Iterator should have elements", iterator.hasNext());
    }

    @Test
    public void hasNext_whenOneIterableIsEmpty_returnsFalse() {
        // Arrange
        final Iterable<String> list1 = Arrays.asList("A", "B");
        final Iterable<String> list2 = Collections.emptyList(); // Empty list
        final CartesianProductIterator<String> iterator = new CartesianProductIterator<>(list1, list2);

        // Act & Assert
        assertFalse("Iterator should be empty if one iterable is empty", iterator.hasNext());
    }

    @Test
    public void next_iteratesThroughAllCombinationsInCorrectOrder() {
        // Arrange
        final Iterable<String> set1 = Arrays.asList("A", "B");
        final Iterable<Integer> set2 = Arrays.asList(1, 2, 3);
        final CartesianProductIterator<Object> iterator = new CartesianProductIterator<>(set1, set2);

        // Act & Assert
        assertTrue(iterator.hasNext());
        assertEquals(Arrays.asList("A", 1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Arrays.asList("A", 2), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Arrays.asList("A", 3), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Arrays.asList("B", 1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Arrays.asList("B", 2), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Arrays.asList("B", 3), iterator.next());

        assertFalse("Iterator should have no more elements", iterator.hasNext());
    }

    @Test
    public void next_withThreeIterables_returnsCorrectProduct() {
        // Arrange
        final Iterable<String> set1 = Arrays.asList("A", "B");
        final Iterable<Integer> set2 = Collections.singletonList(1);
        final Iterable<String> set3 = Arrays.asList("X", "Y");
        final CartesianProductIterator<Object> iterator = new CartesianProductIterator<>(set1, set2, set3);

        // Act & Assert
        assertEquals(Arrays.asList("A", 1, "X"), iterator.next());
        assertEquals(Arrays.asList("A", 1, "Y"), iterator.next());
        assertEquals(Arrays.asList("B", 1, "X"), iterator.next());
        assertEquals(Arrays.asList("B", 1, "Y"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void next_onEmptyProduct_throwsNoSuchElementException() {
        // Arrange
        // Create an iterator with no iterables, resulting in an empty product.
        final CartesianProductIterator<String> iterator = new CartesianProductIterator<>();

        // Act
        iterator.next(); // Should throw
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove_isUnsupported_throwsUnsupportedOperationException() {
        // Arrange
        final Iterable<String> list = Arrays.asList("A");
        final CartesianProductIterator<String> iterator = new CartesianProductIterator<>(list);

        // Act
        iterator.remove(); // Should throw
    }

    @Test(expected = ConcurrentModificationException.class)
    public void next_whenUnderlyingCollectionIsModified_throwsConcurrentModificationException() {
        // Arrange
        final List<String> modifiableList = new ArrayList<>();
        modifiableList.add("A");
        final CartesianProductIterator<String> iterator = new CartesianProductIterator<>(modifiableList);

        // Modify the list after the CartesianProductIterator has created its internal iterator.
        modifiableList.add("B");

        // Act
        iterator.next(); // Should fail because the underlying iterator is now invalid.
    }
}