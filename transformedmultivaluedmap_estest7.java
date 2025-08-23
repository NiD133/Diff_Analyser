package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Contains tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that the {@code transformKey} method correctly delegates the transformation
     * logic to the provided key transformer.
     */
    @Test
    public void transformKeyShouldDelegateToKeyTransformer() {
        // Arrange
        final String expectedTransformedKey = "TRANSFORMED_KEY";

        // Use a ConstantTransformer which ignores its input and always returns a predefined object.
        // This allows us to verify that the transformKey method is indeed using our transformer.
        final Transformer<String, String> keyTransformer =
                ConstantTransformer.constantTransformer(expectedTransformedKey);

        // The base map to be decorated. It can be empty as we are only testing the transformation logic.
        final MultiValuedMap<String, Object> baseMap = new LinkedHashSetValuedLinkedHashMap<>();

        // Create the map under test, providing the key transformer.
        // The value transformer is not relevant for this test, so it can be null.
        final TransformedMultiValuedMap<String, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(baseMap, keyTransformer, null);

        // An arbitrary input key to pass to the method.
        final String inputKey = "ORIGINAL_KEY";

        // Act
        final String actualTransformedKey = transformedMap.transformKey(inputKey);

        // Assert
        // The result must be the exact same object returned by the transformer,
        // confirming that the delegation occurred as expected.
        assertSame("The transformed key should be the instance returned by the key transformer",
                     expectedTransformedKey, actualTransformedKey);
    }
}