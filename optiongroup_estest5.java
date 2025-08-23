package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * This test verifies that after calling setRequired(true), the isRequired() method
     * correctly returns true.
     */
    @Test
    public void shouldBeRequiredWhenSetRequiredIsTrue() {
        // Arrange: Create a new OptionGroup. By default, it is not required.
        OptionGroup optionGroup = new OptionGroup();

        // Act: Mark the OptionGroup as required.
        optionGroup.setRequired(true);

        // Assert: Verify that the group is now marked as required.
        assertTrue("The option group should be marked as required.", optionGroup.isRequired());
    }
}