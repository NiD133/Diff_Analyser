package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for TransformedMultiValuedMap.
 * Note: The original class name "TransformedMultiValuedMap_ESTestTest13" was auto-generated.
 * A more appropriate name would be "TransformedMultiValuedMapTest".
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that the transformValue method correctly propagates exceptions
     * thrown by the underlying value transformer.
     */
    @Test
    public void transformValueShouldPropagateExceptionFromTransformer() {
        // Arrange
        // 1. Create a base map to be decorated.
        final MultiValuedMap<Integer, Object> baseMap = new ArrayListValuedLinkedHashMap<>();

        // 2. Use a transformer that is designed to always throw a RuntimeException.
        final Transformer<Object, Object> exceptionTransformer = ExceptionTransformer.exceptionTransformer();

        // 3. Decorate the base map, providing the exception-throwing transformer for values.
        //    The key transformer is null, so keys will not be transformed.
        final TransformedMultiValuedMap<Integer, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(baseMap, null, exceptionTransformer);

        // Act & Assert
        try {
            // 4. Call the method under test. It should delegate to the exceptionTransformer,
            //    which will throw an exception.
            transformedMap.transformValue(null);
            fail("Expected a RuntimeException to be thrown.");
        } catch (final RuntimeException e) {
            // 5. Verify that the caught exception is the one thrown by ExceptionTransformer.
            assertEquals("ExceptionTransformer invoked", e.getMessage());
        }
    }
}