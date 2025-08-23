package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Contains tests for edge cases in the {@link IndexedCollection} class.
 */
public class IndexedCollectionTest {

    /**
     * Tests that attempting to decorate an existing {@code IndexedCollection} with another
     * {@code IndexedCollection} causes a {@code StackOverflowError}.
     * <p>
     * This occurs because the constructor of the new {@code IndexedCollection} calls
     * {@code reindex()}, which attempts to get an iterator from the decorated collection.
     * Since {@code IndexedCollection} is a decorator, its {@code iterator()} method delegates
     * back to the collection it wraps. This creates an infinite recursive loop when one
     * {@code IndexedCollection} wraps another.
     */
    @Test(expected = StackOverflowError.class)
    public void testDecoratingAnIndexedCollectionWithAnotherCausesStackOverflow() {
        // Arrange
        // Create an initial IndexedCollection. The base collection and key transformer
        // are arbitrary, as the collection's contents are never actually accessed
        // before the error occurs.
        final Collection<Object> baseCollection = new LinkedList<>();
        final IndexedCollection<Object, Object> collectionToDecorate =
                IndexedCollection.nonUniqueIndexedCollection(baseCollection, NOPTransformer.nopTransformer());

        // A transformer for the new collection we are attempting to create.
        final Transformer<Object, Object> keyTransformer = NOPTransformer.nopTransformer();

        // Act & Assert
        // This call is expected to fail with a StackOverflowError during construction
        // because the new collection tries to iterate over the one it's decorating,
        // leading to infinite recursion.
        IndexedCollection.uniqueIndexedCollection(collectionToDecorate, keyTransformer);
    }
}