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
 * Tests for CollectionBag.
 *
 * Focus areas:
 * - Serialization compatibility with canonical forms stored in SCM.
 * - Behavior when decorating a TreeBag that either has or lacks a Comparator.
 *   TreeBag without a Comparator requires elements to be Comparable; a custom Comparator
 *   allows storing non-Comparable elements.
 *
 * Note: The CollectionBag decorator itself is heavily exercised in AbstractBagTest;
 * this class primarily covers serialization and specific interaction with TreeBag.
 */
public class CollectionBagTest<T> extends AbstractCollectionTest<T> {

    private static final int COPIES = 24;

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
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Returns a full collection for use in modification testing.
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> confirmed = makeConfirmedCollection();
        confirmed.addAll(Arrays.asList(getFullElements()));
        return confirmed;
    }

    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    /**
     * Provides a non-Comparable Predicate instance.
     * Using a concrete functor instead of a lambda keeps toString() stable across JVMs,
     * which is useful when ordering via Comparator.comparing(Predicate::toString).
     */
    private static Predicate<Object> nonComparablePredicate() {
        // NonePredicate.nonePredicate(Collection) creates a composite predicate.
        // An empty input collection yields a valid, non-Comparable predicate instance.
        return NonePredicate.nonePredicate(new ArrayList<Predicate<Object>>());
    }

    @Test
    void addNonComparableToTreeBagWithCustomComparator_allowsAddAndTracksCardinality() {
        // Arrange: TreeBag with a Comparator allows storing non-Comparable elements
        final TreeBag<Predicate<Object>> backingTreeBag =
                new TreeBag<>(Comparator.comparing(Predicate::toString));
        final Bag<Predicate<Object>> bag = new CollectionBag<>(backingTreeBag);
        final Predicate<Object> element = nonComparablePredicate();

        // Act
        final boolean changed = bag.add(element, COPIES);

        // Assert
        assertTrue(changed, "Adding should report a change");
        assertEquals(COPIES, bag.getCount(element), "Cardinality should reflect number of copies");
        assertEquals(COPIES, bag.size(), "Bag size should include duplicates");
    }

    @Test
    void addNonComparableToTreeBagWithNaturalOrdering_throwsClassCastException() {
        // Arrange: TreeBag without a Comparator relies on natural ordering (Comparable)
        final TreeBag<Predicate<Object>> backingTreeBag = new TreeBag<>();
        final Bag<Predicate<Object>> bag = new CollectionBag<>(backingTreeBag);
        final Predicate<Object> element = nonComparablePredicate();

        // Act + Assert
        assertThrows(ClassCastException.class, () -> bag.add(element, COPIES),
                "TreeBag without Comparator should reject non-Comparable elements");
    }

//    @Test
//    void testCreateCanonicalForms() throws Exception {
//        // Helper to regenerate canonical serialized forms when changing serialization.
//        resetEmpty();
//        writeExternalFormToDisk((Serializable) getCollection(),
//                "src/test/resources/data/test/CollectionBag.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk((Serializable) getCollection(),
//                "src/test/resources/data/test/CollectionBag.fullCollection.version4.obj");
//    }

    /**
     * Verifies that the current serialized form of an empty bag matches the canonical version in SCM.
     */
    @Test
    void testEmptyBagCompatibility() throws IOException, ClassNotFoundException {
        // Arrange
        final Bag<T> bag = makeObject();

        // Act + Assert (only when canonical tests are enabled)
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> canonical = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));
            assertTrue(canonical.isEmpty(), "Canonical bag should be empty");
            assertEquals(bag, canonical, "Serialized form mismatch for empty bag");
        }
    }

    /**
     * Verifies that the current serialized form of a full bag matches the canonical version in SCM.
     */
    @Test
    void testFullBagCompatibility() throws IOException, ClassNotFoundException {
        // Arrange
        @SuppressWarnings("unchecked")
        final Bag<T> bag = (Bag<T>) makeFullCollection();

        // Act + Assert (only when canonical tests are enabled)
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> canonical = (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));
            assertEquals(bag.size(), canonical.size(), "Size mismatch for full bag");
            assertEquals(bag, canonical, "Serialized form mismatch for full bag");
        }
    }
}