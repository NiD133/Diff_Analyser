package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the {@link WKTWriter} class.
 * The original name {@code WKTWriter_ESTestTest12} suggests it was auto-generated.
 */
public class WKTWriter_ESTestTest12 extends WKTWriter_ESTest_scaffolding {

    /**
     * Tests that an empty BufferedLineString is correctly formatted
     * into its standard Well-Known Text (WKT) representation, which is "LINESTRING ()".
     */
    @Test
    public void toString_withEmptyLineString_shouldReturnEmptyWktRepresentation() {
        // Arrange: Set up the test objects.
        WKTWriter wktWriter = new WKTWriter();
        SpatialContext spatialContext = SpatialContext.GEO;
        List<Point> emptyPoints = Collections.emptyList();

        // The buffer value is required by the constructor but is irrelevant for an empty line string.
        // Using 0.0 makes the intent clearer than a random magic number.
        double irrelevantBuffer = 0.0;
        Shape emptyLineString = new BufferedLineString(emptyPoints, irrelevantBuffer, spatialContext);

        // Act: Call the method under test.
        String actualWkt = wktWriter.toString(emptyLineString);

        // Assert: Verify the result is as expected.
        String expectedWkt = "LINESTRING ()";
        assertEquals("The WKT for an empty LineString should be 'LINESTRING ()'", expectedWkt, actualWkt);
    }
}