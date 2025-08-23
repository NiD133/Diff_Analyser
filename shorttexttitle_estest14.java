package org.jfree.chart.title;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the arrangeFN method throws a NullPointerException if the
     * Graphics2D context is null. The method relies on this context to
     * measure the text and cannot proceed without it.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeFNShouldThrowNullPointerExceptionForNullGraphics() {
        // Given a ShortTextTitle instance
        ShortTextTitle title = new ShortTextTitle("Test Title");
        double fixedWidth = 100.0;

        // When arrangeFN is called with a null Graphics2D object
        // Then a NullPointerException is expected
        title.arrangeFN((Graphics2D) null, fixedWidth);
    }
}