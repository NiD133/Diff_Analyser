package org.jfree.chart.title;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.Range;
import org.junit.Before;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

/**
 * A suite of understandable unit tests for the {@link ShortTextTitle} class.
 * This class verifies the title's layout arrangement, drawing, and handling of various constraints.
 */
public class ShortTextTitle_ImprovedTest {

    private Graphics2D graphics2D;

    /**
     * Sets up a graphics context needed for layout and drawing tests.
     */
    @Before
    public void setUp() {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        graphics2D = image.createGraphics();
    }

    //region Constructor and Argument Validation Tests

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullText_throwsException() {
        // Act
        new ShortTextTitle(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void arrange_withNullConstraint_throwsException() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Test");

        // Act
        title.arrange(graphics2D, null);
    }

    @Test(expected = NullPointerException.class)
    public void arrangeRR_withNullGraphics_throwsException() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Test");

        // Act
        title.arrangeRR(null, new Range(0, 100), new Range(0, 100));
    }
    
    @Test(expected = NullPointerException.class)
    public void draw_withNullGraphics_throwsException() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Test");
        Rectangle2D.Double area = new Rectangle2D.Double(0, 0, 100, 100);

        // Act
        title.draw(null, area, null);
    }

    //endregion

    //region Arrangement (Layout) Tests

    /**
     * Tests that arrangeNN (no constraints) correctly calculates the size for a given text.
     */
    @Test
    public void arrangeNN_withNonEmptyText_returnsSizeGreaterThanZero() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("A Title");

        // Act
        Size2D size = title.arrangeNN(graphics2D);

        // Assert
        assertTrue("Width should be greater than zero for non-empty text", size.getWidth() > 0);
        assertTrue("Height should be greater than zero", size.getHeight() > 0);
    }

    /**
     * Tests that arrangeNN (no constraints) returns a size with zero width for an empty text.
     */
    @Test
    public void arrangeNN_withEmptyText_returnsSizeWithZeroWidth() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("");

        // Act
        Size2D size = title.arrangeNN(graphics2D);

        // Assert
        assertEquals("Width should be zero for empty text", 0.0, size.getWidth(), 0.01);
        assertTrue("Height should still be positive due to font metrics", size.getHeight() > 0);
    }

    /**
     * A ShortTextTitle should return a zero size if the text does not fit within the given width constraint.
     */
    @Test
    public void arrangeRN_whenTextIsWiderThanRange_returnsZeroSize() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("This is a very long title text");
        Range narrowWidthRange = new Range(0.0, 10.0); // A very narrow width

        // Act
        Size2D size = title.arrangeRN(graphics2D, narrowWidthRange);

        // Assert
        assertEquals("Width should be 0.0 when text doesn't fit", 0.0, size.getWidth(), 0.01);
        assertEquals("Height should be 0.0 when text doesn't fit", 0.0, size.getHeight(), 0.01);
    }

    /**
     * A ShortTextTitle should return its calculated size if the text fits within the given width constraint.
     */
    @Test
    public void arrangeRN_whenTextFitsInRange_returnsCalculatedSize() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Fit");
        Range wideWidthRange = new Range(0.0, 100.0); // A wide width

        // Act
        Size2D size = title.arrangeRN(graphics2D, wideWidthRange);

        // Assert
        assertTrue("Width should be greater than zero when text fits", size.getWidth() > 0);
        assertTrue("Height should be greater than zero when text fits", size.getHeight() > 0);
    }
    
    /**
     * The arrangeRN method should treat a null range as unconstrained, behaving like arrangeNN.
     */
    @Test
    public void arrangeRN_withNullRange_behavesAsUnconstrained() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("A Title");
        
        // Act
        Size2D sizeWithNullRange = title.arrangeRN(graphics2D, null);
        Size2D sizeUnconstrained = title.arrangeNN(graphics2D);

        // Assert
        assertEquals("Width with null range should match unconstrained width", 
                     sizeUnconstrained.getWidth(), sizeWithNullRange.getWidth(), 0.01);
        assertEquals("Height with null range should match unconstrained height", 
                     sizeUnconstrained.getHeight(), sizeWithNullRange.getHeight(), 0.01);
    }

    /**
     * Tests that arrangeRR returns a zero size if the text fits the width but not the height constraint.
     */
    @Test
    public void arrangeRR_whenTextTallerThanRange_returnsZeroSize() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Fit");
        Range wideWidthRange = new Range(0.0, 100.0);
        Range shortHeightRange = new Range(0.0, 5.0); // Font height is likely > 5px

        // Act
        Size2D size = title.arrangeRR(graphics2D, wideWidthRange, shortHeightRange);

        // Assert
        assertEquals("Size should be zero if height constraint is not met", 0.0, size.getHeight(), 0.01);
    }
    
    /**
     * Tests that arrangeRR returns a zero size if the height range is null.
     */
    @Test
    public void arrangeRR_withNullHeightRange_returnsZeroSize() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Test");
        Range widthRange = new Range(0.0, 100.0);

        // Act
        Size2D size = title.arrangeRR(graphics2D, widthRange, null);

        // Assert
        assertEquals("Size should be zero if height range is null", 0.0, size.getHeight(), 0.01);
    }

    /**
     * The arrange() method dispatches to other arrange methods based on the constraint type.
     * This test verifies that unimplemented constraint combinations throw a RuntimeException as expected.
     */
    @Test(expected = RuntimeException.class)
    public void arrange_withUnsupportedConstraintType_throwsRuntimeException() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Test");
        // Create a constraint type (e.g., fixed width, range height) that is not implemented.
        RectangleConstraint unsupportedConstraint = new RectangleConstraint(50.0, new Range(0, 50));

        // Act
        title.arrange(graphics2D, unsupportedConstraint);
    }

    //endregion

    //region Drawing and Integration Tests

    /**
     * A simple smoke test to ensure the draw method executes without errors given a valid context.
     * Verifying the graphical output is complex, so we check for successful execution.
     */
    @Test
    public void draw_onValidGraphicsContext_completesWithoutError() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Drawable Title");
        Rectangle2D.Double area = new Rectangle2D.Double(10, 10, 150, 50);

        // Act & Assert
        try {
            title.draw(graphics2D, area, null);
        } catch (Exception e) {
            fail("Drawing the title should not throw an exception: " + e.getMessage());
        }
    }

    /**
     * An integration test to ensure that a ShortTextTitle can be added to a JFreeChart
     * and rendered without causing an error.
     */
    @Test
    public void title_whenAddedToChart_rendersWithoutError() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Chart Subtitle");
        JFreeChart chart = new JFreeChart("Test Chart", new PiePlot());
        chart.addSubtitle(title);

        // Act & Assert
        try {
            // Creating a buffered image forces the chart to be drawn, including its titles.
            chart.createBufferedImage(300, 200);
        } catch (Exception e) {
            fail("Rendering a chart with a ShortTextTitle should not throw an exception: " + e.getMessage());
        }
    }

    //endregion
}