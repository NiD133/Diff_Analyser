package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link IndexedCollection}.
 * This class contains the refactored test case.
 */
public class IndexedCollectionTest {

    /**
     * Tests that if the keyTransformer throws an exception during a remove operation,
     * that exception is propagated to the caller.
     */
    @Test
    public void removeShouldPropagateExceptionFromKeyTransformer() {
        // Arrange
        // 1. Create a transformer that is guaranteed to fail because the method name is invalid.
        final String invalidMethodName = "nonExistentMethod";
        final Transformer<Integer, Object> faultyTransformer = InvokerTransformer.invokerTransformer(invalidMethodName);

        // 2. Create the collection to be decorated.
        final Collection<Integer> sourceCollection = new LinkedList<>();
        final IndexedCollection<Object, Integer> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, faultyTransformer);

        // 3. Add an element to the collection. The remove() operation will act on this element.
        final Integer elementToRemove = 123;
        sourceCollection.add(elementToRemove);

        // Act & Assert
        // The remove() method must first use the transformer to calculate the element's key
        // to remove it from the internal index. This transformation will fail.
        try {
            indexedCollection.remove(elementToRemove);
            fail("Expected a RuntimeException to be thrown due to the faulty transformer.");
        } catch (final RuntimeException e) {
            // Verify that the exception is the one we expect from InvokerTransformer.
            final String expectedMessage = "InvokerTransformer: The method 'nonExistentMethod' on 'class java.lang.Integer' does not exist";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}