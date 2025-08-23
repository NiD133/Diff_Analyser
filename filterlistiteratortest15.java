package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the complex walking behavior of a ListIterator against a baseline
 * (a standard ArrayList ListIterator).
 * <p>
 * This class validates a set of complex traversal patterns that can be used
 * to verify the correctness of any ListIterator implementation. The original
 * test was a monolithic "meta-test" to confirm the test logic itself. This
 * refactored version breaks down the patterns into distinct, understandable test cases.
 * </p>
 */
@DisplayName("ListIterator Traversal Behavior")
class ListIteratorWalkBehaviorTest {

    private List<Integer> list;
    private ListIterator<Integer> controlIterator;
    private ListIterator<Integer> testIterator;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        // The control and test iterators are created from the same known-good list.
        // This setup is designed to validate the test logic itself.
        controlIterator = list.listIterator();
        testIterator = list.listIterator();
    }

    @Test
    @DisplayName("should traverse forward and backward correctly")
    void testFullTraversal() {
        // 1. Walk all the way forward
        while (controlIterator.hasNext()) {
            assertIteratorState("Before next()");
            assertTrue(testIterator.hasNext());
            assertEquals(controlIterator.next(), testIterator.next());
        }
        assertFalse(testIterator.hasNext(), "Should be at the end of the list");

        // 2. Walk all the way back
        while (controlIterator.hasPrevious()) {
            assertIteratorState("Before previous()");
            assertTrue(testIterator.hasPrevious());
            assertEquals(controlIterator.previous(), testIterator.previous());
        }
        assertFalse(testIterator.hasPrevious(), "Should be at the beginning of the list");
    }

    @Test
    @DisplayName("should handle alternating next() and previous() calls")
    void testAlternatingTraversal() {
        while (controlIterator.hasNext()) {
            assertIteratorState("Before alternating sequence");
            
            // Move forward
            assertEquals(controlIterator.next(), testIterator.next());
            
            // Move back
            assertTrue(testIterator.hasPrevious());
            assertEquals(controlIterator.previous(), testIterator.previous());
            
            // Move forward again to advance the cursor
            assertTrue(testIterator.hasNext());
            assertEquals(controlIterator.next(), testIterator.next());
        }
    }

    @Test
    @DisplayName("should handle complex partial traversal patterns")
    void testPartialTraversalPatterns() {
        for (int i = 0; i < list.size(); i++) {
            // A: Walk forward i steps
            moveForward(i, "Pattern A");
            
            // B: Walk back i/2 steps
            moveBackward(i / 2, "Pattern B");
            
            // C: Walk forward i/2 steps
            moveForward(i / 2, "Pattern C");
            
            // D: Walk back i steps to reset for the next iteration
            moveBackward(i, "Pattern D");
        }
    }

    @Test
    @DisplayName("should behave consistently during a random walk")
    void testRandomWalk() {
        final Random random = new Random();
        final StringBuilder walkSequence = new StringBuilder();
        final int steps = 500;

        for (int i = 0; i < steps; i++) {
            if (random.nextBoolean()) {
                // Step forward
                walkSequence.append(" next()");
                if (controlIterator.hasNext()) {
                    assertEquals(controlIterator.next(), testIterator.next(),
                        "Mismatch after sequence:" + walkSequence);
                }
            } else {
                // Step backward
                walkSequence.append(" previous()");
                if (controlIterator.hasPrevious()) {
                    assertEquals(controlIterator.previous(), testIterator.previous(),
                        "Mismatch after sequence:" + walkSequence);
                }
            }
            assertIteratorState("After random step " + (i + 1) + ":" + walkSequence);
        }
    }

    //-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
    // Helper Methods
    //-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~

    private void moveForward(final int steps, final String context) {
        for (int j = 0; j < steps; j++) {
            assertIteratorState("Before moving forward in " + context);
            assertTrue(controlIterator.hasNext(), "Control iterator should have next in " + context);
            assertTrue(testIterator.hasNext(), "Test iterator should have next in " + context);
            assertEquals(controlIterator.next(), testIterator.next());
        }
    }

    private void moveBackward(final int steps, final String context) {
        for (int j = 0; j < steps; j++) {
            assertIteratorState("Before moving backward in " + context);
            assertTrue(controlIterator.hasPrevious(), "Control iterator should have previous in " + context);
            assertTrue(testIterator.hasPrevious(), "Test iterator should have previous in " + context);
            assertEquals(controlIterator.previous(), testIterator.previous());
        }
    }

    private void assertIteratorState(final String message) {
        assertEquals(controlIterator.nextIndex(), testIterator.nextIndex(), message + " - nextIndex mismatch");
        assertEquals(controlIterator.previousIndex(), testIterator.previousIndex(), message + " - previousIndex mismatch");
    }
}