package org.apache.commons.cli;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests to verify that the static OptionBuilder's internal state is reset
 * after every creation attempt, regardless of success or failure.
 *
 * Note: The class under test, OptionBuilder, uses a static singleton pattern,
 * which is generally discouraged as it creates hidden dependencies and makes
 * testing difficult. These tests work around this by verifying the reset
*  mechanism on each call.
 */
@DisplayName("OptionBuilder State Reset")
class OptionBuilderTest {

    @Test
    @DisplayName("State should be reset after a creation attempt with an invalid short name fails")
    void builderShouldBeResetAfterFailingToCreateWithInvalidShortName() {
        // Arrange: Set a description, making the builder's internal state "dirty".
        OptionBuilder.withDescription("This description should be cleared");

        // Act & Assert: Attempt to create an option with an invalid character ('"').
        // This is expected to throw an exception, and the builder's state should be reset.
        assertThrows(IllegalArgumentException.class,
            () -> OptionBuilder.create('"'),
            "Creation with an invalid character should throw an exception.");

        // Assert: Verify that the builder's state was indeed reset. A subsequent,
        // valid option should not retain the description from the failed attempt.
        Option subsequentOption = OptionBuilder.create('x');
        assertNull(subsequentOption.getDescription(),
            "Description should be null, proving the builder was reset after the failed creation.");
    }

    @Test
    @DisplayName("State should be reset after a creation attempt with no option name fails")
    void builderShouldBeResetAfterFailingToCreateWithNoName() {
        // Arrange: Set a description to create a "dirty" state.
        OptionBuilder.withDescription("This description should also be cleared");

        // Act & Assert: Attempt to create an option without providing a name.
        // This is expected to throw an exception, and the builder's state should be reset.
        assertThrows(IllegalStateException.class,
            OptionBuilder::create,
            "Creation without a name should throw an exception.");

        // Assert: Verify the builder's state was reset by creating another valid option
        // and checking that it doesn't have the old description.
        Option subsequentOption = OptionBuilder.create('y');
        assertNull(subsequentOption.getDescription(),
            "Description should be null, proving the builder was reset after the failed creation.");
    }
}