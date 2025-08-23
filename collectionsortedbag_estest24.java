package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * This test class demonstrates the behavior of the CollectionSortedBag decorator.
 * The original test case was refactored for improved understandability.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that calling add(element, count) on a CollectionSortedBag throws a
     * NullPointerException if the element is null and the underlying decorated
     * bag (in this case, a TreeBag) does not support null elements.
     */
    @Test(expected = NullPointerException.class)
    public void addWithCount_whenElementIsNullAndUnderlyingBagDisallowsNulls_throwsNullPointerException() {
        // Arrange: Create a CollectionSortedBag decorating a TreeBag.
        // A TreeBag is a SortedBag implementation that does not permit null elements.
        final SortedBag<String> treeBag = new TreeBag<>();
        final SortedBag<String> collectionSortedBag = new CollectionSortedBag<>(treeBag);
        final int count = 10;

        // Act & Assert: Attempt to add a null element.
        // This call should be forwarded to the underlying TreeBag, which will
        // throw a NullPointerException, which is then expected by the test.
        collectionSortedBag.add(null, count);
    }
}