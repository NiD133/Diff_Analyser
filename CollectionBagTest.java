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
 * Test class for {@link CollectionBag}.
 * <p>
 * This test class focuses on serialization support for the CollectionBag decorator.
 * The CollectionBag is extensively used and tested in AbstractBagTest.
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
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    /**
     * Test adding elements to a CollectionBag with a custom comparator.
     */
    @Test
    void testAddWithCustomComparator() throws Throwable {
        // Create a TreeBag with a custom comparator based on Predicate's toString method
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>(Comparator.comparing(Predicate::toString));
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);

        // Add a NonePredicate to the CollectionBag
        collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24);
    }

    /**
     * Test adding elements to a CollectionBag with the default comparator.
     * Expect a ClassCastException due to lack of natural ordering.
     */
    @Test
    void testAddWithDefaultComparator() throws Throwable {
        // Create a TreeBag with the default comparator
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>();
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);

        // Attempt to add a NonePredicate and expect a ClassCastException
        assertThrows(ClassCastException.class, () -> collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24));
    }

    /**
     * Compares the current serialized form of the Bag against the canonical version in SCM.
     */
    @Test
    void testEmptyBagCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> bag = makeObject();

        // Check if the bag is serializable and serialization tests should be run
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));

            // Assert that the deserialized bag is empty and equal to the original bag
            assertTrue(deserializedBag.isEmpty(), "Deserialized bag should be empty");
            assertEquals(bag, deserializedBag, "Deserialized bag should be equal to the original bag");
        }
    }

    /**
     * Compares the current serialized form of the Bag against the canonical version in SCM.
     */
    @Test
    void testFullBagCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> bag = (Bag<T>) makeFullCollection();

        // Check if the bag is serializable and serialization tests should be run
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));

            // Assert that the deserialized bag has the same size and is equal to the original bag
            assertEquals(bag.size(), deserializedBag.size(), "Deserialized bag should have the same size as the original bag");
            assertEquals(bag, deserializedBag, "Deserialized bag should be equal to the original bag");
        }
    }
}