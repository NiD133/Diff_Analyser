package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Unit tests for the static factory methods of {@link TransformedBag}.
 */
public class TransformedBagTest {

    /**
     * Tests that the transformingBag() factory method throws a NullPointerException
     * when the provided transformer is null.
     */
    @Test(expected = NullPointerException.class)
    public void testTransformingBagFactoryRejectsNullTransformer() {
        // Arrange: Create a bag to decorate.
        final Bag<Integer> bag = new HashBag<>();

        // Act: Attempt to create a TransformedBag with a null transformer.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        TransformedBag.transformingBag(bag, null);
    }
}