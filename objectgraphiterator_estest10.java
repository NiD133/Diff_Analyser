package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for the {@link ObjectGraphIterator} class, focusing on its constructors.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that the constructor taking an iterator as a root creates a new,
     * distinct iterator instance.
     */
    @Test
    public void constructorWithRootIteratorShouldCreateDistinctInstance() {
        // Arrange
        // Create a base iterator that will serve as the root for another iterator.
        ObjectGraphIterator<String> originalIterator = new ObjectGraphIterator<>("root element");

        // Act
        // Create a new iterator using the original iterator as its root.
        // This invokes the constructor: public ObjectGraphIterator(final Iterator<? extends E> rootIterator)
        ObjectGraphIterator<String> newIterator = new ObjectGraphIterator<>(originalIterator);

        // Assert
        // The new iterator should be a distinct object instance from the original.
        // We use assertNotSame because ObjectGraphIterator does not override equals(),
        // and we want to explicitly test for reference inequality.
        assertNotSame(originalIterator, newIterator);
    }
}