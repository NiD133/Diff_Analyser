package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.SystemColor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the constructor of the {@link MeterInterval} class, focusing on invalid arguments.
 */
public class MeterIntervalConstructorTest {

    /**
     * Verifies that the MeterInterval constructor throws an IllegalArgumentException
     * when the 'range' argument is null. A valid range is a mandatory requirement
     * for creating an interval.
     */
    @Test
    public void constructorShouldThrowIllegalArgumentExceptionWhenRangeIsNull() {
        // Arrange: Define valid inputs for other parameters and the expected exception message.
        String label = "Test Interval";
        Paint dummyPaint = SystemColor.controlHighlight;
        Stroke dummyStroke = new BasicStroke();
        String expectedMessage = "Null 'range' argument.";

        // Act & Assert: Attempt to create an instance with a null range and verify the exception.
        try {
            new MeterInterval(label, null, dummyPaint, dummyStroke, dummyPaint);
            fail("Expected an IllegalArgumentException because the range was null, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, ensuring the right error is caught.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}