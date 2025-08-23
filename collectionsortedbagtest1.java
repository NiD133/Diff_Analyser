package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertAll;
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
 * Tests for {@link CollectionSortedBag}, which adapts a {@link org.apache.commons.collections4.SortedBag}
 * to conform to the standard {@link java.util.Collection} contract.
 * <p>
 * This class extends {@link AbstractCollectionTest} to leverage its comprehensive
 * test suite for collection implementations.
 *
 * @param <T> the type of elements in the tested bag.
 */
public class CollectionSortedBagTest<T> extends AbstractCollectionTest<T> {

    public CollectionSortedBagTest() {
        super(CollectionSortedBagTest.class.getSimpleName());
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns an array of 30 distinct, non-null, odd integers. These serve as the
     * primary elements for testing, ensuring they are {@link Comparable}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getFullNonNullElements() {
        final Object[] elements = new Object[30];
        for (int i = 0; i < 30; i++) {
            // Generate a sequence of odd numbers (1, 3, 5, ...)
            elements[i] = 2 * i + 1;
        }
        return (T[]) elements;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns an array of 30 distinct, non-null, even integers. These are used
     * for tests that require a separate set of {@link Comparable} elements (e.g.,
     * for `addAll`, `removeAll`).
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getOtherNonNullElements() {
        final Object[] elements = new Object[30];
        for (int i = 0; i < 30; i++) {
            // Generate a sequence of even numbers (2, 4, 6, ...)
            elements[i] = 2 * i + 2;
        }
        return (T[]) elements;
    }

    /**
     * {@inheritDoc}
     * <p>
     * SortedBags generally do not support null elements.
     *
     * @return {@code false}
     */
    @Override
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Returns an empty {@link ArrayList} for use in modification testing.
     *
     * @return a confirmed empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Returns a full {@link ArrayList} for use in modification testing.
     *
     * @return a confirmed full collection
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    /**
     * Creates a new {@link CollectionSortedBag} instance, decorating a {@link TreeBag}.
     *
     * @return a new, empty bag to be tested.
     */
    @Override
    public Bag<T> makeObject() {
        return CollectionSortedBag.collectionSortedBag(new TreeBag<>());
    }

    /**
     * Verifies that the serialized form of an empty bag is compatible with the
     * canonical version. This test is crucial for maintaining backward
     * compatibility across different versions of the library.
     */
    @Test
    void testEmptyBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> emptyBag = makeObject();

        if (isSerializationTestEnabled(emptyBag)) {
            // Read the canonical (pre-saved) serialized form from disk
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(emptyBag));

            assertAll("A deserialized empty bag should be identical to a newly created one",
                () -> assertEquals(0, deserializedBag.size(), "Deserialized bag should be empty"),
                () -> assertEquals(emptyBag, deserializedBag, "Deserialized bag should equal a new empty bag")
            );
        }
    }

    /**
     * Checks if the bag is serializable and if serialization tests are configured to run.
     *
     * @param bag the bag to check
     * @return {@code true} if the test should proceed, {@code false} otherwise
     */
    private boolean isSerializationTestEnabled(final Bag<T> bag) {
        return bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization();
    }
}