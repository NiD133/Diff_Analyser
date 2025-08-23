package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Verifies that a newly instantiated OptionGroup is not required by default.
     */
    @Test
    public void newOptionGroupShouldNotBeRequired() {
        // Arrange: Create a new OptionGroup instance.
        OptionGroup optionGroup = new OptionGroup();

        // Act: Check if the group is required.
        boolean isRequired = optionGroup.isRequired();

        // Assert: The group should not be required.
        assertFalse("A new OptionGroup should not be required by default.", isRequired);
    }
}