package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Contains tests for {@link CollectionSortedBag}.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that calling remove() with an object of an incompatible type throws a ClassCastException.
     * <p>
     * The {@link CollectionSortedBag} delegates the remove operation to the decorated bag.
     * In this test, we use a {@link TreeBag}, which requires its elements to be comparable.
     * When attempting to remove an object that cannot be cast to the bag's element type,
     * the underlying {@code TreeBag} will fail with a {@code ClassCastException} during its
     * internal comparison logic.
     */
    @Test(expected = ClassCastException.class)
    public void removeWithIncompatibleTypeShouldThrowClassCastException() {
        // Arrange: Create a CollectionSortedBag decorating a TreeBag of Strings.
        final SortedBag<String> decoratedBag = new TreeBag<>();
        final SortedBag<String> collectionBag = new CollectionSortedBag<>(decoratedBag);

        // An object that is not a String and cannot be cast to one.
        final Object incompatibleObject = new Object();

        // Act: Attempt to remove the incompatible object from the bag.
        // This will trigger the ClassCastException from the underlying TreeBag.
        collectionBag.remove(incompatibleObject);

        // Assert: A ClassCastException is expected, as declared in the @Test annotation.
    }
}