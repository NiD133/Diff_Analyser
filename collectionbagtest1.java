package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * Extension of {@link AbstractCollectionTest} for the {@link CollectionBag}.
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
     * Tests that adding elements works correctly when the decorated bag requires a
     * custom comparator.
     * <p>
     * This scenario uses a TreeBag, which needs a comparator for elements that are
     * not naturally ordered (like Predicate). The test verifies that CollectionBag
     * correctly delegates the add operation and updates its state.
     */
    @Test
    void shouldAddItemsWhenDecoratedBagUsesComparator() {
        // Arrange
        // A TreeBag requires a comparator for non-Comparable elements.
        final Bag<Predicate<Object>> sortedBag = new TreeBag<>(Comparator.comparing(Object::toString));
        final Bag<Predicate<Object>> collectionBag = new CollectionBag<>(sortedBag);

        final Predicate<Object> elementToAdd = NonePredicate.nonePredicate();
        final int numberOfItemsToAdd = 24;

        // Act
        final boolean wasAdded = collectionBag.add(elementToAdd, numberOfItemsToAdd);

        // Assert
        // The add operation on CollectionBag should always return true.
        assertTrue(wasAdded, "add(e, n) should return true as the collection is modified.");

        // Verify the count of the added element and the total size of the bag.
        assertEquals(numberOfItemsToAdd, collectionBag.getCount(elementToAdd),
                "The count of the added element should match the number of additions.");
        assertEquals(numberOfItemsToAdd, collectionBag.size(),
                "The total size of the bag should be the number of added elements.");
    }
}