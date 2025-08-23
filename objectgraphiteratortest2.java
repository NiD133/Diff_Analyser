package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObjectGraphIterator}, focusing on its ability to handle nested iterators.
 * <p>
 * This class extends {@link AbstractIteratorTest} to leverage a standard suite of iterator contract tests.
 * The iterators under test are configured to flatten an iterator of iterators.
 * </p>
 */
public class ObjectGraphIteratorTest extends AbstractIteratorTest<Object> {

    /** The complete set of expected elements, used for verification across tests. */
    private final String[] testArray = {"One", "Two", "Three", "Four", "Five", "Six"};

    /** Sub-lists that will be combined into a nested iterator structure. */
    private List<String> list1;
    private List<String> list2;
    private List<String> list3;

    /** A list of iterators, used by the abstract test framework via makeObject(). */
    private List<Iterator<String>> iteratorList;

    @BeforeEach
    public void setUp() {
        list1 = List.of("One", "Two", "Three");
        list2 = List.of("Four");
        list3 = List.of("Five", "Six");
        iteratorList = List.of(list1.iterator(), list2.iterator(), list3.iterator());
    }

    /**
     * Creates an empty iterator for the abstract tests.
     * This tests the base case of an empty root iterator.
     */
    @Override
    public ObjectGraphIterator<Object> makeEmptyIterator() {
        return new ObjectGraphIterator<>(IteratorUtils.emptyIterator());
    }

    /**
     * Creates a fully populated iterator for the abstract tests.
     * The iterator is a nested structure of iterators over strings,
     * which ObjectGraphIterator should flatten.
     */
    @Override
    public ObjectGraphIterator<Object> makeObject() {
        // setUp() is called by JUnit before this method.
        return new ObjectGraphIterator<>(iteratorList.iterator());
    }

    /**
     * Verifies that the iterator correctly traverses a root iterator containing
     * other iterators, effectively flattening the structure. It also ensures
     * that any empty iterators within the structure are skipped gracefully.
     */
    @Test
    void shouldSkipEmptyIteratorsWhenIteratingNestedIterators() {
        // Arrange: Create a list of iterators, with some being empty.
        final List<Iterator<String>> nestedIterators = List.of(
            IteratorUtils.emptyIterator(),
            list1.iterator(),
            IteratorUtils.emptyIterator(),
            list2.iterator(),
            IteratorUtils.emptyIterator(),
            list3.iterator(),
            IteratorUtils.emptyIterator()
        );

        // The ObjectGraphIterator is given an iterator that yields other iterators.
        // It should flatten this structure, skipping the empty ones.
        final Iterator<Object> iterator = new ObjectGraphIterator<>(nestedIterators.iterator());

        // Act: Collect all elements from the iterator.
        final List<Object> traversedElements = IteratorUtils.toList(iterator);

        // Assert: The collected elements should match the concatenation of all non-empty lists.
        final List<String> expectedElements = List.of(testArray);
        assertIterableEquals(expectedElements, traversedElements);
    }
}