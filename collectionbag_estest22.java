package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.IfTransformer;
import org.junit.Test;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.fail;

public class CollectionBag_ESTestTest22 extends CollectionBag_ESTest_scaffolding {

    /**
     * Tests that CollectionBag.add() propagates exceptions thrown by the underlying
     * decorated bag's transformer.
     */
    @Test(timeout = 4000)
    public void testAddWithCountPropagatesExceptionFromUnderlyingTransformer() {
        // Arrange: Set up a CollectionBag that decorates a TransformedBag.
        // The TransformedBag is configured with a transformer designed to throw a
        // NullPointerException when an element is processed.

        final Bag<Predicate<Object>> baseBag = new TreeBag<>();
        final Predicate<Object> alwaysFalsePredicate = FalsePredicate.falsePredicate();

        // This transformer uses a predicate that always returns false. When the predicate is
        // false, it attempts to use the 'false' transformer, which we have set to null.
        // This will cause a NullPointerException when transform() is called.
        final Transformer<Predicate<Object>, Predicate<Object>> npeThrowingTransformer =
                new IfTransformer<>(alwaysFalsePredicate, null, null);

        final Bag<Predicate<Object>> transformedBag =
                TransformedBag.transformedBag(baseBag, npeThrowingTransformer);

        // The CollectionBag under test decorates the problematic TransformedBag.
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(transformedBag);

        final Predicate<Object> elementToAdd = FalsePredicate.falsePredicate();

        // Act & Assert: Verify that calling add() on the CollectionBag propagates the NPE.
        try {
            // Even with a count of 0, TransformedBag still applies the transformer to the object.
            collectionBag.add(elementToAdd, 0);
            fail("Expected a NullPointerException to be thrown by the underlying transformer.");
        } catch (final NullPointerException e) {
            // The exception is expected to originate from the IfTransformer trying to
            // invoke the null 'false' transformer.
            verifyException("org.apache.commons.collections4.functors.IfTransformer", e);
        }
    }
}