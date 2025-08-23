package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class verifies the behavior of the CollectionSortedBag decorator,
 * specifically focusing on how it handles decorated bags that reject elements.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that CollectionSortedBag.add() throws an IllegalArgumentException
     * if the underlying decorated bag rejects the element being added.
     */
    @Test
    public void addShouldThrowExceptionWhenDecoratedBagRejectsElement() {
        // Arrange
        // 1. Create a base sorted bag. TreeBag is a standard implementation.
        final SortedBag<String> baseBag = new TreeBag<>();

        // 2. Create a predicate that always rejects any element.
        final Predicate<String> rejectingPredicate = FalsePredicate.falsePredicate();

        // 3. Decorate the base bag with a PredicatedSortedBag. This bag will use the
        //    rejectingPredicate to throw an exception for any element we try to add.
        final SortedBag<String> predicatedBag =
                PredicatedSortedBag.predicatedSortedBag(baseBag, rejectingPredicate);

        // 4. Decorate the predicatedBag with CollectionSortedBag, the class under test.
        final SortedBag<String> collectionBag = new CollectionSortedBag<>(predicatedBag);

        final String elementToAdd = "any-element";

        // Act & Assert
        try {
            collectionBag.add(elementToAdd);
            fail("Expected an IllegalArgumentException to be thrown because the predicate rejects the element.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception is the one thrown by PredicatedCollection
            // and that its message clearly indicates the reason for failure.
            final String expectedMessage =
                    "Cannot add Object '" + elementToAdd + "' - Predicate rejected it";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}