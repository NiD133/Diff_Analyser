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
 * Unit tests for {@link CollectionBag}.
 * <p>
 * This test class focuses on serialization support and compatibility checks for the CollectionBag decorator.
 * </p>
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
     * Provides an empty collection for testing modifications.
     *
     * @return a new empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Provides a full collection for testing modifications.
     *
     * @return a new full collection
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        Collection<T> fullCollection = makeConfirmedCollection();
        fullCollection.addAll(Arrays.asList(getFullElements()));
        return fullCollection;
    }

    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    /**
     * Tests adding elements with a custom comparator.
     */
    @Test
    void testAddWithCustomComparator() throws Throwable {
        Comparator<Predicate<Object>> predicateComparator = Comparator.comparing(Predicate::toString);
        TreeBag<Predicate<Object>> treeBag = new TreeBag<>(predicateComparator);
        CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);

        collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24);
    }

    /**
     * Tests adding elements with the default comparator, expecting a ClassCastException.
     */
    @Test
    void testAddWithDefaultComparator() throws Throwable {
        TreeBag<Predicate<Object>> treeBag = new TreeBag<>();
        CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);

        assertThrows(ClassCastException.class, () -> 
            collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24)
        );
    }

    /**
     * Verifies that the serialized form of an empty bag matches the canonical version.
     */
    @Test
    void testEmptyBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        Bag<T> bag = makeObject();
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));
            assertTrue(deserializedBag.isEmpty(), "Deserialized bag should be empty");
            assertEquals(bag, deserializedBag, "Deserialized bag should match the original");
        }
    }

    /**
     * Verifies that the serialized form of a full bag matches the canonical version.
     */
    @Test
    void testFullBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        Bag<T> fullBag = (Bag<T>) makeFullCollection();
        if (fullBag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(fullBag));
            assertEquals(fullBag.size(), deserializedBag.size(), "Deserialized bag should have the correct size");
            assertEquals(fullBag, deserializedBag, "Deserialized bag should match the original");
        }
    }
}