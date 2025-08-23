package com.itextpdf.text.pdf.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Contains unit tests for the {@link Vector} class, focusing on mathematical
 * operations that are critical for the {@link LocationTextExtractionStrategy}.
 */
public class LocationTextExtractionStrategyVectorMathTest {

    private static final float ASSERTION_DELTA = 0.0001f;

    /**
     * Tests the projection of a point onto a line's direction vector.
     *
     * <p>This calculation is fundamental to how LocationTextExtractionStrategy orders
     * text chunks. It uses the dot product to find the signed distance from a line's
     * endpoint to a given point, projected onto the line's direction. This test
     * verifies that this distance is calculated correctly.</p>
     *
     * <p>The test defines a horizontal line segment from (0,0) to (1,0) and checks two points:
     * <ul>
     *     <li>A point at (0.9, 0), which lies on the segment. Its projected distance from the
     *         endpoint should be negative (-0.1).</li>
     *     <li>A point at (1.1, 0), which lies past the segment. Its projected distance from
     *         the endpoint should be positive (0.1).</li>
     * </ul>
     * </p>
     */
    @Test
    public void projectPointOntoLine_shouldCalculateCorrectSignedDistance() {
        // Arrange: Define a line segment and its normalized direction vector.
        Vector lineStart = new Vector(0, 0, 1);
        Vector lineEnd = new Vector(1, 0, 1);
        Vector lineDirection = lineEnd.subtract(lineStart).normalize();

        // Arrange: Define points to be projected. One is on the line segment, the other is past it.
        Vector pointOnSegment = new Vector(0.9f, 0, 1);
        Vector pointPastSegment = new Vector(1.1f, 0, 1);

        // Act: Calculate the projection. This is the dot product of the vector from the
        // line's end to the point with the line's direction vector. This yields the
        // signed distance from the line's end along the line's direction.
        float projectedDistanceForPointOnSegment = pointOnSegment.subtract(lineEnd).dot(lineDirection);
        float projectedDistanceForPointPastSegment = pointPastSegment.subtract(lineEnd).dot(lineDirection);

        // Assert: The point on the segment should have a negative distance from the end,
        // and the point past the segment should have a positive distance.
        Assert.assertEquals(
                "The projected distance for a point on the segment should be negative.",
                -0.1f,
                projectedDistanceForPointOnSegment,
                ASSERTION_DELTA);

        Assert.assertEquals(
                "The projected distance for a point past the segment should be positive.",
                0.1f,
                projectedDistanceForPointPastSegment,
                ASSERTION_DELTA);
    }
}