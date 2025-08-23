package org.apache.commons.collections4.properties;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link OrderedProperties} class.
 * This refactored test focuses on verifying exception propagation.
 */
public class OrderedPropertiesRefactoredTest {

    /**
     * Tests that an exception thrown by the mapping function in {@code computeIfAbsent}
     * is correctly propagated to the caller.
     */
    @Test
    public void computeIfAbsentShouldPropagateExceptionFromMappingFunction() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final Object key = "test.key";
        final String expectedExceptionMessage = "Simulated failure in mapping function";

        // Create a mapping function that is designed to throw an exception.
        // This simulates a scenario where the computation of a new value fails.
        final Function<Object, Object> exceptionalMappingFunction = k -> {
            throw new ArrayIndexOutOfBoundsException(expectedExceptionMessage);
        };

        // Act & Assert
        try {
            properties.computeIfAbsent(key, exceptionalMappingFunction);
            fail("Expected an ArrayIndexOutOfBoundsException to be propagated.");
        } catch (final ArrayIndexOutOfBoundsException e) {
            // Verify that the exception from the mapping function was caught.
            assertEquals(expectedExceptionMessage, e.getMessage());

            // Also, verify that the properties map remains unchanged after the failure.
            assertTrue("Properties should remain empty after a failed computation", properties.isEmpty());
        }
    }
}