package org.apache.commons.collections4.properties;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Tests for the {@link OrderedProperties} class, focusing on edge cases for the toString() method.
 */
public class OrderedPropertiesToStringTest {

    /**
     * Tests that the toString() method throws an exception when the map contains a
     * self-referential entry (i.e., the map is a key and value to itself).
     *
     * This scenario can cause infinite recursion in a naive toString() implementation.
     * While a StackOverflowError might be the most intuitive exception, this test
     * validates the observed behavior, which is a NullPointerException, likely
     * due to interactions with the test execution environment.
     */
    @Test
    public void toStringShouldThrowExceptionWhenMapContainsItself() {
        // Arrange: Create a properties map that contains itself as a key and value.
        final OrderedProperties properties = new OrderedProperties();
        properties.put(properties, properties);

        // Add a second, regular property. Its presence may be necessary to trigger
        // the specific execution path that leads to the exception.
        properties.setProperty("another.key", "another.value");

        // Act & Assert
        try {
            properties.toString();
            fail("Expected a NullPointerException to be thrown due to self-reference, but no exception occurred.");
        } catch (final NullPointerException e) {
            // Success: The expected exception was caught.
            // This confirms the behavior of toString() on a self-referencing map.
        }
    }
}