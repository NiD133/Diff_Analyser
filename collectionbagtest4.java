package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Tests the serialization compatibility of {@link CollectionBag}
 * against a canonical version from a previous release.
 *
 * @param <T> the type of elements in the bag under test
 */
public class CollectionBagSerializationTest<T> extends AbstractCollectionTest<T> {

    public CollectionBagSerializationTest() {
        // The original class name was CollectionBagTestTest4.
        // It was renamed for clarity and to better reflect its purpose.
        super(CollectionBagSerializationTest.class.getSimpleName());
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    /**
     * Verifies that the serialized form of a full {@link CollectionBag}
     * is compatible with a canonical version stored in the source code repository.
     * This ensures that changes to the class do not break serialization compatibility.
     */
    @Test
    void serializedFullBagIsCompatibleWithCanonicalVersion() throws IOException, ClassNotFoundException {
        // Arrange: Create a full bag using the test framework's factory method.
        final Bag<T> originalBag = (Bag<T>) makeFullCollection();

        // This test runs only if the bag is serializable and serialization testing is enabled.
        if (originalBag instanceof Serializable && isTestSerialization() && !skipSerializedCanonicalTests()) {
            // Act: Read the canonical serialized bag from a file.
            final String canonicalFileName = getCanonicalFullCollectionName(originalBag);
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(canonicalFileName);

            // Assert: The deserialized bag should be equal to the original.
            // We check size separately for a more specific failure message.
            assertEquals(originalBag.size(), deserializedBag.size(),
                    "Deserialized bag should have the same size as the original.");
            assertEquals(originalBag, deserializedBag,
                    "Deserialized bag should be equal to the original.");
        }
    }
}