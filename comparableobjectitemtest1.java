package org.jfree.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for the {@link ComparableObjectItem} class.
 */
class ComparableObjectItemTest {

    /**
     * Verifies that the constructor correctly stores valid arguments.
     * This test covers the "happy path" scenario.
     */
    @Test
    @DisplayName("Constructor should store valid arguments")
    void constructorShouldStoreArgumentsCorrectly() {
        // Arrange
        Comparable<String> comparable = "TestKey";
        Object obj = "TestValue";

        // Act
        ComparableObjectItem item = new ComparableObjectItem(comparable, obj);

        // Assert
        assertSame(comparable, item.getComparable(), "The 'comparable' field should be the same instance.");
        assertSame(obj, item.getObject(), "The 'object' field should be the same instance.");
    }

    /**
     * Verifies that the constructor accepts a null 'object' argument,
     * as this is permitted behavior.
     */
    @Test
    @DisplayName("Constructor should accept a null object")
    void constructorShouldAcceptNullObject() {
        // Arrange
        Comparable<String> comparable = "TestKey";

        // Act
        ComparableObjectItem item = new ComparableObjectItem(comparable, null);

        // Assert
        assertSame(comparable, item.getComparable(), "The 'comparable' field should be correctly set.");
        assertNull(item.getObject(), "The 'object' field should be null as provided.");
    }

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * 'comparable' argument is null, as it is a required field.
     */
    @Test
    @DisplayName("Constructor should throw exception for null comparable")
    void constructorShouldThrowExceptionForNullComparable() {
        // The constructor should not accept a null 'comparable' argument.
        // The 'Args.nullNotPermitted' helper is expected to throw this exception.
        assertThrows(IllegalArgumentException.class, () -> {
            new ComparableObjectItem(null, "TestValue");
        }, "A null 'comparable' argument should be rejected.");
    }
}