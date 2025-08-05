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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A unit test for the basic functions of {@link BoundedIterator}.
 */
@DisplayName("BoundedIterator")
public class BoundedIteratorTest extends AbstractIteratorTest<String> {

    /** Test array of size 7 */
    private final String[] testArray = {
        "a", "b", "c", "d", "e", "f", "g"
    };

    private List<String> testList;

    @Override
    public Iterator<String> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<String>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<String> makeObject() {
        // Creates an iterator that is bounded from the second element to the end.
        return new BoundedIterator<>(new ArrayList<>(testList).iterator(), 1, testList.size() - 1);
    }

    @BeforeEach
    public void setUp() {
        testList = Arrays.asList(testArray);
    }

    @Test
    @DisplayName("Should iterate over a sub-list when offset and max define a specific range")
    void shouldIterateOverSubListWhenOffsetAndMaxAreWithinBounds() {
        // Arrange: BoundedIterator should start at index 2 ("c") and return at most 4 elements.
        final int offset = 2;
        final int maxElements = 4;
        final Iterator<String> boundedIterator = new BoundedIterator<>(testList.iterator(), offset, maxElements);

        // Act
        final List<String> result = new ArrayList<>();
        boundedIterator.forEachRemaining(result::add);

        // Assert
        final List<String> expected = testList.subList(2, 6); // "c", "d", "e", "f"
        assertEquals(expected, result);
        assertFalse(boundedIterator.hasNext());
        assertThrows(NoSuchElementException.class, boundedIterator::next);
    }

    @Test
    @DisplayName("Should be empty when max is zero")
    void shouldBeEmptyWhenMaxIsZero() {
        // Arrange
        final Iterator<String> boundedIterator = new BoundedIterator<>(testList.iterator(), 3, 0);

        // Act & Assert
        assertFalse(boundedIterator.hasNext());
        assertThrows(NoSuchElementException.class, boundedIterator::next);
    }

    @Test
    @DisplayName("Should iterate to the end of underlying iterator when max is larger than remaining elements")
    void shouldIterateToEndWhenMaxIsLargerThanRemaining() {
        // Arrange: Start at index 1 ("b") and request up to 10 elements.
        final int offset = 1;
        final int maxElements = 10;
        final Iterator<String> boundedIterator = new BoundedIterator<>(testList.iterator(), offset, maxElements);

        // Act
        final List<String> result = new ArrayList<>();
        boundedIterator.forEachRemaining(result::add);

        // Assert
        final List<String> expected = testList.subList(1, testList.size()); // "b" through "g"
        assertEquals(expected, result);
        assertFalse(boundedIterator.hasNext());
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException for a negative max")
    void constructorShouldThrowExceptionForNegativeMax() {
        // Arrange
        final int invalidMax = -1;

        // Act & Assert
        final Exception e = assertThrows(IllegalArgumentException.class,
            () -> new BoundedIterator<>(testList.iterator(), 3, invalidMax));
        assertEquals("Max parameter must not be negative.", e.getMessage());
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException for a negative offset")
    void constructorShouldThrowExceptionForNegativeOffset() {
        // Arrange
        final int invalidOffset = -1;

        // Act & Assert
        final Exception e = assertThrows(IllegalArgumentException.class,
            () -> new BoundedIterator<>(testList.iterator(), invalidOffset, 4));
        assertEquals("Offset parameter must not be negative.", e.getMessage());
    }

    @Test
    @DisplayName("Should be empty when offset is greater than the underlying iterator's size")
    void shouldBeEmptyWhenOffsetIsGreaterThanSize() {
        // Arrange
        final Iterator<String> boundedIterator = new BoundedIterator<>(testList.iterator(), 10, 4);

        // Act & Assert
        assertFalse(boundedIterator.hasNext());
        assertThrows(NoSuchElementException.class, boundedIterator::next);
    }

    @Test
    @DisplayName("remove() should throw IllegalStateException when called twice in a row")
    void removeShouldThrowExceptionWhenCalledTwice() {
        // Arrange
        final List<String> modifiableList = new ArrayList<>(testList);
        final Iterator<String> boundedIterator = new BoundedIterator<>(modifiableList.iterator(), 1, 5);
        boundedIterator.next(); // Consume "b"
        boundedIterator.remove(); // First remove() is valid

        // Act & Assert
        assertThrows(IllegalStateException.class, boundedIterator::remove);
    }

    @Test
    @DisplayName("Should remove the first element of the bounded range from the underlying collection")
    void shouldRemoveFirstElementOfBoundedRange() {
        // Arrange: Bounded range starts at "b"
        final List<String> modifiableList = new ArrayList<>(testList);
        final Iterator<String> boundedIterator = new BoundedIterator<>(modifiableList.iterator(), 1, 5);
        final String firstElement = testList.get(1); // "b"

        // Act
        boundedIterator.next(); // Consume "b"
        boundedIterator.remove();

        // Assert
        assertFalse(modifiableList.contains(firstElement));
        assertEquals(testList.size() - 1, modifiableList.size());

        // Verify remaining elements in iterator
        final List<String> result = new ArrayList<>();
        boundedIterator.forEachRemaining(result::add);
        final List<String> expected = testList.subList(2, 6); // "c", "d", "e", "f"
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should remove the last element returned by next() from the underlying collection")
    void shouldRemoveLastElementOfBoundedRange() {
        // Arrange: Bounded range is "b", "c", "d", "e", "f"
        final List<String> modifiableList = new ArrayList<>(testList);
        final Iterator<String> boundedIterator = new BoundedIterator<>(modifiableList.iterator(), 1, 5);
        final String lastElementInBounds = testList.get(5); // "f"

        // Act: Consume all elements, so the last element returned by next() is "f"
        while (boundedIterator.hasNext()) {
            boundedIterator.next();
        }
        boundedIterator.remove(); // Remove "f"

        // Assert
        assertFalse(modifiableList.contains(lastElementInBounds), "Element 'f' should have been removed.");
        assertEquals(testList.size() - 1, modifiableList.size());
        final List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e", "g");
        assertEquals(expectedList, modifiableList);
    }

    @Test
    @DisplayName("Should remove a middle element of the bounded range from the underlying collection")
    void shouldRemoveMiddleElementOfBoundedRange() {
        // Arrange: Bounded range is "b", "c", "d", "e", "f"
        final List<String> modifiableList = new ArrayList<>(testList);
        final Iterator<String> boundedIterator = new BoundedIterator<>(modifiableList.iterator(), 1, 5);
        final String middleElement = testList.get(3); // "d"

        // Act
        boundedIterator.next(); // "b"
        boundedIterator.next(); // "c"
        boundedIterator.next(); // "d"
        boundedIterator.remove(); // remove "d"

        // Assert
        assertFalse(modifiableList.contains(middleElement));
        assertEquals(testList.size() - 1, modifiableList.size());

        // Verify remaining elements in iterator
        final List<String> result = new ArrayList<>();
        boundedIterator.forEachRemaining(result::add);
        final List<String> expected = testList.subList(4, 6); // "e", "f"
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("remove() should throw UnsupportedOperationException when underlying iterator does not support it")
    void removeShouldThrowExceptionWhenNotSupportedByUnderlying() {
        // Arrange
        final Iterator<String> mockIterator = new AbstractIteratorDecorator<String>(testList.iterator()) {
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        final Iterator<String> boundedIterator = new BoundedIterator<>(mockIterator, 1, 5);
        boundedIterator.next(); // Consume "b"

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, boundedIterator::remove);
    }

    @Test
    @DisplayName("remove() should throw IllegalStateException when called before next()")
    void removeShouldThrowExceptionWhenCalledBeforeNext() {
        // Arrange
        final List<String> modifiableList = new ArrayList<>(testList);
        final Iterator<String> boundedIterator = new BoundedIterator<>(modifiableList.iterator(), 1, 5);

        // Act & Assert
        final Exception e = assertThrows(IllegalStateException.class, boundedIterator::remove);
        assertEquals("remove() cannot be called before calling next()", e.getMessage());
    }

    @Test
    @DisplayName("Should iterate over all elements when offset is 0 and max is size")
    void shouldIterateOverAllElementsWhenNotEffectivelyBounded() {
        // Arrange
        final Iterator<String> boundedIterator = new BoundedIterator<>(testList.iterator(), 0, testList.size());

        // Act
        final List<String> result = new ArrayList<>();
        boundedIterator.forEachRemaining(result::add);

        // Assert
        assertEquals(testList, result);
        assertFalse(boundedIterator.hasNext());
    }
}