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
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link CollectionSortedBag}.
 * <p>
 * This test class focuses on verifying the serialization compatibility of the
 * CollectionSortedBag decorator. The main functionality of the decorator is
 * tested in AbstractSortedBagTest.
 */
public class CollectionSortedBagTest<T> extends AbstractCollectionTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Provides an array of comparable non-null elements for testing.
     * 
     * @return an array of comparable non-null elements
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getFullNonNullElements() {
        final Object[] elements = new Object[30];
        for (int i = 0; i < 30; i++) {
            elements[i] = Integer.valueOf(i * 2 + 1);
        }
        return (T[]) elements;
    }

    /**
     * Provides an array of other comparable non-null elements for testing.
     * 
     * @return an array of other comparable non-null elements
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getOtherNonNullElements() {
        final Object[] elements = new Object[30];
        for (int i = 0; i < 30; i++) {
            elements[i] = Integer.valueOf(i * 2 + 2);
        }
        return (T[]) elements;
    }

    /**
     * Indicates that null elements are not supported by SortedBags.
     * 
     * @return false, as null elements are not supported
     */
    @Override
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Creates an empty collection for use in modification testing.
     * 
     * @return a confirmed empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Creates a full collection for use in modification testing.
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
     * Creates a new CollectionSortedBag instance for testing.
     * 
     * @return a new CollectionSortedBag instance
     */
    @Override
    public Bag<T> makeObject() {
        return CollectionSortedBag.collectionSortedBag(new TreeBag<>());
    }

    /**
     * Tests the serialization compatibility of an empty CollectionSortedBag.
     * 
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    @Test
    void testEmptyBagCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> bag = makeObject();
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));
            assertEquals(0, deserializedBag.size(), "Deserialized bag should be empty");
            assertEquals(bag, deserializedBag, "Deserialized bag should match the original");
        }
    }

    /**
     * Tests the serialization compatibility of a full CollectionSortedBag.
     * 
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    @Test
    void testFullBagCompatibility() throws IOException, ClassNotFoundException {
        final SortedBag<T> bag = (SortedBag<T>) makeFullCollection();
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final SortedBag<?> deserializedBag = (SortedBag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));
            assertEquals(bag.size(), deserializedBag.size(), "Deserialized bag should have the same size");
            assertEquals(bag, deserializedBag, "Deserialized bag should match the original");
        }
    }
}