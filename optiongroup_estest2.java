package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite contains an improved version of an auto-generated test case
 * for the {@link OptionGroup} class.
 *
 * Note: The original class name and inheritance from EvoSuite scaffolding are
 * preserved to show a direct comparison. In a typical project, this class
 * would be named {@code OptionGroupTest}.
 */
public class OptionGroup_ESTestTest2 extends OptionGroup_ESTest_scaffolding {

    /**
     * Verifies that getSelected() correctly returns the name of the option
     * that was explicitly set using setSelected().
     *
     * This test improves upon the original by using descriptive names for the
     * test method, variables, and test data, making the test's purpose clear.
     */
    @Test(timeout = 4000)
    public void getSelectedShouldReturnNameOfPreviouslySetOption() throws AlreadySelectedException {
        // Arrange: Set up the test objects and preconditions.
        final OptionGroup optionGroup = new OptionGroup();
        // Use a meaningful option to represent a realistic use case.
        final Option fileOption = new Option("f", "file", true, "The file to be processed.");

        // Although not strictly required by the setSelected() method's implementation,
        // adding the option to the group first makes the test scenario more
        // intuitive and representative of typical usage.
        optionGroup.addOption(fileOption);

        // Act: Execute the method under test.
        optionGroup.setSelected(fileOption);
        final String selectedOptionName = optionGroup.getSelected();

        // Assert: Verify the outcome.
        // The "name" of an Option is its short identifier ("f" in this case).
        final String expectedOptionName = "f";
        assertEquals("The name of the selected option should be returned.", expectedOptionName, selectedOptionName);
    }
}