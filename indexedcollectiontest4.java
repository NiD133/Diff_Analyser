package org.apache.commons.collections4.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.Test;

/**
 * Tests for IndexedCollection.
 * This class focuses on specific behaviors of IndexedCollection not covered
 * by the abstract test suite.
 */
public class IndexedCollectionTest extends AbstractCollectionTest<String> {

    //<editor-fold desc="Boilerplate from AbstractCollectionTest">
    protected Collection<String> decorateCollection(final Collection<String> collection) {
        return IndexedCollection.nonUniqueIndexedCollection(collection, new IntegerTransformer());
    }

    protected IndexedCollection<Integer, String> decorateUniqueCollection(final Collection<String> collection) {
        return IndexedCollection.uniqueIndexedCollection(collection, new IntegerTransformer());
    }

    @Override
    public String[] getFullElements() {
        return new String[] { "1", "3", "5", "7", "2", "4", "6" };
    }

    @Override
    public String[] getOtherElements() {
        return new String[] { "9", "88", "678", "87", "98", "78", "99" };
    }

    @Override
    public Collection<String> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    @Override
    public Collection<String> makeConfirmedFullCollection() {
        return new ArrayList<>(Arrays.asList(getFullElements()));
    }

    @Override
    public Collection<String> makeFullCollection() {
        return decorateCollection(new ArrayList<>(Arrays.asList(getFullElements())));
    }

    @Override
    public Collection<String> makeObject() {
        return decorateCollection(new ArrayList<>());
    }

    public Collection<String> makeTestCollection() {
        return decorateCollection(new ArrayList<>());
    }

    public Collection<String> makeUniqueTestCollection() {
        return decorateUniqueCollection(new ArrayList<>());
    }

    @Override
    protected boolean skipSerializedCanonicalTests() {
        // FIXME: support canonical tests
        return true;
    }

    private static final class IntegerTransformer implements Transformer<String, Integer>, Serializable {
        private static final long serialVersionUID = 809439581555072949L;

        @Override
        public Integer transform(final String input) {
            return Integer.valueOf(input);
        }
    }
    //</editor-fold>

    /**
     * Tests that reindex() correctly updates the index after the decorated
     * collection has been modified externally, which is a documented behavior.
     */
    @Test
    void reindexShouldUpdateIndexAfterUnderlyingCollectionIsModifiedExternally() {
        // Arrange: Create an indexed collection decorating an empty list.
        final Collection<String> underlyingCollection = new ArrayList<>();
        final IndexedCollection<Integer, String> indexedCollection = decorateUniqueCollection(underlyingCollection);

        // Act: Modify the underlying collection directly, bypassing the decorator.
        underlyingCollection.add("1");
        underlyingCollection.add("2");
        underlyingCollection.add("3");

        // Assert: The index is now out of sync and does not contain the new elements.
        // This confirms that external modifications are not automatically detected.
        assertNull(indexedCollection.get(1), "Index should be stale before reindex for key 1");
        assertNull(indexedCollection.get(2), "Index should be stale before reindex for key 2");
        assertNull(indexedCollection.get(3), "Index should be stale before reindex for key 3");

        // Act: Manually trigger a re-index.
        indexedCollection.reindex();

        // Assert: The index is now synchronized with the underlying collection.
        assertEquals("1", indexedCollection.get(1), "Index should be updated after reindex for key 1");
        assertEquals("2", indexedCollection.get(2), "Index should be updated after reindex for key 2");
        assertEquals("3", indexedCollection.get(3), "Index should be updated after reindex for key 3");
    }
}