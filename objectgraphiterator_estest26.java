package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that the constructor correctly handles a null root object by creating
     * an empty iterator, as specified in its documentation.
     */
    @Test
    public void constructorWithNullRootShouldCreateEmptyIterator() {
        // Arrange: Define a transformer. Its behavior is irrelevant for a null root,
        // but it is a required argument for the constructor.
        final Transformer<Object, Boolean> transformer = ConstantTransformer.nullTransformer();

        // Act: Create an ObjectGraphIterator with a null root.
        final ObjectGraphIterator<Boolean> iterator = new ObjectGraphIterator<>(null, transformer);

        // Assert: The iterator should be successfully created and be empty.
        assertNotNull("The iterator instance should not be null.", iterator);
        assertFalse("An iterator created with a null root should have no elements.", iterator.hasNext());
    }
}