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
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the LoopingListIterator class.
 */
class LoopingListIteratorTest {

    private List<String> list;
    private LoopingListIterator<String> iterator;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        iterator = new LoopingListIterator<>(list);
    }

    @Test
    void constructor_withNullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LoopingListIterator<>(null),
                "Constructor should throw NullPointerException for a null list.");
    }

    @Test
    void iterator_onEmptyList_hasNoNextOrPrevious() {
        // Arrange
        final LoopingListIterator<Object> emptyIterator = new LoopingListIterator<>(Collections.emptyList());

        // Assert
        assertFalse(emptyIterator.hasNext());
        assertFalse(emptyIterator.hasPrevious());
        assertThrows(NoSuchElementException.class, emptyIterator::next);
        assertThrows(NoSuchElementException.class, emptyIterator::previous);
    }

    @Test
    void iterator_onSingleElementList_loopsCorrectly() {
        // Arrange
        final List<String> singleElementList = Collections.singletonList("a");
        final LoopingListIterator<String> singleItemIterator = new LoopingListIterator<>(singleElementList);

        // Act & Assert: Forward iteration should loop continuously
        assertTrue(singleItemIterator.hasNext());
        assertEquals("a", singleItemIterator.next());
        assertEquals("a", singleItemIterator.next());

        // Act & Assert: Backward iteration should also loop continuously
        assertTrue(singleItemIterator.hasPrevious());
        assertEquals("a", singleItemIterator.previous());
        assertEquals("a", singleItemIterator.previous());
    }

    @Test
    void iterator_onTwoElementList_loopsCorrectlyForwardAndBackward() {
        // Arrange
        final List<String> twoElementList = Arrays.asList("a", "b");
        final LoopingListIterator<String> twoItemIterator = new LoopingListIterator<>(twoElementList);

        // Act & Assert: Forward iteration
        assertEquals("a", twoItemIterator.next());
        assertEquals("b", twoItemIterator.next());
        assertEquals("a", twoItemIterator.next());

        // Reset and test backward iteration
        twoItemIterator.reset();

        // Act & Assert: Backward iteration
        assertEquals("b", twoItemIterator.previous());
        assertEquals("a", twoItemIterator.previous());
        assertEquals("b", twoItemIterator.previous());
    }

    @Test
    void nextAndPrevious_whenNotCrossingBoundary_movesCursorCorrectly() {
        // Arrange: list is ["a", "b", "c"]
        // Act & Assert: Jog back and forth between 'a' and 'b'
        assertEquals("a", iterator.next());     // returns "a", cursor is at index 1 ("b")
        assertEquals("a", iterator.previous()); // returns "a", cursor is at index 0 ("a")
        assertEquals("a", iterator.next());     // returns "a", cursor is at index 1 ("b")
    }

    @Test
    void nextAndPrevious_whenCrossingBoundary_loopsCorrectly() {
        // Arrange: list is ["a", "b", "c"]
        // Act & Assert: Jog over the start/end boundary
        assertEquals("c", iterator.previous()); // returns "c", cursor is at index 2 ("c")
        assertEquals("c", iterator.next());     // returns "c", cursor is at index 0 ("a")
        assertEquals("c", iterator.previous()); // returns "c", cursor is at index 2 ("c")
    }

    @Test
    void nextIndexAndPreviousIndex_shouldReturnCorrectIndices() {
        // Assert initial state: cursor at index 0
        assertEquals(0, iterator.nextIndex());
        assertEquals(2, iterator.previousIndex());

        // Assert after next(): cursor at index 1
        iterator.next(); // returns "a"
        assertEquals(1, iterator.nextIndex());
        assertEquals(0, iterator.previousIndex());

        // Assert after previous() crossing boundary: cursor at index 2
        iterator.previous(); // returns "a"
        iterator.previous(); // returns "c"
        assertEquals(2, iterator.nextIndex());
        assertEquals(1, iterator.previousIndex());
    }

    @Test
    void remove_whileIteratingForward_deletesCorrectElements() {
        // Act & Assert: Remove "a"
        iterator.next(); // returns "a"
        iterator.remove();
        assertEquals(Arrays.asList("b", "c"), list);
        assertEquals(0, iterator.nextIndex()); // Cursor is at "b"

        // Act & Assert: Remove "b"
        iterator.next(); // returns "b"
        iterator.remove();
        assertEquals(Collections.singletonList("c"), list);
        assertEquals(0, iterator.nextIndex()); // Cursor is at "c"

        // Act & Assert: Remove "c"
        iterator.next(); // returns "c"
        iterator.remove();
        assertEquals(Collections.emptyList(), list);

        // Assert final state
        assertFalse(iterator.hasNext());
    }

    @Test
    void remove_whileIteratingBackward_deletesCorrectElements() {
        // Act & Assert: Remove "c"
        iterator.previous(); // returns "c"
        iterator.remove();
        assertEquals(Arrays.asList("a", "b"), list);
        assertEquals(1, iterator.previousIndex()); // Cursor is at "b"

        // Act & Assert: Remove "b"
        iterator.previous(); // returns "b"
        iterator.remove();
        assertEquals(Collections.singletonList("a"), list);
        assertEquals(0, iterator.previousIndex()); // Cursor is at "a"

        // Act & Assert: Remove "a"
        iterator.previous(); // returns "a"
        iterator.remove();
        assertEquals(Collections.emptyList(), list);

        // Assert final state
        assertFalse(iterator.hasPrevious());
    }

    @Test
    void reset_afterNext_movesIteratorToStart() {
        // Arrange
        iterator.next(); // returns "a"
        iterator.next(); // returns "b"
        assertEquals(2, iterator.nextIndex());

        // Act
        iterator.reset();

        // Assert
        assertEquals(0, iterator.nextIndex());
        assertEquals("a", iterator.next());
    }

    @Test
    void reset_afterPrevious_movesIteratorToStart() {
        // Arrange
        iterator.previous(); // returns "c"
        assertEquals(2, iterator.nextIndex());

        // Act
        iterator.reset();

        // Assert
        assertEquals(0, iterator.nextIndex());
        assertEquals("c", iterator.previous()); // Still loops from the new start
    }

    @Test
    void set_afterNext_replacesLastReturnedElement() {
        // Arrange
        iterator.next(); // returns "a"
        
        // Act
        iterator.set("x");

        // Assert
        assertEquals(Arrays.asList("x", "b", "c"), list);
        assertEquals("b", iterator.next()); // next element is correct
    }

    @Test
    void set_afterPrevious_replacesLastReturnedElement() {
        // Arrange
        iterator.previous(); // returns "c"

        // Act
        iterator.set("z");

        // Assert
        assertEquals(Arrays.asList("a", "b", "z"), list);
        assertEquals("b", iterator.previous()); // previous element is correct
    }

    @Test
    void add_atStart_insertsElementAndAdjustsCursor() {
        // Arrange
        final List<String> modifiableList = new ArrayList<>(Arrays.asList("b", "e", "f"));
        final LoopingListIterator<String> loop = new LoopingListIterator<>(modifiableList);

        // Act
        loop.add("a"); // Should insert at the beginning

        // Assert
        assertEquals(Arrays.asList("a", "b", "e", "f"), modifiableList);
        assertEquals(1, loop.nextIndex(), "Cursor should be after the new element 'a'");
        assertEquals("b", loop.next(), "next() should return the element after the new one");
    }

    @Test
    void add_whileIteratingForward_insertsElementAndAdjustsCursor() {
        // Arrange
        final List<String> modifiableList = new ArrayList<>(Arrays.asList("a", "e", "f"));
        final LoopingListIterator<String> loop = new LoopingListIterator<>(modifiableList);
        loop.next(); // returns "a", cursor at "e"

        // Act
        loop.add("b");

        // Assert
        assertEquals(Arrays.asList("a", "b", "e", "f"), modifiableList);
        assertEquals(2, loop.nextIndex(), "Cursor should be after the new element 'b'");
        assertEquals("e", loop.next(), "next() should return the element after the new one");
    }

    @Test
    void add_whileIteratingBackward_insertsElementAndAdjustsCursor() {
        // Arrange
        final List<String> modifiableList = new ArrayList<>(Arrays.asList("a", "e", "f"));
        final LoopingListIterator<String> loop = new LoopingListIterator<>(modifiableList);
        loop.previous(); // returns "f"
        loop.previous(); // returns "e", cursor at "e"

        // Act
        loop.add("d");

        // Assert
        assertEquals(Arrays.asList("a", "d", "e", "f"), modifiableList);
        assertEquals(2, loop.nextIndex(), "Cursor should be after the new element 'd'");
        assertEquals("d", loop.previous(), "previous() should return the new element");
    }
}