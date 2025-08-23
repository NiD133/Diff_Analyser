package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link TransformedBag}.
 */
public class TransformedBagTest {

    /**
     * Tests that the transformedBag factory method throws a NullPointerException
     * when the bag to be decorated is null.
     */
    @Test
    public void transformedBag_whenDecoratedBagIsNull_shouldThrowNullPointerException() {
        // Arrange: Create a valid transformer. The bag to be decorated is null.
        final Transformer<Object, Object> transformer = ConstantTransformer.constantTransformer("ignored");
        final Bag<Object> nullBag = null;

        // Act & Assert: Call the factory method and verify the exception.
        final NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> TransformedBag.transformedBag(nullBag, transformer)
        );

        // Verify that the exception message is as expected from the underlying implementation.
        assertEquals("collection", exception.getMessage());
    }
}