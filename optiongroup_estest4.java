package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Verifies that an OptionGroup correctly retains its selected state
     * even when an option is set as selected *before* being formally
     * added to the group.
     */
    @Test
    public void testAddOptionAfterBeingSetAsSelectedShouldKeepGroupSelected() throws Exception {
        // Arrange
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("v", "verbose", false, "Enable verbose mode");

        // Act: Set the option as selected on the group before adding it.
        // This simulates a scenario where the selection state is determined
        // before the group's composition is finalized.
        optionGroup.setSelected(option);
        optionGroup.addOption(option);

        // Assert
        // The group should report that an option is selected.
        assertTrue("The group should be in a selected state after the option is added.", optionGroup.isSelected());
    }
}