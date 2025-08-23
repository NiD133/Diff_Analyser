package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Tests for {@link TransformedMultiValuedMap}.
 * This class focuses on specific behaviors of the transformation logic.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that the {@code transformValue} method returns the original object
     * instance when the value transformer provided during construction is null.
     * This verifies that a null transformer acts as a no-op identity function.
     */
    @Test
    public void transformValueShouldReturnOriginalValueWhenTransformerIsNull() {
        // Arrange
        // Create a base map to be decorated.
        final MultiValuedMap<Integer, Object> backingMap = new ArrayListValuedHashMap<>();

        // Create the TransformedMultiValuedMap with a null value transformer.
        // The key transformer is also null but is not relevant for this test.
        final TransformedMultiValuedMap<Integer, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(backingMap, null, null);

        final Object originalValue = new Object();

        // Act
        // Call the method under test.
        final Object transformedValue = transformedMap.transformValue(originalValue);

        // Assert
        // The returned value should be the exact same instance as the input.
        assertSame("Expected the original object instance when the transformer is null",
                     originalValue, transformedValue);
    }
}