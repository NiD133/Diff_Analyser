import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.io.WKTWriter;
import org.locationtech.spatial4j.shape.Circle;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;
import org.locationtech.spatial4j.shape.impl.PointImpl;

/**
 * Test suite for {@link WKTWriter}, focusing on its ability to correctly
 * serialize various shapes into Well-Known Text (WKT) format.
 */
public class WKTWriterTest {

    private WKTWriter wktWriter;
    private SpatialContext spatialContext;

    @Before
    public void setUp() {
        spatialContext = SpatialContext.GEO;
        wktWriter = new WKTWriter();
    }

    @Test
    public void getFormatName_shouldReturnWKT() {
        // Arrange & Act
        String formatName = wktWriter.getFormatName();

        // Assert
        assertEquals("WKT", formatName);
    }

    @Test
    public void getNumberFormat_shouldReturnDefaultDecimalFormat() {
        // Arrange & Act
        NumberFormat numberFormat = wktWriter.getNumberFormat();
        
        // Assert
        assertTrue("Default NumberFormat should be a DecimalFormat", numberFormat instanceof DecimalFormat);
        assertEquals("###0.######", ((DecimalFormat) numberFormat).toPattern());
    }

    @Test
    public void toString_shouldCorrectlyFormatPoint() {
        // Arrange
        Shape point = new PointImpl(10.123456, -20.987, spatialContext);
        String expectedWkt = "POINT (10.123456 -20.987)";

        // Act
        String actualWkt = wktWriter.toString(point);

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }
    
    @Test
    public void toString_shouldCorrectlyFormatPointWithInfiniteCoordinates() {
        // Arrange
        Shape pointWithInfinity = new PointImpl(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, spatialContext);
        String expectedWkt = "POINT (-\u221E \u221E)";

        // Act
        String actualWkt = wktWriter.toString(pointWithInfinity);

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }

    @Test
    public void toString_shouldCorrectlyFormatEmptyPoint() {
        // Arrange
        // An empty point is represented by NaN coordinates in Spatial4j.
        Shape emptyPoint = new PointImpl(Double.NaN, Double.NaN, spatialContext);
        String expectedWkt = "POINT EMPTY";

        // Act
        String actualWkt = wktWriter.toString(emptyPoint);

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }

    @Test
    public void toString_shouldCorrectlyFormatRectangle() {
        // Arrange
        Shape rectangle = spatialContext.makeRectangle(-10, 10, -20, 20);
        String expectedWkt = "ENVELOPE (-10, 10, 20, -20)";

        // Act
        String actualWkt = wktWriter.toString(rectangle);

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }

    @Test
    public void toString_shouldCorrectlyFormatCircleAsBuffer() {
        // Arrange
        Shape circle = spatialContext.makeCircle(15, 25, 5);
        String expectedWkt = "BUFFER (POINT (15 25), 5)";

        // Act
        String actualWkt = wktWriter.toString(circle);

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }

    @Test
    public void toString_shouldCorrectlyFormatEmptyLineString() {
        // Arrange
        // A BufferedLineString with no points is treated as an empty LineString.
        Shape emptyLineString = new BufferedLineString(Collections.emptyList(), 0, spatialContext);
        String expectedWkt = "LINESTRING ()";

        // Act
        String actualWkt = wktWriter.toString(emptyLineString);

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }

    @Test
    public void toString_shouldCorrectlyFormatBufferedLineString() {
        // Arrange
        List<Point> points = Arrays.asList(
            new PointImpl(10, 20, spatialContext),
            new PointImpl(30, 40, spatialContext)
        );
        Shape bufferedLine = new BufferedLineString(points, 2.5, spatialContext);
        String expectedWkt = "BUFFER (LINESTRING (10 20, 30 40), 2.5)";

        // Act
        String actualWkt = wktWriter.toString(bufferedLine);

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }

    @Test
    public void toString_shouldCorrectlyFormatEmptyShapeCollection() {
        // Arrange
        ShapeCollection<?> emptyCollection = new ShapeCollection<>(Collections.emptyList(), spatialContext);
        String expectedWkt = "GEOMETRYCOLLECTION EMPTY";

        // Act
        String actualWkt = wktWriter.toString(emptyCollection);

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }

    @Test
    public void write_shouldCorrectlyFormatShapeCollection() throws IOException {
        // Arrange
        List<Shape> shapes = Arrays.asList(
            new PointImpl(1, 2, spatialContext),
            spatialContext.makeRectangle(5, 10, 15, 20)
        );
        ShapeCollection<?> collection = new ShapeCollection<>(shapes, spatialContext);
        String expectedWkt = "GEOMETRYCOLLECTION (POINT (1 2), ENVELOPE (5, 10, 20, 15))";
        StringWriter stringWriter = new StringWriter();

        // Act
        wktWriter.write(stringWriter, collection);

        // Assert
        assertEquals(expectedWkt, stringWriter.toString());
    }

    @Test
    public void append_shouldFormatPointWithCustomNumberFormat() {
        // Arrange
        StringBuilder stringBuilder = new StringBuilder();
        Point point = new PointImpl(12.345, 67.89, spatialContext);
        NumberFormat percentFormat = NumberFormat.getPercentInstance(); // Formats 0.5 as "50%"
        String expected = "1,235% 6,789%";
        
        // Act
        wktWriter.append(stringBuilder, point, percentFormat);

        // Assert
        assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void toString_shouldThrowNullPointerException_whenShapeIsNull() {
        // Arrange, Act & Assert
        assertThrows(NullPointerException.class, () -> {
            wktWriter.toString(null);
        });
    }

    @Test
    public void write_shouldThrowNullPointerException_whenWriterIsNull() {
        // Arrange
        Shape point = new PointImpl(0, 0, spatialContext);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            wktWriter.write(null, point);
        });
    }
}