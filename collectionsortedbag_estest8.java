package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Collections;

/**
 * Contains tests for {@link CollectionSortedBag} focusing on its behavior
 * when decorating an unmodifiable bag.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that a modification method (removeAll) on a CollectionSortedBag
     * throws an UnsupportedOperationException if it decorates an unmodifiable bag.
     * This verifies that the method call is correctly delegated to the wrapped bag.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void removeAllOnCollectionBagDecoratingUnmodifiableBagShouldThrowException() {
        // Arrange: Create a decorator chain: TreeBag -> UnmodifiableSortedBag -> CollectionSortedBag
        
        // 1. Create a base, modifiable bag with an element.
        final SortedBag<String> modifiableBag = new TreeBag<>();
        modifiableBag.add("A");

        // 2. Decorate it to be unmodifiable.
        final SortedBag<String> unmodifiableBag = UnmodifiableSortedBag.unmodifiableSortedBag(modifiableBag);

        // 3. Decorate the unmodifiable bag with the class under test.
        final SortedBag<String> collectionSortedBag = new CollectionSortedBag<>(unmodifiableBag);

        // Act: Attempt to modify the bag. This call should be delegated to the
        // unmodifiable decorator, which will throw the expected exception.
        collectionSortedBag.removeAll(Collections.singleton("A"));

        // Assert: The test passes if an UnsupportedOperationException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}