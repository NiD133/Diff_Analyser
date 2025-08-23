package org.jsoup.helper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test suite for {@link Validate}.
 */
public class ValidateTest {

    // A JUnit Rule that allows for declaratively testing for expected exceptions.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void notNullShouldThrowIllegalArgumentExceptionForNullInput() {
        // Arrange: We expect an IllegalArgumentException with a specific message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Object must not be null");

        // Act: Call the method under test with a null argument.
        Validate.notNull(null);

        // Assert: The test passes if the expected exception is thrown.
        // The ExpectedException rule handles this verification automatically.
    }
}