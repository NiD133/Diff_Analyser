package org.apache.commons.cli;

import org.junit.Test;
import java.util.Collection;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    @Test
    public void getOptionsOnNewGroupShouldReturnEmptyCollection() {
        // Arrange: Create a new, empty OptionGroup.
        OptionGroup optionGroup = new OptionGroup();

        // Act: Retrieve the collection of options.
        Collection<Option> options = optionGroup.getOptions();

        // Assert: The collection should be non-null and empty.
        assertNotNull("The options collection should never be null.", options);
        assertTrue("A new OptionGroup should have no options.", options.isEmpty());
    }
}