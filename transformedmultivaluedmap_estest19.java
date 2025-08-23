package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionFactory;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link TransformedMultiValuedMap}.
 * This test case focuses on the behavior of the putAll(Map) method.
 */
// The original test class name is preserved for context.
public class TransformedMultiValuedMap_ESTestTest19 extends TransformedMultiValuedMap_ESTest_scaffolding {

    /**
     * Tests that if the key transformer throws an exception during a putAll(Map) operation,
     * the exception is propagated to the caller.
     */
    @Test
    public void putAllMapShouldPropagateExceptionFromKeyTransformer() {
        // Arrange
        // 1. Create a key transformer that is designed to always throw an exception.
        final Factory<Object> exceptionFactory = ExceptionFactory.exceptionFactory();
        final Transformer<Object, Object> throwingKeyTransformer = new FactoryTransformer<>(exceptionFactory);

        // 2. The value transformer is not relevant to this test, so a no-op transformer is used for clarity.
        final Transformer<Integer, Integer> valueTransformer = NOPTransformer.nopTransformer();

        // 3. Create the map under test, decorating a standard map with our throwing key transformer.
        final MultiValuedMap<Object, Integer> baseMap = new HashSetValuedHashMap<>();
        final MultiValuedMap<Object, Integer> transformedMap =
                TransformedMultiValuedMap.transformingMap(baseMap, throwingKeyTransformer, valueTransformer);

        // 4. Create a source map with a single entry to trigger the transformation.
        final Map<String, Integer> mapToPut = Collections.singletonMap("key", 123);

        // Act & Assert
        try {
            transformedMap.putAll(mapToPut);
            fail("Expected a RuntimeException to be thrown by the key transformer.");
        } catch (final RuntimeException e) {
            // Verify that the caught exception is the one we expected from the factory.
            assertEquals("ExceptionFactory invoked", e.getMessage());
        }
    }
}