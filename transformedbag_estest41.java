package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the static factory method {@link TransformedBag#transformingBag(Bag, Transformer)}.
 */
public class TransformedBagCreationTest {

    /**
     * Tests that creating a transforming bag from an empty source bag results in an empty bag.
     *
     * The {@code transformingBag} factory method does not transform existing elements,
     * so an empty source bag should produce an empty transformed bag.
     */
    @Test
    public void transformingBagFromEmptyBagShouldBeEmpty() {
        // Arrange
        final Bag<Object> emptySourceBag = new HashBag<>();
        final String transformedValue = "always this value";
        final Transformer<Object, Object> transformer = ConstantTransformer.constantTransformer(transformedValue);

        // Act
        // Create a TransformedBag using the factory method.
        final Bag<Object> transformedBag = TransformedBag.transformingBag(emptySourceBag, transformer);

        // Assert
        // The resulting bag should be empty because the source bag was empty.
        assertTrue("A transformed bag created from an empty bag should be empty", transformedBag.isEmpty());
        assertFalse("An empty transformed bag should not contain the value the transformer would produce",
                transformedBag.contains(transformedValue));
    }
}