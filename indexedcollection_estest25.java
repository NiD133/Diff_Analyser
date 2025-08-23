package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the IndexedCollection.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class IndexedCollection_ESTestTest25 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that reindex() propagates a RuntimeException thrown by the key transformer.
     * This scenario occurs when the underlying collection is modified directly,
     * and reindex() is called to synchronize the index.
     */
    @Test
    public void reindexShouldPropagateExceptionFromFailingTransformer() {
        // Arrange
        // 1. A transformer designed to fail on Integer objects by invoking a non-existent method.
        //    The original test used ".", which is a valid but unusual method name to test.
        final Transformer<Integer, Object> failingTransformer = InvokerTransformer.invokerTransformer(".");

        // 2. The underlying collection that the IndexedCollection will decorate.
        final Collection<Integer> underlyingCollection = new LinkedList<>();

        // 3. Create the IndexedCollection while the underlying collection is empty.
        //    The initial reindex() in the constructor will do nothing and succeed.
        final IndexedCollection<Object, Integer> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(underlyingCollection, failingTransformer);

        // 4. Modify the underlying collection directly, making the index out of sync.
        //    This is the scenario where an explicit call to reindex() is needed.
        underlyingCollection.add(-1);

        // Act & Assert
        try {
            indexedCollection.reindex();
            fail("Expected a RuntimeException because the transformer should fail.");
        } catch (final RuntimeException e) {
            // Verify that the exception from the InvokerTransformer was propagated correctly.
            final String expectedMessage = "InvokerTransformer: The method '.' on 'class java.lang.Integer' does not exist";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}