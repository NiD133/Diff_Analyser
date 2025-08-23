package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for CollectionSortedBag.
 * This particular test focuses on exception propagation from decorated components.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that an exception thrown by a transformer in a decorated bag
     * is correctly propagated when addAll() is called on the CollectionSortedBag.
     */
    @Test
    public void addAllShouldPropagateExceptionFromUnderlyingTransformer() {
        // --- Arrange ---

        // 1. Create a base bag. The comparator is trivial as its behavior is not under test.
        final TreeBag<Object> baseBag = new TreeBag<>((o1, o2) -> 0);

        // 2. Create a transformer that is guaranteed to fail by attempting to invoke
        // a method with an empty (and thus invalid) name.
        final Transformer<Object, Object> failingTransformer =
                new InvokerTransformer<>("", new Class[0], new Object[0]);

        // 3. Decorate the base bag with the transformer. Any element added to this bag
        // will first be passed through the failing transformer.
        final SortedBag<Object> transformedBag =
                TransformedSortedBag.transformingSortedBag(baseBag, failingTransformer);

        // 4. Create the CollectionSortedBag under test, decorating the transformed bag.
        final SortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(transformedBag);

        // 5. Create a collection containing an element that will trigger the exception.
        // When addAll attempts to add `transformedBag`, the failingTransformer will be applied to it.
        final Collection<Object> elementsToAdd = Collections.singleton(transformedBag);

        // --- Act & Assert ---
        try {
            collectionSortedBag.addAll(elementsToAdd);
            fail("Expected a RuntimeException to be thrown due to the failing transformer.");
        } catch (final RuntimeException e) {
            // The InvokerTransformer throws a FunctorException (a subclass of RuntimeException)
            // when the method invocation fails. We verify its message.
            final String expectedMessage = "InvokerTransformer: The method '' on 'class " +
                                           "org.apache.commons.collections4.bag.TransformedSortedBag' does not exist";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}