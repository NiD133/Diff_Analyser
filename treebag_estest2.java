package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertSame;

import java.util.Locale;
import org.junit.Test;

/**
 * Test suite for the {@link TreeBag} class.
 */
public class TreeBagTest {

    /**
     * Tests that the {@link TreeBag#last()} method correctly returns the single element
     * when the bag contains only one item.
     */
    @Test
    public void last_shouldReturnTheOnlyElement_whenBagContainsOneItem() {
        // Arrange
        // A TreeBag uses the natural ordering of its elements by default.
        // Locale.FilteringMode is an enum, which is Comparable.
        final TreeBag<Locale.FilteringMode> bag = new TreeBag<>();
        final Locale.FilteringMode singleElement = Locale.FilteringMode.IGNORE_EXTENDED_RANGES;
        bag.add(singleElement);

        // Act
        final Locale.FilteringMode lastElement = bag.last();

        // Assert
        assertSame("The last element should be the only element present in the bag.",
                     singleElement, lastElement);
    }
}