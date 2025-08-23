package org.jfree.chart.annotations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link XYLineAnnotation} class.
 */
@DisplayName("XYLineAnnotation")
class XYLineAnnotationTest {

    /**
     * Verifies that two equal XYLineAnnotation objects produce the same hash code,
     * as required by the Object.hashCode() contract.
     */
    @Test
    @DisplayName("hashCode() returns the same value for two equal objects")
    void hashCode_shouldReturnSameValue_forEqualObjects() {
        // Arrange: Create two identical XYLineAnnotation objects.
        Stroke commonStroke = new BasicStroke(2.0f);
        XYLineAnnotation annotation1 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, commonStroke, Color.BLUE);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10.0, 20.0, 100.0, 200.0, commonStroke, Color.BLUE);

        // Pre-condition: Ensure the objects are considered equal before testing the hash code contract.
        assertEquals(annotation1, annotation2, "Annotations should be equal for this test to be valid.");

        // Act: Calculate the hash codes for both objects.
        int hashCode1 = annotation1.hashCode();
        int hashCode2 = annotation2.hashCode();

        // Assert: The hash codes must be identical.
        assertEquals(hashCode1, hashCode2, "Equal objects must have equal hash codes.");
    }
}