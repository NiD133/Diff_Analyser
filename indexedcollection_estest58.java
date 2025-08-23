package org.apache.commons.collections4.collection;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link IndexedCollection} class, focusing on its behavior
 * when the underlying collection is modified directly.
 */
public class IndexedCollectionTest {

    /**
     * Tests that containsAll() returns false for an element that was added
     * directly to the underlying collection, bypassing the IndexedCollection decorator.
     *
     * This scenario causes the internal index to become out of sync with the
     * decorated collection's actual content. The test confirms that the lookup,
     * which relies on the stale index, fails as expected.
     */
    @Test
    public void containsAllShouldReturnFalseWhenUnderlyingCollectionIsModifiedDirectly() {
        // Arrange
        // Use a transformer that maps any object to a null key.
        final Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();
        final Collection<Object> underlyingList = new LinkedList<>();
        final IndexedCollection<Object, Object> indexedCollection =
                IndexedCollection.nonUniqueIndexedCollection(underlyingList, keyTransformer);

        // Act
        // Modify the underlying list directly. This is the crucial step that makes
        // the IndexedCollection's internal index stale. The list now contains an
        // element that the index is unaware of.
        underlyingList.add(indexedCollection);

        // Call containsAll() with the modified list. The method will use its
        // (now stale) index to perform the check.
        final boolean result = indexedCollection.containsAll(underlyingList);

        // Assert
        // The result must be false because the element added directly to the
        // underlying list was never added to the index.
        assertFalse("containsAll should return false for elements not known to the index", result);
    }
}