package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    @Test
    public void isRequiredShouldReturnTrueWhenGroupIsSetAsRequired() {
        // Arrange: Create a new OptionGroup instance.
        OptionGroup optionGroup = new OptionGroup();

        // Act: Set the option group to be required.
        optionGroup.setRequired(true);

        // Assert: Verify that the isRequired() method now returns true.
        assertTrue("The option group should be marked as required after being set.", optionGroup.isRequired());
    }
}