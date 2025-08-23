package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link TransformedBag}.
 * This class was improved for understandability from an auto-generated test.
 */
public class TransformedBag_ESTestTest28 {

    /**
     * Tests that adding an element to a TransformedBag propagates the IllegalArgumentException
     * thrown by the decorated bag if its predicate rejects the addition.
     */
    @Test
    public void addShouldThrowExceptionWhenDecoratedBagRejectsElement() {
        // Arrange
        // 1. Create a predicate that will reject the element we intend to add.
        // The IdentityPredicate checks for object identity (==). It is configured to only
        // accept a specific 'sentinel' object, thus rejecting all other objects.
        final Object predicateSentinel = new Object();
        final Predicate<Object> rejectingPredicate = IdentityPredicate.identityPredicate(predicateSentinel);

        // 2. Create a bag decorated with this predicate. Any attempt to add an object
        // other than 'predicateSentinel' will result in an IllegalArgumentException.
        final Bag<Object> predicatedBag = PredicatedBag.predicatedBag(new HashBag<>(), rejectingPredicate);

        // 3. Define the object that the transformer will produce. This is the object
        // that will be passed to the predicatedBag and subsequently rejected.
        final Integer transformedElement = 1480;
        final Transformer<Object, Object> transformer = ConstantTransformer.constantTransformer(transformedElement);

        // 4. Create the TransformedBag under test, decorating the predicatedBag.
        final Bag<Object> transformedBag = TransformedBag.transformingBag(predicatedBag, transformer);

        final String elementToAdd = "any element";
        final int numberOfCopies = 39;

        // Act & Assert
        try {
            // Attempt to add an element. The transformer will convert 'elementToAdd' to 'transformedElement' (1480).
            // The underlying predicatedBag will then reject 1480 because it does not match the predicate's sentinel object.
            transformedBag.add(elementToAdd, numberOfCopies);
            fail("Expected an IllegalArgumentException because the underlying predicate should reject the element.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception was thrown for the correct reason.
            final String expectedMessageFragment = "Cannot add Object '" + transformedElement + "' - Predicate";
            assertTrue(
                "The exception message should indicate that the transformed element was rejected by the predicate.",
                e.getMessage().startsWith(expectedMessageFragment)
            );
        }
    }
}