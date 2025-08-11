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

    @Test
    void testConstructorWithNullListThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LoopingListIterator<>(null));
    }

    @Test
    void testEmptyList() {
        // Given
        final List<Object> list = new ArrayList<>();
        final LoopingListIterator<Object> loop = new LoopingListIterator<>(list);

        // Then
        assertFalse(loop.hasNext(), "Should not have next");
        assertFalse(loop.hasPrevious(), "Should not have previous");
        assertThrows(NoSuchElementException.class, loop::next, "Next should throw");
        assertThrows(NoSuchElementException.class, loop::previous, "Previous should throw");
    }

    @Test
    void testSingleElementList() {
        // Given
        final List<String> list = Arrays.asList("a");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a>

        // Then: Next operations
        assertTrue(loop.hasNext(), "Should have next");
        assertEquals("a", loop.next(), "Should return first element"); // <a>
        assertEquals("a", loop.next(), "Should loop to first element"); // <a>

        // Then: Previous operations
        assertTrue(loop.hasPrevious(), "Should have previous");
        assertEquals("a", loop.previous(), "Should return only element"); // <a>
        assertEquals("a", loop.previous(), "Should loop to only element"); // <a>
    }

    @Test
    void testTwoElementList() {
        // Given
        final List<String> list = Arrays.asList("a", "b");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b

        // When & Then: Forward iteration
        assertEquals("a", loop.next(), "First element"); // a <b>
        assertEquals("b", loop.next(), "Second element"); // <a> b
        assertEquals("a", loop.next(), "Loop to first element"); // a <b>

        // When & Then: Backward iteration
        loop.reset(); // Reset to start: <a> b
        assertEquals("b", loop.previous(), "Last element"); // a <b>
        assertEquals("a", loop.previous(), "First element"); // <a> b
        assertEquals("b", loop.previous(), "Loop to last element"); // a <b>
    }

    @Test
    void testJoggingBetweenTwoElementsWithoutCrossingBoundary() {
        // Given
        final List<String> list = Arrays.asList("a", "b");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b

        // When & Then: Between 'a' and 'b' without boundary wrap
        loop.reset();
        assertEquals("a", loop.next());     // a <b>
        assertEquals("a", loop.previous()); // <a> b
        assertEquals("a", loop.next());     // a <b>

        assertEquals("b", loop.next());     // <a> b
        assertEquals("b", loop.previous()); // a <b>
        assertEquals("b", loop.next());     // <a> b
    }

    @Test
    void testJoggingBetweenTwoElementsCrossingBoundary() {
        // Given
        final List<String> list = Arrays.asList("a", "b");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b

        // When & Then: Crossing boundary via previous
        assertEquals("b", loop.previous(), "Previous from start wraps to end"); // a <b>
        assertEquals("b", loop.next(), "Next after wrap returns to start"); // <a> b
        assertEquals("b", loop.previous(), "Previous again wraps to end"); // a <b>

        // When & Then: Crossing boundary via next
        assertEquals("a", loop.previous(), "Previous from end wraps to start"); // <a> b
        assertEquals("a", loop.next(), "Next after wrap returns to end"); // a <b>
        assertEquals("a", loop.previous(), "Previous again wraps to start"); // <a> b
    }

    @Test
    void testNextIndexAndPreviousIndex() {
        // Given
        final List<String> list = Arrays.asList("a", "b", "c");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b c

        // Then: Initial state
        assertEquals(0, loop.nextIndex(), "Next index at start");
        assertEquals(2, loop.previousIndex(), "Previous index at end");

        // When & Then: After next()
        assertEquals("a", loop.next());        // a <b> c
        assertEquals(1, loop.nextIndex(), "Next index after first next");
        assertEquals(0, loop.previousIndex(), "Previous index after first next");

        // When & Then: After previous()
        assertEquals("a", loop.previous());    // <a> b c
        assertEquals(0, loop.nextIndex(), "Next index after previous");
        assertEquals(2, loop.previousIndex(), "Previous index after previous");

        // When & Then: Boundary crossing via previous
        assertEquals("c", loop.previous());    // a b <c>
        assertEquals(2, loop.nextIndex(), "Next index at end");
        assertEquals(1, loop.previousIndex(), "Previous index at end");
    }

    @Test
    void testRemoveWhileIteratingForward() {
        // Given
        final List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b c

        // When: Removing during forward iteration
        assertEquals("a", loop.next()); // a <b> c
        loop.remove();                  // <b> c
        assertEquals(2, list.size(), "Size after first remove");

        assertEquals("b", loop.next()); // b <c>
        loop.remove();                  // <c>
        assertEquals(1, list.size(), "Size after second remove");

        assertEquals("c", loop.next()); // <c>
        loop.remove();                  // ---
        assertEquals(0, list.size(), "Size after third remove");

        // Then: Final state
        assertFalse(loop.hasNext(), "No elements left");
        assertThrows(NoSuchElementException.class, loop::next, "Next should fail");
    }

    @Test
    void testRemoveWhileIteratingBackward() {
        // Given
        final List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b c

        // When: Removing during backward iteration
        assertEquals("c", loop.previous()); // a b <c>
        loop.remove();                      // <a> b
        assertEquals(2, list.size(), "Size after first remove");

        assertEquals("b", loop.previous()); // a <b>
        loop.remove();                      // <a>
        assertEquals(1, list.size(), "Size after second remove");

        assertEquals("a", loop.previous()); // <a>
        loop.remove();                      // ---
        assertEquals(0, list.size(), "Size after third remove");

        // Then: Final state
        assertFalse(loop.hasPrevious(), "No elements left");
        assertThrows(NoSuchElementException.class, loop::previous, "Previous should fail");
    }

    @Test
    void testReset() {
        // Given
        final List<String> list = Arrays.asList("a", "b", "c");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b c

        // When: Iterating forward then resetting
        assertEquals("a", loop.next()); // a <b> c
        assertEquals("b", loop.next()); // a b <c>
        loop.reset();                   // <a> b c
        assertEquals("a", loop.next(), "Reset should return to start"); // a <b> c

        // When: Iterating backward then resetting
        loop.reset();                   // <a> b c
        assertEquals("c", loop.previous()); // a b <c>
        assertEquals("b", loop.previous()); // a <b> c
        loop.reset();                       // <a> b c
        assertEquals("c", loop.previous(), "Reset should allow previous from end"); // a b <c>
    }

    @Test
    void testSetElements() {
        // Given
        final List<String> list = new ArrayList<>(Arrays.asList("q", "r", "z"));
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <q> r z

        // When: Setting during backward iteration
        assertEquals("z", loop.previous()); // q r <z>
        loop.set("c");                      // q r <c>

        // When: Setting during forward iteration
        loop.reset();                       // <q> r c
        assertEquals("q", loop.next());     // q <r> c
        loop.set("a");                      // a <r> c

        assertEquals("r", loop.next());     // a r <c>
        loop.set("b");                      // a b <c>

        // Then: Verify all changes
        loop.reset();                       // <a> b c
        assertEquals("a", loop.next(), "First element set");
        assertEquals("b", loop.next(), "Second element set");
        assertEquals("c", loop.next(), "Third element set");
    }

    @Test
    void testAddWhenIteratingForward() {
        // Given
        List<String> list = new ArrayList<>(Arrays.asList("b", "e", "f"));
        LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <b> e f

        // When: Adding at start during forward iteration
        loop.add("a");                      // <a> b e f
        assertEquals("b", loop.next());     // a <b> e f
        loop.reset();                       // <a> b e f
        assertEquals("a", loop.next());     // a <b> e f
        assertEquals("b", loop.next());     // a b <e> f

        // When: Adding in middle during forward iteration
        loop.add("c");                      // a b c <e> f
        assertEquals("e", loop.next());     // a b c e <f>
        assertEquals("e", loop.previous()); // a b c <e> f
        assertEquals("c", loop.previous()); // a b <c> e f
        assertEquals("c", loop.next());     // a b c <e> f

        // When: Adding again and verifying full sequence
        loop.add("d");                      // a b c d <e> f
        loop.reset();                       // <a> b c d e f
        assertEquals("a", loop.next());     // a <b> c d e f
        assertEquals("b", loop.next());     // a b <c> d e f
        assertEquals("c", loop.next());     // a b c <d> e f
        assertEquals("d", loop.next());     // a b c d <e> f
        assertEquals("e", loop.next());     // a b c d e <f>
        assertEquals("f", loop.next());     // <a> b c d e f
        assertEquals("a", loop.next());     // a <b> c d e f
    }

    @Test
    void testAddWhenIteratingBackward() {
        // Given
        List<String> list = new ArrayList<>(Arrays.asList("b", "e", "f"));
        LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <b> e f

        // When: Adding at start during backward iteration
        loop.add("a");                      // a <b> e f
        assertEquals("a", loop.previous()); // a b e <f>
        loop.reset();                       // <a> b e f
        assertEquals("f", loop.previous()); // a b e <f>
        assertEquals("e", loop.previous()); // a b <e> f

        // When: Adding in middle during backward iteration
        loop.add("d");                      // a b d <e> f
        assertEquals("d", loop.previous()); // a b <d> e f
        loop.add("c");                      // a b c <d> e f
        assertEquals("c", loop.previous()); // a b <c> d e f

        // Then: Verify full sequence
        loop.reset();
        assertEquals("a", loop.next());     // a <b> c d e f
        assertEquals("b", loop.next());     // a b <c> d e f
        assertEquals("c", loop.next());     // a b c <d> e f
        assertEquals("d", loop.next());     // a b c d <e> f
        assertEquals("e", loop.next());     // a b c d e <f>
        assertEquals("f", loop.next());     // <a> b c d e f
        assertEquals("a", loop.next());     // a <b> c d e f
    }
}