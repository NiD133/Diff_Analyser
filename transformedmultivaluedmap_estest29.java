package org.apache.commons.collections4.multimap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

/**
 * Unit tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that the constructor throws a NullPointerException when the decorated map is null.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionWhenMapIsNull() {
        // Arrange: Define transformers. Their specific logic is irrelevant for this test,
        // as the constructor should fail before they are ever used.
        final Transformer<Object, Object> keyTransformer = null; // A null transformer is allowed.
        final Transformer<Object, Object> valueTransformer = NOPTransformer.nopTransformer();

        try {
            // Act: Attempt to create a TransformedMultiValuedMap with a null decorated map.
            new TransformedMultiValuedMap<>(null, keyTransformer, valueTransformer);
            fail("A NullPointerException was expected but not thrown.");
        } catch (final NullPointerException e) {
            // Assert: Verify that the exception is thrown for the correct reason.
            // The superclass constructor is responsible for this null check.
            assertEquals("The exception message should indicate the 'map' parameter is null.", "map", e.getMessage());
        }
    }
}