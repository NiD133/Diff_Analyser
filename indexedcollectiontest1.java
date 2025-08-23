package org.apache.commons.collections4.collection;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Renamed class for clarity and to follow standard naming conventions.
public class IndexedCollectionTest extends AbstractCollectionTest<String> {

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

    @Test
    @DisplayName("get() should return an object that was previously added, using its transformed key")
    void getShouldReturnCorrectObjectForGivenKey() {
        // Arrange: Create a non-unique indexed collection where the key is the integer value of the string.
        // Instantiating the specific class under test makes the setup clearer and avoids casting.
        final IndexedCollection<Integer, String> indexedCollection =
            IndexedCollection.nonUniqueIndexedCollection(new ArrayList<>(), new IntegerTransformer());

        // Act: Add several elements to the collection.
        indexedCollection.add("12");
        indexedCollection.add("16");
        indexedCollection.add("1");
        indexedCollection.addAll(asList("2", "3", "4"));

        // Assert: Verify that each added object can be retrieved by its corresponding key.
        // Using assertAll groups related checks and reports all failures at once.
        assertAll("Retrieve added objects by key",
            () -> assertEquals("12", indexedCollection.get(12)),
            () -> assertEquals("16", indexedCollection.get(16)),
            () -> assertEquals("1", indexedCollection.get(1)),
            () -> assertEquals("2", indexedCollection.get(2)),
            () -> assertEquals("3", indexedCollection.get(3)),
            () -> assertEquals("4", indexedCollection.get(4))
        );
    }
}