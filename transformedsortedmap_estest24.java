package org.apache.commons.collections4.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * This test suite focuses on the constructor behavior of TransformedSortedMap.
 * The original test was auto-generated and has been rewritten for clarity.
 */
// The original test class name and inheritance are preserved for context.
// In a real-world scenario, this might be renamed to e.g., TransformedSortedMapConstructorTest.
public class TransformedSortedMap_ESTestTest24 extends TransformedSortedMap_ESTest_scaffolding {

    /**
     * Tests that the TransformedSortedMap constructor throws a NullPointerException
     * when the decorated map provided is null.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionWhenMapIsNull() {
        try {
            // Attempt to create a TransformedSortedMap with a null map argument.
            // The key and value transformers can be null, but the map itself cannot.
            new TransformedSortedMap<Object, Object>(null, null, null);

            // If the constructor does not throw an exception, this line will be reached,
            // and the test should fail to indicate an issue.
            fail("Expected a NullPointerException to be thrown for a null map.");
        } catch (final NullPointerException e) {
            // This block is expected to be executed.
            // We verify that the exception message clearly indicates that the 'map' parameter was the cause.
            // This behavior is inherited from the AbstractMapDecorator superclass, which uses
            // Objects.requireNonNull(map, "map").
            assertEquals("map", e.getMessage());
        }
    }
}