package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Test suite for {@link IndexedCollection}.
 * This class contains a specific test case that was improved for understandability.
 */
public class IndexedCollection_ESTestTest44 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that adding the backing collection to itself causes a StackOverflowError
     * when an identity-based transformer is used.
     *
     * <p>This scenario occurs when an {@link IndexedCollection} is configured with a
     * transformer that uses the element itself as the key (e.g., {@link NOPTransformer}).
     * The {@code add(object)} operation involves two main steps:</p>
     * <ol>
     *   <li>Updating an internal index map, which requires using the object as a key.
     *       This may trigger a call to the key's {@code hashCode()} or {@code equals()} method.</li>
     *   <li>Adding the object to the backing collection.</li>
     * </ol>
     * <p>When the backing collection is added to itself, it becomes self-referential.
     * If its {@code hashCode()} method is called *after* this self-reference is established
     * (e.g., during the indexing step), it will lead to infinite recursion, resulting
     * in a {@link StackOverflowError}. This test verifies this specific edge case.</p>
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void addBackingCollectionToItselfWithIdentityTransformerThrowsStackOverflowError() {
        // Arrange
        // The collection that backs the IndexedCollection.
        Collection<Object> backingList = new LinkedList<>();

        // A transformer that returns the input object itself as the key.
        Transformer<Object, Object> identityTransformer = NOPTransformer.nopTransformer();

        // The IndexedCollection under test, configured with the backing list and transformer.
        IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(backingList, identityTransformer);

        // Act & Assert
        // Attempt to add the backing list to the collection it backs. This action
        // is expected to cause a StackOverflowError due to recursive hashCode calculation
        // on the now self-referential list during the indexing process.
        indexedCollection.add(backingList);
    }
}