package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    /**
     * Tests that two {@link BufferedLineString} instances are considered equal
     * when they are created with identical parameters but using different constructors.
     * <p>
     * This test verifies that the 3-argument constructor, which omits the
     * {@code expandBufForLongitudeSkew} flag, behaves identically to the 4-argument
     * constructor when this flag is explicitly set to its default value of {@code false}.
     * The test specifically uses an empty list of points to create the shapes.
     */
    @Test
    public void testEquals_withEquivalentConstructorsForEmptyLine_returnsTrue() {
        // Arrange
        final SpatialContext ctx = SpatialContext.GEO;
        final List<Point> emptyPointsList = Collections.emptyList();
        final double buffer = -1827.67990034; // Note: A negative buffer is used from the original test.

        // Create a line string with the explicit 'expandBufForLongitudeSkew' flag set to false.
        BufferedLineString lineStringWithExplicitFlag = new BufferedLineString(emptyPointsList, buffer, false, ctx);

        // Create another line string using the constructor that defaults 'expandBufForLongitudeSkew' to false.
        BufferedLineString lineStringWithDefaultFlag = new BufferedLineString(emptyPointsList, buffer, ctx);

        // Act & Assert
        // Verify that the two line strings are equal, as they were constructed with equivalent parameters.
        assertTrue(
            "Line strings from different but equivalent constructors should be equal.",
            lineStringWithExplicitFlag.equals(lineStringWithDefaultFlag)
        );

        // Verify that the equals contract is symmetric.
        assertTrue(
            "The equals method should be symmetric.",
            lineStringWithDefaultFlag.equals(lineStringWithExplicitFlag)
        );

        // A sanity check on the buffer value of one of the objects.
        assertEquals(
            "Buffer value should be correctly stored.",
            buffer,
            lineStringWithDefaultFlag.getBuf(),
            0.01
        );
    }
}