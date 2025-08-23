package org.apache.commons.cli;

import org.junit.Test;

/**
 * Unit tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Verifies that attempting to add a null Option to an OptionGroup
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void addOption_whenOptionIsNull_shouldThrowNullPointerException() {
        // Arrange: Create a new OptionGroup instance.
        OptionGroup optionGroup = new OptionGroup();

        // Act: Attempt to add a null option.
        // The @Test(expected) annotation will handle the assertion.
        optionGroup.addOption(null);
    }
}