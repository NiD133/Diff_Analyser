package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that calling putAll() with an empty Map does not modify the
     * underlying map and returns false.
     */
    @Test
    public void putAllWithEmptyMapShouldReturnFalse() {
        // Arrange
        final MultiValuedMap<String, Integer> baseMap = new ArrayListValuedHashMap<>();
        final TransformedMultiValuedMap<String, Integer> transformedMap =
                TransformedMultiValuedMap.transformingMap(baseMap, null, null);

        final Map<String, Integer> emptyMapToPut = Collections.emptyMap();

        // Act
        final boolean wasModified = transformedMap.putAll(emptyMapToPut);

        // Assert
        assertFalse("putAll with an empty map should return false as no changes were made", wasModified);
        assertTrue("The transformed map should remain empty", transformedMap.isEmpty());
    }
}