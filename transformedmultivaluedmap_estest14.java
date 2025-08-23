package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.junit.Test;

/**
 * Test suite for {@link TransformedMultiValuedMap}.
 * This test focuses on the behavior of the transformValue method.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that transformValue() propagates a NullPointerException if the underlying
     * valueTransformer throws it. This is simulated by using a ChainedTransformer
     * that is initialized with a null transformer instance.
     */
    @Test(expected = NullPointerException.class)
    public void transformValueShouldThrowNPEWhenUnderlyingTransformerFails() {
        // Arrange
        final MultiValuedMap<String, Integer> baseMap = new HashSetValuedHashMap<>();

        // Create a ChainedTransformer from an array containing a null transformer.
        // This is an invalid state for ChainedTransformer and will cause it to throw
        // a NullPointerException when its transform() method is invoked.
        final Transformer<Integer, Integer>[] transformersWithNull = new Transformer[1];
        final Transformer<Integer, Integer> faultyTransformer = new ChainedTransformer<>(transformersWithNull);

        // Decorate the base map with the faulty value transformer.
        // The key transformer is null, meaning keys are not transformed.
        final TransformedMultiValuedMap<String, Integer> transformedMap =
                TransformedMultiValuedMap.transformingMap(baseMap, null, faultyTransformer);

        final Integer valueToTransform = 42;

        // Act
        // This call should delegate to the faultyTransformer, which will throw the NPE.
        transformedMap.transformValue(valueToTransform);

        // Assert
        // The test expects a NullPointerException, as declared in the @Test annotation.
    }
}