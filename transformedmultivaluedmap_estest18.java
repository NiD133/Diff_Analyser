package org.apache.commons.collections4.multimap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

/**
 * Contains tests for the {@link TransformedMultiValuedMap} class, focusing on
 * exception handling for invalid arguments.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that calling putAll(MultiValuedMap) with a null argument
     * throws a NullPointerException as expected.
     */
    @Test
    public void testPutAllWithNullMultiValuedMapShouldThrowNullPointerException() {
        // Arrange: Create a TransformedMultiValuedMap instance.
        // The actual transformers do not matter for this test, so we use a no-op transformer.
        final MultiValuedMap<Integer, Integer> backingMap = new ArrayListValuedLinkedHashMap<>();
        final Transformer<Integer, Integer> identityTransformer = NOPTransformer.nopTransformer();
        final TransformedMultiValuedMap<Integer, Integer> transformedMap =
                TransformedMultiValuedMap.transformedMap(backingMap, identityTransformer, identityTransformer);

        // Act & Assert: Attempt to put a null map and verify the exception.
        try {
            // The cast is necessary to resolve ambiguity between putAll(Map) and putAll(MultiValuedMap).
            transformedMap.putAll((MultiValuedMap<? extends Integer, ? extends Integer>) null);
            fail("Expected a NullPointerException to be thrown when the input map is null.");
        } catch (final NullPointerException e) {
            // The underlying implementation uses Objects.requireNonNull(map, "map"),
            // so we expect the message to be "map".
            assertEquals("map", e.getMessage());
        }
    }
}