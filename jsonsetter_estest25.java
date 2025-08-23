package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link JsonSetter.Value} class.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that the {@code readResolve()} method, used during deserialization,
     * correctly returns the singleton {@code EMPTY} instance.
     * <p>
     * This ensures that the properties of the singleton, such as having default
     * null-handling settings, are preserved. The test confirms this by checking
     * that {@code nonDefaultContentNulls()} returns {@code null}, which is the
     * expected behavior for the default configuration.
     */
    @Test
    public void readResolveOnEmptyInstanceShouldReturnSingletonWithDefaultSettings() {
        // Arrange
        final JsonSetter.Value emptyInstance = JsonSetter.Value.EMPTY;

        // Act
        // The readResolve() method is intended to return the canonical representation
        // of an object during deserialization. For EMPTY, it should be the singleton itself.
        final JsonSetter.Value resolvedInstance = (JsonSetter.Value) emptyInstance.readResolve();

        // Assert
        // 1. The resolved object should be the exact same instance as the EMPTY singleton.
        assertSame("readResolve() should return the singleton EMPTY instance.",
                emptyInstance, resolvedInstance);

        // 2. As a consequence, its properties should reflect the default settings.
        // For the default EMPTY instance, contentNulls is Nulls.DEFAULT, so
        // nonDefaultContentNulls() is specified to return null.
        assertNull("nonDefaultContentNulls() should be null for the default EMPTY instance.",
                resolvedInstance.nonDefaultContentNulls());
    }
}