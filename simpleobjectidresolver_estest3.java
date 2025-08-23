package com.fasterxml.jackson.annotation;

import org.junit.Test;

/**
 * Tests for {@link SimpleObjectIdResolver}.
 */
public class SimpleObjectIdResolverTest {

    /**
     * This test verifies the behavior when attempting to bind an ID that has already been bound.
     * Specifically, it tests the edge case where the ID is {@code null}.
     * <p>
     * The expected {@link NullPointerException} is a side-effect of the internal error-reporting
     * logic, which attempts to access properties of the ID object to create a detailed
     * error message. When the ID is null, this access results in an NPE.
     */
    @Test(expected = NullPointerException.class)
    public void bindItem_whenRebindingExistingNullId_shouldThrowNullPointerException() {
        // Arrange
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object initialObject = new Object();
        Object secondObject = new Object();

        // Bind the null ID to an initial object. This should succeed.
        resolver.bindItem(null, initialObject);

        // Act
        // Now, attempt to bind the same null ID to a different object.
        // This action is expected to fail.
        resolver.bindItem(null, secondObject);

        // Assert
        // The test expects a NullPointerException, which is declared in the @Test annotation.
        // If no exception is thrown, the test will fail automatically.
    }
}