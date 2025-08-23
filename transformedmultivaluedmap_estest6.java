package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Tests for {@link TransformedMultiValuedMap#transformKey(Object)}.
 */
public class TransformedMultiValuedMapTest {

    @Test
    public void transformKeyShouldReturnResultFromKeyTransformer() {
        // Arrange
        // 1. Define a key transformer that always returns null.
        final Transformer<String, String> keyTransformer = ConstantTransformer.constantTransformer(null);

        // 2. The value transformer is not used by the method under test, so a no-op is sufficient.
        final Transformer<String, String> valueTransformer = NOPTransformer.nopTransformer();

        // 3. Create the map to be decorated. Its contents are irrelevant for this test.
        final MultiValuedMap<String, String> sourceMap = new ArrayListValuedHashMap<>();

        // 4. Create the TransformedMultiValuedMap with the transformers.
        final TransformedMultiValuedMap<String, String> transformedMap =
                TransformedMultiValuedMap.transformingMap(sourceMap, keyTransformer, valueTransformer);

        // Act
        // Call the method under test with an arbitrary key.
        final String transformedKey = transformedMap.transformKey("any key");

        // Assert
        // The result should be null, as dictated by our ConstantTransformer.
        assertNull("The transformed key should be null", transformedKey);
    }
}