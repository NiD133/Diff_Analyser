package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;

/**
 * Test class for {@link IndexedCollection}.
 * This class contains a specific test case for the contains() method.
 */
public class IndexedCollection_ESTestTest7 {

    /**
     * Tests that the contains() method returns false when called on an empty collection.
     */
    @Test
    public void containsShouldReturnFalseOnEmptyCollection() {
        // Arrange
        // 1. Create an empty collection to be decorated.
        final Collection<Object> baseCollection = new LinkedList<>();

        // 2. Define a transformer that maps any element to a single key.
        //    For this test, the specific key doesn't matter as the collection is empty.
        final Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();

        // 3. Create the IndexedCollection, which will also be empty.
        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(baseCollection, keyTransformer);

        // Act
        // 4. Check if the empty collection contains an arbitrary object.
        final boolean result = indexedCollection.contains("some object");

        // Assert
        // 5. The result should be false, as an empty collection cannot contain any elements.
        assertFalse("An empty IndexedCollection should not report containing any object.", result);
    }
}