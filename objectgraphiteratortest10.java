package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Contains tests for the constructors of {@link ObjectGraphIterator}.
 * This class focuses on the specific constructor that accepts a root iterator.
 */
class ObjectGraphIteratorConstructorTest {

    @Test
    @DisplayName("Constructor with a null iterator should create a valid empty iterator")
    void constructorWithNullIteratorShouldCreateEmptyIterator() {
        // Arrange: Define the input for the constructor, which is a null iterator.
        final Iterator<Object> rootIterator = null;

        // Act: Create the ObjectGraphIterator with the null root.
        final Iterator<Object> emptyIterator = new ObjectGraphIterator<>(rootIterator);

        // Assert: The resulting iterator should behave as a standard empty iterator.
        
        // 1. It should report that it has no more elements.
        assertFalse(emptyIterator.hasNext(), "An iterator created with a null root should be empty.");

        // 2. Calling next() should throw an exception.
        assertThrows(NoSuchElementException.class, 
                     () -> emptyIterator.next(),
                     "next() should throw NoSuchElementException on an empty iterator.");

        // 3. Calling remove() before a successful next() call should throw an exception.
        assertThrows(IllegalStateException.class, 
                     () -> emptyIterator.remove(),
                     "remove() should throw IllegalStateException if next() has not been called.");
    }
}