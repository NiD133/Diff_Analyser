package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSortedBagTest} for exercising the {@link TreeBag}.
 *
 * @param <T> the type of elements in the bag being tested.
 */
public class TreeBagTest<T> extends AbstractSortedBagTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public SortedBag<T> makeObject() {
        return new TreeBag<>();
    }

    /**
     * Creates a new bag with known string elements for testing purposes.
     * This fixture is used by tests inherited from {@link AbstractSortedBagTest}.
     */
    @SuppressWarnings("unchecked")
    public SortedBag<T> setupBag() {
        final SortedBag<T> bag = makeObject();
        bag.add((T) "C");
        bag.add((T) "A");
        bag.add((T) "B");
        bag.add((T) "D");
        return bag;
    }

    /**
     * Verifies that adding an object that does not implement {@link Comparable}
     * to a TreeBag using its default natural ordering throws an IllegalArgumentException.
     * <p>
     * This test corresponds to the issue reported in COLLECTIONS-265.
     * </p>
     */
    @Test
    void testAddNonComparableObjectThrowsIllegalArgumentException() {
        // Arrange: Create a TreeBag that uses natural ordering (the default)
        // and an object that is not comparable.
        final Bag<Object> bag = new TreeBag<>();
        final Object nonComparableObject = new Object();

        // Act & Assert: Attempting to add the non-Comparable object should fail.
        assertThrows(IllegalArgumentException.class, () -> bag.add(nonComparableObject));
    }
}