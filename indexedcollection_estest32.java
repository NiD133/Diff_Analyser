package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.fail;

/**
 * Contains tests for edge cases in {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    /**
     * Tests that calling removeAll with a self-referential list causes a StackOverflowError.
     * <p>
     * A self-referential list (one that contains itself as an element) can cause
     * infinite recursion when its equals() method is called. The removeAll() method
     * of the decorated list triggers this recursive call, leading to a stack overflow.
     */
    @Test(timeout = 4000)
    public void removeAllWithSelfReferentialListShouldCauseStackOverflow() {
        // Arrange: Create a list that contains itself as an element.
        LinkedList<Object> selfReferentialList = new LinkedList<>();
        selfReferentialList.add("some element");
        selfReferentialList.add(selfReferentialList); // The list now contains itself.

        // Wrap the list in an IndexedCollection. The specific transformer is not
        // critical to this bug but is required for the constructor.
        Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Object, Object> indexedCollection =
            IndexedCollection.nonUniqueIndexedCollection(selfReferentialList, keyTransformer);

        // Act & Assert: The removeAll() call will iterate through selfReferentialList.
        // When it attempts to remove the list from itself, it delegates to
        // selfReferentialList.remove(selfReferentialList). This internally calls
        // selfReferentialList.equals(selfReferentialList), triggering infinite recursion.
        try {
            indexedCollection.removeAll(selfReferentialList);
            fail("Expected a StackOverflowError due to recursive equals() call on the self-referential list.");
        } catch (final StackOverflowError e) {
            // This is the expected outcome.
        }
    }
}