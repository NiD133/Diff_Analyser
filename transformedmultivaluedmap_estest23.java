package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Contains tests for {@link TransformedMultiValuedMap}.
 * This specific test class focuses on behavior when decorating an unmodifiable map.
 */
public class TransformedMultiValuedMap_ESTestTest23 {

    /**
     * Tests that calling putAll on a TransformedMultiValuedMap throws an
     * UnsupportedOperationException if the decorated map is unmodifiable.
     * The call should be delegated to the underlying map, which then throws the exception.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void putAllShouldThrowUnsupportedOperationExceptionWhenMapIsUnmodifiable() {
        // Arrange
        // 1. Create a base map and wrap it to make it unmodifiable.
        final MultiValuedMap<String, Integer> baseMap = new LinkedHashSetValuedLinkedHashMap<>();
        final MultiValuedMap<String, Integer> unmodifiableMap =
                UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(baseMap);

        // 2. Use NOP transformers, as the transformation logic is not the focus of this test.
        final Transformer<String, String> keyTransformer = NOPTransformer.nopTransformer();
        final Transformer<Integer, Integer> valueTransformer = NOPTransformer.nopTransformer();

        // 3. Create the TransformedMultiValuedMap under test, decorating the unmodifiable map.
        final TransformedMultiValuedMap<String, Integer> transformedMap =
                new TransformedMultiValuedMap<>(unmodifiableMap, keyTransformer, valueTransformer);

        final String key = "test_key";
        final List<Integer> valuesToAdd = Arrays.asList(1, 2, 3);

        // Act
        // Attempt to add elements. This should fail because the underlying map is unmodifiable.
        transformedMap.putAll(key, valuesToAdd);

        // Assert
        // The @Test(expected) annotation handles the exception verification.
        // The test will fail if an UnsupportedOperationException is not thrown.
    }
}