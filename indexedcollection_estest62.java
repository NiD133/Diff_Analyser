package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This test class focuses on specific behaviors of the IndexedCollection.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class IndexedCollectionRefactoredTest {

    /**
     * Tests that {@link IndexedCollection#contains(Object)} throws a NullPointerException
     * if the collection was created with a null keyTransformer.
     * <p>
     * The {@code contains} method requires the transformer to generate a key from the
     * input object before performing a lookup. It cannot operate if the transformer is null.
     */
    @Test(expected = NullPointerException.class)
    public void containsShouldThrowNullPointerExceptionWhenKeyTransformerIsNull() {
        // Arrange: Create an IndexedCollection with a null transformer.
        // The underlying collection can be empty as it's not relevant to this test.
        final Collection<Object> emptyCollection = new LinkedList<>();
        final Transformer<Object, Object> nullTransformer = null;

        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(emptyCollection, nullTransformer);

        // Act & Assert: Calling contains() should immediately throw a NullPointerException
        // because it attempts to use the null transformer. The argument to contains() is irrelevant.
        indexedCollection.contains(new Object());
    }
}