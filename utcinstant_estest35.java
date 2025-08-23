package org.threeten.extra.scale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test suite for the {@link UtcInstant} class.
 */
public class UtcInstantTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that calling {@code UtcInstant.parse()} with a null input
     * throws a {@code NullPointerException} with a specific message.
     * <p>
     * The method contract requires a non-null input, typically enforced
     * by {@code Objects.requireNonNull}.
     */
    @Test
    public void parse_withNullInput_shouldThrowNullPointerException() {
        // Arrange: Define the expected exception type and message.
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("text");

        // Act: Call the method under test with invalid input.
        UtcInstant.parse(null);

        // Assert: The ExpectedException rule handles the assertion.
        // If the expected exception is not thrown, the test will fail.
    }
}