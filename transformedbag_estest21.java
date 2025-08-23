package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

/**
 * Tests for {@link TransformedBag}.
 */
public class TransformedBagTest {

    /**
     * Tests that getCount() throws a ClassCastException when the decorated bag
     * cannot handle the type of the object being queried.
     */
    @Test(expected = ClassCastException.class)
    public void getCountWithIncompatibleObjectTypeShouldThrowException() {
        // Arrange
        // A TreeBag requires its elements to be comparable.
        final Bag<Integer> decoratedBag = new TreeBag<>();
        
        // The transformer is not invoked by getCount, so its behavior is not relevant here.
        final Transformer<Integer, Integer> anyTransformer = ExceptionTransformer.exceptionTransformer();
        final Bag<Integer> transformedBag = TransformedBag.transformingBag(decoratedBag, anyTransformer);

        // Act & Assert
        // The getCount() call is delegated to the underlying TreeBag. The TreeBag
        // will attempt to compare the input object with its elements (Integers),
        // which fails and throws a ClassCastException because the bag itself is not
        // comparable to an Integer.
        transformedBag.getCount(transformedBag);
    }
}