package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMap_ESTestTest1 extends TransformedMultiValuedMap_ESTest_scaffolding {

    /**
     * Tests that putAll() correctly transforms both keys and values before adding them
     * to the underlying map.
     */
    @Test
    public void testPutAllTransformsKeysAndValues() {
        // Arrange: Set up a map that transforms all keys and values to a constant integer.
        final Integer transformedValue = 1270;
        final Transformer<Object, Integer> constantTransformer = new ConstantTransformer<>(transformedValue);

        // The map that will be decorated and will hold the transformed data.
        final MultiValuedMap<Integer, Object> underlyingMap = new ArrayListValuedLinkedHashMap<>();

        // The decorator map under test.
        // Any key/value put into this map will be transformed by the constantTransformer.
        final TransformedMultiValuedMap<Integer, Object> transformedMap =
                new TransformedMultiValuedMap<>(underlyingMap, constantTransformer, constantTransformer);

        // The values to be added. The actual content (e.g., 999) doesn't matter
        // as the transformer will convert it to 1270.
        final Collection<Integer> valuesToAdd = Collections.singletonList(999);

        // Act: Add entries using different keys. Both keys should be transformed to 1270.
        transformedMap.putAll(1, valuesToAdd);
        transformedMap.putAll(null, valuesToAdd);

        // Assert: Verify that the underlying map contains the transformed keys and values.
        // We expect a single key (1270) with two transformed values (both 1270).
        assertEquals("The underlying map should contain only one key after transformation.", 1, underlyingMap.keySet().size());
        assertTrue("The key in the underlying map should be the transformed value.", underlyingMap.containsKey(transformedValue));

        final Collection<Object> resultingValues = underlyingMap.get(transformedValue);
        assertEquals("There should be two values associated with the transformed key.", 2, resultingValues.size());

        // Check that both values were correctly transformed.
        for (final Object value : resultingValues) {
            assertEquals("Each value should have been converted to the transformed value.", transformedValue, value);
        }
    }
}