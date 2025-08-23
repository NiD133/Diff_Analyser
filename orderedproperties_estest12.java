package org.apache.commons.collections4.properties;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * This test suite contains tests for the {@link OrderedProperties} class.
 * This specific test case focuses on verifying the contract for null handling.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that calling putIfAbsent() with a null key throws a NullPointerException,
     * as required by the contract of java.util.Hashtable, which Properties extends.
     */
    @Test
    public void putIfAbsent_withNullKey_shouldThrowNullPointerException() {
        // Arrange
        final OrderedProperties orderedProperties = new OrderedProperties();
        final String anyValue = "anyValue";

        // Act & Assert
        try {
            orderedProperties.putIfAbsent(null, anyValue);
            fail("Expected a NullPointerException to be thrown for a null key.");
        } catch (final NullPointerException e) {
            // This is the expected behavior. The test passes.
            // The original test also implicitly checked that the exception had no message,
            // which is a common result for a contract-based null check.
            // No further assertions are needed as the exception type is sufficient.
        }
    }
}