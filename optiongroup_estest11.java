package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Verifies that setting the selected option to null does not inadvertently
     * change the 'required' status of the OptionGroup. The 'required' status
     * should remain at its default value of false.
     */
    @Test
    public void setSelectedWithNullShouldNotAffectRequiredProperty() {
        // Arrange: Create a new OptionGroup. By default, it is not required.
        OptionGroup optionGroup = new OptionGroup();

        // Act: Set the selected option to null. This is an edge case that
        // should be handled gracefully without side effects on other properties.
        optionGroup.setSelected(null);

        // Assert: The 'required' status of the group should remain unchanged (i.e., false).
        assertFalse("The 'required' property should not change after setSelected(null) is called",
                    optionGroup.isRequired());
    }
}