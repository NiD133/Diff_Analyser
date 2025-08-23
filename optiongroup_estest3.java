package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Tests that getSelected() correctly returns the name of the selected option,
     * even when that name is an empty string.
     */
    @Test
    public void getSelectedShouldReturnEmptyStringForOptionWithEmptyName() throws AlreadySelectedException {
        // Arrange: Create an OptionGroup and an Option with an empty string as its long name.
        // The long name is used as the option's primary identifier in this context.
        OptionGroup optionGroup = new OptionGroup();
        Option optionWithEmptyName = new Option(null, "", false, "An option with an empty name");

        // Act: Select the option within the group.
        optionGroup.setSelected(optionWithEmptyName);
        String selectedOptionName = optionGroup.getSelected();

        // Assert: Verify that the retrieved name is the expected empty string.
        assertEquals("The selected option name should be an empty string", "", selectedOptionName);
    }
}