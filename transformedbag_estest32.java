package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

/**
 * Contains tests for the constructor of {@link TransformedBag}.
 */
public class TransformedBagConstructorTest {

    /**
     * Tests that the TransformedBag constructor throws a NullPointerException
     * when the decorated bag provided is null. The transformer must also be non-null,
     * but its behavior is irrelevant to this specific test.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionWhenBagIsNull() {
        // Arrange: A valid transformer and a null bag.
        final Transformer<Object, Object> transformer = ConstantTransformer.nullTransformer();
        final Bag<Object> decoratedBag = null;

        // Act & Assert: Instantiating with a null bag should throw a NullPointerException.
        new TransformedBag<>(decoratedBag, transformer);
    }
}