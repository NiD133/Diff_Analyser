package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link Validate#ensureNotNull(Object, String, Object...)} method.
 */
public class ValidateTest {

    /**
     * Verifies that ensureNotNull returns the same object instance
     * when the input object is not null.
     */
    @Test
    public void ensureNotNull_withNonNullObject_returnsSameObject() {
        // Arrange: Create a non-null object to be validated.
        // The message and its arguments are irrelevant for this "happy path" test.
        String inputObject = "This is a valid, non-null string";
        String messageForException = "This message should not be used";

        // Act: Call the method under test.
        Object returnedObject = Validate.ensureNotNull(inputObject, messageForException);

        // Assert: Verify that the method returned the exact same object instance.
        assertSame("The method should return the identical object that was passed in.", inputObject, returnedObject);
    }
}