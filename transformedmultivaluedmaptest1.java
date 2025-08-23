package org.apache.commons.collections4.multimap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

public class TransformedMultiValuedMapTestTest1<K, V> extends AbstractMultiValuedMapTest<K, V> {

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    @Override
    public MultiValuedMap<K, V> makeObject() {
        return TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(),
                                                         TransformerUtils.<K>nopTransformer(),
                                                         TransformerUtils.<V>nopTransformer());
    }

    /**
     * Tests that the {@code transformingMap} factory method does not transform existing
     * elements, but does transform new elements added to the decorated map.
     */
    @Test
    void testTransformingMap_decoratesButDoesNotTransformExistingElements() {
        // Arrange: Create a base map.
        // The values will be a mix of String and Integer, so we use Object as the value type.
        final MultiValuedMap<String, Object> originalMap = new ArrayListValuedHashMap<>();
        originalMap.put("A", "1");
        originalMap.put("B", "2");
        originalMap.put("C", "3");

        // This transformer converts a String value to an Integer.
        // A cast is required because the transformer changes the value type from String to Integer,
        // which is not captured by the map's generic type parameter V (Object in this case).
        @SuppressWarnings("unchecked")
        final Transformer<Object, Object> valueTransformer =
                (Transformer<Object, Object>) (Transformer<?, ?>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // Act: Decorate the original map using the `transformingMap` factory.
        // This factory method should not modify the existing elements.
        final MultiValuedMap<String, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(originalMap, null, valueTransformer);

        // Assert: Verify that the original elements are untouched.
        assertEquals(3, transformedMap.size(), "Size should be unchanged after decoration");
        assertTrue(transformedMap.get("A").contains("1"), "Existing value for key 'A' should remain a String");
        assertTrue(transformedMap.get("B").contains("2"), "Existing value for key 'B' should remain a String");
        assertTrue(transformedMap.get("C").contains("3"), "Existing value for key 'C' should remain a String");

        // Act: Add a new element to the transformed map. This element should be transformed.
        transformedMap.put("D", "4");

        // Assert: Verify the new element was transformed from String "4" to Integer 4.
        assertEquals(4, transformedMap.size(), "Size should increase after adding a new element");
        assertTrue(transformedMap.get("D").contains(4), "New value for key 'D' should be transformed to an Integer");
        assertFalse(transformedMap.get("D").contains("4"), "The original String value '4' should not be present");
    }
}