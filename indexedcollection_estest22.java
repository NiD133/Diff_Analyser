package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Contains tests for the {@link IndexedCollection} class, focusing on edge cases and exception handling.
 */
public class IndexedCollectionTest {

    /**
     * Tests that calling removeAll() with a null argument throws a NullPointerException.
     * The behavior is mandated by the java.util.Collection interface specification.
     */
    @Test(expected = NullPointerException.class)
    public void removeAllWithNullCollectionShouldThrowNullPointerException() {
        // Arrange: Create an empty IndexedCollection. The specific contents do not matter
        // for this test, as the null check should happen before any elements are processed.
        Collection<Integer> decoratedCollection = new LinkedList<>();
        Transformer<Integer, Integer> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Integer, Integer> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(decoratedCollection, keyTransformer);

        // Act & Assert: Calling removeAll with a null collection is expected to throw.
        indexedCollection.removeAll(null);
    }
}