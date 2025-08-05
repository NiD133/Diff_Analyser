package org.jfree.chart.plot.compass;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

import static org.junit.Assert.*;

/**
 * A comprehensive test suite for the {@link MeterNeedle} abstract class and its concrete implementations.
 * Tests cover property management, equality contracts, drawing logic, and serialization.
 */
public class MeterNeedleTest {

    private PointerNeedle needle;
    private Graphics2D g2d;
    private Rectangle2D.Double plotArea;

    @Before
    public void setUp() {
        // Arrange: Create a default needle for general tests
        needle = new PointerNeedle();

        // Arrange: Create a dummy graphics context and plot area for drawing tests
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        plotArea = new Rectangle2D.Double(0, 0, 100, 100);
    }

    //region Property Accessors and Defaults

    @Test
    public void constructor_shouldSetDefaultValues() {
        // Assert: A new PointerNeedle should have default properties.
        // Note: Default paints are null in the base constructor.
        assertEquals(5, needle.getSize());
        assertEquals(0.5, needle.getRotateX(), 0.0);
        assertEquals(0.5, needle.getRotateY(), 0.0);
        assertNotNull(needle.getOutlineStroke());
        assertNull(needle.getFillPaint());
        assertNull(needle.getHighlightPaint());
        assertNull(needle.getOutlinePaint());
    }

    @Test
    public void longNeedle_shouldHaveSpecificDefaultRotateY() {
        // Arrange
        LongNeedle longNeedle = new LongNeedle();

        // Assert: LongNeedle overrides the default rotateY value.
        assertEquals(0.8, longNeedle.getRotateY(), 0.0);
    }

    @Test
    public void setSize_shouldUpdateSizeProperty() {
        // Act
        needle.setSize(20);

        // Assert
        assertEquals(20, needle.getSize());
    }

    @Test
    public void setRotateX_shouldUpdateRotateXProperty() {
        // Act
        needle.setRotateX(0.75);

        // Assert
        assertEquals(0.75, needle.getRotateX(), 0.0);
    }

    @Test
    public void setRotateY_shouldUpdateRotateYProperty() {
        // Act
        needle.setRotateY(0.25);

        // Assert
        assertEquals(0.25, needle.getRotateY(), 0.0);
    }

    @Test
    public void setPaintProperties_shouldUpdatePaints() {
        // Arrange
        Paint outlinePaint = Color.RED;
        Paint fillPaint = Color.GREEN;
        Paint highlightPaint = Color.BLUE;

        // Act
        needle.setOutlinePaint(outlinePaint);
        needle.setFillPaint(fillPaint);
        needle.setHighlightPaint(highlightPaint);

        // Assert
        assertEquals(outlinePaint, needle.getOutlinePaint());
        assertEquals(fillPaint, needle.getFillPaint());
        assertEquals(highlightPaint, needle.getHighlightPaint());
    }

    @Test
    public void setOutlineStroke_shouldUpdateStroke() {
        // Arrange
        Stroke newStroke = new BasicStroke(3.0f);

        // Act
        needle.setOutlineStroke(newStroke);

        // Assert
        assertEquals(newStroke, needle.getOutlineStroke());
    }

    //endregion

    //region Equality and HashCode

    @Test
    public void equals_shouldReturnTrue_forSameInstance() {
        assertTrue(needle.equals(needle));
    }

    @Test
    public void equals_shouldReturnTrue_forDifferentInstancesWithSameProperties() {
        // Arrange: ShipNeedle and PointerNeedle have identical default properties
        MeterNeedle needle1 = new ShipNeedle();
        MeterNeedle needle2 = new PointerNeedle();

        // Assert
        assertTrue(needle1.equals(needle2));
    }

    @Test
    public void equals_shouldReturnFalse_forNull() {
        assertFalse(needle.equals(null));
    }

    @Test
    public void equals_shouldReturnFalse_forDifferentObjectType() {
        assertFalse(needle.equals("a string"));
    }

    @Test
    public void equals_shouldReturnFalse_whenSizeDiffers() {
        // Arrange
        MeterNeedle otherNeedle = new PointerNeedle();
        otherNeedle.setSize(99);

        // Assert
        assertFalse(needle.equals(otherNeedle));
    }

    @Test
    public void equals_shouldReturnFalse_whenRotateXDiffers() {
        // Arrange
        MeterNeedle otherNeedle = new PointerNeedle();
        otherNeedle.setRotateX(0.99);

        // Assert
        assertFalse(needle.equals(otherNeedle));
    }

    @Test
    public void equals_shouldReturnFalse_whenRotateYDiffers() {
        // Arrange
        MeterNeedle otherNeedle = new PointerNeedle();
        otherNeedle.setRotateY(0.99);

        // Assert
        assertFalse(needle.equals(otherNeedle));
    }

    @Test
    public void equals_shouldReturnFalse_whenOutlinePaintDiffers() {
        // Arrange
        MeterNeedle otherNeedle = new PointerNeedle();
        needle.setOutlinePaint(Color.RED);
        otherNeedle.setOutlinePaint(Color.BLUE);

        // Assert
        assertFalse(needle.equals(otherNeedle));
    }

    @Test
    public void equals_shouldReturnFalse_whenOutlineStrokeDiffers() {
        // Arrange
        MeterNeedle otherNeedle = new PointerNeedle();
        needle.setOutlineStroke(new BasicStroke(1.0f));
        otherNeedle.setOutlineStroke(new BasicStroke(2.0f));

        // Assert
        assertFalse(needle.equals(otherNeedle));
    }

    @Test
    public void hashCode_shouldBeConsistent_forEqualObjects() {
        // Arrange
        MeterNeedle needle1 = new ShipNeedle();
        MeterNeedle needle2 = new PointerNeedle();

        // Assert: Equal objects must have equal hash codes.
        assertEquals(needle1.hashCode(), needle2.hashCode());
    }

    @Test
    public void hashCode_shouldBeDifferent_forUnequalObjects() {
        // Arrange
        MeterNeedle otherNeedle = new PointerNeedle();
        otherNeedle.setSize(99);

        // Assert: Unequal objects should ideally have different hash codes.
        assertNotEquals(needle.hashCode(), otherNeedle.hashCode());
    }

    //endregion

    //region Drawing Logic

    @Test
    public void draw_shouldExecuteWithoutError_withValidArguments() {
        // Arrange: Use LongNeedle as it has default paints set, avoiding NPEs
        LongNeedle longNeedle = new LongNeedle();
        Point2D.Double rotatePoint = new Point2D.Double(50, 50);

        // Act & Assert: Calling draw methods should not throw exceptions
        longNeedle.draw(g2d, plotArea);
        longNeedle.draw(g2d, plotArea, 45.0);
        longNeedle.draw(g2d, plotArea, rotatePoint, 90.0);
    }

    @Test
    public void draw_shouldNotThrowError_whenRotatePointIsNull() {
        // Arrange
        LongNeedle longNeedle = new LongNeedle();

        // Act & Assert: A null rotate point should be handled gracefully by using default rotation.
        longNeedle.draw(g2d, plotArea, null, 90.0);
    }

    @Test(expected = NullPointerException.class)
    public void draw_shouldThrowException_forNullGraphicsContext() {
        // Act: Call draw with a null Graphics2D context
        needle.draw(null, plotArea, 45.0);
    }

    @Test(expected = NullPointerException.class)
    public void draw_shouldThrowException_forNullPlotArea() {
        // Act: Call draw with a null plot area
        needle.draw(g2d, null, 45.0);
    }

    //endregion

    //region Serialization

    @Test
    public void serialization_shouldPreserveObjectState() throws IOException, ClassNotFoundException {
        // Arrange: Create and configure a needle
        PointerNeedle originalNeedle = new PointerNeedle();
        originalNeedle.setSize(15);
        originalNeedle.setFillPaint(Color.MAGENTA);
        originalNeedle.setOutlineStroke(new BasicStroke(3f));

        // Act: Serialize and then deserialize the needle
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(originalNeedle);
        }

        PointerNeedle deserializedNeedle;
        ByteArrayInputStream inStream = new ByteArrayInputStream(byteStream.toByteArray());
        try (ObjectInputStream objectInStream = new ObjectInputStream(inStream)) {
            deserializedNeedle = (PointerNeedle) objectInStream.readObject();
        }

        // Assert: The deserialized object should be equal to the original
        assertEquals(originalNeedle, deserializedNeedle);
    }

    //endregion
}