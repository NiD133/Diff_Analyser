package org.jfree.chart.title;

import org.jfree.data.Range;
import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the arrangeRN() method throws a NullPointerException if the
     * Graphics2D context is null. This is expected behavior as the method
     * requires a graphics context to perform calculations, such as measuring text.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeRNShouldThrowNullPointerExceptionForNullGraphics() {
        // Arrange: Create a title instance.
        ShortTextTitle title = new ShortTextTitle("Test Title");

        // Act & Assert: Call arrangeRN with a null Graphics2D object.
        // This is expected to throw a NullPointerException.
        title.arrangeRN((Graphics2D) null, (Range) null);
    }
}