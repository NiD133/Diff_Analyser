package org.apache.commons.collections4.multimap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TransformedMultiValuedMap}.
 * This class also runs the abstract test suite.
 *
 * @param <K> the type of the keys in the map under test
 * @param <V> the type of the values in the map under test
 */
public class TransformedMultiValuedMapTest<K, V> extends AbstractMultiValuedMapTest<K, V> {

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    /**
     * Creates a map with no-op transformers for the abstract test suite.
     */
    @Override
    public MultiValuedMap<K, V> makeObject() {
        return TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(),
            TransformerUtils.<K>nopTransformer(), TransformerUtils.<V>nopTransformer());
    }

    /**
     * Tests that keys are transformed on 'put' and that subsequent access
     * must use the transformed key.
     * <p>
     * This test specifically verifies the String-to-Integer transformation use case
     * mentioned in the class Javadoc. This scenario is not strictly type-safe and
     * relies on type erasure, hence the necessary unchecked casts.
     * </p>
     */
    @Test
    @SuppressWarnings("unchecked")
    void testKeyTransformationOnPutAndAccess() {
        // Arrange
        final String[] stringInputs = {"1", "3", "5", "7", "2", "4", "6"};

        // The map is created to transform String keys into Integer keys.
        // The casts are necessary because this test is inside a generic class <K, V>
        // but specifically tests a String -> Integer transformation.
        final Transformer<String, Integer> stringToIntegerTransformer =
            TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;
        final MultiValuedMap<K, V> transformedMap = TransformedMultiValuedMap.transformingMap(
            new ArrayListValuedHashMap<>(),
            (Transformer<? super K, ? extends K>) stringToIntegerTransformer,
            null // No value transformer
        );

        assertEquals(0, transformedMap.size(), "Initially, the map should be empty.");

        // Act & Assert: Test that 'put' transforms keys correctly
        for (int i = 0; i < stringInputs.length; i++) {
            final String originalKey = stringInputs[i];
            final V value = (V) stringInputs[i]; // The value is also the string
            final Integer transformedKey = Integer.valueOf(originalKey);

            // Act: Put an entry with a String key.
            transformedMap.put((K) originalKey, value);

            // Assert: The map's state reflects the transformation.
            assertEquals(i + 1, transformedMap.size(), "Map size should increment after each put.");
            assertTrue(transformedMap.containsKey(transformedKey), "Map should contain the transformed (Integer) key.");
            assertFalse(transformedMap.containsKey(originalKey), "Map should not contain the original (String) key.");
            assertTrue(transformedMap.containsValue(value), "Map should contain the untransformed value.");
            assertTrue(transformedMap.get((K) transformedKey).contains(value), "Value should be retrievable with the transformed key.");
        }

        // Arrange for remove tests
        final String keyToRemoveOriginal = stringInputs[0]; // "1"
        final Integer keyToRemoveTransformed = Integer.valueOf(keyToRemoveOriginal);
        final V valueOfRemovedKey = (V) stringInputs[0];

        // Act & Assert: Attempting to remove using the original key should fail.
        // The remove(Object) method is not decorated and passes the key to the
        // underlying map, which contains Integer keys. A String key will not be found.
        // Per the MultiValuedMap contract, remove() returns null for a non-existent key.
        final Collection<V> removedWithOriginalKey = transformedMap.remove(keyToRemoveOriginal);
        assertNull(removedWithOriginalKey, "Removing with the original key should return null as the key is not found.");

        // Act & Assert: Removing with the transformed key should succeed.
        final Collection<V> removedWithTransformedKey = transformedMap.remove(keyToRemoveTransformed);
        assertTrue(removedWithTransformedKey.contains(valueOfRemovedKey),
            "Removing with the transformed key should return the associated value.");
    }
}