package org.jsoup.helper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for {@link Validate}.
 * This improved version focuses on clarity, modern testing practices, and separation of concerns.
 */
class ValidateTest {

    @Test
    void ensureNotNull_withNonNullObject_returnsSameObject() {
        // Given: A non-null object
        Object obj = new Object();

        // When: The object is passed to ensureNotNull
        Object result = Validate.ensureNotNull(obj);

        // Then: The same object instance is returned
        assertSame(obj, result, "The method should return the same object that was passed in.");
    }

    @Test
    void ensureNotNull_withNullObject_throwsValidationException() {
        // When: ensureNotNull is called with a null input
        // Then: A ValidationException should be thrown
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.ensureNotNull(null);
        }, "A ValidationException should be thrown for a null input.");

        // And: The exception message should be correct
        assertEquals("Object must not be null", exception.getMessage());
    }
}