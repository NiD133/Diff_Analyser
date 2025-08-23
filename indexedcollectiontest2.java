package org.apache.commons.collections4.collection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for IndexedCollection.
 * <p>
 * This class extends AbstractCollectionTest, which provides a comprehensive suite
 * of tests for the Collection interface. The methods overridden here are hooks
 * for that framework.
 * </p>
 */
@DisplayName("IndexedCollection")
public class IndexedCollectionTest extends AbstractCollectionTest<String> {

    // --- Test Fixture Setup for AbstractCollectionTest ---

    @Override
    public String[] getFullElements() {
        return new String[] { "1", "3", "5", "7", "2", "4", "6" };
    }

    @Override
    public String[] getOtherElements() {
        return new String[] { "9", "88", "678", "87", "98", "78", "99" };
    }

    /**
     * The transformer used to extract keys (Integer) from values (String).
     */
    private static final class IntegerTransformer implements Transformer<String, Integer>, Serializable {
        private static final long serialVersionUID = 809439581555072949L;

        @Override
        public Integer transform(final String input) {
            return Integer.valueOf(input);
        }
    }

    @Override
    public Collection<String> makeObject() {
        return IndexedCollection.nonUniqueIndexedCollection(new ArrayList<>(), new IntegerTransformer());
    }

    @Override
    public Collection<String> makeFullCollection() {
        final List<String> list = new ArrayList<>(Arrays.asList(getFullElements()));
        return IndexedCollection.nonUniqueIndexedCollection(list, new IntegerTransformer());
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
    protected boolean skipSerializedCanonicalTests() {
        // FIXME: support canonical tests
        return true;
    }

    // --- Custom Tests for IndexedCollection ---

    @Test
    @DisplayName("should be indexed immediately upon creation")
    void uniqueIndexedCollectionIsIndexedOnCreation() {
        // Arrange
        final Collection<String> sourceCollection = makeConfirmedFullCollection();
        final Transformer<String, Integer> keyTransformer = new IntegerTransformer();

        // Act: Create a unique indexed collection from the source collection.
        final IndexedCollection<Integer, String> indexedCollection =
                IndexedCollection.uniqueIndexedCollection(sourceCollection, keyTransformer);

        // Assert: Verify that elements are indexed and can be retrieved by their key.
        // The key is the integer representation of the string element.
        assertAll("Elements should be retrievable by key after creation",
            () -> assertEquals("1", indexedCollection.get(1), "Should find element with key 1"),
            () -> assertEquals("2", indexedCollection.get(2), "Should find element with key 2"),
            () -> assertEquals("6", indexedCollection.get(6), "Should find element with key 6"),
            () -> assertNull(indexedCollection.get(99), "Should return null for a non-existent key")
        );
    }
}