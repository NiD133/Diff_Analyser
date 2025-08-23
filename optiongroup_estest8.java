package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link OptionGroup} class, focusing on its string representation.
 */
public class OptionGroupToStringTest {

    /**
     * Verifies that the toString() method correctly formats a string representation
     * of all the options contained within the group.
     */
    @Test
    public void toStringShouldReturnFormattedStringOfAllOptions() {
        // Arrange
        // An option with a null long name is represented as "--null".
        Option optionWithNullLongName = new Option(null, "description for null option");

        // An option with only a short name is represented as "-<shortName>".
        Option optionWithOnlyShortName = new Option("vN", false, "description for short option");

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(optionWithNullLongName);
        optionGroup.addOption(optionWithOnlyShortName);

        String expectedString = "[--null, -vN ]";

        // Act
        String actualString = optionGroup.toString();

        // Assert
        assertEquals("The string representation of the option group is incorrect.",
                expectedString, actualString);
    }
}