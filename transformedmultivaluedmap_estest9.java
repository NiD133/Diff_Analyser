package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains an improved version of a test for TransformedMultiValuedMap.
 * The original test was auto-generated and lacked clarity.
 */
public class TransformedMultiValuedMap_ESTestTest9 extends TransformedMultiValuedMap_ESTest_scaffolding {

    /**
     * Tests that the put() method's return value correctly reflects whether the underlying
     * collection was modified.
     * <p>
     * When decorating a map that uses a Set for its values (like HashSetValuedHashMap),
     * the first put of a key-value pair should return true. A subsequent put of the
     * same pair should return false, as adding a duplicate element does not modify a Set.
     */
    @Test(timeout = 4000)
    public void putShouldReturnTrueForNewElementAndFalseForDuplicateWhenUsingSetValuedMap() {
        // Arrange
        // The underlying map uses a HashSet to store values for each key.
        final MultiValuedMap<Integer, Integer> underlyingMap = new HashSetValuedHashMap<>();

        // Use a no-op transformer, as the transformation logic is not the focus of this test.
        final Transformer<Integer, Integer> nopTransformer = NOPTransformer.nopTransformer();
        final TransformedMultiValuedMap<Integer, Integer> transformedMap =
                TransformedMultiValuedMap.transformingMap(underlyingMap, nopTransformer, nopTransformer);

        final Integer key = -1;
        final Integer value = -1;

        // Act
        // First put: adds a new value. The underlying HashSet is modified.
        final boolean firstPutResult = transformedMap.put(key, value);

        // Second put: attempts to add the same value again. The HashSet already contains it,
        // so no modification occurs.
        final boolean secondPutResult = transformedMap.put(key, value);

        // Assert
        assertTrue("The first put of a new element should modify the map and return true.", firstPutResult);
        assertFalse("The second put of a duplicate element should not modify the map and return false.", secondPutResult);
    }
}