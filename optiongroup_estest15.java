package org.apache.commons.cli;

import org.junit.Test;
import java.util.Collection;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link OptionGroup} class.
 */
public class OptionGroupTest {

    /**
     * Verifies that calling getNames() on a newly created, empty OptionGroup
     * returns a non-null, empty collection.
     */
    @Test
    public void getNamesShouldReturnEmptyCollectionForNewGroup() {
        // Arrange: Create a new OptionGroup with no options added.
        OptionGroup optionGroup = new OptionGroup();

        // Act: Retrieve the collection of option names.
        Collection<String> names = optionGroup.getNames();

        // Assert: The returned collection must exist but be empty.
        assertNotNull("The names collection should never be null.", names);
        assertTrue("A new OptionGroup should have no names.", names.isEmpty());
    }
}