package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link IndexedCollection} class.
 */
public class IndexedCollectionTest {

    /**
     * Tests that the get() method correctly retrieves an element using its key.
     */
    @Test
    public void getShouldReturnCorrectObjectForExistingKey() {
        // Arrange
        final List<String> sourceList = new ArrayList<>();
        final String elementToAdd = "test_element";
        sourceList.add(elementToAdd);

        // The NOPTransformer (No-Operation) uses the element itself as the key.
        final Transformer<String, String> identityTransformer = NOPTransformer.nopTransformer();

        final IndexedCollection<String, String> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(sourceList, identityTransformer);

        // Act
        // Retrieve the element using its value as the key.
        final String retrievedElement = indexedCollection.get(elementToAdd);

        // Assert
        // The retrieved object should be the exact same instance that was added.
        assertSame("The element retrieved via get() should be the same instance as the one originally added.",
                     elementToAdd, retrievedElement);
    }
}