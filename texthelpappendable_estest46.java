package org.apache.commons.cli.help;

import org.junit.Test;

/**
 * Tests for the static utility methods in {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that indexOfWrap throws a StringIndexOutOfBoundsException when the
     * start position is negative, as a negative index is invalid.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void indexOfWrapShouldThrowExceptionForNegativeStartPosition() {
        // Call the method with a valid string and width, but an invalid negative start position.
        TextHelpAppendable.indexOfWrap("some text", 10, -1);
    }
}