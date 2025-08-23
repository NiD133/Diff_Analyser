package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.impl.PointImpl;

import java.text.NumberFormat;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link WKTWriter}.
 */
public class WKTWriterTest {

    /**
     * This test verifies that the WKTWriter's append method correctly uses a
     * provided NumberFormat to format a Point's coordinates.
     */
    @Test(timeout = 4000)
    public void appendShouldFormatPointUsingProvidedNumberFormat() {
        // Arrange
        WKTWriter wktWriter = new WKTWriter();
        SpatialContext spatialContext = SpatialContext.GEO;
        Point point = new PointImpl(4883.694876, 4.0, spatialContext);

        // Use a specific format (percent) to test the writer's behavior.
        // The PercentInstance format multiplies by 100 and adds a '%' sign.
        // It also uses locale-specific grouping and rounding.
        // Note: Using a specific Locale (e.g., US) makes the test deterministic
        // regardless of the default locale of the machine running the test.
        //  - X: 4883.694876 * 100 -> 488369.4876 -> "488,369%"
        //  - Y: 4.0 * 100         -> 400.0         -> "400%"
        NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.US);

        // The StringBuilder has some initial content to ensure we are appending correctly.
        String initialContent = "PREFIX ";
        StringBuilder stringBuilder = new StringBuilder(initialContent);

        String expectedFormattedPoint = "488,369% 400%";
        String expectedResult = initialContent + expectedFormattedPoint;

        // Act
        wktWriter.append(stringBuilder, point, percentFormat);

        // Assert
        assertEquals("The point coordinates should be appended using the provided percent format.",
                expectedResult, stringBuilder.toString());
    }
}