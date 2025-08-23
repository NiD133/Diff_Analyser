package org.apache.commons.cli.help;

import org.apache.commons.cli.help.TextStyle;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that calling the resize method with a null TextStyle.Builder
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void resizeWithNullBuilderShouldThrowNullPointerException() {
        // Arrange: Create an instance of the class under test.
        // The systemOut() method is protected, so this test must be in the same package.
        TextHelpAppendable textHelpAppendable = TextHelpAppendable.systemOut();
        TextStyle.Builder nullBuilder = null;
        final double fraction = 1.0;

        // Act: Call the method with a null argument.
        // Assert: The @Test(expected) annotation handles the assertion that a
        // NullPointerException is thrown.
        textHelpAppendable.resize(nullBuilder, fraction);
    }
}