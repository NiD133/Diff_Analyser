package org.jfree.chart.annotations;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.Drawable;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

/**
 * A set of more understandable tests for the {@link XYDrawableAnnotation} class.
 * This class demonstrates best practices for writing clear and maintainable tests.
 */
public class XYDrawableAnnotationTest {

    private static final double X_COORD = 10.0;
    private static final double Y_COORD = 20.0;
    private static final double DISPLAY_WIDTH = 100.0;
    private static final double DISPLAY_HEIGHT = 50.0;
    private static final double DRAW_SCALE_FACTOR = 1.5;
    private static final Drawable MOCK_DRAWABLE = new TextTitle("Test");
    private static final Drawable OTHER_MOCK_DRAWABLE = new TextTitle("Other");

    // --- Constructor and Getter Tests ---

    @Test
    public void constructor_withAllParameters_shouldSetFieldsCorrectly() {
        // Arrange & Act
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, DRAW_SCALE_FACTOR, MOCK_DRAWABLE);

        // Assert
        assertEquals(X_COORD, annotation.getX(), 0.0);
        assertEquals(Y_COORD, annotation.getY(), 0.0);
        assertEquals(DISPLAY_WIDTH, annotation.getDisplayWidth(), 0.0);
        assertEquals(DISPLAY_HEIGHT, annotation.getDisplayHeight(), 0.0);
        assertEquals(DRAW_SCALE_FACTOR, annotation.getDrawScaleFactor(), 0.0);
    }

    @Test
    public void constructor_withoutScaleFactor_shouldUseDefaultScaleFactorOfOne() {
        // Arrange & Act
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);

        // Assert
        assertEquals(1.0, annotation.getDrawScaleFactor(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullDrawable_shouldThrowIllegalArgumentException() {
        // Arrange, Act & Assert
        new XYDrawableAnnotation(X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithScale_withNullDrawable_shouldThrowIllegalArgumentException() {
        // Arrange, Act & Assert
        new XYDrawableAnnotation(X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, DRAW_SCALE_FACTOR, null);
    }

    // --- Equals and HashCode Tests ---

    @Test
    public void equals_shouldReturnTrue_forSameInstance() {
        // Arrange
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);

        // Act & Assert
        assertTrue(annotation.equals(annotation));
    }

    @Test
    public void equals_shouldReturnTrue_forIdenticalObjects() {
        // Arrange
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, DRAW_SCALE_FACTOR, MOCK_DRAWABLE);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, DRAW_SCALE_FACTOR, MOCK_DRAWABLE);

        // Act & Assert
        assertTrue(annotation1.equals(annotation2));
    }

    @Test
    public void equals_shouldReturnFalse_forNullObject() {
        // Arrange
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);

        // Act & Assert
        assertFalse(annotation.equals(null));
    }

    @Test
    public void equals_shouldReturnFalse_forDifferentClass() {
        // Arrange
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);
        Object otherObject = new Object();

        // Act & Assert
        assertFalse(annotation.equals(otherObject));
    }

    @Test
    public void equals_shouldReturnFalse_whenXIsDifferent() {
        // Arrange
        XYDrawableAnnotation baseAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);
        XYDrawableAnnotation differentAnnotation = new XYDrawableAnnotation(
                99.0, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);

        // Act & Assert
        assertFalse(baseAnnotation.equals(differentAnnotation));
    }

    @Test
    public void equals_shouldReturnFalse_whenYIsDifferent() {
        // Arrange
        XYDrawableAnnotation baseAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);
        XYDrawableAnnotation differentAnnotation = new XYDrawableAnnotation(
                X_COORD, 99.0, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);

        // Act & Assert
        assertFalse(baseAnnotation.equals(differentAnnotation));
    }

    @Test
    public void equals_shouldReturnFalse_whenDisplayWidthIsDifferent() {
        // Arrange
        XYDrawableAnnotation baseAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);
        XYDrawableAnnotation differentAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, 999.0, DISPLAY_HEIGHT, MOCK_DRAWABLE);

        // Act & Assert
        assertFalse(baseAnnotation.equals(differentAnnotation));
    }

    @Test
    public void equals_shouldReturnFalse_whenDisplayHeightIsDifferent() {
        // Arrange
        XYDrawableAnnotation baseAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);
        XYDrawableAnnotation differentAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, 999.0, MOCK_DRAWABLE);

        // Act & Assert
        assertFalse(baseAnnotation.equals(differentAnnotation));
    }



    @Test
    public void equals_shouldReturnFalse_whenDrawScaleFactorIsDifferent() {
        // Arrange
        XYDrawableAnnotation baseAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, DRAW_SCALE_FACTOR, MOCK_DRAWABLE);
        XYDrawableAnnotation differentAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, 9.9, MOCK_DRAWABLE);

        // Act & Assert
        assertFalse(baseAnnotation.equals(differentAnnotation));
    }

    @Test
    public void equals_shouldReturnFalse_whenDrawableIsDifferent() {
        // Arrange
        XYDrawableAnnotation baseAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);
        XYDrawableAnnotation differentAnnotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, OTHER_MOCK_DRAWABLE);

        // Act & Assert
        assertFalse(baseAnnotation.equals(differentAnnotation));
    }

    @Test
    public void hashCode_shouldBeEqual_forEqualObjects() {
        // Arrange
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, DRAW_SCALE_FACTOR, MOCK_DRAWABLE);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, DRAW_SCALE_FACTOR, MOCK_DRAWABLE);

        // Act & Assert
        assertEquals(annotation1.hashCode(), annotation2.hashCode());
    }

    // --- Cloneable Interface Test ---

    @Test
    public void clone_shouldReturnEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange
        XYDrawableAnnotation original = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);

        // Act
        XYDrawableAnnotation clone = (XYDrawableAnnotation) original.clone();

        // Assert
        assertEquals(original, clone);
        assertNotSame(original, clone);
    }

    // --- Drawing Tests ---

    @Test
    public void draw_shouldExecuteWithoutErrors_onValidPlot() {
        // Arrange
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);
        Graphics2D g2 = createTestGraphics2D();
        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 1000, 1000);
        ValueAxis domainAxis = new NumberAxis("X");
        ValueAxis rangeAxis = new NumberAxis("Y");
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        // Act & Assert (expect no exception)
        try {
            annotation.draw(g2, plot, dataArea, domainAxis, rangeAxis, 0, info);
        } catch (Exception e) {
            fail("Drawing should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void draw_shouldAddEntityToInfo_whenUrlIsSet() {
        // Arrange
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);
        annotation.setURL("http://example.com");

        Graphics2D g2 = createTestGraphics2D();
        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 1000, 1000);
        ValueAxis domainAxis = new NumberAxis("X");
        ValueAxis rangeAxis = new NumberAxis("Y");
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);

        // Act
        annotation.draw(g2, plot, dataArea, domainAxis, rangeAxis, 0, plotInfo);

        // Assert
        assertNotNull("Chart entity collection should not be null", chartInfo.getEntityCollection());
        assertEquals("An entity should have been added", 1, chartInfo.getEntityCollection().getEntityCount());
    }

    @Test(expected = NullPointerException.class)
    public void draw_withNullRangeAxis_shouldThrowNullPointerException() {
        // Arrange
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                X_COORD, Y_COORD, DISPLAY_WIDTH, DISPLAY_HEIGHT, MOCK_DRAWABLE);
        Graphics2D g2 = createTestGraphics2D();
        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 1000, 1000);
        ValueAxis domainAxis = new NumberAxis("X");
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());

        // Act
        annotation.draw(g2, plot, dataArea, domainAxis, null, 0, info);
    }

    /**
     * Helper method to create a Graphics2D instance for testing.
     */
    private Graphics2D createTestGraphics2D() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        return image.createGraphics();
    }
}