package org.apache.commons.collections4.properties;

import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link OrderedProperties} class.
 * This specific test focuses on the behavior of the computeIfAbsent method.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that an exception thrown by the mapping function in computeIfAbsent
     * is correctly propagated to the caller.
     */
    @Test
    public void computeIfAbsentShouldPropagateExceptionWhenMappingFunctionThrows() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final String key = "nonExistentKey";
        // This function is designed to always throw a RuntimeException when applied.
        final Function<Object, ?> exceptionThrowingFunction = ExceptionTransformer.exceptionTransformer();

        // Act & Assert
        try {
            properties.computeIfAbsent(key, exceptionThrowingFunction);
            fail("Expected a RuntimeException to be thrown, but no exception was caught.");
        } catch (final RuntimeException e) {
            // Verify that the exception from the transformer was propagated.
            final String expectedMessage = "ExceptionTransformer invoked";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}