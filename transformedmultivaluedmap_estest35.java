package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test case focuses on the behavior of the
 * {@link TransformedMultiValuedMap#transformedMap(MultiValuedMap, Transformer, Transformer)}
 * factory method.
 */
public class TransformedMultiValuedMap_ESTestTest35 {

    /**
     * Tests that the {@code transformedMap} factory method propagates exceptions
     * thrown by the value transformer when applied to pre-existing values in the map.
     * <p>
     * The {@code transformedMap} factory is documented to transform all existing
     * elements of the map it decorates. This test verifies that if the transformation
     * of an existing value fails, the exception is correctly thrown.
     */
    @Test
    public void transformedMapFactory_whenValueTransformerThrowsExceptionOnExistingValue_shouldPropagateException() {
        // Arrange
        // Create a base MultiValuedMap with a pre-existing entry.
        final MultiValuedMap<String, Object> baseMap = new ArrayListValuedHashMap<>();
        baseMap.put("key1", "value1");

        // A transformer designed to always throw a RuntimeException upon invocation.
        final Transformer<Object, Object> exceptionThrowingTransformer = ExceptionTransformer.exceptionTransformer();

        // Act & Assert
        try {
            // The transformedMap() factory method attempts to transform all existing values
            // in the base map. We expect this to fail because our transformer
            // will be applied to "value1", which will trigger the exception.
            TransformedMultiValuedMap.transformedMap(baseMap, null, exceptionThrowingTransformer);
            fail("Expected a RuntimeException to be thrown, but the method completed successfully.");
        } catch (final RuntimeException e) {
            // Verify that the caught exception is the one thrown by our transformer.
            final String expectedMessage = "ExceptionTransformer invoked";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}