package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Abstract test suite for {@link CollectionSortedBag}.
 *
 * @param <T> the type of elements tested
 */
// Renamed class from "CollectionSortedBagTestTest2" to the more standard "CollectionSortedBagTest"
public class CollectionSortedBagTest<T> extends AbstractCollectionTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Returns an array of distinct, comparable, non-null objects for populating a collection.
     * This implementation returns a sequence of odd integers.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getFullNonNullElements() {
        final Object[] elements = new Object[30];
        for (int i = 0; i < 30; i++) {
            // Generates odd numbers: 1, 3, 5, ...
            elements[i] = Integer.valueOf(i * 2 + 1);
        }
        return (T[]) elements;
    }

    /**
     * Returns an array of distinct, comparable, non-null objects that are not
     * present in the full collection.
     * This implementation returns a sequence of even integers.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getOtherNonNullElements() {
        final Object[] elements = new Object[30];
        for (int i = 0; i < 30; i++) {
            // Generates even numbers: 2, 4, 6, ...
            elements[i] = Integer.valueOf(i * 2 + 2);
        }
        return (T[]) elements;
    }

    /**
     * Overridden to reflect that {@link SortedBag} implementations typically do not support null elements.
     * @return false
     */
    @Override
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Returns a new, empty {@link ArrayList} for use in modification testing.
     * @return a confirmed empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Returns a new, full {@link ArrayList} for use in modification testing.
     * @return a confirmed full collection
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    /**
     * Returns a new, empty {@link CollectionSortedBag} wrapping a {@link TreeBag}.
     * This is the primary object under test.
     * @return a new, empty bag
     */
    @Override
    public Bag<T> makeObject() {
        return CollectionSortedBag.collectionSortedBag(new TreeBag<>());
    }

    /**
     * Verifies that the serialized form of a full bag is compatible with a canonical version.
     */
    @Test
    @DisplayName("Serialization should be compatible with the canonical form")
    void testSerializationCompatibilityWithCanonicalVersion() throws IOException, ClassNotFoundException {
        // This test ensures that changes to the class do not break serialization
        // compatibility with previously serialized versions.
        final SortedBag<T> originalBag = (SortedBag<T>) makeFullCollection();

        // Preconditions for running this serialization test
        assumeTrue(originalBag instanceof Serializable, "The bag must be Serializable");
        assumeTrue(!skipSerializedCanonicalTests(), "Canonical serialization test is skipped");
        assumeTrue(isTestSerialization(), "Serialization testing is disabled");

        // Read the canonical (expected) version from a file
        final String canonicalFileName = getCanonicalFullCollectionName(originalBag);
        final SortedBag<?> deserializedBag = (SortedBag<?>) readExternalFormFromDisk(canonicalFileName);

        // Assert that the deserialized bag is identical to the original
        assertAll("Deserialized bag should match the original",
            () -> assertEquals(originalBag.size(), deserializedBag.size(),
                    "Deserialized bag should have the same size as the original"),
            () -> assertEquals(originalBag, deserializedBag,
                    "Deserialized bag should be equal to the original")
        );
    }
}