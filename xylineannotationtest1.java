package org.jfree.chart.annotations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for the {@link XYLineAnnotation} class.
 */
class XYLineAnnotationTest {

    private static final double EPSILON = 0.000000001;

    @Test
    @DisplayName("Constructor with custom stroke and paint should set properties correctly")
    void constructorWithCustomStrokeAndPaint_ShouldSetProperties() {
        // Arrange
        double x1 = 10.0;
        double y1 = 20.0;
        double x2 = 100.0;
        double y2 = 200.0;
        Stroke expectedStroke = new BasicStroke(2.0f);
        Paint expectedPaint = Color.BLUE;

        // Act
        XYLineAnnotation annotation = new XYLineAnnotation(x1, y1, x2, y2, expectedStroke, expectedPaint);

        // Assert
        assertAll("Annotation properties should match constructor arguments",
                () -> assertEquals(x1, annotation.getX1(), EPSILON, "x1 coordinate should be set"),
                () -> assertEquals(y1, annotation.getY1(), EPSILON, "y1 coordinate should be set"),
                () -> assertEquals(x2, annotation.getX2(), EPSILON, "x2 coordinate should be set"),
                () -> assertEquals(y2, annotation.getY2(), EPSILON, "y2 coordinate should be set"),
                () -> assertSame(expectedStroke, annotation.getStroke(), "The exact stroke instance should be stored"),
                () -> assertSame(expectedPaint, annotation.getPaint(), "The exact paint instance should be stored")
        );
    }

    @Test
    @DisplayName("Constructor with coordinates only should use default stroke and paint")
    void constructorWithCoordinatesOnly_ShouldUseDefaultValues() {
        // Arrange
        double x1 = 15.0;
        double y1 = 25.0;
        double x2 = 150.0;
        double y2 = 250.0;
        
        Stroke expectedDefaultStroke = new BasicStroke(1.0f);
        Paint expectedDefaultPaint = Color.BLACK;

        // Act
        XYLineAnnotation annotation = new XYLineAnnotation(x1, y1, x2, y2);

        // Assert
        assertAll("Annotation should be created with default stroke and paint",
            () -> assertEquals(x1, annotation.getX1(), EPSILON, "x1 coordinate should be set"),
            () -> assertEquals(y1, annotation.getY1(), EPSILON, "y1 coordinate should be set"),
            () -> assertEquals(x2, annotation.getX2(), EPSILON, "x2 coordinate should be set"),
            () -> assertEquals(y2, annotation.getY2(), EPSILON, "y2 coordinate should be set"),
            () -> assertEquals(expectedDefaultStroke, annotation.getStroke(), "Stroke should be the default"),
            () -> assertEquals(expectedDefaultPaint, annotation.getPaint(), "Paint should be the default")
        );
    }
}