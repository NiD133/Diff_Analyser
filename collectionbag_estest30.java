package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.junit.Test;

/**
 * Tests for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that the add() method on a CollectionBag respects the predicate
     * of a decorated PredicatedBag. If the predicate rejects an element,
     * an IllegalArgumentException should be thrown.
     */
    @Test
    public void addShouldThrowExceptionWhenDecoratedBagPredicateRejectsElement() {
        // Arrange
        // Create two distinct object instances with the same value.
        // The IdentityPredicate will distinguish them based on instance identity (==).
        final Integer acceptedObject = new Integer(4);
        final Integer rejectedObject = new Integer(4);

        // The predicate is configured to only accept the specific 'acceptedObject' instance.
        final Predicate<Integer> identityPredicate = new IdentityPredicate<>(acceptedObject);

        // Create a base bag and decorate it with the predicate.
        final SortedBag<Integer> baseBag = new TreeBag<>();
        final Bag<Integer> predicatedBag = new PredicatedSortedBag<>(baseBag, identityPredicate);

        // The CollectionBag under test decorates the predicated bag.
        final Bag<Integer> collectionBag = new CollectionBag<>(predicatedBag);

        // Act & Assert
        try {
            // Attempt to add the object that should be rejected by the predicate.
            collectionBag.add(rejectedObject);
            fail("Expected an IllegalArgumentException because the predicate should reject the object.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception was thrown for the correct reason.
            final String actualMessage = e.getMessage();
            assertTrue(
                "Exception message should indicate that a predicate rejected the object. Actual: " + actualMessage,
                actualMessage.contains("Predicate") && actualMessage.contains("rejected it")
            );
        }
    }
}