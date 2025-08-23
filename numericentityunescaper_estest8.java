package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Tests that the translate method throws an IndexOutOfBoundsException when the
     * starting index is outside the bounds of the input CharSequence.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void translateShouldThrowIndexOutOfBoundsExceptionWhenIndexIsOutOfBounds() throws IOException {
        // Arrange
        // An unescaper with default options is sufficient for this test.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "test input";
        final Writer writer = new StringWriter();

        // An index equal to the string's length is, by definition, out of bounds.
        // Valid indices for "test input" (length 10) are 0 through 9.
        final int outOfBoundsIndex = input.length();

        // Act
        // This call should trigger the exception because the index is invalid.
        unescaper.translate(input, outOfBoundsIndex, writer);

        // Assert
        // The test framework asserts that an IndexOutOfBoundsException is thrown via
        // the @Test(expected=...) annotation.
    }
}