package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ForClosure;
import org.apache.commons.collections4.functors.NOPClosure;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// Note: Many unused imports from the original generated test have been removed for clarity.

public class IndexedCollection_ESTestTest29 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that creating an IndexedCollection with a computationally expensive
     * key transformer completes within a reasonable time limit. This serves as a
     * basic performance regression test for the indexing process.
     */
    @Test(timeout = 4000)
    public void testCreationWithSlowTransformerCompletesWithinTimeout() {
        // Arrange: Create a source collection with a single element.
        final Collection<Integer> sourceCollection = new LinkedList<>();
        final Integer element = 5980;
        sourceCollection.add(element);

        // Arrange: Create a "slow" transformer. This transformer simulates a
        // computationally expensive key-generation process by looping many times.
        // The number of loops is deliberately set to be the same as the element's value,
        // a characteristic of the original auto-generated test.
        final int expensiveLoopCount = 5980;
        final Closure<Integer> longRunningClosure = new ForClosure<>(expensiveLoopCount, NOPClosure.nopClosure());
        final Transformer<Integer, Integer> slowKeyTransformer = new ClosureTransformer<>(longRunningClosure);

        // Act: Create the IndexedCollection. The constructor will iterate through the
        // source collection and apply the slow transformer to each element to build its index.
        final IndexedCollection<Integer, Integer> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(sourceCollection, slowKeyTransformer);

        // Assert: The main assertion is the @Test(timeout), which ensures the operation
        // completes within 4 seconds. We also add explicit assertions to verify the
        // resulting collection is correctly initialized.
        assertNotNull("The indexed collection should not be null.", indexedCollection);
        assertEquals("The indexed collection should have one element.", 1, indexedCollection.size());
        assertTrue("The indexed collection should contain the original element.", indexedCollection.contains(element));
    }
}