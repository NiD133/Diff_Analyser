package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link IndexedCollection#get(Object)} method.
 */
// The original test class name is kept for context, but in a real project,
// it would be renamed to something like IndexedCollectionTest.
public class IndexedCollection_ESTestTest56 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that get() returns null when the collection is empty.
     */
    @Test
    public void getShouldReturnNullWhenCollectionIsEmpty() {
        // Arrange: Create an empty collection and an IndexedCollection decorator.
        final Collection<String> emptyCollection = new LinkedList<>();

        // The key transformer can be any valid transformer. Its behavior is irrelevant
        // for an empty collection.
        final Transformer<String, Object> keyTransformer = ConstantTransformer.constantTransformer("ANY_KEY");

        final IndexedCollection<Object, String> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(emptyCollection, keyTransformer);

        // An arbitrary key to search for.
        final Object keyToSearch = new Object();

        // Act: Attempt to retrieve an element from the empty collection.
        final String result = indexedCollection.get(keyToSearch);

        // Assert: The result should be null, as no element can be found for any key.
        assertNull("get() on an empty collection should return null", result);
    }
}