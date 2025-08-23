package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ListIterator;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FilterListIterator}.
 * This class focuses on specific edge cases and bug fixes.
 */
class FilterListIteratorTest {

    /**
     * Tests that a FilterListIterator created without a backing list iterator
     * behaves correctly (like an empty iterator). This is to verify the fix for
     * <a href="https://issues.apache.org/jira/browse/COLLECTIONS-360">COLLECTIONS-360</a>,
     * where constructing an iterator with only a predicate could lead to a NullPointerException.
     */
    @Test
    @DisplayName("Iterator constructed without a list should behave as empty")
    void testIteratorConstructedWithoutListBehavesAsEmpty() {
        // GIVEN a predicate. The specific predicate does not matter here, as there is
        // no underlying iterator to apply it to. We use a simple one for clarity.
        final Predicate<Object> alwaysFalsePredicate = PredicateUtils.falsePredicate();

        // WHEN a FilterListIterator is created using the constructor that only takes a predicate,
        // leaving its internal ListIterator as null.
        final ListIterator<Object> emptyIterator = new FilterListIterator<>(alwaysFalsePredicate);

        // THEN the iterator should behave as if it's empty.
        assertFalse(emptyIterator.hasNext(), "hasNext() should return false when no iterator is set");
        assertFalse(emptyIterator.hasPrevious(), "hasPrevious() should return false when no iterator is set");
    }
}