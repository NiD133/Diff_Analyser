package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link IndexedCollection} class, focusing on edge cases
 * during its construction.
 */
public class IndexedCollectionTest {

    /**
     * Tests that creating an IndexedCollection with a cloning transformer on a
     * self-referential collection causes a StackOverflowError.
     * <p>
     * The constructor of IndexedCollection immediately indexes the elements of the
     * source collection. This involves applying the provided key transformer to each
     * element. When the transformer attempts to clone a collection that contains
     * itself, it results in an infinite recursion, leading to a StackOverflowError.
     */
    @Test
    public void constructorWithCloningTransformerOnSelfReferentialCollectionThrowsStackOverflowError() {
        // Arrange: Create a collection that contains a reference to itself.
        Collection<Object> selfReferentialCollection = new LinkedList<>();
        selfReferentialCollection.add(selfReferentialCollection);

        // Arrange: A transformer that attempts to deep-clone its input.
        Transformer<Object, Object> cloningTransformer = CloneTransformer.cloneTransformer();

        // Act & Assert: Expect a StackOverflowError when creating the IndexedCollection.
        // The constructor's re-indexing process will trigger the cloning transformer on the
        // self-referential collection, causing the overflow.
        // Note: A try-catch block is used here because @Test(expected=...) may not
        // reliably catch Errors like StackOverflowError in all JUnit versions.
        try {
            IndexedCollection.nonUniqueIndexedCollection(selfReferentialCollection, cloningTransformer);
            fail("A StackOverflowError was expected but not thrown.");
        } catch (final StackOverflowError e) {
            // This is the expected outcome, so the test passes.
        }
    }
}