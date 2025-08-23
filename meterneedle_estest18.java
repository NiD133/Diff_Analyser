package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * A test suite for the {@link MeterNeedle} class.
 * <p>
 * Since {@code MeterNeedle} is an abstract class, these tests use a concrete
 * subclass, {@link PointerNeedle}, to instantiate and test the base class
 * functionality.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the default constructor correctly initializes all properties
     * to their expected default values.
     */
    @Test
    public void defaultConstructorShouldSetDefaultValues() {
        // Arrange: Create a new needle instance using the default constructor.
        PointerNeedle needle = new PointerNeedle();

        // Act: No action is necessary as we are testing the state immediately
        // after construction.

        // Assert: Confirm that all properties are set to their documented default values.
        assertEquals("Default size should be 5", 5, needle.getSize());
        assertEquals("Default rotateX should be 0.5", 0.5, needle.getRotateX(), 0.01);
        assertEquals("Default rotateY should be 0.5", 0.5, needle.getRotateY(), 0.01);

        // Assert default paint properties
        assertEquals("Default outline paint should be black", Color.BLACK, needle.getOutlinePaint());
        assertNull("Default fill paint should be null", needle.getFillPaint());
        assertNull("Default highlight paint should be null", needle.getHighlightPaint());

        // Assert default stroke properties
        assertNotNull("Default outline stroke should not be null", needle.getOutlineStroke());
        assertTrue("Default outline stroke should be an instance of BasicStroke",
                needle.getOutlineStroke() instanceof BasicStroke);

        BasicStroke stroke = (BasicStroke) needle.getOutlineStroke();
        assertEquals("Default outline stroke width should be 2.0f", 2.0f, stroke.getLineWidth(), 0.0f);
    }
}