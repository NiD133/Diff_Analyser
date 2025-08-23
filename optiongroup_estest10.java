package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Tests that calling setSelected() multiple times with the same option
     * does not throw an exception and remains idempotent.
     */
    @Test
    public void setSelectedShouldNotThrowExceptionWhenCalledMultipleTimesWithSameOption() throws AlreadySelectedException {
        // Arrange: Create an option group and an option to be selected.
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("s", "select", false, "The selected option");

        // Act: Select the same option twice.
        // The first call sets the selected option.
        optionGroup.setSelected(option);
        
        // The second call with the same option should be a no-op and not throw an exception.
        optionGroup.setSelected(option);

        // Assert: Verify that the option is still correctly selected.
        // The primary test is that the second call above did not throw an AlreadySelectedException.
        assertEquals("The selected option name should be 's'", "s", optionGroup.getSelected());
    }
}