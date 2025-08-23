package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Verifies that getSelected() returns null for a newly created OptionGroup
     * that has no selected option.
     */
    @Test
    public void getSelectedShouldReturnNullForNewGroup() {
        // Arrange: Create a new, empty OptionGroup.
        OptionGroup optionGroup = new OptionGroup();

        // Act: Call getSelected() on the new group.
        String selectedOption = optionGroup.getSelected();

        // Assert: The result should be null, as no option has been set.
        assertNull("A new OptionGroup should not have a selected option.", selectedOption);
    }
}