package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.awt.Color;
import java.awt.Paint;

/**
 * Unit tests for the {@link MeterNeedle} class, using the concrete subclass 
 * {@link WindNeedle} for instantiation and testing property accessors.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the setFillPaint() method correctly updates the fill paint
     * property, which can then be retrieved by getFillPaint().
     */
    @Test
    public void setFillPaintShouldUpdateTheFillPaintProperty() {
        // Arrange: Create a needle instance and the paint to be set.
        WindNeedle needle = new WindNeedle();
        Paint expectedPaint = Color.BLUE;

        // Act: Set the fill paint on the needle.
        needle.setFillPaint(expectedPaint);
        Paint actualPaint = needle.getFillPaint();

        // Assert: Verify that the retrieved paint is the one that was set.
        assertNotNull("The retrieved paint should not be null", actualPaint);
        assertEquals("The fill paint should match the value set", expectedPaint, actualPaint);
    }

    /**
     * Verifies that a new WindNeedle instance is created with the expected
     * default property values.
     */
    @Test
    public void newWindNeedleShouldHaveDefaultProperties() {
        // Arrange & Act: Create a new WindNeedle instance.
        WindNeedle needle = new WindNeedle();

        // Assert: Check that the properties have their expected default values.
        assertEquals("Default size should be 5", 5, needle.getSize());
        assertEquals("Default rotateX should be 0.5", 0.5, needle.getRotateX(), 0.01);
        assertEquals("Default rotateY should be 0.5", 0.5, needle.getRotateY(), 0.01);
    }
}