package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSortedBagTest} for exercising the {@link TreeBag}.
 *
 * Note: The original class name `TreeBagTestTest2` was likely a typo or temporary name.
 * It has been renamed to `TreeBagTest` for clarity.
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
     * Verifies that adding a null element to a TreeBag using natural ordering
     * throws a NullPointerException. This behavior is expected as TreeBag relies
     * on TreeMap, which does not permit nulls with natural ordering.
     *
     * This test addresses ticket COLLECTIONS-555.
     */
    @Test
    void addNull_toBagWithNaturalOrdering_throwsNullPointerException() {
        // Arrange
        final Bag<Object> bag = new TreeBag<>();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> bag.add(null),
                "A TreeBag with natural ordering should not accept null elements.");
    }

    /**
     * Verifies that adding a null element to a non-empty TreeBag that uses a
     * comparator throws a NullPointerException.
     *
     * This test addresses ticket COLLECTIONS-555.
     */
    @Test
    void addNull_toNonEmptyBagWithComparator_throwsNullPointerException() {
        // Arrange
        final Bag<String> bag = new TreeBag<>(String::compareTo);

        // A bug in some JDK versions allows adding null to an empty TreeMap
        // even with a comparator. To ensure this test is reliable across different
        // Java versions, we first populate the bag with a non-null element.
        bag.add("a non-null element");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> bag.add(null),
                "A TreeBag with a comparator should not accept null elements.");
    }
}