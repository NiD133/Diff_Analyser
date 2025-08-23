package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.fail;

import java.awt.Graphics2D;
import org.jfree.data.Range;

// Removed unused imports: SystemColor, ChronoUnit, Calendar, HorizontalAlignment,
// RectangleAnchor, VerticalAlignment, TextBlockAnchor, TimePeriodAnchor,
// TimeSeries, EvoRunner, EvoRunnerParameters, MockGregorianCalendar.

// The original test class name implies it's part of a larger generated suite.
// For a standalone example, a more direct name would be better, but we'll keep it for consistency.
public class GridArrangement_ESTestTest4 extends GridArrangement_ESTest_scaffolding {

    /**
     * Verifies that the arrangeRF method throws a NullPointerException when the
     * Graphics2D context is null. The arrangeRF method is responsible for
     * arranging blocks with a Range constraint on width and a Fixed constraint on height.
     */
    @Test
    public void arrangeRFShouldThrowNullPointerExceptionWhenGraphics2DIsNull() {
        // Arrange: Set up the necessary objects for the test.
        // We need a GridArrangement instance, a container, and a valid constraint
        // for the arrangeRF method. The Graphics2D object is intentionally null.
        GridArrangement arrangement = new GridArrangement(2, 2);
        BlockContainer container = new BlockContainer();
        
        // arrangeRF requires a constraint with a Range for width and a Fixed value for height.
        RectangleConstraint constraint = new RectangleConstraint(new Range(0.0, 100.0), 50.0);
        Graphics2D nullGraphics = null;

        // Act & Assert: Call the method and verify that it throws the expected exception.
        try {
            arrangement.arrangeRF(container, nullGraphics, constraint);
            fail("Expected a NullPointerException, but no exception was thrown.");
        } catch (NullPointerException e) {
            // Success: The expected exception was caught.
            // The test passes.
        }
    }
}