package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TextHelpAppendable} class.
 *
 * This is an improved version of an auto-generated test case, focusing on clarity
 * and adherence to standard testing practices.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that the value set by setMaxWidth() is correctly returned by getMaxWidth().
     *
     * This test uses a negative value to confirm that the setter and getter function
     * as a simple property pair, without performing any validation on the input value,
     * which reflects the current implementation.
     */
    @Test
    public void getMaxWidthShouldReturnTheValuePreviouslySet() {
        // Arrange
        // Instantiating with a null Appendable is sufficient for testing property methods
        // and avoids any potential side effects of writing to System.out.
        TextHelpAppendable helpAppendable = new TextHelpAppendable(null);
        final int expectedWidth = -449;

        // Act
        helpAppendable.setMaxWidth(expectedWidth);
        final int actualWidth = helpAppendable.getMaxWidth();

        // Assert
        assertEquals("The retrieved max width should match the value that was set.",
                     expectedWidth, actualWidth);
    }
}