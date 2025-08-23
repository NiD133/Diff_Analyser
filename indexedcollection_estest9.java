package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Contains tests for edge cases in the {@link IndexedCollection} class.
 * This class retains the original test's structure but improves its clarity.
 */
public class IndexedCollection_ESTestTest9 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that calling {@code values()} with a self-referential collection as a key
     * results in a StackOverflowError.
     * <p>
     * This is an edge case where the underlying map, which powers the index,
     * attempts to compute the hashCode of the key. For a collection that contains
     * itself, this leads to infinite recursion.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void valuesWithSelfReferentialListAsKeyShouldThrowStackOverflow() {
        // Arrange
        // A list that will be used as the decorated collection and also as an element within itself.
        final LinkedList<Object> selfReferentialList = new LinkedList<>();

        // A transformer is required for construction, but its behavior is not central to this test.
        final Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();
        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(selfReferentialList, keyTransformer);

        // Add the list to the decorated collection. Since the decorated collection
        // IS the list, the list now contains itself, creating a circular reference.
        indexedCollection.add(selfReferentialList);

        // Act
        // Attempt to look up values using the self-referential list as the key.
        // This triggers the list's hashCode() method, which will recurse infinitely
        // and cause a StackOverflowError before any value can be returned.
        indexedCollection.values(selfReferentialList);

        // Assert is handled by the 'expected' parameter in the @Test annotation.
    }
}