package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for {@link TransformedMultiValuedMap}.
 * This particular test was improved for clarity from an auto-generated test case.
 */
public class TransformedMultiValuedMap_ESTestTest30 {

    /**
     * Tests that {@link TransformedMultiValuedMap#putAll(MultiValuedMap)} throws a RuntimeException
     * if the key transformation fails during the operation.
     */
    @Test
    public void putAllShouldThrowExceptionWhenKeyTransformationFails() {
        // Arrange
        // 1. Define a transformer that is guaranteed to fail for an Integer input.
        // The InvokerTransformer tries to call a method by name on the input object.
        // Since the Integer class has no method named "", this will throw a RuntimeException.
        final Transformer<Object, Integer> failingTransformer = InvokerTransformer.invokerTransformer("");

        // 2. Create the map under test, decorated with the failing transformer for its keys.
        final MultiValuedMap<Integer, Integer> decoratedMap =
                new TransformedMultiValuedMap<>(new HashSetValuedHashMap<>(), failingTransformer, null);

        // 3. Create a source map containing data to be added to the decorated map.
        final MultiValuedMap<Integer, Integer> sourceMap = new HashSetValuedHashMap<>();
        sourceMap.put(1, 100); // The key '1' will be passed to the failing transformer.

        // Act & Assert
        try {
            // This call should fail because the key '1' from the source map
            // cannot be transformed by the failingTransformer.
            decoratedMap.putAll(sourceMap);
            fail("Expected a RuntimeException because the key transformation should fail.");
        } catch (final RuntimeException e) {
            // Verify that the exception is the one expected from InvokerTransformer.
            final String expectedMessage = "InvokerTransformer: The method '' on 'class java.lang.Integer' does not exist";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}