package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * This class contains tests for the {@link TransformedMultiValuedMap}.
 * This specific test focuses on the behavior of the putAll(K, Iterable) method.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that putAll(key, iterable) throws a RuntimeException if the value transformer
     * is designed to throw an exception. The transformation should be attempted for each
     * value in the provided iterable.
     */
    @Test
    public void putAllWithIterableShouldThrowExceptionWhenValueTransformerThrows() {
        // Arrange
        final MultiValuedMap<Integer, String> baseMap = new HashSetValuedHashMap<>();
        final Transformer<String, ?> exceptionThrowingValueTransformer = ExceptionTransformer.exceptionTransformer();

        // Decorate the base map, applying the exception-throwing transformer to values.
        // The key transformer is null, meaning keys are not transformed.
        final TransformedMultiValuedMap<Integer, String> transformedMap =
                TransformedMultiValuedMap.transformingMap(baseMap, null, exceptionThrowingValueTransformer);

        final List<String> valuesToAdd = Collections.singletonList("anyValue");
        final Integer key = 123;

        // Act & Assert
        // The call to putAll should trigger the value transformer, which throws an exception.
        final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transformedMap.putAll(key, valuesToAdd);
        });

        // Verify that the exception is the one from our transformer.
        assertEquals("ExceptionTransformer invoked", thrown.getMessage());
    }
}