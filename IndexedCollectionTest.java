package org.apache.commons.collections4.collection;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.Test;

/**
 * Test suite for the {@link IndexedCollection} implementation.
 * Extends {@link AbstractCollectionTest} to leverage common collection test utilities.
 */
@SuppressWarnings("boxing")
class IndexedCollectionTest extends AbstractCollectionTest<String> {

    /**
     * Transformer implementation to convert String to Integer.
     */
    private static final class IntegerTransformer implements Transformer<String, Integer>, Serializable {
        private static final long serialVersionUID = 809439581555072949L;

        @Override
        public Integer transform(final String input) {
            return Integer.valueOf(input);
        }
    }

    /**
     * Decorates a collection with a non-unique {@link IndexedCollection}.
     */
    protected Collection<String> decorateCollection(final Collection<String> collection) {
        return IndexedCollection.nonUniqueIndexedCollection(collection, new IntegerTransformer());
    }

    /**
     * Decorates a collection with a unique {@link IndexedCollection}.
     */
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
        // Skipping serialized canonical tests due to current limitations.
        return true;
    }

    @Test
    void testAddedObjectsCanBeRetrievedByKey() {
        final Collection<String> collection = makeTestCollection();
        collection.add("12");
        collection.add("16");
        collection.add("1");
        collection.addAll(asList("2", "3", "4"));

        @SuppressWarnings("unchecked")
        final IndexedCollection<Integer, String> indexedCollection = (IndexedCollection<Integer, String>) collection;
        assertEquals("12", indexedCollection.get(12));
        assertEquals("16", indexedCollection.get(16));
        assertEquals("1", indexedCollection.get(1));
        assertEquals("2", indexedCollection.get(2));
        assertEquals("3", indexedCollection.get(3));
        assertEquals("4", indexedCollection.get(4));
    }

    @Test
    void testDecoratedCollectionIsIndexedOnCreation() {
        final Collection<String> originalCollection = makeFullCollection();
        final IndexedCollection<Integer, String> indexedCollection = decorateUniqueCollection(originalCollection);

        assertEquals("1", indexedCollection.get(1));
        assertEquals("2", indexedCollection.get(2));
        assertEquals("3", indexedCollection.get(3));
    }

    @Test
    void testEnsureDuplicateObjectsCauseException() {
        final Collection<String> uniqueCollection = makeUniqueTestCollection();
        uniqueCollection.add("1");

        assertThrows(IllegalArgumentException.class, () -> uniqueCollection.add("1"));
    }

    @Test
    void testReindexUpdatesIndexWhenDecoratedCollectionIsModifiedSeparately() {
        final Collection<String> originalCollection = new ArrayList<>();
        final IndexedCollection<Integer, String> indexedCollection = decorateUniqueCollection(originalCollection);

        originalCollection.add("1");
        originalCollection.add("2");
        originalCollection.add("3");

        // Before reindexing, the index should not contain the new elements.
        assertNull(indexedCollection.get(1));
        assertNull(indexedCollection.get(2));
        assertNull(indexedCollection.get(3));

        // After reindexing, the index should be updated to reflect the new elements.
        indexedCollection.reindex();

        assertEquals("1", indexedCollection.get(1));
        assertEquals("2", indexedCollection.get(2));
        assertEquals("3", indexedCollection.get(3));
    }
}