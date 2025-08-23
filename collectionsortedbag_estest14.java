package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the factory method of {@link CollectionSortedBag}.
 */
public class CollectionSortedBagTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that the factory method collectionSortedBag() throws a NullPointerException
     * when the bag to be decorated is null.
     */
    @Test
    public void collectionSortedBagFactoryShouldThrowNullPointerExceptionForNullInput() {
        // Arrange: Define the expected exception and its message.
        // The decorator's constructor is expected to throw this exception.
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("bag");

        // Act: Call the factory method with a null argument.
        // The cast to a specific SortedBag type is necessary for type inference.
        CollectionSortedBag.collectionSortedBag((SortedBag<Object>) null);

        // Assert: The ExpectedException rule handles the assertion.
    }
}