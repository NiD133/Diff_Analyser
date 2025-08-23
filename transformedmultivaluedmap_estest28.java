package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains an improved version of a test for TransformedMultiValuedMap.
 * The original test was auto-generated and has been refactored for better readability and maintainability.
 */
public class TransformedMultiValuedMap_ESTestTest28 extends TransformedMultiValuedMap_ESTest_scaffolding {

    /**
     * Tests that an exception thrown by the decorated map's 'put' method
     * is correctly propagated by the TransformedMultiValuedMap.
     *
     * The test sets up a scenario where the underlying map (ArrayListValuedHashMap)
     * is initialized with a negative capacity, which causes its 'put' method
     * to throw an IllegalArgumentException when a new key is added.
     */
    @Test
    public void putShouldPropagateExceptionFromUnderlyingMap() {
        // Arrange
        // 1. Define a negative capacity that will cause an exception.
        final int negativeCapacity = -173;

        // 2. Create an underlying map that is guaranteed to fail when a new key is added.
        //    ArrayListValuedHashMap throws an IllegalArgumentException if its internal
        //    ArrayList is created with a negative capacity.
        final MultiValuedMap<Integer, Integer> underlyingMap =
                new ArrayListValuedHashMap<>(negativeCapacity);

        // 3. The transformers are required by the factory method but are not the cause of the exception.
        //    Any input will be transformed to the negativeCapacity value.
        final Transformer<Integer, Integer> transformer = ConstantTransformer.constantTransformer(negativeCapacity);

        // 4. Decorate the faulty map with TransformedMultiValuedMap.
        final MultiValuedMap<Integer, Integer> transformedMap =
                TransformedMultiValuedMap.transformedMap(underlyingMap, transformer, transformer);

        // Act & Assert
        try {
            // This call will first transform the key and value, then delegate to the
            // underlying map's put method, which will throw the exception.
            transformedMap.put(1, 1);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (final IllegalArgumentException e) {
            // Verify that the propagated exception is the one we expect from the underlying map.
            final String expectedMessage = "Illegal Capacity: " + negativeCapacity;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}