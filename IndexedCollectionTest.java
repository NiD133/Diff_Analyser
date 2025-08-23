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
 * Tests for {@link IndexedCollection}.
 * <p>
 * This test suite focuses on readability:
 * - Clear, intention-revealing helper methods.
 * - No unchecked casts in tests.
 * - Minimal use of magic values and inline comments explaining intent.
 */
@SuppressWarnings("boxing")
class IndexedCollectionTest extends AbstractCollectionTest<String> {

    /**
     * Transforms String values to Integer keys for indexing.
     */
    private static final class StringToIntegerTransformer implements Transformer<String, Integer>, Serializable {
        private static final long serialVersionUID = 809439581555072949L;

        @Override
        public Integer transform(final String input) {
            return Integer.valueOf(input);
        }
    }

    private static final Transformer<String, Integer> STRING_TO_INTEGER = new StringToIntegerTransformer();

    /**
     * Factory: non-unique index over the given backing collection.
     */
    protected Collection<String> decorateCollection(final Collection<String> backingCollection) {
        return IndexedCollection.nonUniqueIndexedCollection(backingCollection, STRING_TO_INTEGER);
    }

    /**
     * Factory: unique index over the given backing collection.
     */
    protected IndexedCollection<Integer, String> decorateUniqueCollection(final Collection<String> backingCollection) {
        return IndexedCollection.uniqueIndexedCollection(backingCollection, STRING_TO_INTEGER);
    }

    /**
     * Convenience: create a fresh non-unique IndexedCollection with an empty ArrayList as backing store.
     */
    private IndexedCollection<Integer, String> newNonUniqueIndexed() {
        return IndexedCollection.nonUniqueIndexedCollection(new ArrayList<>(), STRING_TO_INTEGER);
    }

    /**
     * Convenience: create a fresh unique IndexedCollection with an empty ArrayList as backing store.
     */
    private IndexedCollection<Integer, String> newUniqueIndexed() {
        return IndexedCollection.uniqueIndexedCollection(new ArrayList<>(), STRING_TO_INTEGER);
    }

    @Override
    public String[] getFullElements() {
        // A shuffled sequence to ensure indexing is not order-dependent.
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

    // Maintained for compatibility with existing tests that expect these helpers.
    public Collection<String> makeTestCollection() {
        return decorateCollection(new ArrayList<>());
    }

    public Collection<String> makeUniqueTestCollection() {
        return decorateUniqueCollection(new ArrayList<>());
    }

    @Override
    protected boolean skipSerializedCanonicalTests() {
        // Canonical serialization tests are not supported here.
        return true;
    }

    @Test
    void addedObjectsCanBeRetrievedByKey() {
        // Given a non-unique indexed collection
        final IndexedCollection<Integer, String> indexed = newNonUniqueIndexed();

        // When adding elements
        indexed.add("12");
        indexed.add("16");
        indexed.add("1");
        indexed.addAll(asList("2", "3", "4"));

        // Then they are retrievable via their transformed keys
        assertEquals("12", indexed.get(12));
        assertEquals("16", indexed.get(16));
        assertEquals("1", indexed.get(1));
        assertEquals("2", indexed.get(2));
        assertEquals("3", indexed.get(3));
        assertEquals("4", indexed.get(4));
    }

    @Test
    void decoratedCollectionIsIndexedOnCreation() {
        // Given a plain backing collection with existing elements
        final Collection<String> backing = new ArrayList<>(Arrays.asList(getFullElements()));

        // When decorating with a unique index
        final IndexedCollection<Integer, String> indexed = decorateUniqueCollection(backing);

        // Then the index is initialized from the existing contents
        assertEquals("1", indexed.get(1));
        assertEquals("2", indexed.get(2));
        assertEquals("3", indexed.get(3));
    }

    @Test
    void addingDuplicateKeyToUniqueIndexThrows() {
        // Given a unique indexed collection
        final IndexedCollection<Integer, String> unique = newUniqueIndexed();

        // When adding an element that maps to an existing key
        unique.add("1");

        // Then adding another element with the same key fails
        assertThrows(IllegalArgumentException.class, () -> unique.add("1"));
    }

    @Test
    void reindexRefreshesIndexAfterExternalModification() {
        // Given a unique indexed collection
        final ArrayList<String> backing = new ArrayList<>();
        final IndexedCollection<Integer, String> indexed = decorateUniqueCollection(backing);

        // And the backing collection is modified directly (outside of the decorator)
        backing.add("1");
        backing.add("2");
        backing.add("3");

        // Then the index is stale until reindex() is called
        assertNull(indexed.get(1));
        assertNull(indexed.get(2));
        assertNull(indexed.get(3));

        // When reindexing
        indexed.reindex();

        // Then the index reflects the backing collection
        assertEquals("1", indexed.get(1));
        assertEquals("2", indexed.get(2));
        assertEquals("3", indexed.get(3));
    }
}