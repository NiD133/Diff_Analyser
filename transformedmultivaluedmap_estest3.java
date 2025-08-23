package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link TransformedMultiValuedMap} class.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that the {@code transformingMap} factory method does not apply the
     * provided transformers to the elements already present in the decorated map.
     *
     * <p>This test improves upon an auto-generated test by making the intent explicit.
     * Instead of using an obscure transformer that would fail implicitly, this test
     * uses transformers that explicitly call {@code fail()} if invoked. This clearly
     * demonstrates that the transformers are not called on the pre-existing map data.</p>
     */
    @Test
    public void transformingMapShouldNotApplyTransformersToExistingElements() {
        // Arrange: Create a map with pre-existing data.
        final MultiValuedMap<String, Integer> originalMap = new ArrayListValuedHashMap<>();
        originalMap.put("ONE", 1);

        // Arrange: Create transformers that will fail the test if they are ever executed.
        // This is the core of the test: to prove these are not called on existing elements.
        final Transformer<String, String> failingKeyTransformer = key -> {
            fail("The key transformer should not be called on existing elements when using transformingMap().");
            return "unreachable"; // Return value is required by the Transformer interface.
        };

        final Transformer<Integer, Integer> failingValueTransformer = value -> {
            fail("The value transformer should not be called on existing elements when using transformingMap().");
            return -1; // Return value is required by the Transformer interface.
        };

        // Act: Decorate the map using the transformingMap() factory method.
        // This method is documented to NOT transform existing elements.
        final MultiValuedMap<String, Integer> transformedMap = TransformedMultiValuedMap.transformingMap(
                originalMap, failingKeyTransformer, failingValueTransformer);

        // Assert: Verify that the map still contains the original, untransformed data.
        assertEquals("The size of the map should remain unchanged.", 1, transformedMap.size());
        assertTrue("The map should contain the original, untransformed key-value pair.",
                   transformedMap.containsMapping("ONE", 1));
    }
}