package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * This test class contains tests for CollectionBag.
 * The original test was auto-generated and has been improved for clarity.
 */
public class CollectionBag_ESTestTest9 extends CollectionBag_ESTest_scaffolding {

    /**
     * Tests that a modification attempt on a CollectionBag that wraps an UnmodifiableBag
     * correctly propagates the resulting UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeShouldThrowExceptionWhenDecoratingUnmodifiableBag() {
        // Arrange: Create a CollectionBag that decorates an UnmodifiableBag.
        final Bag<Integer> baseBag = new TreeBag<>();
        final Bag<Object> unmodifiableBag = UnmodifiableBag.unmodifiableBag(baseBag);
        final Bag<Object> collectionBag = new CollectionBag<>(unmodifiableBag);
        final Object objectToRemove = new Object();

        // Act: Attempt to remove an element. This action is delegated to the
        // underlying unmodifiable bag, which should throw an exception.
        collectionBag.remove(objectToRemove);

        // Assert: The @Test(expected) annotation verifies that an
        // UnsupportedOperationException is thrown.
    }
}