package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
 * Tests for CollectionSortedBag.
 *
 * Focus: Backward-compatible serialization of empty and full instances.
 * Notes:
 * - This test leverages AbstractCollectionTest's serialization utilities and
 *   canonical serialized resources stored under src/test/resources.
 * - The bag under test must contain comparable elements because it is a SortedBag.
 */
@DisplayName("CollectionSortedBag serialization compatibility")
public class CollectionSortedBagTest extends AbstractCollectionTest<Integer> {

    private static final int ELEMENT_COUNT = 30;

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Provide comparable elements (odd numbers 1..59) to populate a full bag.
     * Using a predictable, strictly increasing sequence makes ordering expectations clear.
     */
    @Override
    public Integer[] getFullNonNullElements() {
        final Integer[] elements = new Integer[ELEMENT_COUNT];
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            elements[i] = 2 * i + 1; // 1, 3, 5, ..., 59
        }
        return elements;
    }

    /**
     * Provide a distinct set of comparable elements (even numbers 2..60).
     */
    @Override
    public Integer[] getOtherNonNullElements() {
        final Integer[] elements = new Integer[ELEMENT_COUNT];
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            elements[i] = 2 * i + 2; // 2, 4, 6, ..., 60
        }
        return elements;
    }

    /**
     * SortedBags generally do not accept nulls.
     */
    @Override
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Returns an empty confirmed collection implementation for cross-checking.
     */
    @Override
    public Collection<Integer> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Returns a full confirmed collection implementation for cross-checking.
     */
    @Override
    public Collection<Integer> makeConfirmedFullCollection() {
        final Collection<Integer> confirmed = makeConfirmedCollection();
        confirmed.addAll(Arrays.asList(getFullElements()));
        return confirmed;
    }

    /**
     * Returns the bag under test, wrapped so it conforms to the Collection contract.
     */
    @Override
    public Bag<Integer> makeObject() {
        return CollectionSortedBag.collectionSortedBag(new TreeBag<>());
    }

    // Helper to centralize the conditional that determines whether to run
    // serialization compatibility assertions in the current test environment.
    private boolean shouldVerifySerialization(final Object candidate) {
        return candidate instanceof Serializable
            && !skipSerializedCanonicalTests()
            && isTestSerialization();
    }

//    /**
//     * Utility to regenerate the canonical serialized forms on disk.
//     * Disabled by default; run manually when intentional serialization changes occur.
//     */
//    void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(),
//            "src/test/resources/data/test/CollectionSortedBag.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(),
//            "src/test/resources/data/test/CollectionSortedBag.fullCollection.version4.obj");
//    }

    /**
     * Verify that the serialized form of an empty bag matches the canonical serialization.
     */
    @Test
    @DisplayName("Empty bag serialization is backward compatible")
    void emptyBagSerializationIsBackwardCompatible() throws IOException, ClassNotFoundException {
        // Arrange
        final Bag<Integer> emptyBag = makeObject();

        // Act/Assert (only if serialization checks are enabled in this environment)
        if (shouldVerifySerialization(emptyBag)) {
            final Bag<?> canonical =
                (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(emptyBag));

            assertEquals(0, canonical.size(), "Canonical empty bag should have size 0");
            assertEquals(emptyBag, canonical, "Empty bag should match canonical serialized form");
        }
    }

    /**
     * Verify that the serialized form of a full bag matches the canonical serialization.
     */
    @Test
    @DisplayName("Full bag serialization is backward compatible")
    void fullBagSerializationIsBackwardCompatible() throws IOException, ClassNotFoundException {
        // Arrange
        @SuppressWarnings("unchecked")
        final SortedBag<Integer> fullBag = (SortedBag<Integer>) makeFullCollection();

        // Act/Assert (only if serialization checks are enabled in this environment)
        if (shouldVerifySerialization(fullBag)) {
            final SortedBag<?> canonical =
                (SortedBag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(fullBag));

            assertEquals(fullBag.size(), canonical.size(), "Sizes should match canonical form");
            assertEquals(fullBag, canonical, "Contents should match canonical serialized form");
        }
    }
}