package org.jfree.chart.title;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.Range;
import org.junit.Test;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the arrangeRR method throws a NullPointerException
     * when the Graphics2D argument is null. The method requires a valid
     * graphics context to measure text dimensions and cannot proceed without it.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeRRShouldThrowNullPointerExceptionWhenGraphicsContextIsNull() {
        // Arrange: Create a title instance and a dummy range for the layout.
        ShortTextTitle title = new ShortTextTitle("Test Title");
        Range anyRange = ValueAxis.DEFAULT_RANGE;

        // Act: Call the method under test with a null Graphics2D object.
        title.arrangeRR(null, anyRange, anyRange);

        // Assert: The test will pass only if a NullPointerException is thrown,
        // as specified by the @Test annotation.
    }
}