package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the TransformedBag class, focusing on specific behaviors.
 */
public class TransformedBagTest {

    /**
     * Tests that calling hashCode() on an empty TransformedBag returns the
     * correct value (0) and does not throw an exception.
     *
     * The hashCode of a TransformedBag is delegated to the decorated bag.
     * For an empty bag, this is expected to be 0.
     */
    @Test
    public void testHashCodeOnEmptyBagIsZero() {
        // Arrange: Create an empty bag to be decorated.
        final Bag<Object> emptySourceBag = new HashBag<>();
        
        // The specific transformer is irrelevant for this test, as hashCode() on an
        // empty bag does not involve transforming elements.
        final Transformer<Object, Object> dummyTransformer = ConstantTransformer.constantTransformer("ignored");

        // Act: Decorate the empty bag.
        final Bag<Object> transformedBag = TransformedBag.transformingBag(emptySourceBag, dummyTransformer);

        // Assert: The hashCode of an empty collection should be 0.
        // This also implicitly verifies that the method call completes without an exception.
        assertEquals("The hashCode of an empty bag should be 0", 0, transformedBag.hashCode());
    }
}