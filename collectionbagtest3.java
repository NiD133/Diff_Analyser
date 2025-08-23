package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CollectionBag}.
 * <p>
 * This class extends {@link AbstractCollectionTest} to leverage its comprehensive
 * contract testing capabilities for the {@link java.util.Collection} interface.
 * </p>
 * @param <T> the type of elements in the tested bag
 */
public class CollectionBagTest<T> extends AbstractCollectionTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    /**
     * Returns an empty List for use in modification testing.
     *
     * @return a confirmed empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Returns a full Set for use in modification testing.
     *
     * @return a confirmed full collection
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> set = makeConfirmedCollection();
        set.addAll(Arrays.asList(getFullElements()));
        return set;
    }

    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    /**
     * Verifies that the serialized form of an empty bag is compatible with the
     * canonical version stored on disk, ensuring backward compatibility.
     */
    @Test
    void testEmptyBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        // Arrange
        final Bag<T> emptyBag = makeObject();

        // This test is only relevant for serializable bags and when serialization
        // tests are enabled in the test framework.
        if (!(emptyBag instanceof Serializable && isTestSerialization() && !skipSerializedCanonicalTests())) {
            return; // Skip test if not applicable
        }

        // Act: Read the canonical serialized empty bag from a file.
        final String canonicalFileName = getCanonicalEmptyCollectionName(emptyBag);
        final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(canonicalFileName);

        // Assert
        assertTrue(deserializedBag.isEmpty(), "Deserialized bag should be empty");
        assertEquals(emptyBag, deserializedBag, "Deserialized bag should be equal to a new empty bag");
    }
}