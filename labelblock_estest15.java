package org.jfree.chart.block;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

/**
 * Unit tests for the {@link LabelBlock} class, focusing on constructor validation.
 */
public class LabelBlockTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when the 'font' argument is null.
     */
    @Test
    public void constructorShouldThrowExceptionForNullFont() {
        // Arrange: Define expectations for the exception
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Null 'font' argument.");

        // Act: Attempt to create a LabelBlock with a null font
        String text = "Test Label";
        Paint paint = Color.GREEN;
        new LabelBlock(text, null, paint);
    }
}