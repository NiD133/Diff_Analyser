package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link CollectionSortedBag} focusing on exception propagation from the decorated bag.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that an exception thrown by the decorated bag's add() method
     * is propagated up through the CollectionSortedBag.
     */
    @Test
    public void addShouldPropagateExceptionFromDecoratedBag() {
        // Arrange: Create a bag that will throw an exception when an element is added.
        // This is achieved by decorating a standard TreeBag with a TransformedSortedBag
        // that uses a transformer designed to always throw an exception.
        final Transformer<Object, Object> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        final SortedBag<Object> exceptionThrowingBag =
                new TransformedSortedBag<>(new TreeBag<>(), exceptionTransformer);

        // Arrange: Decorate the exception-throwing bag with the class under test.
        final SortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(exceptionThrowingBag);
        final Object objectToAdd = "some element";

        // Act & Assert
        try {
            collectionSortedBag.add(objectToAdd);
            fail("A RuntimeException should have been thrown by the decorated bag.");
        } catch (final RuntimeException e) {
            // Assert: Verify that the correct exception was caught.
            final String expectedMessage = "ExceptionTransformer invoked";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}