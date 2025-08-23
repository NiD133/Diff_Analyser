package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the IndexedCollection class.
 * This class retains the original test's scaffolding for compatibility.
 */
public class IndexedCollection_ESTestTest52 extends IndexedCollection_ESTest_scaffolding {

    /**
     * Tests that `removeIf()` does not modify the collection and returns false
     * when the predicate does not match any elements.
     */
    @Test
    public void testRemoveIfShouldNotModifyCollectionWhenPredicateIsFalse() {
        // Arrange
        Collection<String> underlyingCollection = new LinkedList<>();
        Transformer<String, Object> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Object, String> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(underlyingCollection, keyTransformer);
        indexedCollection.add("Apple");

        // A predicate that will not match the element "Apple"
        Predicate<String> noMatchPredicate = EqualPredicate.equalPredicate("Banana");

        // Act
        boolean wasModified = indexedCollection.removeIf(noMatchPredicate);

        // Assert
        assertFalse("removeIf should return false as no elements were removed", wasModified);
        assertEquals("Collection size should not change", 1, indexedCollection.size());
        assertTrue("Collection should still contain the original element", indexedCollection.contains("Apple"));
    }
}