package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Verifies that calling getDescription() on a formatter created with a null Option
     * throws a NullPointerException, as the internal option object is null.
     */
    @Test(expected = NullPointerException.class)
    public void getDescriptionShouldThrowNullPointerExceptionWhenCreatedWithNullOption() {
        // Arrange: Create a formatter from a null Option object.
        // This is a valid, though unusual, state for the formatter.
        OptionFormatter formatter = OptionFormatter.from(null);

        // Act: Attempt to get the description. This should fail because it
        // will try to access methods on the null internal Option object.
        formatter.getDescription();

        // Assert: The @Test(expected) annotation automatically verifies that a
        // NullPointerException was thrown. The test fails if no exception or a
        // different exception is thrown.
    }
}