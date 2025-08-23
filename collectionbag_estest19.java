package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link CollectionBag}.
 * This test class has been improved for clarity and maintainability.
 */
public class CollectionBag_ESTestTest19 { // Retaining original class name for context

    /**
     * Tests that addAll() throws an IllegalArgumentException when the underlying
     * decorated bag is a PredicatedBag and its predicate rejects an element.
     * This scenario is triggered by trying to add an element that already exists
     * to a bag decorated with a UniquePredicate.
     */
    @Test
    public void addAllShouldThrowExceptionWhenDecoratedBagPredicateRejectsElement() {
        // Arrange
        // 1. Create a base bag and add an element to it.
        final SortedBag<Integer> baseBag = new TreeBag<>();
        final Integer element = 977;
        baseBag.add(element);

        // 2. Decorate the base bag with a predicate that only allows unique elements.
        // The predicated bag now contains one element, and the predicate has seen it once.
        final Predicate<Integer> uniquePredicate = new UniquePredicate<>();
        final SortedBag<Integer> predicatedBag =
                PredicatedSortedBag.predicatedSortedBag(baseBag, uniquePredicate);

        // 3. The CollectionBag under test decorates the predicated bag.
        final CollectionBag<Integer> collectionBag = new CollectionBag<>(predicatedBag);

        // Act & Assert
        try {
            // Attempt to add all elements from the predicatedBag back into itself.
            // This will try to add the element '977' a second time.
            // The UniquePredicate will return false, causing the PredicatedBag to throw an exception.
            collectionBag.addAll(predicatedBag);
            fail("Expected an IllegalArgumentException to be thrown because the predicate should reject the duplicate element.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message clearly indicates that a predicate rejected the element.
            final String message = e.getMessage();
            assertTrue("Exception message should mention the rejected object.", message.contains("Cannot add Object '977'"));
            assertTrue("Exception message should indicate that a predicate was the cause.", message.contains("rejected it"));
        }
    }
}