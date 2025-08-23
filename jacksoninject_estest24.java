package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the {@link JacksonInject.Value} class,
 * specifically its "mutant factory" methods like {@code with...()}.
 */
public class JacksonInjectValueTest {

    /**
     * Verifies that the {@code withUseInput()} method returns the same instance
     * if the provided value is identical to the existing one.
     * This behavior is an important optimization to avoid creating unnecessary objects.
     */
    @Test
    public void withUseInput_whenValueIsUnchanged_shouldReturnSameInstance() {
        // Arrange
        Object injectionId = new Object();
        Boolean useInputFlag = false; // The original test used `new Boolean("")`, which evaluates to false.
        JacksonInject.Value initialValue = new JacksonInject.Value(injectionId, useInputFlag, useInputFlag);

        // Act
        // Call the method with the same value it was constructed with.
        JacksonInject.Value resultValue = initialValue.withUseInput(useInputFlag);

        // Assert
        // The method should return the original instance, not a new one.
        assertSame("Calling withUseInput() with the same value should return the same instance",
                initialValue, resultValue);

        // A secondary check to ensure the returned object is in a valid state.
        assertTrue("The returned instance should still have a valid ID", resultValue.hasId());
    }
}