package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Contains tests for {@link CollectionSortedBag} to ensure it correctly
 * propagates behavior from the decorated bag.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that add(null) throws a NullPointerException if the underlying
     * bag does not support null elements.
     */
    @Test(expected = NullPointerException.class)
    public void add_whenUnderlyingBagRejectsNull_throwsNullPointerException() {
        // Arrange: Create a CollectionSortedBag that decorates a TreeBag.
        // A standard TreeBag uses natural ordering and does not allow nulls.
        final SortedBag<Object> underlyingBag = new TreeBag<>();
        final SortedBag<Object> bagUnderTest = new CollectionSortedBag<>(underlyingBag);

        // Act: Attempt to add a null element. This call is delegated to the
        // underlying TreeBag, which is expected to throw the exception.
        bagUnderTest.add(null);

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}