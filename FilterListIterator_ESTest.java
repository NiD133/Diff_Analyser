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

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Provides a more understandable and maintainable suite of tests for the {@link FilterListIterator}.
 * This class replaces an auto-generated test suite with focused, clearly-named tests.
 */
public class FilterListIteratorTest {

    private List<Integer> sourceList;
    private List<Integer> evenNumbers;
    private Predicate<Integer> evenNumberPredicate;
    private Predicate<Integer> falsePredicate;

    @Before
    public void setUp() {
        sourceList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));
        evenNumbers = Arrays.asList(0, 2, 4, 6);
        evenNumberPredicate = number -> number != null && number % 2 == 0;
        falsePredicate = object -> false;
    }

    private <T> List<T> drainIterator(ListIterator<T> iterator) {
        List<T> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    // --- Basic Iteration Tests ---

    @Test
    public void nextShouldReturnOnlyMatchingElements() {
        // Arrange
        ListIterator<Integer> filteredIterator = new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);

        // Act
        List<Integer> actual = drainIterator(filteredIterator);

        // Assert
        assertEquals(evenNumbers, actual);
    }

    @Test
    public void previousShouldReturnOnlyMatchingElementsInReverse() {
        // Arrange
        ListIterator<Integer> filteredIterator = new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);
        // Go to the end of the iterator
        while (filteredIterator.hasNext()) {
            filteredIterator.next();
        }

        // Act
        List<Integer> actualReversed = new ArrayList<>();
        while (filteredIterator.hasPrevious()) {
            actualReversed.add(filteredIterator.previous());
        }

        // Assert
        List<Integer> expectedReversed = new ArrayList<>(evenNumbers);
        Collections.reverse(expectedReversed);
        assertEquals(expectedReversed, actualReversed);
    }

    @Test
    public void shouldIterateCorrectlyWhenNoElementsMatch() {
        // Arrange
        ListIterator<Integer> filteredIterator = new FilterListIterator<>(sourceList.listIterator(), falsePredicate);

        // Act & Assert
        assertFalse(filteredIterator.hasNext());
        assertFalse(filteredIterator.hasPrevious());
    }

    @Test
    public void shouldIterateCorrectlyWhenAllElementsMatch() {
        // Arrange
        ListIterator<Integer> filteredIterator = new FilterListIterator<>(sourceList.listIterator(), TruePredicate.truePredicate());

        // Act
        List<Integer> actual = drainIterator(filteredIterator);

        // Assert
        assertEquals(sourceList, actual);
    }

    @Test
    public void shouldWorkWithEmptyList() {
        // Arrange
        List<Integer> emptyList = Collections.emptyList();
        ListIterator<Integer> filteredIterator = new FilterListIterator<>(emptyList.listIterator(), evenNumberPredicate);

        // Act & Assert
        assertFalse(filteredIterator.hasNext());
        assertFalse(filteredIterator.hasPrevious());
    }

    @Test
    public void alternatingNextAndPreviousShouldMaintainCorrectPosition() {
        // Arrange
        ListIterator<Integer> iterator = new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);

        // Act & Assert
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.previous());
        assertEquals(Integer.valueOf(0), iterator.previous());
        assertFalse(iterator.hasPrevious());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(4), iterator.next());
        assertEquals(Integer.valueOf(6), iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(Integer.valueOf(6), iterator.previous());
    }

    // --- Indexing Tests ---

    @Test
    public void nextIndexAndPreviousIndexShouldBeCorrectDuringForwardIteration() {
        // Arrange
        ListIterator<Integer> iterator = new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);

        // Assert initial state
        assertEquals(0, iterator.nextIndex());
        assertEquals(-1, iterator.previousIndex());

        // Assert after first next()
        iterator.next(); // returns 0
        assertEquals(1, iterator.nextIndex());
        assertEquals(0, iterator.previousIndex());

        // Assert after second next()
        iterator.next(); // returns 2
        assertEquals(2, iterator.nextIndex());
        assertEquals(1, iterator.previousIndex());

        // Drain the rest
        drainIterator(iterator);

        // Assert final state
        assertEquals(evenNumbers.size(), iterator.nextIndex());
        assertEquals(evenNumbers.size() - 1, iterator.previousIndex());
    }

    // --- Exception and Edge Case Tests ---

    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowExceptionWhenNoMoreElements() {
        // Arrange
        ListIterator<Integer> iterator = new FilterListIterator<>(sourceList.listIterator(), falsePredicate);

        // Act
        iterator.next(); // Should throw
    }

    @Test(expected = NoSuchElementException.class)
    public void previousShouldThrowExceptionWhenNoPreviousElements() {
        // Arrange
        ListIterator<Integer> iterator = new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);

        // Act
        iterator.previous(); // Should throw
    }

    @Test(expected = ConcurrentModificationException.class)
    public void hasNextShouldThrowExceptionWhenUnderlyingListIsModified() {
        // Arrange
        List<Integer> modifiableList = new ArrayList<>(Arrays.asList(1, 2, 3));
        ListIterator<Integer> iterator = new FilterListIterator<>(modifiableList.listIterator(), TruePredicate.truePredicate());

        // Act
        modifiableList.add(4);

        // Assert
        iterator.hasNext(); // Should throw
    }

    @Test(expected = ConcurrentModificationException.class)
    public void hasPreviousShouldThrowExceptionWhenUnderlyingListIsModified() {
        // Arrange
        List<Integer> modifiableList = new ArrayList<>(Arrays.asList(1, 2, 3));
        ListIterator<Integer> iterator = new FilterListIterator<>(modifiableList.listIterator(), TruePredicate.truePredicate());
        iterator.next(); // Move cursor forward

        // Act
        modifiableList.add(4);

        // Assert
        iterator.hasPrevious(); // Should throw
    }

    @Test
    public void shouldPropagateExceptionFromPredicate() {
        // Arrange
        Predicate<Object> failingPredicate = o -> {
            throw new RuntimeException("Predicate failure");
        };
        ListIterator<Integer> iterator = new FilterListIterator<>(sourceList.listIterator(), failingPredicate);

        // Act & Assert
        try {
            iterator.hasNext();
            fail("Expected RuntimeException was not thrown.");
        } catch (RuntimeException e) {
            assertEquals("Predicate failure", e.getMessage());
        }
    }
    
    @Test
    public void shouldHandleNullsInList() {
        // Arrange
        List<Integer> listWithNulls = new ArrayList<>(Arrays.asList(0, null, 2, 3, null, 6));
        Predicate<Integer> notNullPredicate = object -> object != null;
        ListIterator<Integer> iterator = new FilterListIterator<>(listWithNulls.listIterator(), notNullPredicate);
        
        // Act
        List<Integer> actual = drainIterator(iterator);
        
        // Assert
        assertEquals(Arrays.asList(0, 2, 3, 6), actual);
    }

    // --- Unsupported Operation Tests ---

    @Test(expected = UnsupportedOperationException.class)
    public void addShouldThrowUnsupportedOperationException() {
        // Arrange
        ListIterator<Integer> iterator = new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);

        // Act
        iterator.add(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeShouldThrowUnsupportedOperationException() {
        // Arrange
        ListIterator<Integer> iterator = new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);
        iterator.next();

        // Act
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setShouldThrowUnsupportedOperationException() {
        // Arrange
        ListIterator<Integer> iterator = new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);
        iterator.next();

        // Act
        iterator.set(100);
    }
    
    // --- Constructor and Setter Tests ---

    @Test
    public void defaultConstructorShouldCreateIteratorWithNullProperties() {
        // Arrange
        FilterListIterator<Integer> iterator = new FilterListIterator<>();
        
        // Assert
        assertNull(iterator.getListIterator());
        assertNull(iterator.getPredicate());
    }
    
    @Test
    public void setListIteratorShouldResetTheIterator() {
        // Arrange
        ListIterator<Integer> iterator = new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        
        // Act
        // Reset with a new iterator for the same list
        ((FilterListIterator<Integer>) iterator).setListIterator(sourceList.listIterator());
        
        // Assert
        assertEquals(0, iterator.nextIndex());
        assertEquals(-1, iterator.previousIndex());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
    }
}