package com.google.common.collect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link AbstractIterator}.
 */
public class AbstractIteratorTest {

    @Test
    @DisplayName("Iterator should correctly advance, cache values, and terminate")
    void testStandardIterationLifecycle() {
        // ARRANGE: Create an iterator that produces 0, then 1, and then signals it's done.
        // The implementation includes a check to ensure computeNext() is not called again
        // after the iterator is exhausted.
        Iterator<Integer> iterator = new AbstractIterator<Integer>() {
            private int calls = 0;

            @Override
            @Nullable
            protected Integer computeNext() {
                switch (calls++) {
                    case 0:
                        return 0; // First element
                    case 1:
                        return 1; // Second element
                    case 2:
                        return endOfData(); // Signal end of iteration
                    default:
                        // This proves that computeNext() is not called after it has
                        // signaled the end of the data, a key contract of AbstractIterator.
                        throw new AssertionError("computeNext() was called unexpectedly");
                }
            }
        };

        // ACT & ASSERT

        // 1. First element: hasNext() computes and caches the value.
        assertTrue(iterator.hasNext(), "Iterator should have an element at the start");
        assertEquals(0, iterator.next(), "next() should return the first element");

        // 2. Idempotency of hasNext(): Calling it multiple times should not change the state.
        // The first call to hasNext() here will compute and cache the next value (1).
        assertTrue(iterator.hasNext(), "hasNext() should be true before the second element");
        assertTrue(iterator.hasNext(), "Subsequent hasNext() calls should be idempotent");

        // 3. Second element: Retrieve the cached value.
        assertEquals(1, iterator.next(), "next() should return the second element");

        // 4. End of iteration: hasNext() should now return false.
        assertFalse(iterator.hasNext(), "Iterator should be exhausted after the last element");

        // 5. Behavior after exhaustion: hasNext() remains false, and next() throws.
        assertFalse(iterator.hasNext(), "hasNext() should remain false after exhaustion");
        assertThrows(
            NoSuchElementException.class,
            iterator::next,
            "Calling next() on an exhausted iterator should throw NoSuchElementException");
    }
}