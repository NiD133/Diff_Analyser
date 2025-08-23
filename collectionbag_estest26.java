package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains an improved version of a test for {@link CollectionBag}.
 * The original test was auto-generated and lacked clarity.
 */
public class CollectionBag_ESTestTest26 { // Note: Class name kept for consistency.

    /**
     * Tests that if the underlying decorated bag throws a RuntimeException during an add operation,
     * the CollectionBag correctly propagates that exception.
     */
    @Test
    public void addShouldPropagateExceptionFromDecoratedBag() {
        // Arrange: Create a decorated bag that is designed to throw a RuntimeException
        // when an element is added. This is achieved by using a TransformedSortedBag
        // with an InvokerTransformer that attempts to call a method that does not exist.

        final String nonExistentMethodName = "aMethodThatDoesNotExist";
        final Transformer<Object, Object> failingTransformer =
                new InvokerTransformer<>(nonExistentMethodName, null, null);

        final SortedBag<Object> underlyingBag = new TreeBag<>();
        final Bag<Object> decoratedBagThatThrows =
                TransformedSortedBag.transformingSortedBag(underlyingBag, failingTransformer);

        // The CollectionBag is the class under test. It wraps the faulty decorated bag.
        final Bag<Object> collectionBag = new CollectionBag<>(decoratedBagThatThrows);
        final Object elementToAdd = new Object();

        // Act & Assert: Verify that calling add() on the CollectionBag propagates the expected exception.
        try {
            collectionBag.add(elementToAdd);
            fail("Expected a RuntimeException to be thrown because the underlying transformer should fail.");
        } catch (final RuntimeException e) {
            // Verify that the exception is the one thrown by InvokerTransformer.
            final String expectedMessage = "InvokerTransformer: The method '" + nonExistentMethodName +
                                           "' on 'class java.lang.Object' does not exist";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}