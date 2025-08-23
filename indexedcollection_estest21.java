package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.LinkedList;
import java.util.ConcurrentModificationException;

import static org.junit.Assert.fail;

/**
 * Test suite for {@link IndexedCollection}.
 * This class contains the refactored test case.
 */
public class IndexedCollection_ESTestTest21 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that a ConcurrentModificationException is thrown when removeAll is called
     * with the underlying collection as its argument.
     *
     * The removeAll() implementation iterates over the provided collection and calls
     * remove() on the decorated collection for each element. When the argument is the
     * decorated collection itself, this leads to modifying the collection while iterating
     * over it, which is an illegal state.
     */
    @Test
    public void removeAll_whenModifyingUnderlyingCollectionDuringIteration_throwsConcurrentModificationException() {
        // Arrange
        final LinkedList<String> baseList = new LinkedList<>();
        baseList.add("Element 1");
        baseList.add("Element 2");

        // A transformer is required for the constructor, but its behavior is not relevant to this test.
        final Transformer<String, String> keyTransformer = ConstantTransformer.constantTransformer("anyKey");

        final IndexedCollection<String, String> indexedCollection =
            IndexedCollection.nonUniqueIndexedCollection(baseList, keyTransformer);

        // Act & Assert
        try {
            // This call will fail because it attempts to modify baseList while iterating over it.
            indexedCollection.removeAll(baseList);
            fail("Expected a ConcurrentModificationException to be thrown.");
        } catch (final ConcurrentModificationException e) {
            // This is the expected outcome. The test passes.
        }
    }
}