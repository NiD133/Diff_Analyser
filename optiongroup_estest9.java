package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link OptionGroup} class, focusing on exception handling.
 */
public class OptionGroupTest {

    /**
     * Tests that attempting to select a second option in a group where one is
     * already selected throws an AlreadySelectedException.
     */
    @Test
    public void setSelected_whenAnOptionIsAlreadySelected_shouldThrowAlreadySelectedException() {
        // Arrange: Create an option group and two options.
        // Then, select the first option to set the initial state.
        OptionGroup optionGroup = new OptionGroup();
        Option firstOption = new Option("a", "first-option", false, "The first option");
        Option secondOption = new Option("b", "second-option", false, "The second option");

        try {
            optionGroup.setSelected(firstOption);
        } catch (AlreadySelectedException e) {
            fail("Setup failed: Selecting the first option should not have thrown an exception.");
        }

        // Act & Assert: Attempt to select the second option and verify that the
        // correct exception is thrown with a descriptive message.
        try {
            optionGroup.setSelected(secondOption);
            fail("Expected an AlreadySelectedException to be thrown, but it was not.");
        } catch (AlreadySelectedException e) {
            // Verify the exception message clearly identifies the conflicting options.
            String expectedMessage = "The option 'b' was specified but an option from this group has already been selected: 'a'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}