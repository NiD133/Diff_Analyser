package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link IndexedCollection#retainAll(Collection)} method.
 */
public class IndexedCollectionRetainAllTest {

    /**
     * Tests that calling retainAll with an empty collection on a non-empty IndexedCollection
     * correctly removes all elements and returns true.
     */
    @Test
    public void retainAllWithEmptyCollectionShouldRemoveAllElements() {
        // Arrange: Create a non-empty IndexedCollection.
        final Transformer<String, String> keyTransformer = ConstantTransformer.nullTransformer();
        final Collection<String> decoratedCollection = new ArrayList<>();
        decoratedCollection.add("element1");

        final IndexedCollection<String, String> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(decoratedCollection, keyTransformer);

        // Act: Call retainAll with an empty collection. This should modify the original collection.
        final boolean wasModified = indexedCollection.retainAll(Collections.emptyList());

        // Assert: Verify the collection was modified and is now empty.
        assertTrue("retainAll should return true because the collection was modified.", wasModified);
        assertTrue("The collection should be empty after retaining an empty collection.", indexedCollection.isEmpty());
    }
}