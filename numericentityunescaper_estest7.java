package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Tests that the translate method throws a NullPointerException
     * when the input CharSequence is null.
     */
    @Test(expected = NullPointerException.class)
    public void translateShouldThrowNullPointerExceptionForNullInput() {
        // Arrange
        // The specific unescaping options do not affect the outcome of a null-input check.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final Writer writer = new StringWriter();
        final int anyIndex = 0; // The index is irrelevant when the input is null.

        // Act & Assert
        // This call is expected to throw a NullPointerException.
        unescaper.translate(null, anyIndex, writer);
    }
}