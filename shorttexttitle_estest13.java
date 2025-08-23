package org.jfree.chart.title;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

import java.awt.Graphics2D;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the arrangeNN method throws a NullPointerException
     * when called with a null Graphics2D object, as this is a required parameter.
     */
    @Test
    public void arrangeNNShouldThrowNullPointerExceptionWhenGraphicsIsNull() {
        // Arrange: Create a ShortTextTitle instance. The specific text is not
        // relevant for this test case.
        ShortTextTitle title = new ShortTextTitle("Test Title");

        // Act & Assert: Verify that calling arrangeNN with a null Graphics2D
        // context throws the expected NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            title.arrangeNN(null);
        });
    }
}