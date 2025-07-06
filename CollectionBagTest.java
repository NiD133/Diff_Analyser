package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.functors.NonePredicate;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link CollectionBag} class, focusing on serialization support and Collection contract compliance.
 * <p>
 * Note: This test class is mainly for serialization support, as the CollectionBag decorator is extensively used and tested in AbstractBagTest.
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
     * Creates an empty List for modification testing.
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Creates a full Set for modification testing.
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> set = makeConfirmedCollection();
        set.addAll(Arrays.asList(getFullElements()));
        return set;
    }

    /**
     * Creates a CollectionBag instance for testing.
     */
    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    /**
     * Tests the add operation with a custom comparator.
     */
    @Test
    void testAddWithCustomComparator() throws Throwable {
        // Create a tree bag with a custom comparator
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>(Comparator.comparing(Predicate::toString));
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);

        // Add an element to the collection bag
        collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24);
    }

    /**
     * Tests the add operation with the default comparator.
     */
    @Test
    void testAddWithDefaultComparator() throws Throwable {
        // Create a tree bag with the default comparator
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>();
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);

        // Verify that adding an element throws a ClassCastException
        assertThrows(ClassCastException.class, () -> collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24));
    }

    /**
     * Tests the serialization of an empty CollectionBag.
     */
    @Test
    void testEmptyBagSerialization() throws IOException, ClassNotFoundException {
        // Create an empty collection bag
        final Bag<T> bag = makeObject();

        // Check if the bag is serializable and if serialization tests are enabled
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            // Read the serialized form of the bag from disk
            final Bag<?> bag2 = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));

            // Verify that the deserialized bag is empty and equal to the original bag
            assertTrue(bag2.isEmpty());
            assertEquals(bag, bag2);
        }
    }

    /**
     * Tests the serialization of a full CollectionBag.
     */
    @Test
    void testFullBagSerialization() throws IOException, ClassNotFoundException {
        // Create a full collection bag
        final Bag<T> bag = (Bag<T>) makeFullCollection();

        // Check if the bag is serializable and if serialization tests are enabled
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            // Read the serialized form of the bag from disk
            final Bag<?> bag2 = (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));

            // Verify that the deserialized bag has the same size and is equal to the original bag
            assertEquals(bag.size(), bag2.size());
            assertEquals(bag, bag2);
        }
    }
}