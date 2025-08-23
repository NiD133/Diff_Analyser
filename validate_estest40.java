package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link Validate} class.
 */
public class ValidateTest {

    /**
     * Verifies that {@link Validate#ensureNotNull(Object)} returns the same object
     * instance when the input is not null. This confirms the expected behavior for
     * valid, non-null arguments.
     */
    @Test
    public void ensureNotNull_whenObjectIsNotNull_shouldReturnSameObject() {
        // Arrange: Create a non-null object.
        Object inputObject = new Object();

        // Act: Call the method under test.
        Object resultObject = Validate.ensureNotNull(inputObject);

        // Assert: The returned object should be the exact same instance as the input.
        assertSame("The method should return the same object instance that was passed in.", inputObject, resultObject);
    }
}