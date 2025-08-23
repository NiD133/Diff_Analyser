package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Verifies that a newly created, empty OptionGroup is not considered selected by default.
     */
    @Test
    public void testIsSelectedReturnsFalseForNewGroup() {
        // Arrange: Create a new, empty OptionGroup.
        OptionGroup optionGroup = new OptionGroup();

        // Act: Check if the group has a selected option.
        boolean isSelected = optionGroup.isSelected();

        // Assert: The group should not be selected.
        assertFalse("A new OptionGroup should not be selected by default", isSelected);
    }
}