package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link TransformedMultiValuedMap#putAll(Map)}.
 */
public class TransformedMultiValuedMapTest {

    @Test
    public void testPutAllWithKeyAndValueTransformers() {
        // Arrange
        // 1. The map to be decorated.
        MultiValuedMap<Object, Object> underlyingMap = new LinkedHashSetValuedLinkedHashMap<>();

        // 2. A map to define the transformation logic. The transformers will look up
        // keys and values in this map to find their transformed counterparts.
        Map<Object, Object> transformationLogicMap = new HashMap<>();
        transformationLogicMap.put("keyToTransform", "transformedKey");
        transformationLogicMap.put("valueToTransform", "transformedValue");

        // 3. Transformers that use the logic map. If a key/value is not found in the
        // transformationLogicMap, the MapTransformer will return null.
        Transformer<Object, Object> keyTransformer = MapTransformer.mapTransformer(transformationLogicMap);
        Transformer<Object, Object> valueTransformer = MapTransformer.mapTransformer(transformationLogicMap);

        // 4. The TransformedMultiValuedMap instance under test.
        MultiValuedMap<Object, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(underlyingMap, keyTransformer, valueTransformer);

        // 5. The map containing entries to be added. These keys and values will be
        // transformed before being put into the underlying map.
        Map<Object, Object> mapToAdd = new HashMap<>();
        mapToAdd.put("keyToTransform", "unmappedValue");   // Key transforms, value becomes null
        mapToAdd.put("unmappedKey", "valueToTransform"); // Key becomes null, value transforms

        // Act
        boolean wasModified = transformedMap.putAll(mapToAdd);

        // Assert
        assertTrue("putAll should return true as the map was modified", wasModified);
        assertEquals("The underlying map should contain two entries after putAll", 2, underlyingMap.size());

        // Verify that the first entry was transformed correctly:
        // Key: "keyToTransform" -> "transformedKey"
        // Value: "unmappedValue" -> null (as it's not in transformationLogicMap)
        assertTrue("Map should contain the first transformed entry",
                   underlyingMap.containsMapping("transformedKey", null));

        // Verify that the second entry was transformed correctly:
        // Key: "unmappedKey" -> null (as it's not in transformationLogicMap)
        // Value: "valueToTransform" -> "transformedValue"
        assertTrue("Map should contain the second transformed entry",
                   underlyingMap.containsMapping(null, "transformedValue"));
    }
}