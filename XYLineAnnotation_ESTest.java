package org.jfree.chart.annotations;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.*;

/**
 * A set of tests for the {@link XYLineAnnotation} class, focusing on constructor logic,
 * property access, equality, cloning, and serialization.
 */
public class XYLineAnnotationTest {

    private static final double DELTA = 1e-9;

    //region Constructor Tests

    @Test
    public void constructor_withCoordinates_shouldSetDefaultsForStrokeAndPaint() {
        // Arrange & Act
        XYLineAnnotation annotation = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0);

        // Assert
        assertEquals(10.0, annotation.getX1(), DELTA);
        assertEquals(20.0, annotation.getY1(), DELTA);
        assertEquals(30.0, annotation.getX2(), DELTA);
        assertEquals(40.0, annotation.getY2(), DELTA);
        assertEquals("Default stroke should be a 1.0f BasicStroke", new BasicStroke(1.0f), annotation.getStroke());
        assertEquals("Default paint should be Color.BLACK", Color.BLACK, annotation.getPaint());
    }

    @Test
    public void constructor_withAllArguments_shouldSetPropertiesCorrectly() {
        // Arrange
        Stroke testStroke = new BasicStroke(2.5f);
        Paint testPaint = Color.RED;

        // Act
        XYLineAnnotation annotation = new XYLineAnnotation(1.5, 2.5, 3.5, 4.5, testStroke, testPaint);

        // Assert
        assertEquals(1.5, annotation.getX1(), DELTA);
        assertEquals(2.5, annotation.getY1(), DELTA);
        assertEquals(3.5, annotation.getX2(), DELTA);
        assertEquals(4.5, annotation.getY2(), DELTA);
        assertSame("Stroke should be the provided instance", testStroke, annotation.getStroke());
        assertSame("Paint should be the provided instance", testPaint, annotation.getPaint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullStroke_shouldThrowException() {
        new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, null, Color.RED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNullPaint_shouldThrowException() {
        new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, new BasicStroke(1.0f), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_withNonFiniteCoordinate_shouldThrowException() {
        // Test with one non-finite coordinate; the class should reject any.
        new XYLineAnnotation(Double.POSITIVE_INFINITY, 2.0, 3.0, 4.0);
    }

    //endregion

    //region Equals and HashCode Tests

    @Test
    public void equals_shouldAdhereToContract() {
        // Arrange
        Stroke stroke = new BasicStroke(2.0f);
        Paint paint = Color.BLUE;

        XYLineAnnotation annotation1 = new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, stroke, paint);
        XYLineAnnotation annotation2 = new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, stroke, paint); // Identical
        XYLineAnnotation diffX1 = new XYLineAnnotation(9.0, 2.0, 3.0, 4.0, stroke, paint);
        XYLineAnnotation diffY1 = new XYLineAnnotation(1.0, 9.0, 3.0, 4.0, stroke, paint);
        XYLineAnnotation diffX2 = new XYLineAnnotation(1.0, 2.0, 9.0, 4.0, stroke, paint);
        XYLineAnnotation diffY2 = new XYLineAnnotation(1.0, 2.0, 3.0, 9.0, stroke, paint);
        XYLineAnnotation diffStroke = new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, new BasicStroke(3.0f), paint);
        XYLineAnnotation diffPaint = new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, stroke, Color.GREEN);

        // Assert
        assertTrue("An object should be equal to itself.", annotation1.equals(annotation1));
        assertTrue("Objects with identical properties should be equal.", annotation1.equals(annotation2));

        assertFalse("Should not be equal to null.", annotation1.equals(null));
        assertFalse("Should not be equal to an object of a different type.", annotation1.equals("Some String"));
        assertFalse("Should be unequal if x1 differs.", annotation1.equals(diffX1));
        assertFalse("Should be unequal if y1 differs.", annotation1.equals(diffY1));
        assertFalse("Should be unequal if x2 differs.", annotation1.equals(diffX2));
        assertFalse("Should be unequal if y2 differs.", annotation1.equals(diffY2));
        assertFalse("Should be unequal if stroke differs.", annotation1.equals(diffStroke));
        assertFalse("Should be unequal if paint differs.", annotation1.equals(diffPaint));
    }

    @Test
    public void hashCode_shouldBeConsistentWithEquals() {
        // Arrange
        Stroke stroke = new BasicStroke(2.0f);
        Paint paint = Color.BLUE;
        XYLineAnnotation annotation1 = new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, stroke, paint);
        XYLineAnnotation annotation2 = new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, stroke, paint);
        XYLineAnnotation annotation3 = new XYLineAnnotation(9.0, 2.0, 3.0, 4.0, stroke, paint);

        // Assert
        assertEquals("Hashcode should be the same for equal objects.", annotation1.hashCode(), annotation2.hashCode());
        assertNotEquals("Hashcode should be different for unequal objects.", annotation1.hashCode(), annotation3.hashCode());
    }

    //endregion

    //region Cloning and Serialization

    @Test
    public void clone_shouldProduceIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        XYLineAnnotation original = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0);

        // Act
        XYLineAnnotation clone = (XYLineAnnotation) original.clone();

        // Assert
        assertNotSame("Clone should be a different object instance.", original, clone);
        assertEquals("Clone should be equal to the original object.", original, clone);
    }

    @Test
    public void serialization_shouldPreserveObjectState() throws IOException, ClassNotFoundException {
        // Arrange
        XYLineAnnotation original = new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, new BasicStroke(2f), Color.RED);
        XYLineAnnotation deserialized;

        // Act
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            deserialized = (XYLineAnnotation) ois.readObject();
        }

        // Assert
        assertEquals("Deserialized object should be equal to the original.", original, deserialized);
    }

    //endregion

    //region Drawing Test

    @Test
    public void draw_shouldExecuteWithoutErrors() {
        // Arrange
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        
        XYPlot plot = new XYPlot(null, new NumberAxis("X"), new NumberAxis("Y"), null);
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 400, 300);
        PlotRenderingInfo info = new PlotRenderingInfo(null);

        XYLineAnnotation annotation = new XYLineAnnotation(10.0, 20.0, 80.0, 90.0);
        annotation.setToolTipText("Test Tooltip");
        annotation.setURL("http://example.com");

        // Act & Assert
        try {
            annotation.draw(g2, plot, dataArea, plot.getDomainAxis(), plot.getRangeAxis(), 0, info);
            // The test passes if the draw method executes without throwing an exception.
        } catch (Exception e) {
            fail("Drawing the annotation should not throw an exception: " + e.getMessage());
        } finally {
            g2.dispose();
        }
    }

    //endregion
}