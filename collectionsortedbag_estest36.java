package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * This test suite contains an improved version of a test case for the {@link CollectionSortedBag} class.
 * The original test was auto-generated and lacked clarity.
 */
public class CollectionSortedBag_ESTestTest36 extends CollectionSortedBag_ESTest_scaffolding {

    /**
     * Verifies that calling {@code retainAll} with a null collection argument
     * results in a {@code NullPointerException}.
     * <p>
     * This behavior is consistent with the contract of {@link java.util.Collection#retainAll(java.util.Collection)}.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void retainAllWithNullCollectionShouldThrowNullPointerException() {
        // Arrange: Create an instance of the class under test.
        // The bag is initialized empty; its contents are not relevant for this test case.
        final SortedBag<Object> decorated = new TreeBag<>();
        final CollectionSortedBag<Object> bag = new CollectionSortedBag<>(decorated);

        // Act: Call the method under test with a null argument.
        bag.retainAll(null);

        // Assert: The @Test(expected) annotation verifies that a NullPointerException is thrown.
    }
}