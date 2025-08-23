package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Font;

/**
 * Contains tests for the equals() method in the {@link LabelBlock} class,
 * focusing on handling of null properties.
 */
public class LabelBlockEqualsTest {

    /**
     * Verifies that calling equals() on a LabelBlock instance with a null font
     * throws a NullPointerException. This behavior, while likely a bug in the
     * source class, is documented by this test. The equals() implementation
     * should ideally handle nulls gracefully instead of throwing an exception.
     */
    @Test(expected = NullPointerException.class)
    public void equals_whenInstanceHasNullFont_shouldThrowNullPointerException() {
        // Arrange: Create one block with a null font and another with a default font.
        LabelBlock blockWithNullFont = new LabelBlock("Test Label", (Font) null);
        LabelBlock blockWithDefaultFont = new LabelBlock("Test Label");

        // Act & Assert: Calling equals() on the instance with the null font is expected
        // to throw a NullPointerException.
        blockWithNullFont.equals(blockWithDefaultFont);
    }
}