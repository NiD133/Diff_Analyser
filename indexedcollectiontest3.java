package org.apache.commons.collections4.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for IndexedCollection.
 * <p>
 * This class extends AbstractCollectionTest, which provides a comprehensive suite
 * of tests for the Collection interface. The methods overridden here are used to
 * configure the test harness for IndexedCollection.
 * </p>
 */
@DisplayName("IndexedCollection Test")
public class IndexedCollectionTest extends AbstractCollectionTest<String> {

    /**
     * The transformer used to generate keys from elements (String -> Integer).
     */
    private static final Transformer<String, Integer> INTEGER_TRANSFORMER = new IntegerTransformer();

    //--- Test Harness Methods (required by AbstractCollectionTest) ---

    @Override
    public Collection<String> makeObject() {
        return decorateCollection(new ArrayList<>());
    }

    @Override
    public Collection<String> makeFullCollection() {
        return decorateCollection(new ArrayList<>(Arrays.asList(getFullElements())));
    }

    @Override
    public String[] getFullElements() {
        return new String[]{"1", "3", "5", "7", "2", "4", "6"};
    }

    @Override
    public String[] getOtherElements() {
        return new String[]{"9", "88", "678", "87", "98", "78", "99"};
    }

    @Override
    protected boolean skipSerializedCanonicalTests() {
        // FIXME: support canonical tests
        return true;
    }

    //--- Helper methods for creating specific collection types ---

    /**
     * Decorates a collection with a non-unique IndexedCollection.
     */
    protected Collection<String> decorateCollection(final Collection<String> collection) {
        return IndexedCollection.nonUniqueIndexedCollection(collection, INTEGER_TRANSFORMER);
    }

    /**
     * Creates a new IndexedCollection that enforces unique keys for testing.
     */
    protected IndexedCollection<Integer, String> makeUniqueKeyCollection() {
        return IndexedCollection.uniqueIndexedCollection(new ArrayList<>(), INTEGER_TRANSFORMER);
    }

    /**
     * A transformer that converts a String to its Integer value.
     */
    private static final class IntegerTransformer implements Transformer<String, Integer>, Serializable {
        private static final long serialVersionUID = 809439581555072949L;

        @Override
        public Integer transform(final String input) {
            return Integer.valueOf(input);
        }
    }

    //--- Custom Tests for IndexedCollection ---

    /**
     * Tests that adding an element which maps to an already existing key in a
     * unique IndexedCollection throws an IllegalArgumentException.
     */
    @Test
    @DisplayName("add() should throw exception for duplicate key in unique collection")
    void addShouldThrowExceptionWhenAddingElementWithExistingKeyToUniqueCollection() {
        // Arrange: Create a collection that enforces unique keys and add an initial element.
        // The key for "1" is the Integer 1.
        final Collection<String> uniqueCollection = makeUniqueKeyCollection();
        uniqueCollection.add("1");

        // Act & Assert: Verify that adding another element mapping to the same key throws an exception.
        assertThrows(IllegalArgumentException.class,
            () -> uniqueCollection.add("1"),
            "Adding an element with a duplicate key to a unique IndexedCollection should throw an exception.");
    }
}