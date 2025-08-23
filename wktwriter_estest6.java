package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.impl.PointImpl;

import java.text.ChoiceFormat;
import java.text.NumberFormat;

/**
 * Test suite for {@link WKTWriter}.
 */
public class WKTWriterTest {

    /**
     * Tests that the WKTWriter's internal append method correctly propagates exceptions
     * thrown by a faulty NumberFormat instance. The writer should not swallow
     * or wrap the exception, ensuring that issues from its dependencies are surfaced.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void appendPointWithInvalidNumberFormatPropagatesException() {
        // Arrange
        WKTWriter wktWriter = new WKTWriter();
        StringBuilder stringBuilder = new StringBuilder();
        SpatialContext spatialContext = SpatialContext.GEO;
        Point point = new PointImpl(10.0, 20.0, spatialContext);

        // A ChoiceFormat requires a pattern like "limit#string|limit#string...".
        // The provided pattern is invalid and is known to cause an
        // ArrayIndexOutOfBoundsException when its format() method is called.
        String invalidChoiceFormatPattern = "$+(8+";
        NumberFormat faultyNumberFormat = new ChoiceFormat(invalidChoiceFormatPattern);

        // Act
        // This call is expected to fail internally when faultyNumberFormat.format() is invoked.
        // The test verifies that the original exception is propagated up.
        wktWriter.append(stringBuilder, point, faultyNumberFormat);

        // Assert
        // The test will pass if the expected ArrayIndexOutOfBoundsException is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}