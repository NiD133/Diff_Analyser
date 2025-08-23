package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on the behavior of the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnoreProperties_ESTestTest58 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that merging a {@link JsonIgnoreProperties.Value} instance with a null override
     * returns the original base instance.
     */
    @Test
    public void mergeWithNullOverridesShouldReturnBaseValue() {
        // Arrange: Create a base Value instance.
        // The 'empty' instance has its 'merge' property set to true by default.
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.empty();

        // Act: Merge the base value with a null override.
        JsonIgnoreProperties.Value result = JsonIgnoreProperties.Value.merge(baseValue, null);

        // Assert: The result should be the original base value instance.
        // This is a more specific and stronger assertion than just checking a property.
        assertSame("Merging with a null override should return the base instance itself.", baseValue, result);

        // The original test asserted this property, which is a correct consequence
        // of returning the base instance. We include it for completeness.
        assertTrue("The 'merge' property of the result should remain true.", result.getMerge());
    }
}