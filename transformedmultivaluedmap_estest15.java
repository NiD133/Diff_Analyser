package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * This test class focuses on the exception handling of the TransformedMultiValuedMap.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that an exception thrown by the key transformer is propagated
     * when transformKey() is called.
     */
    @Test
    public void transformKeyShouldPropagateExceptionFromKeyTransformer() {
        // Arrange
        // Create a key transformer that is guaranteed to fail by trying to invoke a non-existent method ("").
        final Transformer<Integer, Integer> failingKeyTransformer = InvokerTransformer.invokerTransformer("");

        // The value transformer is not relevant for this test, so it can be null.
        final MultiValuedMap<Integer, Object> originalMap = new ArrayListValuedHashMap<>();
        final TransformedMultiValuedMap<Integer, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(originalMap, failingKeyTransformer, null);

        final Integer keyToTransform = 123;

        // Act & Assert
        // Expect a RuntimeException to be thrown by the failing transformer.
        final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transformedMap.transformKey(keyToTransform);
        });

        // Verify that the exception message is the one expected from InvokerTransformer.
        final String expectedMessage = "InvokerTransformer: The method '' on 'class java.lang.Integer' does not exist";
        assertEquals(expectedMessage, thrown.getMessage());
    }
}