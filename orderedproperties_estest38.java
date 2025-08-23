package org.apache.commons.collections4.properties;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.junit.Test;

import java.util.Enumeration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link OrderedProperties}.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that computeIfAbsent propagates exceptions from the mapping function.
     * Specifically, it verifies that using a CloneTransformer with a non-cloneable key
     * results in an IllegalArgumentException.
     */
    @Test
    public void computeIfAbsentWithCloneTransformerShouldThrowExceptionForNonCloneableKey() {
        // Arrange: Create properties, a non-cloneable key, and a transformer that requires a cloneable input.
        final OrderedProperties properties = new OrderedProperties();
        
        // The key must not be present in the map for the mapping function (transformer) to be called.
        // An Enumeration is a good example of a non-cloneable object.
        final Enumeration<Object> nonCloneableKey = properties.keys();
        
        // The CloneTransformer will attempt to clone its input when invoked.
        final Transformer<Object, Object> cloneTransformer = CloneTransformer.cloneTransformer();

        // Act & Assert: Expect an IllegalArgumentException because the key cannot be cloned.
        try {
            properties.computeIfAbsent(nonCloneableKey, cloneTransformer);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception is the one thrown by the cloning logic.
            final String expectedMessage = "The prototype must be cloneable via a public clone method";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}