/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Tests the LoopingListIterator class.
 */
class LoopingListIteratorTest {

    // Test data constants
    private static final List<String> EMPTY_LIST = new ArrayList<>();
    private static final List<String> SINGLE_ELEMENT_LIST = Arrays.asList("a");
    private static final List<String> TWO_ELEMENT_LIST = Arrays.asList("a", "b");
    private static final List<String> THREE_ELEMENT_LIST = Arrays.asList("a", "b", "c");

    @Test
    void shouldThrowNullPointerExceptionWhenConstructedWithNullList() {
        assertThrows(NullPointerException.class, () -> new LoopingListIterator<>(null));
    }

    @Test
    void shouldReturnFalseForHasNextAndHasPreviousWhenListIsEmpty() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(EMPTY_LIST);
        
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenCallingNextOnEmptyList() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(EMPTY_LIST);
        
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenCallingPreviousOnEmptyList() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(EMPTY_LIST);
        
        assertThrows(NoSuchElementException.class, iterator::previous);
    }

    @Test
    void shouldLoopContinuouslyForwardOnSingleElementList() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(SINGLE_ELEMENT_LIST);

        // Should always have next element and return the same element
        for (int i = 0; i < 5; i++) {
            assertTrue(iterator.hasNext(), "Should always have next element");
            assertEquals("a", iterator.next(), "Should always return the single element");
        }
    }

    @Test
    void shouldLoopContinuouslyBackwardOnSingleElementList() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(SINGLE_ELEMENT_LIST);

        // Should always have previous element and return the same element
        for (int i = 0; i < 5; i++) {
            assertTrue(iterator.hasPrevious(), "Should always have previous element");
            assertEquals("a", iterator.previous(), "Should always return the single element");
        }
    }

    @Test
    void shouldLoopForwardThroughTwoElementList() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(TWO_ELEMENT_LIST);

        // First loop: a -> b -> a
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("a", iterator.next(), "Should loop back to first element");
    }

    @Test
    void shouldLoopBackwardThroughTwoElementList() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(TWO_ELEMENT_LIST);

        // Starting from beginning, going backward should wrap to end: b -> a -> b
        assertEquals("b", iterator.previous(), "Should wrap to last element");
        assertEquals("a", iterator.previous());
        assertEquals("b", iterator.previous(), "Should wrap to last element again");
    }

    @Test
    void shouldMaintainCorrectPositionWhenJoggingWithinBounds() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(TWO_ELEMENT_LIST);

        // Move to first element, then jog back and forth
        assertEquals("a", iterator.next());
        assertEquals("a", iterator.previous(), "Should return to first element");
        assertEquals("a", iterator.next(), "Should move forward to first element again");

        // Move to second element, then jog back and forth
        assertEquals("b", iterator.next());
        assertEquals("b", iterator.previous(), "Should return to second element");
        assertEquals("b", iterator.next(), "Should move forward to second element again");
    }

    @Test
    void shouldMaintainCorrectPositionWhenJoggingAcrossBounds() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(TWO_ELEMENT_LIST);

        // Start by going backward (wraps to end), then jog back and forth
        assertEquals("b", iterator.previous());
        assertEquals("b", iterator.next(), "Should move forward from last element, wrapping to first");
        assertEquals("b", iterator.previous(), "Should move backward from first element, wrapping to last");

        // Move backward to first element, then jog
        assertEquals("a", iterator.previous());
        assertEquals("a", iterator.next(), "Should move forward from first element");
        assertEquals("a", iterator.previous(), "Should move backward to first element");
    }

    @Test
    void shouldReturnCorrectNextAndPreviousIndices() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(THREE_ELEMENT_LIST);

        // At start: cursor before first element
        assertEquals(0, iterator.nextIndex(), "Next index should be 0 at start");
        assertEquals(2, iterator.previousIndex(), "Previous index should wrap to last element");

        // After moving to first element
        iterator.next();
        assertEquals(1, iterator.nextIndex(), "Next index should be 1 after first element");
        assertEquals(0, iterator.previousIndex(), "Previous index should be 0 after first element");

        // Move back to start
        iterator.previous();
        assertEquals(0, iterator.nextIndex());
        assertEquals(2, iterator.previousIndex());

        // Move backward (wraps to end)
        iterator.previous();
        assertEquals(2, iterator.nextIndex(), "Next index should be 2 at last element");
        assertEquals(1, iterator.previousIndex(), "Previous index should be 1 at last element");
    }

    @Test
    void shouldResetToBeginningOfList() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(THREE_ELEMENT_LIST);

        // Move through list, then reset
        iterator.next(); // Move to 'a'
        iterator.next(); // Move to 'b'
        iterator.reset();
        assertEquals("a", iterator.next(), "Should return to first element after reset");

        // Reset again and verify
        iterator.reset();
        assertEquals("a", iterator.next(), "Should return to first element after second reset");

        // Move through entire list, then reset
        iterator.next(); // 'b'
        iterator.next(); // 'c' (wraps to start)
        iterator.reset();
        assertEquals("a", iterator.next(), "Should return to first element after full loop and reset");
    }

    @Test
    void shouldResetCorrectlyAfterBackwardMovement() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(THREE_ELEMENT_LIST);

        // Move backward, then reset
        iterator.previous(); // 'c'
        iterator.previous(); // 'b'
        iterator.reset();
        assertEquals("c", iterator.previous(), "Should wrap to last element after reset");

        // Reset and verify forward movement
        iterator.reset();
        assertEquals("a", iterator.next(), "Should start from first element after reset");
    }

    @Test
    void shouldAddElementAtCurrentPosition() {
        List<String> modifiableList = new ArrayList<>(Arrays.asList("b", "e", "f"));
        LoopingListIterator<String> iterator = new LoopingListIterator<>(modifiableList);

        // Add at beginning
        iterator.add("a");
        assertEquals("b", iterator.next(), "Next element should be 'b' after adding 'a'");
        
        // Verify the addition by resetting and checking sequence
        iterator.reset();
        assertEquals("a", iterator.next(), "First element should now be 'a'");
        assertEquals("b", iterator.next(), "Second element should be 'b'");
    }

    @Test
    void shouldAddMultipleElementsAndMaintainOrder() {
        List<String> modifiableList = new ArrayList<>(Arrays.asList("b", "e", "f"));
        LoopingListIterator<String> iterator = new LoopingListIterator<>(modifiableList);

        // Add elements at different positions
        iterator.add("a"); // List: [a, b, e, f]
        iterator.next(); // Move to 'b'
        iterator.next(); // Move to 'e'
        iterator.add("c"); // List: [a, b, c, e, f]
        iterator.add("d"); // List: [a, b, c, d, e, f]

        // Verify final order
        iterator.reset();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        assertEquals("e", iterator.next());
        assertEquals("f", iterator.next());
        assertEquals("a", iterator.next(), "Should loop back to beginning");
    }

    @Test
    void shouldRemoveElementsWhileIteratingForward() {
        List<String> modifiableList = new ArrayList<>(THREE_ELEMENT_LIST);
        LoopingListIterator<String> iterator = new LoopingListIterator<>(modifiableList);

        // Remove first element
        assertEquals("a", iterator.next());
        iterator.remove();
        assertEquals(2, modifiableList.size(), "List should have 2 elements after removal");

        // Remove second element
        assertEquals("b", iterator.next());
        iterator.remove();
        assertEquals(1, modifiableList.size(), "List should have 1 element after removal");

        // Remove last element
        assertEquals("c", iterator.next());
        iterator.remove();
        assertEquals(0, modifiableList.size(), "List should be empty after removal");

        assertFalse(iterator.hasNext(), "Should have no next element when list is empty");
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldRemoveElementsWhileIteratingBackward() {
        List<String> modifiableList = new ArrayList<>(THREE_ELEMENT_LIST);
        LoopingListIterator<String> iterator = new LoopingListIterator<>(modifiableList);

        // Remove from end to beginning
        assertEquals("c", iterator.previous());
        iterator.remove();
        assertEquals(2, modifiableList.size());

        assertEquals("b", iterator.previous());
        iterator.remove();
        assertEquals(1, modifiableList.size());

        assertEquals("a", iterator.previous());
        iterator.remove();
        assertEquals(0, modifiableList.size());

        assertFalse(iterator.hasPrevious(), "Should have no previous element when list is empty");
        assertThrows(NoSuchElementException.class, iterator::previous);
    }

    @Test
    void shouldSetElementAtCurrentPosition() {
        List<String> modifiableList = new ArrayList<>(Arrays.asList("q", "r", "z"));
        LoopingListIterator<String> iterator = new LoopingListIterator<>(modifiableList);

        // Set last element by going backward
        assertEquals("z", iterator.previous());
        iterator.set("c");

        // Set first element
        iterator.reset();
        assertEquals("q", iterator.next());
        iterator.set("a");

        // Set middle element
        assertEquals("r", iterator.next());
        iterator.set("b");

        // Verify all changes
        iterator.reset();
        assertEquals("a", iterator.next(), "First element should be 'a'");
        assertEquals("b", iterator.next(), "Second element should be 'b'");
        assertEquals("c", iterator.next(), "Third element should be 'c'");
    }
}