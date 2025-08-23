package org.apache.commons.collections4.collection;

import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This test class contains tests for the {@link IndexedCollection} class.
 * This particular test was improved for clarity from an auto-generated test case.
 */
public class IndexedCollection_ESTestTest15 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that calling retainAll() with a null collection argument
     * throws a NullPointerException.
     * The behavior is inherited from AbstractCollection and is a standard contract.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void retainAllShouldThrowNullPointerExceptionWhenArgumentIsNull() {
        // Arrange: Create an empty IndexedCollection.
        // The key transformer can be null for this test since the collection is empty
        // and the retainAll method doesn't use it before the null check.
        final Collection<Integer> sourceCollection = new LinkedList<>();
        final IndexedCollection<Integer, Integer> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, null);

        // Act & Assert: Call retainAll with null and expect a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        indexedCollection.retainAll(null);
    }
}