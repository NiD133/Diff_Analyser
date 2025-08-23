package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Contains test cases for the {@link IndexedCollection} class.
 */
public class IndexedCollectionTest {

    /**
     * Verifies that calling addAll() with a null collection argument
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void addAll_withNullCollection_shouldThrowNullPointerException() {
        // Arrange: Create an instance of IndexedCollection.
        // The specific backing collection and transformer are not critical for this test,
        // as the null check on the input collection should occur before they are used.
        final Collection<Object> backingCollection = new LinkedList<>();
        final Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();
        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(backingCollection, keyTransformer);

        // Act: Attempt to add a null collection.
        // The test expects this line to throw a NullPointerException, which is
        // verified by the @Test(expected=...) annotation.
        indexedCollection.addAll(null);
    }
}