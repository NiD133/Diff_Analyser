package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the TreeBag class, focusing on its sorting behavior.
 */
@DisplayName("TreeBag Ordering")
public class TreeBagTest extends AbstractSortedBagTest<String> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }



    @Override
    public SortedBag<String> makeObject() {
        return new TreeBag<>();
    }

    /**
     * Creates a pre-populated TreeBag for testing.
     * Elements are added out of their natural order to verify the bag sorts them correctly.
     *
     * @return A new TreeBag containing {"A", "B", "C", "D"}.
     */
    private SortedBag<String> createBagWithKnownElements() {
        final SortedBag<String> bag = makeObject();
        bag.add("C");
        bag.add("A");
        bag.add("B");
        bag.add("D");
        return bag;
    }

    @Test
    @DisplayName("should maintain elements in natural sort order")
    void testTreeBagMaintainsNaturalOrder() {
        // Arrange
        final SortedBag<String> bag = createBagWithKnownElements();
        final String[] expectedOrder = {"A", "B", "C", "D"};

        // Act & Assert
        // Group related assertions to ensure all are checked and reported together.
        assertAll("Verify elements are sorted and accessible",
            () -> assertArrayEquals(expectedOrder, bag.toArray(),
                "toArray() should return elements in sorted order"),
            () -> assertEquals("A", bag.first(),
                "first() should return the lowest element"),
            () -> assertEquals("D", bag.last(),
                "last() should return the highest element")
        );
    }
}